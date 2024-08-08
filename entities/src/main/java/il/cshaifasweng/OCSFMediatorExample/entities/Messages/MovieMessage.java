package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieMessage extends Message
{
    public List<Movie> movies;
    public int id;
    public RequestType requestType;
    public ResponseType responseType;

    public MovieMessage(MessageType messageType,Movie movie,RequestType requestType)
    {
        // ADD_MOVIE ,UPDATE_MOVIE
        super(messageType);
        movies = new ArrayList<>();
        movies.add(movie);
        this.requestType = requestType;

    }
    public MovieMessage(MessageType messageType,RequestType requestType)
    {
        // GET_ALL_MOVIES
        super(messageType);
        this.requestType = requestType;
        System.out.println(movies.toString());
    }
    public MovieMessage(MessageType messageType,RequestType requestType, int id)
    {
        // DELETE_MOVIE
        super(messageType);
        this.requestType = requestType;
        this.id = id;
    }

    public enum ResponseType
    {
        MOVIE_ADDED,
        MOVIE_UPDATED,
        MOVIE_DELETED,
        MOVIE_NOT_ADDED,
        MOVIE_NOT_UPDATED,
        MOVIE_NOT_DELETED,
        RETURN_MOVIES,
        MOVIE_MESSAGE_FAILED

    }
    public enum RequestType
    {
        ADD_MOVIE,
        DEACTIVATE_MOVIE,
        GET_ALL_MOVIES,
        UPDATE_MOVIE,
    }

}
