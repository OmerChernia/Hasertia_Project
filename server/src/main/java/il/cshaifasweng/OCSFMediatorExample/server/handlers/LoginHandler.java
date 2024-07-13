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

    protected void login()
    {
        String hql = "SELECT isOnline FROM hasertia.employees WHERE id_number = :user_id";
        //Query query = session.createQuery();
        //query.setParameter("user_id", message.id);

    }
    protected void logout()
    {

    }

}
