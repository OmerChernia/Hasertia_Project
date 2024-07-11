package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

public class MovieInstanceHandler extends MessageHandler
{
    private MovieInstanceMessage message;

    public MovieInstanceHandler(MovieInstanceMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case ADD_MOVIE_INSTANCE -> add_movie_intance();
            case GET_MOVIE_INSTANCE -> get_movie_instance();
            case DELETE_MOVIE_INSTANCE -> delete_movie_intance();
            case UPDATE_MOVIE_INSTANCE -> update_movie_instance();
            case GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME -> get_all_movie_instances_by_theater_name();
            case GET_ALL_MOVIE_INSTANCES_BY_GENRE -> get_all_movie_instances_by_genre();
            case GET_ALL_MOVIE_INSTANCES -> get_all_movie_instances();
            case GET_ALL_MOVIE_INSTANCES_BY_DATE -> get_all_movie_instances_by_date();
        }
    }
    private void add_movie_intance()
    {

    }
    private void get_movie_instance()
    {

    }
    private void delete_movie_intance()
    {

    }
    private void update_movie_instance()
    {

    }
    private void get_all_movie_instances_by_theater_name()
    {

    }
    private void get_all_movie_instances_by_genre()
    {

    }
    private void get_all_movie_instances()
    {

    }
    private void get_all_movie_instances_by_date()
    {

    }

}
