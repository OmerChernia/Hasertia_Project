package il.cshaifasweng.OCSFMediatorExample.server;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;

    public class EventNotifier {
        void baruhfunc

        {
            LocalDateTime start = LocalDateTime.now();
            start=start.withMinute(0).withSecond(0).withNano(0);//ronded time
            HomeViewingPackageInstance curent;
            LocalDateTime end=start.plusHours(1);
            while(true) {
            String sql = "SELECT * FROM movie_mainactors WHERE activationDate BETWEEN start AND end, AND activationDate!=null";

            List<HomeViewingPackageInstance> events = getHomeViewingPackagesByActorAndTime(Session session, String mainActor, start,end);
            curent = events.getFirst();

            while(curent != null) {
                notifyUser(curent);
                events.removeFirst();
                curent = events.getFirst();
            }
            start=end;
            end=start.plusHours(1);
            while(start.isAfter(LocalDateTime.now())) {//spin lock
            }
        }
          }

        public List<HomeViewingPackageInstance> getHomeViewingPackagesByActorAndTime(LocalDateTime startTime, LocalDateTime endTime) {
            String hql = "FROM HomeViewingPackageInstance H WHERE H.movie.mainActors LIKE :mainActor AND H.activationDate IS NOT NULL AND H.activationDate BETWEEN :startTime AND :endTime";
            Query<HomeViewingPackageInstance> query = this.session.createQuery(hql, HomeViewingPackageInstance.class);
            query.setParameter("mainActor", "%" +mainActor+ "%");
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            return query.list();
        }




            public void notifyUser(HomeViewingPackageInstance event )
            {
                // Implement your notification logic here, like sending an email or SMS
            System.out.println("Notifying user about event:"+((event.getOwner()).getEmail()));

            }


    }