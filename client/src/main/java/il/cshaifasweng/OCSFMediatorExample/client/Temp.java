package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;

import java.io.IOException;

public class Temp {

    private static SimpleClient client;


    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();
        LoginMessage loginMessage = new LoginMessage("318111222", Message.MessageType.REQUEST,LoginMessage.RequestType.LOGIN);
        EmployeeLoginMessage employeeLoginMessage = new EmployeeLoginMessage("705182943", Message.MessageType.REQUEST, LoginMessage.RequestType.LOGIN,"password10");
        SimpleClient.getClient().sendToServer(employeeLoginMessage);

    }
    public static void func1(LoginMessage message)
        {
            System.out.println(message.responseType);
        }
    public static void func2(EmployeeLoginMessage message)
    {
        System.out.println(message.responseType);
    }
}
