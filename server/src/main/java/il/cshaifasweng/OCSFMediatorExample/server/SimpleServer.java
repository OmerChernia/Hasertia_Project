package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.*;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.*;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleServer extends AbstractServer
{
	private static ArrayList<SubscribedClient> clients = new ArrayList<>();
	static Session session;
	private final String password;


	public SimpleServer(int port,String password)
	{
		super(port);
		this.password = password;
		session = getSessionFactory(password).openSession();
		GenerateDB db = new GenerateDB(session);
		db.initializeDatabase();
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client){
		try {
			Message message = (Message) msg;
			MessageHandler messageHandler = null;

			session.beginTransaction();

			if (message.messageType == Message.MessageType.REQUEST) {
				if (msg instanceof ComplaintMessage) {
					messageHandler = new ComplaintHandler((ComplaintMessage) msg, client, session);
				} else if (msg instanceof EmployeeLoginMessage) {
					messageHandler = new EmployeeLoginHandler((EmployeeLoginMessage) msg, client, session);
				} else if (msg instanceof LoginMessage) {
					messageHandler = new LoginHandler((LoginMessage) msg, client, session);
				} else if (msg instanceof MovieMessage) {
					messageHandler = new MovieHandler((MovieMessage) msg, client, session);
				} else if (msg instanceof MovieInstanceMessage) {
					messageHandler = new MovieInstanceHandler((MovieInstanceMessage) msg, client, session);
				} else if (msg instanceof PriceRequestMessage) {
					messageHandler = new PriceRequestHandler((PriceRequestMessage) msg, client, session);
				} else if (msg instanceof PurchaseMessage) {
					messageHandler = new PurchaseHandler((PurchaseMessage) msg, client, session);
				} else if (msg instanceof SeatMessage) {
					messageHandler = new SeatHandler((SeatMessage) msg, client, session);
				} else if (msg instanceof TheaterMessage) {
					messageHandler = new TheaterHandler((TheaterMessage) msg, client, session);

				}

				if (messageHandler != null) {
					messageHandler.handleMessage();            	// handle the message ,and change DB if needed
					session.getTransaction().commit();          // save changes in DB
					messageHandler.setMessageTypeToResponse();  //change message to response that client will know it is a response from server

					System.out.println("message handled");
					client.sendToClient(msg);
					System.out.println("message sent");
				}
			}

		}
		catch (Exception exception) {
			if (session != null)
				session.getTransaction().rollback();
			exception.printStackTrace();
		}

	}

	private static SessionFactory getSessionFactory(String password) throws HibernateException {
		Configuration configuration = new Configuration();
		configuration.setProperty("hibernate.connection.password", password);

		// Add ALL of your entities here. You can also try adding a whole package.
		configuration.addAnnotatedClass(Complaint.class);
		configuration.addAnnotatedClass(Person.class);
		configuration.addAnnotatedClass(Hall.class);
		configuration.addAnnotatedClass(HomeViewingPackageInstance.class);
		configuration.addAnnotatedClass(Movie.class);
		configuration.addAnnotatedClass(MovieInstance.class);
		configuration.addAnnotatedClass(PriceRequest.class);
		configuration.addAnnotatedClass(Seat.class);
		configuration.addAnnotatedClass(Theater.class);
		configuration.addAnnotatedClass(MultiEntryTicket.class);
		configuration.addAnnotatedClass(RegisteredUser.class);
		configuration.addAnnotatedClass(TheaterManager.class);
		configuration.addAnnotatedClass(Employee.class);
		configuration.addAnnotatedClass(MovieTicket.class);
		configuration.addAnnotatedClass(Purchase.class);

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();

		return configuration.buildSessionFactory(serviceRegistry);
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
