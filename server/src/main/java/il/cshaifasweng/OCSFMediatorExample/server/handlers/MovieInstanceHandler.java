package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import il.cshaifasweng.OCSFMediatorExample.server.scheduler.LinkAndInstanceScheduler;
import il.cshaifasweng.OCSFMediatorExample.server.scheduler.OrderScheduler;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieInstanceHandler extends MessageHandler
{
    private MovieInstanceMessage message;

    public MovieInstanceHandler(MovieInstanceMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }
    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case ADD_MOVIE_INSTANCE -> add_movie_instance();
            case GET_MOVIE_INSTANCE -> get_movie_instance_by_id();
            case DELETE_MOVIE_INSTANCE -> delete_movie_instance();
            case UPDATE_MOVIE_INSTANCE -> update_movie_instance();
            case GET_ALL_MOVIE_INSTANCES_BY_MOVIE_ID -> get_all_movie_instances_by_movie_id();
            case GET_ALL_MOVIE_INSTANCES_BY_MOVIE_ID_AND_THEATER_NAME -> get_all_movie_instances_by_movie_id_and_theater_name();
            case GET_ALL_MOVIE_INSTANCES_BY_GENRE -> get_all_movie_instances_by_genre();
            case GET_ALL_MOVIE_INSTANCES -> get_all_movie_instances();
            case GET_ALL_MOVIE_INSTANCES_BY_MOVIE_ID_THEATER_ID_DATE -> get_all_movie_instances_by_movie_theater_id_and_date();
            case GET_ALL_MOVIE_INSTANCES_BY_NAME -> get_all_movie_instances_by_name();
            case GET_MOVIE_INSTANCE_AFTER_SELECTION -> get_movie_instance_after_selection();
            case GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME -> get_all_movie_instances_by_theater_name();
            case GET_MOVIE_INSTANCES_BETWEEN_DATES -> getMovieInstancesBetweenDates();

        }
    }

    private void getMovieInstancesBetweenDates() {
        StringBuilder queryString = new StringBuilder(
                "FROM MovieInstance WHERE movie.available = :available"
        );

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (message.afterDate != null) {
            startDateTime = message.afterDate.atStartOfDay(); // Start of the afterDate
            queryString.append(" AND time >= :startDateTime");
        }

        if (message.beforeDate != null) {
            endDateTime = message.beforeDate.plusDays(1).atStartOfDay(); // Start of the day after beforeDate
            queryString.append(" AND time < :endDateTime");
        }

        Query<MovieInstance> query = session.createQuery(queryString.toString(), MovieInstance.class);
        query.setParameter("available", Movie.Availability.AVAILABLE);

        if (startDateTime != null) {
            query.setParameter("startDateTime",startDateTime );
        }

        if (endDateTime != null) {
            query.setParameter("endDateTime",endDateTime );
        }

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;
    }


    private void get_all_movie_instances_by_theater_name() {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where hall.theater.location= :theater and movie.available =: available", MovieInstance.class);
        query.setParameter("theater",message.key);
        query.setParameter("available",Movie.Availability.AVAILABLE);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;
    }

    private void get_movie_instance_after_selection() {
        System.out.println("print get_movie_instance_after_selection");
        System.out.println(message.date);
        System.out.println(message.id);
        System.out.println(message.theaterName);

        Query<MovieInstance> query = session.createNativeQuery(
                "SELECT * FROM Movie_Instances WHERE movie_id = :movie AND hall_id IN (SELECT id FROM Halls WHERE theater_id = (SELECT id FROM Theaters WHERE location = :theater)) AND DATE_FORMAT(time, '%Y-%m-%d %H:%i') = DATE_FORMAT(:dateTime, '%Y-%m-%d %H:%i')",
                MovieInstance.class
        );
        query.setParameter("movie", message.id);
        query.setParameter("theater", message.theaterName);
        query.setParameter("dateTime", message.date.toString());

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE;
    }



    private void get_all_movie_instances_by_movie_id_and_theater_name()
    {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.id = :movie and hall.theater.location= :theater and movie.available =:available", MovieInstance.class);
        query.setParameter("movie",message.id);
        query.setParameter("theater",message.theaterName);
        query.setParameter("available", Movie.Availability.AVAILABLE);
        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;
    }

    private void add_movie_instance() {
        if (message.movies.getFirst() != null) {
            session.save(message.movies.getFirst());
            session.flush();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_ADDED;

            // Schedule the movie instance for deactivation after it ends
            LinkAndInstanceScheduler.getInstance().scheduleMovieInstanceDeactivation(message.movies.getFirst());

            Movie movie = message.movies.getFirst().getMovie();

            // Load the movie from the session to avoid NonUniqueObjectException
            movie = (Movie) session.get(Movie.class, movie.getId());

            if (!movie.getNotificationSent()) {
                // Set the notification flag to true
                movie.setNotificationSent(true);
                session.update(movie);
                session.flush();

                // Fetch users with MultiEntryTickets
                List<RegisteredUser> usersWithMultiEntryTickets = getUsersWithMultiEntryTickets();

                // Schedule to notify users about the new movie one day before the screening
                OrderScheduler.getInstance().scheduleNotifyNewMovieOneDayBefore(
                        movie,
                        usersWithMultiEntryTickets,
                        message.movies.getFirst().getTime()
                );
            }
        } else {
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
        }
    }



    /**
     * Fetches all users who have purchased MultiEntryTickets.
     *
     * @return a list of registered users who own MultiEntryTickets.
     */
    public List<RegisteredUser> getUsersWithMultiEntryTickets() {
        List<RegisteredUser> users = null;
        try (Session session = SimpleServer.session.getSession().getSessionFactory().openSession()) {
            // Create an HQL query to fetch users who have purchased MultiEntryTickets
            String hql = "SELECT DISTINCT p.owner " +
                    "FROM Purchase p " +
                    "WHERE TYPE(p) = il.cshaifasweng.OCSFMediatorExample.entities.MultiEntryTicket";

            Query<RegisteredUser> query = session.createQuery(hql, RegisteredUser.class);
            users = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }


    private void get_movie_instance_by_id()
    {
        try
        {
            MovieInstance movieInstance = session.get(MovieInstance.class, message.id);
            message.movies.add(movieInstance);

            // Set the response type
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE;
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }



    private void delete_movie_instance() {
        // Load the movie instance
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where id = :id", MovieInstance.class);
        query.setParameter("id", message.id);

        MovieInstance movieInstance = query.uniqueResult();
        movieInstance.setIsActive(false);
        session.update(movieInstance);

        message.movies.add(movieInstance);

        // Fetch all tickets for the movie instance
        Query<MovieTicket> queryMovieTickets = session.createQuery("FROM MovieTicket where movieInstance = :movie", MovieTicket.class);
        queryMovieTickets.setParameter("movie", movieInstance);

        List<MovieTicket> movieTickets = queryMovieTickets.list();

        for (MovieTicket movieTicket : movieTickets) {
            // Setting movie ticket to not be active anymore
            movieTicket.setisActive(false);
            session.update(movieTicket);

            // Lower taken array in seat
            Seat seat = session.get(Seat.class, movieTicket.getSeat().getId());
            seat.deleteMovieInstance(movieInstance);
            session.update(seat);
        }

        // Flush all updates at once
        session.flush();

        // Schedule emails for canceled screening
        OrderScheduler.getInstance().scheduleEmailsForCanceledScreening(movieTickets, movieInstance);
        OrderScheduler.getInstance().cancelScheduledNotifyNewMovie(movieInstance.getMovie());
        LinkAndInstanceScheduler.getInstance().cancelMovieInstanceTasks(movieInstance);

        message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_REMOVED;
    }



    private void update_movie_instance() {
        try {

            MovieInstance mergedInstance = (MovieInstance) session.merge(message.movies.getFirst());
            session.flush();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_UPDATED;
            // Schedule email notifications for updates
            scheduleUpdateNotifications(mergedInstance);
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    private void scheduleUpdateNotifications(MovieInstance movieInstance) {
        Query<Object[]> emailQuery = session.createQuery(
                "SELECT mt.owner.email, mt.owner.name FROM MovieTicket mt " +
                        "WHERE mt.movieInstance.id = :movieInstanceId AND mt.isActive = true",
                Object[].class
        );
        emailQuery.setParameter("movieInstanceId", movieInstance.getId());

        List<Object[]> customerData = emailQuery.list();

        // Pass the customer data and movie instance info to the scheduler
        OrderScheduler.getInstance().scheduleEmailsForUpdatedScreening(customerData, movieInstance);
    }



    private void get_all_movie_instances_by_movie_id()
    {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.id = :movie and isActive=true", MovieInstance.class);
        query.setParameter("movie",message.id);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;
    }
    private void get_all_movie_instances_by_genre()
    {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.genre = :genre", MovieInstance.class);
        query.setParameter("genre",message.key);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;
    }
    private void get_all_movie_instances()
    {
        try {
            // Create an HQL query to fetch all complaints
            Query<MovieInstance> query = session.createQuery("FROM MovieInstance where isActive = true", MovieInstance.class);
            // Execute the query and get the result list
            message.movies = query.getResultList();

            // Set the response type
            message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }
    private void get_all_movie_instances_by_movie_theater_id_and_date() {
        get_all_movie_instances_by_movie_id_and_theater_name();
        if (message.movies == null) {
            System.out.println("Empty movies ");
            return;
        }
        // Use an iterator to safely remove elements from the list
        Iterator<MovieInstance> iterator = message.movies.iterator();
        while (iterator.hasNext()) {
            MovieInstance movie = iterator.next();
            System.out.println(movie.getId());
            if (!movie.getTime().toLocalDate().equals(message.date.toLocalDate())) {
                iterator.remove();
            }
        }
    }

    private void get_all_movie_instances_by_name()
    {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.hebrewName = :hebrew", MovieInstance.class);
        query.setParameter("hebrew",message.key);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;
    }

}
