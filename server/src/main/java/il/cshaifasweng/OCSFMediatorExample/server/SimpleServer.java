package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.server.events.MovieEvent;
import il.cshaifasweng.OCSFMediatorExample.server.events.MovieInstanceCanceledEvent;
import il.cshaifasweng.OCSFMediatorExample.server.events.SeatStatusChangedEvent;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.*;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.*;

import il.cshaifasweng.OCSFMediatorExample.server.scheduler.ComplaintFollowUpScheduler;
import il.cshaifasweng.OCSFMediatorExample.server.scheduler.LinkAndInstanceScheduler;
import il.cshaifasweng.OCSFMediatorExample.server.scheduler.OrderScheduler;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleServer extends AbstractServer
{
	private static SimpleServer instance;  // Static variable to hold the single instance
	private static ArrayList<SubscribedClient> clients = new ArrayList<>();
	public static Session session;
	private final String password;


	public SimpleServer(int port,String password)
	{
		super(port);
		this.password = password;
		session = getSessionFactory(password).openSession();

		// Initialize schedulers as singletons
		ComplaintFollowUpScheduler complaintScheduler = ComplaintFollowUpScheduler.getInstance();
		OrderScheduler emailNotificationScheduler = OrderScheduler.getInstance();
		LinkAndInstanceScheduler linkAndInstanceScheduler = LinkAndInstanceScheduler.getInstance();

		GenerateDB db = new GenerateDB(session);
		db.initializeDatabase();

		// Schedule all active complaints
		complaintScheduler.scheduleAllActiveComplaints();
		// Schedule all home viewing packages
		linkAndInstanceScheduler.scheduleHomeViewingPackages();
		// Schedule all movie instances
		linkAndInstanceScheduler.scheduleMovieInstances();
	}


	public static SimpleServer getServer() {
		return instance;
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
				} else if (msg instanceof ConnectionMessage) {
					messageHandler = new ConnectionHandler((ConnectionMessage) msg , client, session ,clients);
				} else if (msg instanceof RegisteredUserMessage) {
					messageHandler = new RegisteredUserHandler((RegisteredUserMessage)msg , client, session);
				} else if (msg instanceof HallMessage) {
					messageHandler = new HallHandler((HallMessage) msg, client, session);
				}

				if (messageHandler != null) {
					messageHandler.handleMessage();            	// handle the message ,and change DB if needed
					session.getTransaction().commit();          // save changes in DB
					messageHandler.setMessageTypeToResponse();  //change message to response that client will know it is a response from server

					if(msg instanceof MovieInstanceMessage &&(
							((MovieInstanceMessage) msg).requestType == MovieInstanceMessage.RequestType.DELETE_MOVIE_INSTANCE
							||((MovieInstanceMessage) msg).requestType == MovieInstanceMessage.RequestType.UPDATE_MOVIE_INSTANCE
							||((MovieInstanceMessage) msg).requestType == MovieInstanceMessage.RequestType.ADD_MOVIE_INSTANCE))
					{
						sendToAllClients(new MovieInstanceCanceledEvent(((MovieInstanceMessage) msg).movies.getFirst()));
					}
					else if(msg instanceof SeatMessage &&
							(((SeatMessage) msg).requestType == SeatMessage.RequestType.SEATS_CANCELATION
							|| ((SeatMessage) msg).requestType == SeatMessage.RequestType.SEATS_RESERVED))
					{
						sendToAllClientsExceptMe(new SeatStatusChangedEvent(((SeatMessage) msg).hallSeats),client);
					}
					else if (msg instanceof PurchaseMessage) {
						if (((PurchaseMessage) msg).requestType == PurchaseMessage.RequestType.REMOVE_PURCHASE) {
							if (((PurchaseMessage) msg).purchases.getFirst() instanceof MovieTicket) {
								MovieTicket ticket = (MovieTicket) ((PurchaseMessage) msg).purchases.getFirst();
								sendToAllClients(new SeatStatusChangedEvent(List.of(ticket.getSeat())));
								System.out.println("Sent SeatStatusChangedEvent to all clients for MovieTicket removal.");
							}
						}
					}

					else if(msg instanceof MovieMessage
							&&(((MovieMessage) msg).requestType == MovieMessage.RequestType.ADD_MOVIE))
					{
						System.out.println("Hello");
						sendToAllClientsExceptMe(new MovieEvent(),client);
					}


					System.out.println("message handled");

                    if (msg instanceof ConnectionMessage && ((ConnectionMessage) msg).requestType == ConnectionMessage.RequestType.DELETE_CONNECTION){
                        System.out.println("message didn't sent");
                        return;
                    }

					client.sendToClient(msg);					//send the message to the client
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

	@Override
	public void sendToAllClients(Object message) {
		try {
			for (SubscribedClient SubscribedClient : clients)
			{
				if(SubscribedClient.getClient().isAlive()) // for insurance
					SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public void sendToAllClientsExceptMe(Object message, ConnectionToClient client) {
		try {
			for (SubscribedClient SubscribedClient : clients)
			{
				if(!SubscribedClient.getClient().equals(client) &&  SubscribedClient.getClient().isAlive()) // for insurance
					SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}