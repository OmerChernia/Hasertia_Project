package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;

import java.util.ArrayList;

public class MovieMessage extends Message
{
    ArrayList<Movie> movies;
    int id;
    Movie new_movie;

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
