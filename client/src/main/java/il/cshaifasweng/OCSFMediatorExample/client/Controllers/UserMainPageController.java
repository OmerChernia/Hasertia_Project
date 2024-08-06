package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.Connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;

public class UserMainPageController {

    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        MovieMessage getAllMovies = new MovieMessage(Message.MessageType.REQUEST, MovieMessage.RequestType.GET_ALL_MOVIES);
        String theaterName = "";  //needs to be initialized!!!!!!!!!!
        MovieInstanceMessage getFilteredMovieByTheater = new MovieInstanceMessage(Message.MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME, theaterName);
        TheaterMessage getAllTheaters = new TheaterMessage(Message.MessageType.REQUEST, TheaterMessage.RequestType.GET_ALL_THEATERS);
        String movieName = "";  //needs to be initialized!!!!!!!!!!
        MovieInstanceMessage getMovieFilteredByName = new MovieInstanceMessage(Message.MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_NAME, movieName);
       // MovieInstanceMessage getMoviesFilteredByDate = new MovieInstanceMessage(Message.MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_DATE);

        SimpleClient.getClient().sendToServer(getAllMovies);
        SimpleClient.getClient().sendToServer(getFilteredMovieByTheater);
        SimpleClient.getClient().sendToServer(getAllTheaters);
        SimpleClient.getClient().sendToServer(getMovieFilteredByName);
       // SimpleClient.getClient().sendToServer(getMoviesFilteredByDate);
    }
}
