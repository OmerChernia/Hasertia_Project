package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;

import java.time.LocalDateTime;

public class MovieInstanceController {

    public static void requestAllMovieInstances() {
        // Create a request to get all movie instances
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES, "");
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void addMovieInstance(int movieId, LocalDateTime dateTime, int hallId) {
        // Since there is no constructor that directly accepts movieId, dateTime, and hallId,
        // use the constructor with an id and handle the details server-side
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.ADD_MOVIE_INSTANCE, movieId);
        requestMessage.date = dateTime;
        requestMessage.key = String.valueOf(hallId); // Reusing the key field to pass the hallId
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void updateMovieInstance(int screeningId, int movieId, LocalDateTime dateTime, int hallId) {
        // Similar to add, but for updating an existing movie instance
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.UPDATE_MOVIE_INSTANCE, screeningId);
        requestMessage.date = dateTime;
        requestMessage.key = movieId + ":" + hallId; // Combining movieId and hallId in the key
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void deleteMovieInstance(int id) {
        // Create a request to delete a movie instance by ID
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.DELETE_MOVIE_INSTANCE, id);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstanceById(int id) {
        // Create a request to get a movie instance by ID
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_MOVIE_INSTANCE, id);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstancesByTheaterName(String theaterName) {
        // Create a request to get all movie instances by theater name
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME, theaterName);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstancesByGenre(String genre) {
        // Create a request to get all movie instances by genre
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_GENRE, genre);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstancesByDate(LocalDateTime date) {
        // Create a request to get all movie instances by date
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_DATE, date);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstancesByName(String movieName) {
        // Create a request to get all movie instances by movie name
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_NAME, movieName);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
