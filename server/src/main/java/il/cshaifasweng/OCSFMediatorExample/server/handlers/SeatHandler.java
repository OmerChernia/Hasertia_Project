package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.SeatMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class SeatHandler extends MessageHandler
{
    private SeatMessage message;

    public SeatHandler(SeatMessage message, ConnectionToClient client)
    {
        super(client);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case SEAT_RESERVED -> seat_reserved();
            case SEAT_CANCELATION -> seat_cancelation();
            case GET_ALL_SEAT_BY_HALL -> get_all_seat_by_hall();
        }
    }

    private void seat_reserved()
    {

    }
    private void seat_cancelation()
    {

    }
    private void get_all_seat_by_hall()
    {

    }
}
