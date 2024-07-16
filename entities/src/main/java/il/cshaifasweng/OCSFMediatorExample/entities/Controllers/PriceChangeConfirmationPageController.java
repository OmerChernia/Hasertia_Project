package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PriceRequestMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.PriceRequest;

import java.io.IOException;

public class PriceChangeConfirmationPageController {
    private static SimpleClient client;

    public PriceRequest[] getAllRequests() {
        PriceRequestMessage priceRequestMessage = new PriceRequestMessage(Message.MessageType.REQUEST, PriceRequestMessage.RequestType.GET_ALL_REQUESTS);
        try {
            SimpleClient.getClient().sendToServer(priceRequestMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PriceRequest[0];
    }

    public void acceptRequest(PriceRequest request) {
        PriceRequestMessage priceRequestMessage = new PriceRequestMessage(Message.MessageType.REQUEST, PriceRequestMessage.RequestType.ACCEPT_REQUEST, request);
        try {
            SimpleClient.getClient().sendToServer(priceRequestMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void denyRequest(PriceRequest request) {
        PriceRequestMessage priceRequestMessage = new PriceRequestMessage(Message.MessageType.REQUEST, PriceRequestMessage.RequestType.DENY_REQUEST, request);
        try {
            SimpleClient.getClient().sendToServer(priceRequestMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        PriceChangeConfirmationPageController controller = new PriceChangeConfirmationPageController();
    }
}