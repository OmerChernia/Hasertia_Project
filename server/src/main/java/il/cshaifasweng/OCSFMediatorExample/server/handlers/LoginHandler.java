package il.cshaifasweng.OCSFMediatorExample.server.handlers;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class LoginHandler extends MessageHandler{

    LoginMessage message;

    public LoginHandler(LoginMessage message, ConnectionToClient client)
    {
        super(client);
        this.message = message;
    }
    public void handleMessage()
    {
        // if(user.isonline==1){no}
        switch (message.responseType)
        {
            case LOGIN_SUCCESFUL -> login_s();
            case LOGIN_FAILED -> login_f();
            case LOGOUT_SUCCESFUL -> logout_s();
            case LOGOUT_FAILED -> logout_f();
            case ALREADY_LOGGED -> already_logged();

        }
    }

    private void login_s()
    {

    }
    private void login_f()
    {

    }
    private void logout_s()
    {

    }
    private void logout_f()
    {

    }
    private void already_logged()
    {

    }

}
