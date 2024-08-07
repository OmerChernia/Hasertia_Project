package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage.RequestType;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;

public class MovieController {

    public void requestAllMovies() {
        MovieMessage requestMessage = new MovieMessage(MessageType.REQUEST, RequestType.GET_ALL_MOVIES);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void addMovie(Movie movie) {
        MovieMessage requestMessage = new MovieMessage(MessageType.REQUEST, movie, RequestType.ADD_MOVIE);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void updateMovie(Movie movie) {
        MovieMessage requestMessage = new MovieMessage(MessageType.REQUEST, movie, RequestType.UPDATE_MOVIE);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void deleteMovie(int id) {
        MovieMessage requestMessage = new MovieMessage(MessageType.REQUEST, RequestType.DEACTIVATE_MOVIE, id);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
