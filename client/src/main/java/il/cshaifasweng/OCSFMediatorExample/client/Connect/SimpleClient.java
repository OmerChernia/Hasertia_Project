package il.cshaifasweng.OCSFMediatorExample.client.Connect;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;


public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	public static String host = "";
	public static int port = 0;

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

	public void sendUpdateScreeningRequest(Object msg) {

	}

	public void sendUpdateMovieRequest(Object msg) {

	}

	public void sendGetMoviesRequest() {

	}

	public void notifyAllClients(Object msg) {

	}

	@Override
	protected void handleMessageFromServer(Object msg) {

	}

	public void sendRequest(String action) {

	}



}
