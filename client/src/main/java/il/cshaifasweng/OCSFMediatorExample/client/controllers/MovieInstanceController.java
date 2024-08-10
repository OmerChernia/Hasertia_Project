package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;

import java.time.LocalDateTime;

public class MovieInstanceController {

    public static void requestAllMovieInstances() {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES, "");
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void addMovieInstance(MovieInstance movieInstance) {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.ADD_MOVIE_INSTANCE, movieInstance);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void updateMovieInstance(MovieInstance movieInstance) {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.UPDATE_MOVIE_INSTANCE, movieInstance);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void deleteMovieInstance(int id) {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.DELETE_MOVIE_INSTANCE, id);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstanceById(int id) {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_MOVIE_INSTANCE, id);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstancesByTheaterName(String theaterName) {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME, theaterName);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstancesByGenre(String genre) {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_GENRE, genre);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstancesByDate(LocalDateTime date) {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_DATE, date);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestMovieInstancesByName(String movieName) {
        MovieInstanceMessage requestMessage = new MovieInstanceMessage(MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_NAME, movieName);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
