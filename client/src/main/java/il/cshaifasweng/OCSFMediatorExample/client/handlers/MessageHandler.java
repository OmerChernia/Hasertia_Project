package il.cshaifasweng.OCSFMediatorExample.client.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;

import java.io.IOException;

public abstract class MessageHandler {
     public abstract void handle(Message message);

     public abstract void handle(MovieMessage message) throws IOException;
}
