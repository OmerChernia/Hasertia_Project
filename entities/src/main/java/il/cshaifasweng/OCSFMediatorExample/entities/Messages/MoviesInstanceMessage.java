package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;

import java.util.ArrayList;
import java.util.Date;

public class MoviesInstanceMessage extends Message
{
    ArrayList<MovieInstance> movies;
    String theater_name;
    String movie_name;
    String genre;
    int id;
    Date date;
    RequestType requestType;
    ResponseType responseType;

    public MoviesInstanceMessage(MessageType messageType,  RequestType requestType , MovieInstance movieInstance)
    {
        //ADD_MOVIE_INSTANCE,UPDATE_MOVIE_INSTANCE

        super(messageType);
        movies = new ArrayList<>();
        movies.add(movieInstance);
        //this.theater_name = movieInstance.getTheater();
        this.requestType = requestType;
    }
    public MoviesInstanceMessage(MessageType messageType,  RequestType requestType, String theater_name, String movie_name)
    {
        //GET_MOVIE_INSTANCE,DELETE_MOVIE_INSTANCE

        super(messageType);
        this.theater_name = theater_name;
        this.movie_name = movie_name;
        this.requestType = requestType;
    }
    public MoviesInstanceMessage(MessageType messageType,  RequestType requestType)
    {
        // GET_ALL_MOVIE_INSTANCES
        super(messageType);
        this.requestType = requestType;
    }
    public MoviesInstanceMessage(MessageType messageType,  RequestType requestType,String theater_name)
    {
        // GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME,
        super(messageType);
        this.requestType = requestType;
        this.theater_name = theater_name;
    }
    public MoviesInstanceMessage(MessageType messageType, String genre,  RequestType requestType)
    {
        // GET_ALL_MOVIE_INSTANCES_BY_GENRE
        super(messageType);
        this.requestType = requestType;
        this.genre = genre;
    }
    public MoviesInstanceMessage(MessageType messageType,  RequestType requestType, Date date)
    {
        // GET_ALL_MOVIE_INSTANCES_BY_DATE
        super(messageType);
        this.requestType = requestType;
        this.date = date;
    }

    public enum ResponseType
    {
        FILLTERD_LIST,
        MOVIE_INSTANCE_ADDED,
        MOVIE_INSTANCE_REMOVED,
        MOVIE_INSTANCE_UPDATED,
        MOVIE_INSTANCE
        
    }
    public enum RequestType
    {
        ADD_MOVIE_INSTANCE,
        GET_MOVIE_INSTANCE,
        DELETE_MOVIE_INSTANCE,
        UPDATE_MOVIE_INSTANCE,
        GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME,
        GET_ALL_MOVIE_INSTANCES_BY_GENRE,
        GET_ALL_MOVIE_INSTANCES,
        GET_ALL_MOVIE_INSTANCES_BY_DATE


    }
}
