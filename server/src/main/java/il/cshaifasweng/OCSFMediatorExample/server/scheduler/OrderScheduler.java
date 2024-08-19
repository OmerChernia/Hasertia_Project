package il.cshaifasweng.OCSFMediatorExample.server.scheduler;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class OrderScheduler {

    private static OrderScheduler instance; // Singleton instance
    private final ExecutorService emailExecutor;
    private final ScheduledExecutorService scheduledExecutor;
    private final Map<String, ScheduledFuture<?>> scheduledTasks; // Map to keep track of scheduled tasks

    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    private OrderScheduler() {
        emailExecutor = Executors.newFixedThreadPool(5);
        scheduledExecutor = Executors.newScheduledThreadPool(5);
        scheduledTasks = new ConcurrentHashMap<>();
    }

    /**
     * Returns the singleton instance of the OrderScheduler.
     *
     * @return the singleton instance of the OrderScheduler.
     */
    public static synchronized OrderScheduler getInstance() {
        if (instance == null) {
            instance = new OrderScheduler();
        }
        return instance;
    }

    /**
     * Schedules a purchase confirmation email to be sent to the customer.
     *
     * @param purchase The purchase for which the confirmation email should be sent.
     */
    public void schedulePurchaseConfirmation(Purchase purchase) {
        System.out.println(ANSI_GREEN + "Scheduling purchase confirmation email for purchase ID: " + purchase.getId() + ANSI_RESET);
        emailExecutor.submit(() -> sendPurchaseConfirmationEmail(purchase));
    }

    /**
     * Schedules a cancellation email to be sent to the customer for the given purchase.
     *
     * @param purchase The purchase for which the cancellation email should be sent.
     */
    public void scheduleEmailCancellation(Purchase purchase) {
        System.out.println(ANSI_GREEN + "Scheduling cancellation email for purchase ID: " + purchase.getId() + ANSI_RESET);
        emailExecutor.submit(() -> sendCancellationEmail(purchase));
    }

    /**
     * Schedules emails to be sent to customers informing them that their movie screening has been canceled.
     *
     * @param customerData  A list of MovieTicket objects representing the customers whose screenings were canceled.
     * @param movieInstance The movie instance that was canceled.
     */
    public void scheduleEmailsForCanceledScreening(List<MovieTicket> customerData, MovieInstance movieInstance) {
        System.out.println(ANSI_GREEN + "Scheduling emails for canceled screening of movie instance ID: " + movieInstance.getId() + ANSI_RESET);
        for (MovieTicket customer : customerData) {
            String email = customer.getOwner().getEmail();
            String name = customer.getOwner().getName();

            emailExecutor.submit(() -> {
                System.out.println(ANSI_GREEN + "Sending cancellation email to " + email + " for movie: " + movieInstance.getMovie().getEnglishName() + ANSI_RESET);
                EmailSender.sendEmail(email, "Canceled Ticket from Monkii Movies",
                        String.format("Dear %s,\n\nYour ticket for the movie '%s' scheduled on %s has been canceled. We apologize for the inconvenience. You will receive a full refund.\n\nThank you,\nMonkii Movies",
                                name,
                                movieInstance.getMovie().getEnglishName(),
                                movieInstance.getTime().toLocalDate().toString()));
                EmailSender.sendEmail("hasertiaproject@gmail.com", "Canceled Ticket from Monkii Movies",
                        String.format("Dear %s,\n\nYour ticket for the movie '%s' scheduled on %s has been canceled. We apologize for the inconvenience. You will receive a full refund.\n\nThank you,\nMonkii Movies",
                                name,
                                movieInstance.getMovie().getEnglishName(),
                                movieInstance.getTime().toLocalDate().toString()));
            });
        }
    }

    /**
     * Schedules emails to be sent to customers informing them that the details of their movie screening have been updated.
     *
     * @param customerData  A list of customer data containing their email addresses and names.
     * @param movieInstance The movie instance whose screening details were updated.
     */
    public void scheduleEmailsForUpdatedScreening(List<Object[]> customerData, MovieInstance movieInstance) {
        System.out.println(ANSI_GREEN + "Scheduling emails for updated screening of movie instance ID: " + movieInstance.getId() + ANSI_RESET);
        for (Object[] customer : customerData) {
            String email = (String) customer[0];
            String name = (String) customer[1];

            emailExecutor.submit(() -> {
                System.out.println(ANSI_GREEN + "Sending updated screening email to " + email + " for movie: " + movieInstance.getMovie().getEnglishName() + ANSI_RESET);
                EmailSender.sendEmail(
                        email,
                        "Updated Movie Screening Notification from Monkii Movies",
                        String.format(
                                "Dear %s,\n\nWe would like to inform you that the screening of '%s' that you booked has been updated. " +
                                        "The updated details are as follows:\n\n" +
                                        "Movie: %s\n" +
                                        "Date & Time: %s\n" +
                                        "Location: %s, Hall %d\n\n" +
                                        "Please contact us if you have any questions.\n\n" +
                                        "Thank you,\nMonkii Movies",
                                name,
                                movieInstance.getMovie().getEnglishName(),
                                movieInstance.getMovie().getEnglishName(),
                                movieInstance.getTime().toString(),
                                movieInstance.getHall().getTheater().getLocation(),
                                movieInstance.getHall().getId()
                        )
                );
                EmailSender.sendEmail(
                        "hasertiaproject@gmail.com",
                        "Updated Movie Screening Notification from Monkii Movies",
                        String.format(
                                "Dear %s,\n\nWe would like to inform you that the screening of '%s' that you booked has been updated. " +
                                        "The updated details are as follows:\n\n" +
                                        "Movie: %s\n" +
                                        "Date & Time: %s\n" +
                                        "Location: %s, Hall %d\n\n" +
                                        "Please contact us if you have any questions.\n\n" +
                                        "Thank you,\nMonkii Movies",
                                name,
                                movieInstance.getMovie().getEnglishName(),
                                movieInstance.getMovie().getEnglishName(),
                                movieInstance.getTime().toString(),
                                movieInstance.getHall().getTheater().getLocation(),
                                movieInstance.getHall().getId()
                        )
                );
            });
        }
    }

    /**
     * Sends notification emails to registered users about a new movie being added.
     *
     * @param movie  The movie that is being added.
     * @param users  The list of registered users to notify.
     */
    public void notifyNewMovie(Movie movie, List<RegisteredUser> users) {
        System.out.println(ANSI_GREEN + "Scheduling notification emails for new movie: " + movie.getEnglishName() + ANSI_RESET);
        for (RegisteredUser user : users) {
            emailExecutor.submit(() -> {
                String emailBody = String.format(
                        "Dear %s,\n\nWe are excited to announce a new movie in our collection: %s (%s).\n" +
                                "Produced by %s, this %s film features an incredible cast including %s.\n" +
                                "With a runtime of %d minutes, this movie is available for %s.\n\n" +
                                "Don't miss out on this exciting release!\n\nBest regards,\nYour Cinema Team",
                        user.getName(),
                        movie.getEnglishName(), movie.getHebrewName(),
                        movie.getProducer(),
                        movie.getGenre(), String.join(", ", movie.getMainActors()),
                        movie.getDuration(), movie.getStreamingType()
                );

                System.out.println(ANSI_GREEN + "Sending new movie email to " + user.getEmail() + " for movie: " + movie.getEnglishName() + ANSI_RESET);
                EmailSender.sendEmail(user.getEmail(), "New Movie Available: " + movie.getEnglishName(), emailBody);
                // Optionally, email the company
                EmailSender.sendEmail("hasertiaproject@gmail.com", "New Movie Available: " + movie.getEnglishName(), emailBody);
            });
        }
    }

    /**
     * Schedules notification emails to be sent to registered users one day before a movie is screened.
     *
     * @param movie           The movie that is being screened.
     * @param users           The list of registered users to notify.
     * @param screeningDateTime The date and time of the movie screening.
     */
    public void scheduleNotifyNewMovieOneDayBefore(Movie movie, List<RegisteredUser> users, LocalDateTime screeningDateTime) {
        System.out.println(ANSI_GREEN + "Scheduling notification email one day before screening for movie: " + movie.getEnglishName() + ANSI_RESET);
        // Calculate the delay until 24 hours before the screening
        long delay = Duration.between(LocalDateTime.now(), screeningDateTime.minusDays(1)).toMillis();
        String taskKey = "notify-" + movie.getId();

        ScheduledFuture<?> scheduledTask = scheduledExecutor.schedule(() -> notifyNewMovie(movie, users), delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(taskKey, scheduledTask);
    }

    /**
     * Cancels the scheduled notification email for a new movie, if it exists.
     *
     * @param movie The movie for which the notification email should be canceled.
     */
    public void cancelScheduledNotifyNewMovie(Movie movie) {
        String taskKey = "notify-" + movie.getId();
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(taskKey);
        if (scheduledTask != null && !scheduledTask.isDone()) {
            System.out.println(ANSI_GREEN + "Canceling scheduled notification email for movie: " + movie.getEnglishName() + ANSI_RESET);
            scheduledTask.cancel(true);
            scheduledTasks.remove(taskKey);
            System.out.println(ANSI_GREEN + "Canceled scheduled notification for movie: " + movie.getEnglishName() + ANSI_RESET);
        }
    }

    /**
     * Sends a purchase confirmation email to the customer for the given purchase.
     *
     * @param purchase The purchase for which the confirmation email should be sent.
     */
    private void sendPurchaseConfirmationEmail(Purchase purchase) {
        String purchaseType = (purchase instanceof MovieTicket)
                ? ((MovieTicket) purchase).getMovieInstance().getMovie().getEnglishName() + " ticket"
                : ((HomeViewingPackageInstance) purchase).getMovie().getEnglishName() + " home viewing package";

        String confirmation = String.format("Dear %s,\n\n" +
                        "Thank you for your recent purchase of %s. " +
                        "Your purchase has been confirmed and processed successfully. If you have any questions or need further assistance, please contact us.\n\n" +
                        "Thank you for choosing Monkii Movies.\n\nBest regards,\nMonkii Movies Team",
                purchase.getOwner().getName(), purchaseType);

        System.out.println(ANSI_GREEN + "Sending purchase confirmation email to " + purchase.getOwner().getEmail() + ANSI_RESET);
        // Send the email to the customer
        EmailSender.sendEmail(purchase.getOwner().getEmail(), "Confirmation of Your Purchase from Monkii Movies", confirmation);

        // Optionally, email the company
        EmailSender.sendEmail("hasertiaproject@gmail.com", "New Purchase Confirmation from Monkii Movies", confirmation);
    }

    /**
     * Sends a cancellation email to the customer for the given purchase.
     *
     * @param purchase The purchase for which the cancellation email should be sent.
     */
    private void sendCancellationEmail(Purchase purchase) {
        String purchaseType = (purchase instanceof MovieTicket)
                ? ((MovieTicket) purchase).getMovieInstance().getMovie().getEnglishName() + " ticket"
                : ((HomeViewingPackageInstance) purchase).getMovie().getEnglishName() + " home viewing package";

        String confirmation = String.format("Dear %s,\n\n" +
                        "We wanted to confirm that your recent purchase of %s has been successfully canceled. " +
                        "If you have any questions or require further assistance, please don't hesitate to reach out to us.\n\n" +
                        "Thank you for your understanding.\n\nBest regards,\nMonkii Movies Team",
                purchase.getOwner().getName(), purchaseType);

        System.out.println(ANSI_GREEN + "Sending cancellation email to " + purchase.getOwner().getEmail() + ANSI_RESET);
        // Send the email to the customer
        EmailSender.sendEmail(purchase.getOwner().getEmail(), "Confirmation of Your Canceled Purchase from Monkii Movies", confirmation);

        // Optionally, email the company
        EmailSender.sendEmail("hasertiaproject@gmail.com", "A purchase has been canceled.", confirmation);
    }
}
