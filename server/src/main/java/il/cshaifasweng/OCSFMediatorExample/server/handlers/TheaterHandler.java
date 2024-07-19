package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.TheaterMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TheaterHandler extends MessageHandler
{
    private TheaterMessage message;

    public TheaterHandler(TheaterMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
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
    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    private void get_all_theaters() {
        try {
            // Create an HQL query to fetch all theaters
            Query<Theater> query = session.createQuery("FROM Theater", Theater.class);

            // Execute the query and get the result list
            List<Theater> theaters = query.getResultList();

            // Set the result list to the message
            message.theaterList = new ArrayList<>(theaters);
            message.responseType = TheaterMessage.ResponseType.ALL_THEATERS;
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = TheaterMessage.ResponseType.THEATER_BY_ID;  // Indicating an error
        }
    }

    private void get_theater() {
        try {
            // Create an HQL query to fetch a theater by ID
            Theater theater = session.get(Theater.class, message.id);

            if (theater != null) {
                // Set the theater to the message
                message.theaterList = new ArrayList<>();
                message.theaterList.add(theater);
                message.responseType = TheaterMessage.ResponseType.THEATER_BY_ID;
            } else {
                message.responseType = TheaterMessage.ResponseType.THEATER_BY_ID;  // Indicating an error
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = TheaterMessage.ResponseType.THEATER_BY_ID;  // Indicating an error
        }
    }
}
