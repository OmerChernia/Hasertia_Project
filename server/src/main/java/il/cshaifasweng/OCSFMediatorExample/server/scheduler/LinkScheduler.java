package il.cshaifasweng.OCSFMediatorExample.server.scheduler;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class LinkScheduler {

    private static LinkScheduler instance;
    private final ScheduledThreadPoolExecutor scheduler;
    private final ScheduledExecutorService emailExecutor;
    private final Map<String, ScheduledFuture<?>> scheduledTasks; // Map to keep track of scheduled tasks

    private LinkScheduler() {
        scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        emailExecutor = Executors.newScheduledThreadPool(10);
        scheduledTasks = new ConcurrentHashMap<>();
    }

    public static synchronized LinkScheduler getInstance() {
        if (instance == null) {
            instance = new LinkScheduler();
        }
        return instance;
    }

    public void scheduleHomeViewingPackages() {
        try (Session session = SimpleServer.session.getSession().getSessionFactory().openSession()) {
            Query<HomeViewingPackageInstance> query = session.createQuery(
                    "FROM HomeViewingPackageInstance WHERE activationDate >= :now", HomeViewingPackageInstance.class
            );
            query.setParameter("now", LocalDateTime.now());

            List<HomeViewingPackageInstance> packages = query.list();

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            List<Callable<Void>> tasks = packages.stream()
                    .map(viewingPackage -> (Callable<Void>) () -> {
                        scheduleLinkActivation(viewingPackage);
                        return null;
                    })
                    .toList();

            executorService.invokeAll(tasks);
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scheduleLinkActivation(HomeViewingPackageInstance booking) {
        // Calculate delay for link activation
        long linkActivationDelay = Duration.between(LocalDateTime.now(), booking.getViewingDate()).toMillis();
        ScheduledFuture<?> activationFuture = scheduler.schedule(() -> activateViewingLink(booking), linkActivationDelay, TimeUnit.MILLISECONDS);

        // Schedule email notification 1 hour before the movie starts
        LocalDateTime emailTime = booking.getViewingDate().minusHours(1);
        long emailDelay = Duration.between(LocalDateTime.now(), emailTime).toMillis();
        ScheduledFuture<?> emailFuture = emailExecutor.schedule(() -> sendEmailNotification(booking), emailDelay, TimeUnit.MILLISECONDS);

        // Store the scheduled tasks
        scheduledTasks.put("activation-" + booking.getId(), activationFuture);
        scheduledTasks.put("email-" + booking.getId(), emailFuture);
    }

    private void scheduleLinkDeactivation(HomeViewingPackageInstance booking) {
        int movieDuration = booking.getMovie().getDuration();
        LocalDateTime deactivationTime = booking.getViewingDate().plusMinutes(movieDuration);
        long delay = Duration.between(LocalDateTime.now(), deactivationTime).toMillis();

        ScheduledFuture<?> deactivationFuture = scheduler.schedule(() -> {
            System.out.println("Deactivating link for booking ID " + booking.getId() + " at " + LocalDateTime.now());
            booking.deactivateLink();
            updateBookingInDatabase(booking);
        }, delay, TimeUnit.MILLISECONDS);

        scheduledTasks.put("deactivation-" + booking.getId(), deactivationFuture);
    }

    private void activateViewingLink(HomeViewingPackageInstance booking) {
        booking.activateLink();
        updateBookingInDatabase(booking);
        scheduleLinkDeactivation(booking);
    }

    private void updateBookingInDatabase(HomeViewingPackageInstance booking) {
        try (Session session = SimpleServer.session.getSession().getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(booking);
            session.getTransaction().commit();
            System.out.println("Updated booking in database for booking ID " + booking.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmailNotification(HomeViewingPackageInstance booking) {
        String email = booking.getOwner().getEmail();
        String movieLink = booking.getLink();
        EmailSender.sendEmail(
                email,
                "Your Movie Link from Monkii Movies",
                String.format("Dear Customer,\n\nYour movie is ready to watch at the scheduled time. Here is your link: %s\n\nThank you,\nMonkii Movies", movieLink)
        );
        System.out.println("Sending movie link email to " + email + " at " + LocalDateTime.now());
    }

    public void cancelScheduledTasks(HomeViewingPackageInstance booking) {
        // Cancel the activation task
        ScheduledFuture<?> activationFuture = scheduledTasks.get("activation-" + booking.getId());
        if (activationFuture != null && !activationFuture.isDone()) {
            activationFuture.cancel(true);
            System.out.println("Canceled link activation for booking ID " + booking.getId());
        }

        // Cancel the email task
        ScheduledFuture<?> emailFuture = scheduledTasks.get("email-" + booking.getId());
        if (emailFuture != null && !emailFuture.isDone()) {
            emailFuture.cancel(true);
            System.out.println("Canceled email notification for booking ID " + booking.getId());
        }

        // Cancel the deactivation task (if scheduled)
        ScheduledFuture<?> deactivationFuture = scheduledTasks.get("deactivation-" + booking.getId());
        if (deactivationFuture != null && !deactivationFuture.isDone()) {
            deactivationFuture.cancel(true);
            System.out.println("Canceled link deactivation for booking ID " + booking.getId());
        }

        // Remove the tasks from the map
        scheduledTasks.remove("activation-" + booking.getId());
        scheduledTasks.remove("email-" + booking.getId());
        scheduledTasks.remove("deactivation-" + booking.getId());
    }
}
