package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;

public class ViewUserComplaintsController {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        ComplaintMessage getComplaint = new ComplaintMessage();

                // NOT FULLY IMPLEMENTED YET BY COMPLAINTMESSAGE!!!!
    }
}
