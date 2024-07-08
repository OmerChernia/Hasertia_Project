package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;

import java.util.ArrayList;

public class MovieMessage extends Message
{
    ArrayList<Movie> movies;
    int id;
    Movie new_movie;
    RequestType requestType;
    ResponseType responseType;

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
    }
    public MovieMessage(MessageType messageType,RequestType requestType, int id)
    {
        // GET_ALL_MOVIES
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
        RETURN_MOVIES
    }
    public enum RequestType
    {
        ADD_MOVIE,
        DELETE_MOVIE,
        GET_ALL_MOVIES,
        UPDATE_MOVIE,
    }

}
