package il.cshaifasweng.OCSFMediatorExample.server.handlers;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class LoginHandler extends MessageHandler{

    private LoginMessage message;

    public LoginHandler(LoginMessage message, ConnectionToClient client)
    {
        super(client);
        this.message = message;
    }
    public void handleMessage()
    {
        // if(user.isonline==1){no}
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
