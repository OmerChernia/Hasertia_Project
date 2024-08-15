package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

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
            case ADD_MOVIE_INSTANCE -> add_movie_intance();
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

    private void add_movie_intance()
    {
        if(message.movies.getFirst()!=null)
        {
            session.save(message.movies.getFirst());
            session.flush();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_ADDED;
            if(!message.movies.getFirst().getMovie().getNotificationSent())
            {
                message.movies.getFirst().getMovie().setNotificationSent(true);
                session.update(message.movies.getFirst().getMovie());
                session.flush();
            }
        }
        else
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;

    }
    private void get_movie_instance_by_id()
    {
        try {
            // Create an HQL query to fetch all complaints
            Query<MovieInstance> query = session.createQuery("FROM MovieInstance where id = :id", MovieInstance.class);
            query.setParameter("id", message.id);
            // Execute the query and get the result list
            MovieInstance movieInstance = query.uniqueResult();
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
    private void delete_movie_instance()
    {
        // Create an HQL query to fetch all complaints
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where id = :id", MovieInstance.class);
        query.setParameter("id", message.id);

        MovieInstance movieInstance = query.uniqueResult();

        movieInstance.setIsActive(false);
        session.update(movieInstance);
        session.flush();
        message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_REMOVED;
    }
    private void update_movie_instance() {
        try {
            // Get the ID from the incoming message
            int screeningId = message.id;

            // Retrieve the current persistent instance of the movie from the session
            MovieInstance persistentMovieInstance = session.get(MovieInstance.class,screeningId);

            if (persistentMovieInstance != null) {
                // Update the persistent with the new values from the message
                persistentMovieInstance.setTime(message.date);

                // Save the changes
                session.update(persistentMovieInstance);
                session.flush();
                message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_UPDATED;
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
    private void get_all_movie_instances_by_movie_id()
    {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.id = :movie", MovieInstance.class);
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
            Query<MovieInstance> query = session.createQuery("FROM MovieInstance ", MovieInstance.class);
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
