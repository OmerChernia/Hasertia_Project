package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.server.scheduler.LinkScheduler;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.events.MovieInstanceCanceledEvent;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.scheduler.OrderScheduler;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class MovieInstanceHandler extends MessageHandler {

    private MovieInstanceMessage message;

    public MovieInstanceHandler(MovieInstanceMessage message, ConnectionToClient client, Session session) {
        super(client, session);
        this.message = message;
    }

    @Override
    public void setMessageTypeToResponse() {
        message.messageType = MovieInstanceMessage.MessageType.RESPONSE;
    }

    public void handleMessage() {
        switch (message.requestType) {
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

    private void add_movie_instance() {
        if (message.movies != null && !message.movies.isEmpty()) {
            MovieInstance movieInstance = message.movies.get(0);
            session.save(movieInstance);
            session.flush();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_ADDED;

            if (!movieInstance.getMovie().getNotificationSent()) {
                movieInstance.getMovie().setNotificationSent(true);
                session.update(movieInstance.getMovie());
                session.flush();
            }

            // Schedule email notifications
            scheduleEmailNotifications(movieInstance);
        } else {
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
        }
    }

    private void delete_movie_instance() {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where id = :id", MovieInstance.class);
        query.setParameter("id", message.id);

        MovieInstance movieInstance = query.uniqueResult();
        if (movieInstance != null) {
            movieInstance.setIsActive(false);
            session.update(movieInstance);

            message.movies.add(movieInstance);

            // Notify clients about cancellation and schedule cancellation emails
            notifyAndScheduleCancellation(movieInstance);

            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_REMOVED;
        } else {
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
        }
    }

    private void update_movie_instance() {
        try {
            MovieInstance mergedInstance = (MovieInstance) session.merge(message.movies.get(0));
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

    private void notifyAndScheduleCancellation(MovieInstance movieInstance) {
        // Notify all clients about the movie instance cancellation
        SimpleServer.getServer().sendToAllClients(new MovieInstanceCanceledEvent(movieInstance));

        // Schedule email notifications for canceled screenings
        scheduleEmailNotifications(movieInstance);
    }




    private void scheduleEmailNotifications(MovieInstance movieInstance) {
        Query<Object[]> emailQuery = session.createQuery(
                "SELECT mt.owner.email, mt.owner.name FROM MovieTicket mt " +
                        "WHERE mt.movieInstance.id = :movieInstanceId AND mt.isActive = true",
                Object[].class
        );
        emailQuery.setParameter("movieInstanceId", movieInstance.getId());

        List<Object[]> customerData = emailQuery.list();

        // Pass the customer data and movie instance info to the scheduler
        OrderScheduler.getInstance().scheduleEmailsForCanceledScreening(customerData, movieInstance);
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

    private void get_movie_instance_by_id() {
        try {
            MovieInstance movieInstance = session.get(MovieInstance.class, message.id);
            if (movieInstance != null) {
                message.movies.add(movieInstance);
                message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE;
            } else {
                message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    private void get_all_movie_instances_by_movie_id() {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.id = :movieId AND isActive = true", MovieInstance.class);
        query.setParameter("movieId", message.id);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILTERED_LIST;
    }

    private void filter_movie_instances_by_theater_name() {
        try {
            // Filter the existing movie instances by theater name
            if (message.movies == null || message.movies.isEmpty()) {
                System.out.println("No movie instances available to filter by theater name.");
                return;
            }

            String theaterName = message.theaterName;
            message.movies.removeIf(movieInstance ->
                    !movieInstance.getHall().getTheater().getLocation().equals(theaterName)
            );

            if (message.movies.isEmpty()) {
                System.out.println("No movie instances found in the theater: " + theaterName);
            } else {
                System.out.println("Filtered movie instances found: " + message.movies.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    private void get_all_movie_instances_by_movie_id_and_theater_name() {
        get_all_movie_instances_by_movie_id();
        filter_movie_instances_by_theater_name();
    }

    private void get_all_movie_instances_by_genre() {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.genre = :genre AND isActive = true", MovieInstance.class);
        query.setParameter("genre", message.key);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILTERED_LIST;
    }

    private void get_all_movie_instances() {
        try {
            // Create an HQL query to fetch all active movie instances
            Query<MovieInstance> query = session.createQuery("FROM MovieInstance where isActive = true", MovieInstance.class);
            // Execute the query and get the result list
            message.movies = query.getResultList();

            // Set the response type
            message.responseType = MovieInstanceMessage.ResponseType.FILTERED_LIST;

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    public void get_all_movie_instances_by_movie_theater_id_and_date() {
        try {
            // Create an HQL query using the Session
            String hql = "FROM MovieInstance mi WHERE mi.movie.id = :movieId AND mi.hall.theater.location = :theaterName AND mi.time BETWEEN :startOfDay AND :endOfDay AND mi.isActive = true";
            Query<MovieInstance> query = session.createQuery(hql, MovieInstance.class);
            query.setParameter("movieId", message.id);
            query.setParameter("theaterName", message.theaterName);
            query.setParameter("startOfDay", message.date.toLocalDate().atStartOfDay());
            query.setParameter("endOfDay", message.date.toLocalDate().atTime(23, 59, 59));

            // Execute the query and set the result list in the message
            message.movies = query.list();

            if (message.movies.isEmpty()) {
                System.out.println("Empty movies");
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    private void get_movie_instance_after_selection() {
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

    private void getMovieInstancesBetweenDates() {
        StringBuilder queryString = new StringBuilder(
                "FROM MovieInstance WHERE movie.available = :available AND isActive = true"
        );

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (message.afterDate != null) {
            startDateTime = message.afterDate.atStartOfDay();
            queryString.append(" AND time >= :startDateTime");
        }

        if (message.beforeDate != null) {
            endDateTime = message.beforeDate.plusDays(1).atStartOfDay();
            queryString.append(" AND time < :endDateTime");
        }

        Query<MovieInstance> query = session.createQuery(queryString.toString(), MovieInstance.class);
        query.setParameter("available", Movie.Availability.AVAILABLE);

        if (startDateTime != null) {
            query.setParameter("startDateTime", startDateTime);
        }

        if (endDateTime != null) {
            query.setParameter("endDateTime", endDateTime);
        }

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILTERED_LIST;
    }

    private void get_all_movie_instances_by_name() {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.hebrewName = :hebrew AND isActive = true", MovieInstance.class);
        query.setParameter("hebrew", message.key);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILTERED_LIST;
    }

    private void get_all_movie_instances_by_theater_name() {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where hall.theater.location = :theater AND movie.available = :available AND isActive = true", MovieInstance.class);
        query.setParameter("theater", message.key);
        query.setParameter("available", Movie.Availability.AVAILABLE);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILTERED_LIST;
    }
}
