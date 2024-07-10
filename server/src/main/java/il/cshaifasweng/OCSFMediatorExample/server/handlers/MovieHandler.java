package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class MovieHandler extends MessageHandler
{
    private MovieMessage message;

    public MovieHandler(MovieMessage message, ConnectionToClient client)
    {
        super(client);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case ADD_MOVIE -> add_movie();
            case DELETE_MOVIE -> delete_movie();
            case GET_ALL_MOVIES -> get_all_movies();
            case UPDATE_MOVIE -> update_movie();}
    }

    private void add_movie()
    {

    }
    private void delete_movie()
    {

    }
    private void get_all_movies()
    {

    }
    private void update_movie()
    {

    }
}
