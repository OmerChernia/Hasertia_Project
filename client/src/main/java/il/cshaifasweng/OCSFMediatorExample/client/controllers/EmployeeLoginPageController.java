package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;

public class EmployeeLoginPageController {

    public void requestEmployeeLogin(String username, String password) {
        EmployeeLoginMessage requestMessage = new EmployeeLoginMessage(username,MessageType.REQUEST, LoginMessage.RequestType.LOGIN, password);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void requestEmployeeLogOut(String username) {
        EmployeeLoginMessage requestMessage = new EmployeeLoginMessage(username,MessageType.REQUEST, LoginMessage.RequestType.LOGOUT,"");
        SimpleClient.getClient().sendRequest(requestMessage);
    }



}
