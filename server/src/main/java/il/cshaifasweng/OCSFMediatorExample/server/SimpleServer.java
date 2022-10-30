package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	public SimpleServer(int port) {
		super(port);
		
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;
		String request = message.getMessage();
		try {
			if (request.isBlank()){
				System.out.println("Error!");
				message.setMessage("Error! Please try again");
				client.sendToClient(message);
			}else if(request.startsWith("send Submitters IDs")){
				message.setData("123456789 , 987654321");
				message.setMessage("update submitters IDs");
				client.sendToClient(message);
			}else if (request.startsWith("send Submitters")){
				message.setMessage("Shir1 , Shir2");
				client.sendToClient(message);
			}else if(request.startsWith("echo Hello")){
				message.setMessage("Hello World!");
				client.sendToClient(message);
			}else if(request.startsWith("change submitters IDs:")){
				message.setData(request.substring(23));
				message.setMessage("update submitters IDs");
				for(SubscribedClient SubscribedClient:SubscribersList){
					SubscribedClient.getClient().sendToClient(message);
				}
			} else if (request.equals("add client")){
				SubscribedClient connection = new SubscribedClient(client);
				SubscribersList.add(connection);
				message.setMessage("client added successfully");
				client.sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
