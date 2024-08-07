package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;

import java.io.IOException;

public class LoginPageController
{

    public void requestEmployeeLogin(String username, String password) {
        EmployeeLoginMessage requestMessage = new EmployeeLoginMessage(username, Message.MessageType.REQUEST, LoginMessage.RequestType.LOGIN, password);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void requestEmployeeLogOut(String username) {
        EmployeeLoginMessage requestMessage = new EmployeeLoginMessage(username, Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT,"");
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void requestUserLogin(String username) {
        LoginMessage requestMessage = new LoginMessage(username, Message.MessageType.REQUEST, LoginMessage.RequestType.LOGIN);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void requestUserLogOut(String username) {
        LoginMessage requestMessage = new LoginMessage(username, Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT);
        SimpleClient.getClient().sendRequest(requestMessage);
    }


}


