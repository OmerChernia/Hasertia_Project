package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MovieHandler extends MessageHandler
{
    private MovieMessage message;

    public MovieHandler(MovieMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case ADD_MOVIE -> add_movie();
            case DEACTIVATE_MOVIE -> deactivate_movie();
            case GET_ALL_MOVIES -> get_all_movies();
            case UPDATE_MOVIE -> update_movie();
        }
    }
    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    private void add_movie()
    {
        if(message.movies.getFirst() != null)
        {
            // Create an HQL query to fetch all complaints
            // Searching if the movie is existed in DB
            Query<Movie> query = session.createQuery("FROM Movie WHERE englishName = :_englishName or hebrewName = :_hebrewName", Movie.class);
            query.setParameter("_englishName", message.movies.getFirst().getEnglishName());
            query.setParameter("_hebrewName", message.movies.getFirst().getHebrewName());

            List<Movie> movies = query.list();

            if(movies == null) {

                session.save(message.movies.getFirst());
                session.flush();
                message.responseType = MovieMessage.ResponseType.MOVIE_ADDED;
            }
            else // if movie already existed , don't add it
                message.responseType = MovieMessage.ResponseType.MOVIE_NOT_ADDED;
        }
        else // if we don't have any movie to add
            message.responseType = MovieMessage.ResponseType.MOVIE_NOT_ADDED;
    }
    private void deactivate_movie()
    {
        Query<Movie> query = session.createQuery("FROM Movie where id= :_id", Movie.class);
        query.setParameter("_id", message.id);

        Movie movie = query.uniqueResult();

        if(movie != null)
        {
            movie.setActive(false);
            session.update(message.movies.getFirst());
            session.flush();
            message.responseType = MovieMessage.ResponseType.MOVIE_UPDATED;
        }
        else
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;


    }
    private void get_all_movies()
    {
        try {
            // Create an HQL query to fetch all complaints
            Query<Movie> query = session.createQuery("FROM Movie where isActive= :_active", Movie.class);
            query.setParameter("_active", true);
            // Execute the query and get the result list
            message.movies = query.getResultList();

            // Set the response type
            message.responseType = MovieMessage.ResponseType.RETURN_MOVIES;

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }
    private void update_movie()
    {
        // Create an HQL query to fetch all complaints
        // Searching if the movie is existed in DB
        Query<Movie> query = session.createQuery("FROM Movie WHERE id = :_id", Movie.class);
        query.setParameter("_id", message.movies.getFirst().getId());

        Movie movie = query.uniqueResult();

        if(movie != null)
        {
            session.update(message.movies.getFirst());
            session.flush();
            message.responseType = MovieMessage.ResponseType.MOVIE_UPDATED;
        }
        else
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;
    }
}