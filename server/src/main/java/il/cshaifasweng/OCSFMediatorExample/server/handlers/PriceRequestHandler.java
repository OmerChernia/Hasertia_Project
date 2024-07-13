package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PriceRequestMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

public class PriceRequestHandler extends MessageHandler
{
    private PriceRequestMessage message;

    public PriceRequestHandler(PriceRequestMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }
    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case CREATE_NEW_PRICE_REQUEST -> create_new_price_request();
            case GET_ALL_PRICE_REQUESTS -> get_all_price_requests();
            case APPROVE_PRICE_REQUEST -> approve_price_requests();
            case DECLINE_PRICE_REQUEST -> decline_price_requests();
        }
    }

    private void create_new_price_request()
    {

    }
    private void get_all_price_requests()
    {

    }
    private void approve_price_requests()
    {

    }
    private void decline_price_requests()
    {

    }
}
