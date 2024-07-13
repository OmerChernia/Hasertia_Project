package il.cshaifasweng.OCSFMediatorExample.server.handlers;
import il.cshaifasweng.OCSFMediatorExample.entities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;


public class LoginHandler extends MessageHandler{

    private LoginMessage message;

    public LoginHandler(LoginMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }
    public void handleMessage()
    {
        switch (message.requestType)
        {
            case LOGIN -> login();
            case LOGOUT -> logout();
        }
    }
    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    protected void login() {
        try {
            // Create a query to find the user by ID
            Query<RegisteredUser> query = session.createQuery("FROM RegisteredUser WHERE id_number = :id", RegisteredUser.class);
            query.setParameter("id", message.id);

            // Execute the query and get the result
            RegisteredUser user = query.uniqueResult();

            // Check if the user exists and verify the login credentials
            if (user != null) {
                // Check if the user is already logged in
                if (user.isOnline()) {
                    message.responseType = LoginMessage.ResponseType.ALREADY_LOGGED;
                } else {
                    // Set the user as online
                    user.setOnline(true);
                    session.update(user);

                    // Commit the transaction
                    session.getTransaction().commit();

                    message.responseType = LoginMessage.ResponseType.LOGIN_SUCCESFUL;
                }
            } else {
                message.responseType = LoginMessage.ResponseType.LOGIN_FAILED;
            }
        } catch (Exception e) {
            // Rollback the transaction in case of error
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            message.responseType = LoginMessage.ResponseType.LOGIN_FAILED;
            e.printStackTrace();
        }
    }

    protected void logout() {
        try {

            // Create a query to find the user by ID
            Query<RegisteredUser> query = session.createQuery("FROM RegisteredUser WHERE id_number = :id", RegisteredUser.class);
            query.setParameter("id", message.id);

            // Execute the query and get the result
            RegisteredUser user = query.uniqueResult();

            // Check if the user exists and is currently logged in
            if (user != null) {
                if (user.isOnline()) {
                    // Set the user as offline
                    user.setOnline(false);
                    session.update(user);

                    // Commit the transaction
                    session.getTransaction().commit();

                    message.responseType = LoginMessage.ResponseType.LOGOUT_SUCCESFUL;
                } else {
                    message.responseType = LoginMessage.ResponseType.LOGOUT_FAILED;
                }
            } else {
                message.responseType = LoginMessage.ResponseType.LOGOUT_FAILED;
            }
        } catch (Exception e) {
            // Rollback the transaction in case of error
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            message.responseType = LoginMessage.ResponseType.LOGOUT_FAILED;
            e.printStackTrace();
        }
    }


}
