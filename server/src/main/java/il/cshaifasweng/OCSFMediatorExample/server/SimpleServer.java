package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.*;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.*;

import il.cshaifasweng.OCSFMediatorExample.server.scheduler.ComplaintFollowUpScheduler;
import il.cshaifasweng.OCSFMediatorExample.server.scheduler.OrderScheduler;
import il.cshaifasweng.OCSFMediatorExample.server.scheduler.LinkScheduler;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleServer extends AbstractServer {
 	private static SimpleServer instance;  // Static variable to hold the single instance
	private static final ArrayList<SubscribedClient> clients = new ArrayList<>();
	private static SessionFactory sessionFactory;  // Use SessionFactory instead of Session for static context
	private final String password;
	public static Session session;

    public SimpleServer(int port, String password) {
		super(port);
		this.password = password;
		instance = this;
		session = getSessionFactory(password).openSession();

		// Initialize schedulers as singletons
        ComplaintFollowUpScheduler complaintScheduler = ComplaintFollowUpScheduler.getInstance();
        OrderScheduler emailNotificationScheduler = OrderScheduler.getInstance();
		LinkScheduler scheduledEventNotifier = LinkScheduler.getInstance();

		// Initialize the database
		GenerateDB db = new GenerateDB(session);
		db.initializeDatabase();
	}

	public static SimpleServer getServer() {
		return instance;
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
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
				} else if (msg instanceof ConnectionMessage) {
					messageHandler = new ConnectionHandler((ConnectionMessage) msg, client, session, clients);
				} else if (msg instanceof RegisteredUserMessage) {
					messageHandler = new RegisteredUserHandler((RegisteredUserMessage) msg, client, session);
				} else if (msg instanceof HallMessage) {
					messageHandler = new HallHandler((HallMessage) msg, client, session);
				}

				if (messageHandler != null) {
					messageHandler.handleMessage();  // Handle the message, including DB changes and notifications
					session.getTransaction().commit();  // Save changes in the DB
					messageHandler.setMessageTypeToResponse();  // Change message to response so the client knows it's a response from the server

					System.out.println("Message handled");

					if (msg instanceof ConnectionMessage && ((ConnectionMessage) msg).requestType == ConnectionMessage.RequestType.DELETE_CONNECTION)
						return;

					client.sendToClient(msg);  // Send the message to the client
					System.out.println("Message sent");
				}
			}
		} catch (Exception exception) {
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

	@Override
	public void sendToAllClients(Object message) {
		try {
			System.out.println("size: " + clients.size());
			for (SubscribedClient subscribedClient : clients) {
				subscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
