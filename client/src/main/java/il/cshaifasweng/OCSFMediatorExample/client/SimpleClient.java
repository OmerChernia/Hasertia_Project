package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.util.List;


public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg)
	{
		System.out.println("handleMessageFromServer print");
		//not final version!!! , only for testing
		if(msg instanceof EmployeeLoginMessage)
			Temp.func2((EmployeeLoginMessage) msg);
		else if(msg instanceof LoginMessage)
			Temp.func1((LoginMessage) msg);
		else if(msg instanceof ComplaintMessage)
			Temp.func3((ComplaintMessage) msg);
		else if(msg instanceof MovieMessage)
			Temp.func4((MovieMessage) msg);
		else if(msg instanceof MovieInstanceMessage)
			Temp.func5((MovieInstanceMessage) msg);

	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
