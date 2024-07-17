package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PriceRequestMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.PriceRequest;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

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
        //not finished!!!
        Query<PriceRequest> query = session.createQuery("FROM PriceRequest where movie = :_movie and type =:_type", PriceRequest.class);
        query.setParameter("_movie", message.requests.getFirst().getMovie());
        query.setParameter("_type", message.requests.getFirst().getType());

        PriceRequest priceRequest = query.uniqueResult();

        if(priceRequest == null)
        {
            session.save(message.requests.getFirst());
        }
        else
        {
            session.update(message.requests.getFirst());
        }
        session.flush();
        message.responseType = PriceRequestMessage.ResponseType.REQUEST_CREATED;
    }
    private void get_all_price_requests()
    {
        message.requests = session.createQuery("FROM PriceRequest", PriceRequest.class).list();
        message.responseType = PriceRequestMessage.ResponseType.REQUESTS_LIST;
    }
    private void approve_price_requests()
    {
        //Query<PriceRequest> query = session.createQuery("FROM Movie where movie = :_movie", PriceRequest.class);
    }
    private void decline_price_requests()
    {
        Query<PriceRequest> query = session.createQuery("FROM PriceRequest where id = :_id", PriceRequest.class);
        query.setParameter("_id",message.requests.getFirst().getId());

        PriceRequest priceRequest = query.uniqueResult();

        if(priceRequest != null)
        {
            session.delete(priceRequest);
            session.flush();
            message.responseType = PriceRequestMessage.ResponseType.PRICE_REQUEST_DECIDE;
        }
        else
            message.responseType = PriceRequestMessage.ResponseType.PRICE_REQUEST_MESSAGE_FAILED;
    }
}
