package il.cshaifasweng.OCSFMediatorExample.client.connect;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Person;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;


public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	public static String host = "";
	public static int port = 0;

	public static Person user = null; //this to know what user is online


	SimpleClient(String host, int port) {
		super(host, port);
	}

	public static SimpleClient getClient() {
		if (client == null) {
			try {
				client = new SimpleClient(host, port);
				client.openConnection();
			} catch (IOException e) {
				System.err.println("Failed to create client: " + e.getMessage());
			}
		}
		return client;
	}

	public static SimpleClient getClient(String host, int port) throws IOException {
		if (client == null) {
			try {
				client = new SimpleClient(host, port);
				client.openConnection();
			} catch (IOException e) {
				System.err.println("Failed to create client: " + e.getMessage());
			}
		}
		return client;
	}


	public void notifyAllClients(Object msg) {

	}

	// Method to send a generic message to the server
	public void sendRequest(Message message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			System.err.println("Failed to send message to server: " + e.getMessage());
		}
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof Message)
		{
			// Post the received message to EventBus
			EventBus.getDefault().post(msg);
		}
		else
		{
			System.err.println("Received unknown message type from server: " + msg.getClass());
		}
	}


}
