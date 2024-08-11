package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ConnectionMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import org.hibernate.Session;

import java.util.ArrayList;

public class ConnectionHandler extends MessageHandler
{
    private final ConnectionMessage message;
    public ArrayList<SubscribedClient> clients;

    public ConnectionHandler(ConnectionMessage message, ConnectionToClient client, Session session, ArrayList<SubscribedClient> clients)
    {
        super(client, session);
        this.message = message;
        this.clients = clients;
    }
    public void handleMessage()
    {
        switch (message.requestType)
        {
            case FIRST_CONNECTION -> first_connection();
            case DELETE_CONNECTION -> delete_connection();
        }
    }

    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    public void first_connection()
    {
        SubscribedClient connection = new SubscribedClient(client);
        clients.add(connection);
        System.out.println("client added successfully");
    }
    public void delete_connection()
    {
        // ???? d'ont know if we need to implament
    }


}
