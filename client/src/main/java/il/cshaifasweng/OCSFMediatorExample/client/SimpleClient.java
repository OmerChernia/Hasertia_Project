package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;


public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	public static String host = "";
	public static int port = 0;




	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		Message message = (Message) msg;
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient(host, port);
		}
		return client;
	}

}
