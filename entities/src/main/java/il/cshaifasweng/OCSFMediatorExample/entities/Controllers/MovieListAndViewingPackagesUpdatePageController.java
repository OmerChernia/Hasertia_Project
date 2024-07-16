package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;

import java.io.IOException;

public class MovieListAndViewingPackagesUpdatePageController {
    private static SimpleClient client;

    public Movie[] getAllMovies() {
        MovieMessage movieMessage = new MovieMessage(Message.MessageType.REQUEST, MovieMessage.RequestType.GET_ALL_MOVIES);
        try {
            SimpleClient.getClient().sendToServer(movieMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Movie[0];
    }

    public Movie[] getAllHomeViewingPackages() {
        MovieMessage movieMessage = new MovieMessage(Message.MessageType.REQUEST, MovieMessage.RequestType.GET_ALL_HOME_VIEWING_PACKAGES);
        try {
            SimpleClient.getClient().sendToServer(movieMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Movie[0];
    }

    public void addNewMovie(String[] movieInfo) {
        Movie newMovie = new Movie(); // You should set the movie info here
        MovieMessage movieMessage = new MovieMessage(Message.MessageType.REQUEST, newMovie, MovieMessage.RequestType.ADD_MOVIE);
        try {
            SimpleClient.getClient().sendToServer(movieMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMovie(String[] movieInfo) {
        Movie updatedMovie = new Movie(); // You should set the updated movie info here
        MovieMessage movieMessage = new MovieMessage(Message.MessageType.REQUEST, updatedMovie, MovieMessage.RequestType.UPDATE_MOVIE);
        try {
            SimpleClient.getClient().sendToServer(movieMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(Movie movie) {
        MovieMessage movieMessage = new MovieMessage(Message.MessageType.REQUEST, movie, MovieMessage.RequestType.DELETE_MOVIE);
        try {
            SimpleClient.getClient().sendToServer(movieMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMassageForPriceChange(Movie movie, String new_price) {
        MovieMessage movieMessage = new MovieMessage(Message.MessageType.REQUEST, MovieMessage.RequestType.SEND_MESSAGE_FOR_PRICE_CHANGE, movie, new_price);
        try {
            SimpleClient.getClient().sendToServer(movieMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        MovieListAndViewingPackagesUpdatePageController controller = new MovieListAndViewingPackagesUpdatePageController();
    }
}