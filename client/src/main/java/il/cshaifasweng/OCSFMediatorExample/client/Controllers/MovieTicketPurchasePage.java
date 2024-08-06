package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.io.IOException;

public class MovieTicketPurchasePage {
    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

   //   Purchase purchase = new Purchase();
     //   String customerID = "";
     //   PurchaseMessage isCreditCardValid = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.ADD_PURCHASE, purchase, customerID );
    }
}
