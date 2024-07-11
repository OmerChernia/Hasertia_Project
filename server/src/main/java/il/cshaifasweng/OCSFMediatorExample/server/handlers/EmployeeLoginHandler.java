package il.cshaifasweng.OCSFMediatorExample.server.handlers;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

public class EmployeeLoginHandler extends LoginHandler {

    private EmployeeLoginMessage message;

    public EmployeeLoginHandler(EmployeeLoginMessage message, ConnectionToClient client, Session session)
    {
        super(message,client,session);
        this.message = message;
    }

    public void handleMessage()
    {

        switch (message.requestType)
        {
            case LOGIN -> employeelogin();
            case LOGOUT -> employeelogout();
        }
    }

    private void employeelogin()
    {
        super.login();

    }
    private void employeelogout()
    {
        super.logout();
    }
}