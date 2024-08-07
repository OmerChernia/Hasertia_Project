package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage.RequestType;

public class ViewUserComplaintsController {

    public void requestUserComplaints(String userId) {
        ComplaintMessage requestMessage = new ComplaintMessage(MessageType.REQUEST, RequestType.GET_COMPLIANTS_BY_CUSTOMER_ID, userId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
