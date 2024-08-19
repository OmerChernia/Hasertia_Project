package il.cshaifasweng.OCSFMediatorExample.server.scheduler;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ComplaintFollowUpScheduler {

    private static ComplaintFollowUpScheduler instance; // Singleton instance
    private final ScheduledThreadPoolExecutor scheduler;
    private final Map<Integer, Future<?>> scheduledTasks;
    private static ExecutorService emailExecutor;

    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    private ComplaintFollowUpScheduler() {
        emailExecutor = Executors.newFixedThreadPool(5);
        this.scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        this.scheduledTasks = new ConcurrentHashMap<>();
    }

    /**
     * Returns the singleton instance of the ComplaintFollowUpScheduler.
     *
     * @return the singleton instance of the ComplaintFollowUpScheduler.
     */
    public static synchronized ComplaintFollowUpScheduler getInstance() {
        if (instance == null) {
            instance = new ComplaintFollowUpScheduler();
        }
        return instance;
    }

    /**
     * Schedules the handling of a complaint, sending an email to notify that the complaint is being handled.
     *
     * @param complaint The complaint to be handled.
     */
    public static void scheduleComplaintHandling(Complaint complaint) {
         emailExecutor.submit(() -> getInstance().sendComplaintHandling(complaint));
    }

    /**
     * Schedules the notification that a complaint has been received.
     *
     * @param complaint The complaint that has been received.
     */
    public static void scheduleComplaintReceive(Complaint complaint) {
         emailExecutor.submit(() -> getInstance().sendComplaintReceivedEmail(complaint));
    }

    /**
     * Cancels the scheduled handling of a complaint.
     *
     * @param complaintId The ID of the complaint whose scheduled handling should be canceled.
     */
    public static void scheduleCancelScheduledComplaintHandling(int complaintId) {
         getInstance().cancelScheduledComplaintHandling(complaintId);
    }

    /**
     * Schedules the handling of a complaint after 24 hours.
     *
     * @param complaint The complaint to be handled.
     */
    public void scheduleHandleComplaintAfter24Hours(Complaint complaint) {
         emailExecutor.submit(() -> handleComplaintAfter24Hours(complaint));
    }

    /**
     * Schedules a notification email to the customer about the complaint status.
     *
     * @param complaint The complaint for which the notification should be sent.
     */
    public void scheduleNotificationToCustomer(Complaint complaint) {
         emailExecutor.submit(() -> sendNotificationToCustomer(complaint));
    }

    /**
     * Schedules an email response to be sent to the customer regarding their complaint.
     *
     * @param complaint The complaint for which the response email should be sent.
     */
    public static void scheduleResponseEmailToCustomer(Complaint complaint) {
        System.out.println(ANSI_YELLOW + "Scheduling response email to customer for complaint ID: " + complaint.getId() + ANSI_RESET);
        emailExecutor.submit(() -> sendResponseEmailToCustomer(complaint));
    }

    /**
     * Schedules the handling of all active complaints in the system.
     */
    public void scheduleAllActiveComplaints() {
        System.out.println(ANSI_YELLOW + "Scheduling all active complaints." + ANSI_RESET);
        new Thread(() -> {
            try (Session session = SimpleServer.session.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                List<Complaint> activeComplaints = session.createQuery("from Complaint where isClosed = false", Complaint.class).list();

                // Use parallel stream to process complaints concurrently
                activeComplaints.parallelStream().forEach(ComplaintFollowUpScheduler::scheduleComplaintHandling);

                transaction.commit();
                System.out.println(ANSI_YELLOW + "Scheduled all active complaints." + ANSI_RESET);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Schedules the handling of a specific complaint after a delay of 24 hours.
     *
     * @param complaint The complaint to handle.
     */
    private void sendComplaintHandling(Complaint complaint) {
        LocalDateTime targetTime = complaint.getCreationDate().plusHours(24);
        long delay = Duration.between(LocalDateTime.now(), targetTime).toMillis();
        System.out.println(ANSI_YELLOW + "Scheduling complaint ID " + complaint.getId() + " for handling in " + delay + " milliseconds." + ANSI_RESET);

        Future<?> future = scheduler.schedule(() -> {
            System.out.println(ANSI_YELLOW + "Handling complaint ID " + complaint.getId() + " at " + LocalDateTime.now() + ANSI_RESET);
            handleComplaintAfter24Hours(complaint);
        }, delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(complaint.getId(), future);
    }

    /**
     * Cancels the scheduled handling of a complaint.
     *
     * @param complaintId The ID of the complaint whose scheduled handling should be canceled.
     */
    private void cancelScheduledComplaintHandling(int complaintId) {
        Future<?> future = scheduledTasks.get(complaintId);
        if (future != null && !future.isDone()) {
            System.out.println(ANSI_YELLOW + "Cancelling scheduled handling for complaint ID " + complaintId + ANSI_RESET);
            future.cancel(true);
            scheduledTasks.remove(complaintId);
        } else {
            System.out.println(ANSI_YELLOW + "No scheduled task found for complaint ID " + complaintId + " or it has already been completed." + ANSI_RESET);
        }
    }

    /**
     * Handles a complaint after a delay of 24 hours.
     *
     * @param complaint The complaint to handle.
     */
    private void handleComplaintAfter24Hours(Complaint complaint) {
        Session session = null;
        try {
            session = SimpleServer.session.getSessionFactory().openSession();
            session.beginTransaction();

            sendNotificationToCustomer(complaint);

            complaint.setClosed(true);
            session.update(complaint);
            System.out.println(ANSI_YELLOW + "Complaint ID " + complaint.getId() + " has been automatically closed after 24 hours." + ANSI_RESET);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Sends a notification email to the customer when their complaint is automatically handled.
     *
     * @param complaint The complaint for which the notification should be sent.
     */
    private void sendNotificationToCustomer(Complaint complaint) {
        String customerEmail = complaint.getPurchase().getOwner().getEmail();
        String emailBody = String.format(
                "Dear %s,\n\nUnfortunately, we were unable to resolve your complaint (ID: %d) within the expected time frame. " +
                        "Your complaint has now been closed.\n\n" +
                        "We apologize for the inconvenience and encourage you to contact our customer service for further assistance.\n\n" +
                        "Best regards,\nCustomer Service Team",
                complaint.getPurchase().getOwner().getName(), complaint.getId()
        );

        System.out.println(ANSI_YELLOW + "Sending email notification to customer for complaint ID " + complaint.getId() + ANSI_RESET);
        EmailSender.sendEmail(customerEmail, "Your Complaint Has Been Closed", emailBody);
        // Optionally, email the company
        EmailSender.sendEmail("hasertiaproject@gmail.com", "Your Complaint Has Been Closed", emailBody);
    }

    /**
     * Sends a response email to the customer regarding their complaint.
     *
     * @param complaint The complaint for which the response email should be sent.
     */
    private static void sendResponseEmailToCustomer(Complaint complaint) {
        String customerEmail = complaint.getPurchase().getOwner().getEmail();
        String emailBody = String.format(
                "Dear %s,\n\nWe have responded to your complaint regarding your purchase. " +
                        "Please check your account for the details of our response.\n\n" +
                        "Thank you for your patience.\n\nBest regards,\nCustomer Service Team",
                complaint.getPurchase().getOwner().getName()
        );

        System.out.println(ANSI_YELLOW + "Sending response email to customer for complaint ID " + complaint.getId() + ANSI_RESET);
        EmailSender.sendEmail(customerEmail, "Response to Your Complaint", emailBody);

        // Optionally, email the company
        EmailSender.sendEmail("hasertiaproject@gmail.com", "Response to Your Complaint", emailBody);
    }

    /**
     * Sends an email to the customer confirming receipt of their complaint.
     *
     * @param complaint The complaint that was received.
     */
    public void sendComplaintReceivedEmail(Complaint complaint) {
        String customerEmail = complaint.getRegisteredUser().getEmail();
        String subject = "Complaint Received";
        String body = "Dear " + complaint.getRegisteredUser().getName() + ",\n\n"
                + "We have received your complaint regarding: " + complaint.getInfo() + ".\n"
                + "Our team will address it within 24 hours.\n\n"
                + "Thank you for your patience.\n"
                + "Best regards,\n"
                + "Customer Service Team";

        System.out.println(ANSI_YELLOW + "Sending complaint received email to customer for complaint ID " + complaint.getId() + ANSI_RESET);
        EmailSender.sendEmail(customerEmail, subject, body);
        // Optionally, email the company
        EmailSender.sendEmail("hasertiaproject@gmail.com", subject, body);
    }
}
