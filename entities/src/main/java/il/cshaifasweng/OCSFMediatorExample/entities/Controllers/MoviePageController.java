package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MoviesInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.TheaterMessage;

import java.io.IOException;

public class MoviePageController {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        int id=0;  //NEEDS TO BE INITIALIZED!!!!
        MovieMessage setPage = new MovieMessage(Message.MessageType.REQUEST, MovieMessage.RequestType.GET_ALL_MOVIES, id);
        SimpleClient.getClient().sendToServer(setPage);
    }
}
