package il.cshaifasweng.OCSFMediatorExample.client.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage.ResponseType;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer.HomeBoundary;

import java.io.IOException;
import java.util.List;

public class MovieHandler extends MessageHandler {

    private HomeBoundary homeController;

    public MovieHandler(HomeBoundary homeController) {
        this.homeController = homeController;
    }

    @Override
    public void handle(Message message) {

    }

    @Override
    public void handle(MovieMessage message) throws IOException {
        if (message.messageType == Message.MessageType.RESPONSE && message.responseType == ResponseType.RETURN_MOVIES) {
            List<Movie> movies = message.movies;
            homeController.setItems(movies);
            // Handle other response types if needed
            System.out.println("Received different response type: " + message.responseType);
        }
    }
}
