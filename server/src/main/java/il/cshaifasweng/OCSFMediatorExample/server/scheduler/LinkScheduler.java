package il.cshaifasweng.OCSFMediatorExample.server.scheduler;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import org.hibernate.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;


public class LinkScheduler {

    private static LinkScheduler instance;
    private final ScheduledThreadPoolExecutor scheduler;
    private final ScheduledExecutorService emailExecutor;

    private LinkScheduler() {
        scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        emailExecutor = Executors.newScheduledThreadPool(10);  // Changed to ScheduledExecutorService
    }

    public static synchronized LinkScheduler getInstance() {
        if (instance == null) {
            instance = new LinkScheduler();
        }
        return instance;
    }


    public void scheduleLinkActivation(HomeViewingPackageInstance booking) {
        long delay = Duration.between(LocalDateTime.now(), booking.getViewingDate()).toMillis();
        scheduler.schedule(() -> activateViewingLink(booking), delay, TimeUnit.MILLISECONDS);
    }

    private void scheduleLinkDeactivation(HomeViewingPackageInstance booking) {
        int movieDuration = booking.getMovie().getDuration();
        LocalDateTime deactivationTime = booking.getViewingDate().plusMinutes(movieDuration);
        long delay = Duration.between(LocalDateTime.now(), deactivationTime).toMillis();

        scheduler.schedule(() -> {
            System.out.println("Deactivating link for booking ID " + booking.getId() + " at " + LocalDateTime.now());
            booking.deactivateLink();
            updateBookingInDatabase(booking);
        }, delay, TimeUnit.MILLISECONDS);
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


    public void scheduleEmailNotification(LocalDateTime emailTime, String email, String movieLink) {
        long delay = Duration.between(LocalDateTime.now(), emailTime).toMillis();
        System.out.println("Scheduling email notification for " + email + " in " + delay + " milliseconds.");

        emailExecutor.schedule(() -> {
            System.out.println("Sending movie link email to " + email + " at " + LocalDateTime.now());
            EmailSender.sendEmail(
                    email,
                    "Your Movie Link from Monkii Movies",
                    String.format("Dear Customer,\n\nYour movie is ready to watch at the scheduled time. Here is your link: %s\n\nThank you,\nMonkii Movies", movieLink)
            );
        }, delay, TimeUnit.MILLISECONDS);
    }
}
