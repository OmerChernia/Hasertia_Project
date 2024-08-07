package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage.RequestType;

public class ComplaintHandlingPageController {

    public void requestAllComplaints() {
        ComplaintMessage requestMessage = new ComplaintMessage(MessageType.REQUEST, RequestType.GET_ALL_COMPLIANTS);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void updateComplaintStatus(String complaintId) {
        ComplaintMessage requestMessage = new ComplaintMessage(MessageType.REQUEST, RequestType.ANSWER_COMPLIANT, complaintId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }


}
