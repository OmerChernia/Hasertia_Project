package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;

import java.io.IOException;

public class Temp {

    private static SimpleClient client;


    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();
        LoginMessage loginMessage = new LoginMessage("318111222", Message.MessageType.REQUEST,LoginMessage.RequestType.LOGIN);
        SimpleClient.getClient().sendToServer(loginMessage);

        System.out.println(loginMessage.responseType);
    }

}
