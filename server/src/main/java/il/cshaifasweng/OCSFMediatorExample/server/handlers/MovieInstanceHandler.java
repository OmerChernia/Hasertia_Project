package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
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

        // Fetch all tickets for the movie instance
        Query<MovieTicket> queryMovieTickets = session.createQuery("FROM MovieTicket where movieInstance = :movie", MovieTicket.class);
        queryMovieTickets.setParameter("movie", movieInstance);

        List<MovieTicket> movieTickets = queryMovieTickets.list();

        // Prepare the list of emails to be sent
        List<Runnable> emailTasks = new ArrayList<>();

        for (MovieTicket movieTicket : movieTickets) {
            // Setting movie ticket to not be active anymore
            movieTicket.setisActive(false);
            session.update(movieTicket);

            // Lower taken array in seat
            Seat seat = session.get(Seat.class, movieTicket.getSeat().getId());
            seat.deleteMovieInstance(movieInstance);
            session.update(seat);

            // Prepare the email task
            emailTasks.add(() -> {
                EmailSender.sendEmail(movieTicket.getOwner().getEmail(), "Canceled ticket from Hasertia",
                        String.format("Dear %s, your ticket for the movie '%s' has been canceled. We apologize for the inconvenience.",
                                movieTicket.getOwner().getName(),
                                movieInstance.getMovie().getEnglishName()));
            });
        }

        // Flush all updates at once
        session.flush();

        // Execute email tasks asynchronously
        for (Runnable task : emailTasks) {
            new Thread(task).start(); // You can use an ExecutorService for more controlled async processing
        }

        message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_REMOVED;
    }

    private void update_movie_instance() {
        try {
            //need to implement what to do with the seats associated with the movie instance: delete them or change them???

            MovieInstance mergedInstance = (MovieInstance) session.merge(message.movies.getFirst());
            session.flush();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_UPDATED;
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
