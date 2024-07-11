package il.cshaifasweng.OCSFMediatorExample.server.handlers;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

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

    protected void login()
    {

    }
    protected void logout()
    {

    }

}
