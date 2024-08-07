package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;

public class ComplaintHandlingPageController {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        String id = "";  //needs to be initialized!!!!!!!!!!
        ComplaintMessage getComplaintList = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_ALL_COMPLIANTS, id);

        SimpleClient.getClient().sendToServer(getComplaintList);
    }
}