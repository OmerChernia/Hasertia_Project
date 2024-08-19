package il.cshaifasweng.OCSFMediatorExample.server.scheduler;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class LinkAndInstanceScheduler {

    private static LinkAndInstanceScheduler instance;
    private final ScheduledThreadPoolExecutor scheduler;
    private final ScheduledExecutorService emailExecutor;
    private final Map<String, ScheduledFuture<?>> scheduledTasks; // Map to keep track of scheduled tasks

    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";

    private LinkAndInstanceScheduler() {
        scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        emailExecutor = Executors.newScheduledThreadPool(10);
        scheduledTasks = new ConcurrentHashMap<>();
    }

    /**
     * @return The singleton instance of the `LinkAndInstanceScheduler`.
     */
    public static synchronized LinkAndInstanceScheduler getInstance() {
        if (instance == null) {
            instance = new LinkAndInstanceScheduler();
        }
        return instance;
    }

    /**
     * Schedules the activation and deactivation of all home viewing packages that have not yet been activated.
     */
    public void scheduleHomeViewingPackages() {
        System.out.println(ANSI_BLUE + "Scheduling home viewing packages..." + ANSI_RESET);
        try (Session session = SimpleServer.session.getSession().getSessionFactory().openSession()) {
            Query<HomeViewingPackageInstance> query = session.createQuery(
                    "FROM HomeViewingPackageInstance WHERE activationDate >= :now", HomeViewingPackageInstance.class
            );
            query.setParameter("now", LocalDateTime.now());

            List<HomeViewingPackageInstance> packages = query.list();
            System.out.println(ANSI_BLUE + "Found " + packages.size() + " home viewing packages to schedule." + ANSI_RESET);

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

    /**
     * Schedules the deactivation of all movie instances that are yet to be screened.
     */
    public void scheduleMovieInstances() {
        System.out.println(ANSI_BLUE + "Scheduling movie instances..." + ANSI_RESET);
        try (Session session = SimpleServer.session.getSession().getSessionFactory().openSession()) {
            Query<MovieInstance> query = session.createQuery(
                    "FROM MovieInstance WHERE time >= :now", MovieInstance.class
            );
            query.setParameter("now", LocalDateTime.now());

            List<MovieInstance> instances = query.list();
            System.out.println(ANSI_BLUE + "Found " + instances.size() + " movie instances to schedule." + ANSI_RESET);

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            List<Callable<Void>> tasks = instances.stream()
                    .map(movieInstance -> (Callable<Void>) () -> {
                        scheduleMovieInstanceDeactivation(movieInstance);
                        return null;
                    })
                    .toList();

            executorService.invokeAll(tasks);
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Schedules the activation, email notification, and deactivation of a viewing link.
     *
     * @param booking The home viewing package instance to schedule.
     */
    public void scheduleLinkActivation(HomeViewingPackageInstance booking) {
        System.out.println(ANSI_BLUE + "Scheduling link activation for home viewing package ID: " + booking.getId() + ANSI_RESET);
        // Calculate delay for link activation
        long linkActivationDelay = Duration.between(LocalDateTime.now(), booking.getViewingDate()).toMillis();
        ScheduledFuture<?> activationFuture = scheduler.schedule(() -> activateViewingLink(booking), linkActivationDelay, TimeUnit.MILLISECONDS);

        // Schedule email notification 1 hour before the movie starts
        LocalDateTime emailTime = booking.getViewingDate().minusHours(1);
        long emailDelay = Duration.between(LocalDateTime.now(), emailTime).toMillis();
        ScheduledFuture<?> emailFuture = emailExecutor.schedule(() -> sendEmailNotification(booking), emailDelay, TimeUnit.MILLISECONDS);

        // Schedule link deactivation after the movie ends
        int movieDuration = booking.getMovie().getDuration();
        LocalDateTime deactivationTime = booking.getViewingDate().plusMinutes(movieDuration);
        long deactivationDelay = Duration.between(LocalDateTime.now(), deactivationTime).toMillis();
        ScheduledFuture<?> deactivationFuture = scheduler.schedule(() -> deactivateViewingLink(booking), deactivationDelay, TimeUnit.MILLISECONDS);

        // Store the scheduled tasks
        scheduledTasks.put("activation-" + booking.getId(), activationFuture);
        scheduledTasks.put("email-" + booking.getId(), emailFuture);
        scheduledTasks.put("deactivation-" + booking.getId(), deactivationFuture);
        System.out.println(ANSI_BLUE + "Scheduled activation, email, and deactivation tasks for home viewing package ID: " + booking.getId() + ANSI_RESET);
    }

    /**
     * Schedules the deactivation of a movie instance after it ends.
     *
     * @param movieInstance The movie instance to schedule.
     */
    public void scheduleMovieInstanceDeactivation(MovieInstance movieInstance) {
        // Calculate delay for movie instance deactivation
        int movieDuration = movieInstance.getMovie().getDuration();
        LocalDateTime deactivationTime = movieInstance.getTime().plusMinutes(movieDuration);
        long delay = Duration.between(LocalDateTime.now(), deactivationTime).toMillis();

        ScheduledFuture<?> deactivationFuture = scheduler.schedule(() -> {
            deactivateMovieInstance(movieInstance);
        }, delay, TimeUnit.MILLISECONDS);

        // Store the deactivation task
        scheduledTasks.put("deactivation-" + movieInstance.getId(), deactivationFuture);
        System.out.println(ANSI_BLUE + "Scheduled deactivation task for movie instance ID: " + movieInstance.getId() + ANSI_RESET);
    }

    /**
     * Cancels all scheduled tasks for a movie instance.
     *
     * @param movieInstance The movie instance whose tasks should be canceled.
     */
    public void cancelMovieInstanceTasks(MovieInstance movieInstance) {
        // Cancel the deactivation task
        ScheduledFuture<?> deactivationFuture = scheduledTasks.get("deactivation-" + movieInstance.getId());
        if (deactivationFuture != null && !deactivationFuture.isDone()) {
            deactivationFuture.cancel(true);
            System.out.println(ANSI_BLUE + "Canceled movie instance deactivation for ID " + movieInstance.getId() + ANSI_RESET);
        }

        // Remove the task from the map
        scheduledTasks.remove("deactivation-" + movieInstance.getId());
    }

    /**
     * Deactivates a movie instance and updates the database.
     *
     * @param movieInstance The movie instance to deactivate.
     */
    private void deactivateMovieInstance(MovieInstance movieInstance) {
        System.out.println(ANSI_BLUE + "Deactivating movie instance ID: " + movieInstance.getId() + ANSI_RESET);
        movieInstance.setIsActive(false);
        updateMovieInstanceInDatabase(movieInstance);
    }

    /**
     * Activates the viewing link for a home viewing package and schedules its deactivation.
     *
     * @param booking The home viewing package instance to activate.
     */
    private void activateViewingLink(HomeViewingPackageInstance booking) {
        booking.activateLink();
        updateBookingInDatabase(booking);

        int movieDuration = booking.getMovie().getDuration();
        LocalDateTime deactivationTime = booking.getViewingDate().plusMinutes(movieDuration);
        long deactivationDelay = Duration.between(LocalDateTime.now(), deactivationTime).toMillis();
        ScheduledFuture<?> deactivationFuture = scheduler.schedule(() -> deactivateViewingLink(booking), deactivationDelay, TimeUnit.MILLISECONDS);

        scheduledTasks.put("deactivation-" + booking.getId(), deactivationFuture);
        System.out.println(ANSI_BLUE + "Scheduled deactivation after activation for home viewing package ID: " + booking.getId() + ANSI_RESET);
    }

    /**
     * Deactivates the viewing link for a home viewing package and updates the database.
     *
     * @param booking The home viewing package instance to deactivate.
     */
    private void deactivateViewingLink(HomeViewingPackageInstance booking) {
        System.out.println(ANSI_BLUE + "Deactivating viewing link for home viewing package ID: " + booking.getId() + ANSI_RESET);
        booking.deactivateLink();
        updateBookingInDatabase(booking);
    }

    /**
     * Updates the database entry for a home viewing package instance.
     *
     * @param booking The home viewing package instance to update.
     */
    private void updateBookingInDatabase(HomeViewingPackageInstance booking) {
        try (Session session = SimpleServer.session.getSession().getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(booking);
            session.getTransaction().commit();
            System.out.println(ANSI_BLUE + "Updated booking in database for booking ID " + booking.getId() + ANSI_RESET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the database entry for a movie instance.
     *
     * @param movieInstance The movie instance to update.
     */
    private void updateMovieInstanceInDatabase(MovieInstance movieInstance) {
        try (Session session = SimpleServer.session.getSession().getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(movieInstance);
            session.getTransaction().commit();
            System.out.println(ANSI_BLUE + "Updated movie instance in database for ID " + movieInstance.getId() + ANSI_RESET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an email notification about an upcoming viewing link.
     *
     * @param booking The home viewing package instance for which the email should be sent.
     */
    private void sendEmailNotification(HomeViewingPackageInstance booking) {
        String email = booking.getOwner().getEmail();
        String movieLink = booking.getLink();
        EmailSender.sendEmail(
                email,
                "Your Movie Link from Monkii Movies",
                String.format("Dear Customer,\n\nYour movie is ready to watch at the scheduled time. Here is your link: %s\n\nThank you,\nMonkii Movies", movieLink)
        );
        System.out.println(ANSI_BLUE + "Sending movie link email to " + email + " at " + LocalDateTime.now() + ANSI_RESET);
    }

    /**
     * Cancels all scheduled tasks for a home viewing package instance.
     *
     * @param booking The home viewing package instance whose tasks should be canceled.
     */
    public void cancelScheduledTasks(HomeViewingPackageInstance booking) {
        // Cancel the activation task
        ScheduledFuture<?> activationFuture = scheduledTasks.get("activation-" + booking.getId());
        if (activationFuture != null && !activationFuture.isDone()) {
            activationFuture.cancel(true);
            System.out.println(ANSI_BLUE + "Canceled link activation for booking ID " + booking.getId() + ANSI_RESET);
        }

        // Cancel the email task
        ScheduledFuture<?> emailFuture = scheduledTasks.get("email-" + booking.getId());
        if (emailFuture != null && !emailFuture.isDone()) {
            emailFuture.cancel(true);
            System.out.println(ANSI_BLUE + "Canceled email notification for booking ID " + booking.getId() + ANSI_RESET);
        }

        // Cancel the deactivation task
        ScheduledFuture<?> deactivationFuture = scheduledTasks.get("deactivation-" + booking.getId());
        if (deactivationFuture != null && !deactivationFuture.isDone()) {
            deactivationFuture.cancel(true);
            System.out.println(ANSI_BLUE + "Canceled link deactivation for booking ID " + booking.getId() + ANSI_RESET);
        }

        // Remove the tasks from the map
        scheduledTasks.remove("activation-" + booking.getId());
        scheduledTasks.remove("email-" + booking.getId());
        scheduledTasks.remove("deactivation-" + booking.getId());
    }
}
