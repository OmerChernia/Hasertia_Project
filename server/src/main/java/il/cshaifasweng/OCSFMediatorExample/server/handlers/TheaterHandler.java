package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.TheaterMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class TheaterHandler extends MessageHandler
{
    TheaterMessage message;

    public TheaterHandler(TheaterMessage message, ConnectionToClient client)
    {
        super(client);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case GET_ALL_THEATERS -> get_all_theaters();
            case GET_THEATER -> get_theater();
        }
    }

    private void get_all_theaters()
    {

    }
    private void get_theater()
    {

    }
}
