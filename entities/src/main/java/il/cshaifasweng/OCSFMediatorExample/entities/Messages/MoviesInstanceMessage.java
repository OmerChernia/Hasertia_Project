package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;

import java.util.ArrayList;

public class MoviesInstanceMessage extends Message
{
    ArrayList<MovieInstance> movies;
    String city;
    String name;
    String genre;
    String id;

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
