package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.SeatMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

public class SeatHandler extends MessageHandler
{
    private SeatMessage message;

    public SeatHandler(SeatMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
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
    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
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
