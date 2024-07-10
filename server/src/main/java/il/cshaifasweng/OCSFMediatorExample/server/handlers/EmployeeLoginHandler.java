package il.cshaifasweng.OCSFMediatorExample.server.handlers;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class EmployeeLoginHandler extends LoginHandler {

    private EmployeeLoginMessage message;

    public EmployeeLoginHandler(EmployeeLoginMessage message, ConnectionToClient client)
    {
        super(message,client);
        this.message = message;
    }

    public void handleMessage()
    {
       // if(employ.isonline==1){no}
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