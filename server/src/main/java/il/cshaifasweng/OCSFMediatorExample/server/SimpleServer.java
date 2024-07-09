package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.ComplaintHandler;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.MessageHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleServer extends AbstractServer
{
	private static ArrayList<SubscribedClient> clients = new ArrayList<>();

	public SimpleServer(int port)
	{
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;
		MessageHandler messageHandler = null;

		if(message.messageType== Message.MessageType.REQUEST)
		{
			if (message instanceof ComplaintMessage) {
				messageHandler = new ComplaintHandler((ComplaintMessage) message, client);
			} else if (message instanceof EmployeeLoginMessage) {

			} else if (message instanceof LoginMessage) {

			} else if (message instanceof MovieMessage) {

			} else if (message instanceof MoviesInstanceMessage) {

			} else if (message instanceof PriceRequestMessage) {

			} else if (message instanceof PurchaseMessage) {

			} else if (message instanceof SeatMessage) {

			} else if (message instanceof TheaterMessage) {

			}

			if (messageHandler != null) {
				messageHandler.handleMessage();
			}
		}

	}

	public void sendToAllClients(Message message) {
		try {
			for (SubscribedClient SubscribedClient : clients) {
				SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
