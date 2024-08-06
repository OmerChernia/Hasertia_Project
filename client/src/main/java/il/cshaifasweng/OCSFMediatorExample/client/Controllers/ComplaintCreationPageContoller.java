package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.Connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;

public class ComplaintCreationPageContoller {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        ComplaintMessage submitComplaint = new ComplaintMessage();

        // NOT FULLY IMPLEMENTED YET BY COMPLAINTMESSAGE!!!!
    }
}
