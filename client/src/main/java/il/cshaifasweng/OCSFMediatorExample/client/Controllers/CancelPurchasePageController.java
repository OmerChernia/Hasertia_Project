package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;

import java.io.IOException;

public class CancelPurchasePageController {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        String id = "";  //needs to be initialized!!!!!!!!!!
        PurchaseMessage getPurchaseHistory = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.GET_PURCHASES_BY_CUSTOMER_ID);
        PurchaseMessage purchaseDelete = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.REMOVE_PURCHASE);

        SimpleClient.getClient().sendToServer(getPurchaseHistory);
        SimpleClient.getClient().sendToServer(purchaseDelete);

    }
}
