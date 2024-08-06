package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;

import java.io.IOException;

public class EmployeeLoginPageController {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        String id = "";
        String password = "";

        EmployeeLoginMessage isEmployeeEntered = new EmployeeLoginMessage(id, Message.MessageType.REQUEST, LoginMessage.RequestType.LOGIN, password);

        SimpleClient.getClient().sendToServer(isEmployeeEntered);
    }
}