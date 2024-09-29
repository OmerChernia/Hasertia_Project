package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.List;

public class EventNotifier extends Thread {

    @Override
    public void run() {
        LocalDateTime start = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0); // Rounded Time
        LocalDateTime end = start.plusHours(1);

        while (true) {
            try {
                List<HomeViewingPackageInstance> events = getHomeViewingPackagesByTime(start, end);

                for (HomeViewingPackageInstance current : events) {
                    notifyUser(current);
                }

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

    private List<HomeViewingPackageInstance> getHomeViewingPackagesByTime(LocalDateTime startTime, LocalDateTime endTime) {
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
        System.out.println("Notifying user about event: " + event.getOwner().getEmail());
    }
}