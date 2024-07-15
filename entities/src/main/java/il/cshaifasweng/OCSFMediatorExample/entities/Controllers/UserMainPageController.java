package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;

public class UserMainPageController {

    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        MovieMessage getAllMovies = new MovieMessage(Message.MessageType.REQUEST, MovieMessage.RequestType.GET_ALL_MOVIES);
        String theaterName = "";  //needs to be initialized!!!!!!!!!!
        MoviesInstanceMessage getFilteredMovieByTheater = new MoviesInstanceMessage(Message.MessageType.REQUEST, MoviesInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME, theaterName);
        TheaterMessage getAllTheaters = new TheaterMessage(Message.MessageType.REQUEST, TheaterMessage.RequestType.GET_ALL_THEATERS);
        String movieName = "";  //needs to be initialized!!!!!!!!!!
        MoviesInstanceMessage getMovieFilteredByName = new MoviesInstanceMessage(Message.MessageType.REQUEST, MoviesInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_NAME, movieName);
        MoviesInstanceMessage getMoviesFilteredByDate = new MoviesInstanceMessage(Message.MessageType.REQUEST, MoviesInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_DATE);

        SimpleClient.getClient().sendToServer(getAllMovies);
        SimpleClient.getClient().sendToServer(getFilteredMovieByTheater);
        SimpleClient.getClient().sendToServer(getAllTheaters);
        SimpleClient.getClient().sendToServer(getMovieFilteredByName);
        SimpleClient.getClient().sendToServer(getMoviesFilteredByDate);
    }
}
