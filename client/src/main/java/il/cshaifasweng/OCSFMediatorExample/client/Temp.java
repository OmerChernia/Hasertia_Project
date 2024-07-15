package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;

public class Temp {

    private static SimpleClient client;


    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();
        LoginMessage loginMessage = new LoginMessage("318111222", Message.MessageType.REQUEST,LoginMessage.RequestType.LOGIN);
        EmployeeLoginMessage employeeLoginMessage = new EmployeeLoginMessage("238947615", Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT,"password120");
        ComplaintMessage complaintMessage = new ComplaintMessage(Message.MessageType.REQUEST,ComplaintMessage.RequestType.GET_ALL_COMPLIANTS);
        SimpleClient.getClient().sendToServer(complaintMessage);

    }
    public static void func1(LoginMessage message)
        {
            System.out.println(message.responseType);
        }
    public static void func2(EmployeeLoginMessage message)
    {
        System.out.println(message.responseType);
    }
    public static void func3(ComplaintMessage message)
    {
        for(Complaint comp :  message.compliants)
        {
            System.out.println(comp.getId());
        }
    }
}
