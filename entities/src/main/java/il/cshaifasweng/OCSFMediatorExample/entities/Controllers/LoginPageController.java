package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;

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
