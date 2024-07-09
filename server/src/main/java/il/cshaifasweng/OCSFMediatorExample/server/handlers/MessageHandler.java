package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public abstract class MessageHandler
{
    public ConnectionToClient client;

    public MessageHandler(ConnectionToClient client)
    {
        this.client = client;
    }
    public MessageHandler(){}

    public abstract void handleMessage();
}
