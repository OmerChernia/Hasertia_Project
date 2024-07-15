package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;

import java.io.IOException;

public class ComplaintHandlingPageController {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        String id = "";  //needs to be initialized!!!!!!!!!!
        ComplaintMessage getComplaintList = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_ALL_COMPLAINTS, id);

        SimpleClient.getClient().sendToServer(getComplaintList);
    }
}