package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

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
            case GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME -> get_all_movie_instances_by_theater_name();
            case GET_ALL_MOVIE_INSTANCES_BY_GENRE -> get_all_movie_instances_by_genre();
            case GET_ALL_MOVIE_INSTANCES -> get_all_movie_instances();
            case GET_ALL_MOVIE_INSTANCES_BY_DATE -> get_all_movie_instances_by_date();
            case GET_ALL_MOVIE_INSTANCES_BY_NAME -> get_all_movie_instances_by_name();
        }
    }
    private void add_movie_intance()
    {
        if(message.movies.getFirst()!=null)
        {
            session.save(message.movies.getFirst());
            session.flush();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_ADDED;
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
        //?????
    }
    private void update_movie_instance()
    {
        if(message.movies.getFirst()!=null)
        {
            session.update(message.movies.getFirst());
            session.flush();
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_UPDATED;
        }
            message.responseType = MovieInstanceMessage.ResponseType.MOVIE_INSTANCE_MESSAGE_FAILED;
    }
    private void get_all_movie_instances_by_theater_name()
    {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where hall.theater.location = :theater", MovieInstance.class);
        query.setParameter("theater",message.key);

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
    private void get_all_movie_instances_by_date()
    {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where time = :time", MovieInstance.class);
        query.setParameter("time",message.date);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;
    }
    private void get_all_movie_instances_by_name()
    {
        Query<MovieInstance> query = session.createQuery("FROM MovieInstance where movie.hebrewName = :hebrew", MovieInstance.class);
        query.setParameter("hebrew",message.key);

        message.movies = query.list();
        message.responseType = MovieInstanceMessage.ResponseType.FILLTERD_LIST;
    }

}
