package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;

import java.time.LocalDateTime;

public class ComplaintController {

     public static void getAllComplaints() {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_ALL_COMPLIANTS);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

     public static void addComplaint(String info, LocalDateTime creationDate, Purchase purchase, boolean isClosed, RegisteredUser registeredUser) {
         Complaint complaint = new Complaint( info,  creationDate,  purchase,  isClosed,  registeredUser);
         ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.ADD_COMPLIANT, complaint);

        SimpleClient.getClient().sendRequest(requestMessage);
    }

     public static void getComplaintsByTheater(Theater theater) {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_COMPLIANTS_BY_THEATER, theater);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

     public static void getComplaintsByCustomerId(String customerId) {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_COMPLIANTS_BY_CUSTOMER_ID, customerId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

     public static void getOpenComplaints() {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_OPEN_COMPLIANTS);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

     public static void answerComplaint(Complaint complaint) {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.ANSWER_COMPLIANT, complaint);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
