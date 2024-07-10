package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.TheaterMessage;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.*;
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
				messageHandler = new EmployeeLoginHandler((EmployeeLoginMessage) message, client);
			} else if (message instanceof LoginMessage) {
				messageHandler = new LoginHandler((LoginMessage) message, client);
			} else if (message instanceof MovieMessage) {
				messageHandler = new MovieHandler((MovieMessage) message, client);
			} else if (message instanceof MovieInstanceMessage) {
				messageHandler = new MovieInstanceHandler((MovieInstanceMessage) message, client);
			} else if (message instanceof PriceRequestMessage) {
				messageHandler = new PriceRequestHandler((PriceRequestMessage) message, client);
			} else if (message instanceof PurchaseMessage) {
				messageHandler = new PurchaseHandler((PurchaseMessage) message, client);
			} else if (message instanceof SeatMessage) {
				messageHandler = new SeatHandler((SeatMessage) message, client);
			} else if (message instanceof TheaterMessage) {
				messageHandler = new TheaterHandler((TheaterMessage) message, client);

			}

			if (messageHandler != null) {
				messageHandler.handleMessage();
			}
		}

	}
/*
	private static SessionFactory getSessionFactory(String password) throws HibernateException {
		Configuration configuration = new Configuration();
		configuration.setProperty("hibernate.connection.password", password);

		// Add ALL of your entities here. You can also try adding a whole package.
		configuration.addAnnotatedClass(Car.class);
		configuration.addAnnotatedClass(Person.class);
		configuration.addAnnotatedClass(Image.class);
		configuration.addAnnotatedClass(CarShop.class);

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();

		return configuration.buildSessionFactory(serviceRegistry);
*/
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
