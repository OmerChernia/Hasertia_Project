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

    private ComplaintFollowUpScheduler() {
        emailExecutor = Executors.newFixedThreadPool(5);
        this.scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        this.scheduledTasks = new ConcurrentHashMap<>();
    }

    public static synchronized ComplaintFollowUpScheduler getInstance() {
        if (instance == null) {
            instance = new ComplaintFollowUpScheduler();
        }
        return instance;
    }

    public static void scheduleComplaintHandling(Complaint complaint) {
        emailExecutor.submit(() -> getInstance().sendComplaintHandling(complaint));
    }
    public static void scheduleComplaintReceive(Complaint complaint) {
        emailExecutor.submit(() -> getInstance().sendComplaintReceivedEmail(complaint));
    }
    public static void scheduleCancelScheduledComplaintHandling(int complaintId) {
        getInstance().cancelScheduledComplaintHandling(complaintId);
    }

    public void scheduleHandleComplaintAfter24Hours(Complaint complaint) {
        emailExecutor.submit(() -> handleComplaintAfter24Hours(complaint));
    }

    public void scheduleNotificationToCustomer(Complaint complaint) {
        emailExecutor.submit(() -> sendNotificationToCustomer(complaint));
    }

    public static void scheduleResponseEmailToCustomer(Complaint complaint) {
        emailExecutor.submit(() -> sendResponseEmailToCustomer(complaint));
    }


    public void scheduleAllActiveComplaints() {
        new Thread(() -> {
            try (Session session = SimpleServer.session.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                List<Complaint> activeComplaints = session.createQuery("from Complaint where isClosed = false", Complaint.class).list();

                // Use parallel stream to process complaints concurrently
                activeComplaints.parallelStream().forEach(ComplaintFollowUpScheduler::scheduleComplaintHandling);

                transaction.commit();
                System.out.println("Scheduled all active complaints.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendComplaintHandling(Complaint complaint) {
        LocalDateTime targetTime = complaint.getCreationDate().plusHours(24);
        long delay = Duration.between(LocalDateTime.now(), targetTime).toMillis();
        System.out.println("Scheduling complaint ID " + complaint.getId() + " for handling in " + delay + " milliseconds.");

        Future<?> future = scheduler.schedule(() -> {
            System.out.println("Handling complaint ID " + complaint.getId() + " at " + LocalDateTime.now());
            handleComplaintAfter24Hours(complaint);
        }, delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(complaint.getId(), future);
    }

    private void cancelScheduledComplaintHandling(int complaintId) {
        Future<?> future = scheduledTasks.get(complaintId);
        if (future != null && !future.isDone()) {
            System.out.println("Cancelling scheduled handling for complaint ID " + complaintId);
            future.cancel(true);
            scheduledTasks.remove(complaintId);
        } else {
            System.out.println("No scheduled task found for complaint ID " + complaintId + " or it has already been completed.");
        }
    }

    private void handleComplaintAfter24Hours(Complaint complaint) {
        Session session = null;
        try {
            session = SimpleServer.session.getSessionFactory().openSession();
            session.beginTransaction();

            sendNotificationToCustomer(complaint);

            complaint.setClosed(true);
            session.update(complaint);
            System.out.println("Complaint ID " + complaint.getId() + " has been automatically closed after 24 hours.");

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

    private void sendNotificationToCustomer(Complaint complaint) {
        String customerEmail = complaint.getPurchase().getOwner().getEmail();
        String emailBody = String.format(
                "Dear %s,\n\nUnfortunately, we were unable to resolve your complaint (ID: %d) within the expected time frame. " +
                        "Your complaint has now been closed.\n\n" +
                        "We apologize for the inconvenience and encourage you to contact our customer service for further assistance.\n\n" +
                        "Best regards,\nCustomer Service Team",
                complaint.getPurchase().getOwner().getName(), complaint.getId()
        );

        System.out.println("Sending email notification to customer for complaint ID " + complaint.getId());
        EmailSender.sendEmail(customerEmail, "Your Complaint Has Been Closed", emailBody);
    }

    private static void sendResponseEmailToCustomer(Complaint complaint) {
        String customerEmail = complaint.getPurchase().getOwner().getEmail();
        String emailBody = String.format(
                "Dear %s,\n\nWe have responded to your complaint regarding your purchase. " +
                        "Please check your account for the details of our response.\n\n" +
                        "Thank you for your patience.\n\nBest regards,\nCustomer Service Team",
                complaint.getPurchase().getOwner().getName()
        );

        System.out.println("Sending response email to customer for complaint ID " + complaint.getId());
        EmailSender.sendEmail(customerEmail, "Response to Your Complaint", emailBody);
    }

    public void sendComplaintReceivedEmail(Complaint complaint) {
        String customerEmail = complaint.getRegisteredUser().getEmail();
        String subject = "Complaint Received";
        String body = "Dear " + complaint.getRegisteredUser().getName() + ",\n\n"
                + "We have received your complaint regarding: " + complaint.getInfo() + ".\n"
                + "Our team will address it within 24 hours.\n\n"
                + "Thank you for your patience.\n"
                + "Best regards,\n"
                + "Customer Service Team";

        EmailSender.sendEmail(customerEmail, subject, body);
    }


}
