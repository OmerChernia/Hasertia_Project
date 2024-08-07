package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;

import java.io.IOException;

public class LoginPageController {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        String id="";  //NEEDS TO BE INITIALIZED!!!!
        LoginMessage checkID = new LoginMessage(id, Message.MessageType.REQUEST, LoginMessage.RequestType.LOGIN);
        SimpleClient.getClient().sendToServer(checkID);
    }
}
