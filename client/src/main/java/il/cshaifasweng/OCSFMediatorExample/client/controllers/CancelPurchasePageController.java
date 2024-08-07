package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.CancelPurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.CancelPurchaseMessage.RequestType;

public class CancelPurchasePageController {

    public void requestCancelPurchase(int purchaseId) {
        CancelPurchaseMessage requestMessage = new CancelPurchaseMessage(MessageType.REQUEST, RequestType.REQUEST_CANCEL_PURCHASE, purchaseId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void confirmCancelPurchase(int purchaseId) {
        CancelPurchaseMessage requestMessage = new CancelPurchaseMessage(MessageType.REQUEST, RequestType.CONFIRM_CANCEL_PURCHASE, purchaseId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
