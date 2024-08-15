package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventNotifier extends Thread {

    @Override
    public void run() {
        LocalDateTime start = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0); // rounded time
        start= start.plusHours(4);// 3 h ofseet and 1 h befor scrinig
        LocalDateTime end = start.plusHours(1);
        while (true) {
            try {
                List<HomeViewingPackageInstance> events = getHomeViewingPackagesByTime(start, end);
                for (HomeViewingPackageInstance current : events) {
                    notifyUser(current);
                }

                if (LocalDateTime.now().getHour() == 15 && LocalDateTime.now().getMinute() == 0) {
                   AlertNewMoviesForSubscribers();
                }

                //checkForUnhandledComplaints();

                    start = end;
                end = start.plusHours(1);

                // Sleep until the next cycle
                while (LocalDateTime.now().isBefore(start)) {
                    Thread.sleep(1000); // sleep for 1 second
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Log exception or handle it as necessary
            }
        }
    }

//    private void checkForUnhandledComplaints()
//    {
//        Query<Complaint> query = SimpleServer.session.createQuery(
//                "from Complaint where isClosed= false and creationDate > :date ", Complaint.class
//        );
//        query.setParameter("date", LocalDate.now().minusDays(1));
//        List<Complaint> complaints = query.list();
//
//    }

    private void AlertNewMoviesForSubscribers()
    {
        Query<MovieInstance> query = SimpleServer.session.createQuery(
                "from MovieInstance where movie.notificationSent = false and time = :date ",
                MovieInstance.class
        );
        query.setParameter("date", LocalDate.now());
        List<MovieInstance> movieInstances= query.list();
        Set<Movie> uniqueMovies = movieInstances.stream()
                .map(MovieInstance::getMovie)
                .collect(Collectors.toSet());
        for(Movie movie: uniqueMovies)
        {
            notifyMultiEntryClients(movie);
        }
    }

    public void notifyMultiEntryClients(Movie movie)
    {
        System.out.println("hello");
        String text = "Dear Customer,\n\n" +
                "We are excited to announce a new movie in our collection: " + movie.getEnglishName() + " (" + movie.getHebrewName() + ").\n" +
                "Produced by " + movie.getProducer() + ", this " + movie.getGenre() + " film features an incredible cast including " + String.join(", ", movie.getMainActors()) + ".\n" +
                "With a runtime of " + movie.getDuration() + " minutes, this movie is "+movie.getAvailability()+" for " + movie.getStreamingType() + ".\n";

        if(movie.getStreamingType() == Movie.StreamingType.BOTH)
        {
            text += "Home Viewing Price: " + movie.getHomeViewingPrice() + " ILS\n" +"Theater Price: " + movie.getTheaterPrice() + " ILS\n\n";
        }
        if(movie.getStreamingType() == Movie.StreamingType.HOME_VIEWING)
        {
            text += "Home Viewing Price: " + movie.getHomeViewingPrice() + " ILS\n\n";
        }
        if(movie.getStreamingType() == Movie.StreamingType.THEATER_VIEWING)
        {
            text += "Theater Price: " + movie.getTheaterPrice() + " ILS\n\n";
        }
        text+=  "Don't miss out on this exciting release!\n" + "Best regards,\nHasertia Movie Team <3";
        Query<RegisteredUser> query = SimpleServer.session.createQuery("FROM RegisteredUser WHERE ticket_counter > 0", RegisteredUser.class);
        List<RegisteredUser> users = query.getResultList();
        for (RegisteredUser user : users) {
            EmailSender.sendEmail(user.getEmail(), "New Movie in Hasertia", text);
        }
        EmailSender.sendEmail("hasertiaproject@gmail.com", "New Movie in Hasertia", text);
    }
    private List<HomeViewingPackageInstance> getHomeViewingPackagesByTime(LocalDateTime startTime, LocalDateTime endTime) {
        System.out.println("getHomeViewingPackagesByTime was called");
        // Query for HomeViewingPackageInstance based on activationDate within the time range
        Query<HomeViewingPackageInstance> query = SimpleServer.session.createQuery(
                "from HomeViewingPackageInstance where activationDate IS NOT NULL " +
                        "and activationDate >= :startDateTime and activationDate < :endDateTime",
                HomeViewingPackageInstance.class
        );
        query.setParameter("startDateTime", startTime);
        query.setParameter("endDateTime", endTime);
        return query.list();
    }

    public void notifyUser(HomeViewingPackageInstance event) {
        // Implement your notification logic here, like sending an email or SMS
         String emailBody = String.format(
                "Dear "+event.getOwner().getName()+",\n\n" +
                        "We are pleased to inform you that the movie you purchased,"+event.getMovie().getEnglishName()+", will be available for viewing in just 1 hour. " +
                        "You can access the movie using the following link:\n\n" +
                        event.getLink() +"\n\n" +
                        "Thank you for choosing our home viewing service. We hope you enjoy your movie experience!\n\n" +
                        "Best regards,\n" +
                        "Haseretia Movies Team <3\n" +
                        "Customer Support Team"
        );
        System.out.println("Notifying user about event: " + event.getOwner().getEmail());
        EmailSender.sendEmail(event.getOwner().getEmail(), "Your Home Viewing Purchase Will Be availible in 1 Hour", emailBody);
    }

}