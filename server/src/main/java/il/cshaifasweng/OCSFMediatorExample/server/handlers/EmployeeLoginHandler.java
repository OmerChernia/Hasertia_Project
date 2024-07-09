package il.cshaifasweng.OCSFMediatorExample.server.handlers;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class EmployeeLoginHandler extends MessageHandler{

    EmployeeLoginMessage message;

    public EmployeeLoginHandler(EmployeeLoginMessage message, ConnectionToClient client)
    {
        super(client);
        this.message = message;
    }

    public void handleMessage()
    {
       // if(employ.isonline==1){no}
        switch (message.employeeType)
        {
            case THEATER_MANAGER -> login_theater_manager();
            case COMPANY_MANAGER -> login_manager();
            case CUSTOMER_SERVICE -> login_customer_service();
            case CONTENT_MANAGER -> login_content_manager();

        }
    }


    private void login_theater_manager()
    {

    }
    private void login_manager()
    {

    }
    private void login_customer_service()
    {

    }
    private void login_content_manager()
    {

    }

}