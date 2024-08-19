package il.cshaifasweng.OCSFMediatorExample.server.scheduler;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderScheduler {
/*הקרנה שנגמרה להפוך לnotactive */
/*ביום של ההקרנה של סרט חדש (הקרנה ראשונה) להודיעה ללקוחות הרלוונטים */

    private static OrderScheduler instance; // Singleton instance
    private final ExecutorService emailExecutor;

    private OrderScheduler() {
        emailExecutor = Executors.newFixedThreadPool(5);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
    }

    public static synchronized OrderScheduler getInstance() {
        if (instance == null) {
            instance = new OrderScheduler();
        }
        return instance;
    }

    public void schedulePurchaseConfirmation(Purchase purchase) {
        emailExecutor.submit(() -> sendPurchaseConfirmationEmail(purchase));
    }

    public void scheduleEmailCancellation(Purchase purchase) {
        emailExecutor.submit(() -> sendCancellationEmail(purchase));
    }

    public void scheduleEmailsForCanceledScreening(List<MovieTicket> customerData, MovieInstance movieInstance) {
        for (MovieTicket customer : customerData) {
            String email = customer.getOwner().getEmail();
            String name = customer.getOwner().getName();

            emailExecutor.submit(() -> {
                System.out.println("Sending cancellation email to " + email + " for movie: " + movieInstance.getMovie().getEnglishName());
                EmailSender.sendEmail(email, "Canceled Ticket from Monkii Movies",
                        String.format("Dear %s,\n\nYour ticket for the movie '%s' scheduled on %s has been canceled. We apologize for the inconvenience. You will receive a full refund.\n\nThank you,\nMonkii Movies",
                                name,
                                movieInstance.getMovie().getEnglishName(),
                                movieInstance.getTime().toLocalDate().toString()));
            });
        }
    }

    public void scheduleEmailsForUpdatedScreening(List<Object[]> customerData, MovieInstance movieInstance) {
        for (Object[] customer : customerData) {
            String email = (String) customer[0];
            String name = (String) customer[1];

            emailExecutor.submit(() -> {
                System.out.println("Sending updated screening email to " + email + " for movie: " + movieInstance.getMovie().getEnglishName());
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
            });
        }
    }

    public void notifyNewMovie(Movie movie, List<RegisteredUser> users) {
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

                System.out.println("Sending new movie email to " + user.getEmail() + " for movie: " + movie.getEnglishName());
                EmailSender.sendEmail(user.getEmail(), "New Movie Available: " + movie.getEnglishName(), emailBody);
            });
        }
    }

    private void sendPurchaseConfirmationEmail(Purchase purchase) {
        String purchaseType = (purchase instanceof MovieTicket)
                ? ((MovieTicket) purchase).getMovieInstance().getMovie().getEnglishName() + " ticket"
                : ((HomeViewingPackageInstance) purchase).getMovie().getEnglishName() + " home viewing package";

        String confirmation = String.format("Dear %s,\n\n" +
                        "Thank you for your recent purchase of %s. " +
                        "Your purchase has been confirmed and processed successfully. If you have any questions or need further assistance, please contact us.\n\n" +
                        "Thank you for choosing Monkii Movies.\n\nBest regards,\nMonkii Movies Team",
                purchase.getOwner().getName(), purchaseType);

        // Send the email to the customer
        EmailSender.sendEmail(purchase.getOwner().getEmail(), "Confirmation of Your Purchase from Monkii Movies", confirmation);

        // Optionally, email the company
         EmailSender.sendEmail("hasertiaproject@gmail.com", "New Purchase Confirmation from Monkii Movies", confirmation);
    }

    private void sendCancellationEmail(Purchase purchase) {
        String purchaseType = (purchase instanceof MovieTicket)
                ? ((MovieTicket) purchase).getMovieInstance().getMovie().getEnglishName() + " ticket"
                : ((HomeViewingPackageInstance) purchase).getMovie().getEnglishName() + " home viewing package";

        String confirmation = String.format("Dear %s,\n\n" +
                        "We wanted to confirm that your recent purchase of %s has been successfully canceled. " +
                        "If you have any questions or require further assistance, please don't hesitate to reach out to us.\n\n" +
                        "Thank you for your understanding.\n\nBest regards,\nMonkii Movies Team",
                purchase.getOwner().getName(), purchaseType);

        // Send the email to the customer
        EmailSender.sendEmail(purchase.getOwner().getEmail(), "Confirmation of Your Canceled Purchase from Monkii Movies", confirmation);

        // Optionally, email the company
        EmailSender.sendEmail("hasertiaproject@gmail.com", "A purchase has been canceled.", confirmation);
    }
}
