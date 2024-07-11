package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

public abstract class MessageHandler
{
    public ConnectionToClient client;
    private Session session;

    public MessageHandler(ConnectionToClient client, Session session)
    {
        this.client = client;
        this.session = session;
    }
    public MessageHandler(){}

    public abstract void handleMessage();
}
