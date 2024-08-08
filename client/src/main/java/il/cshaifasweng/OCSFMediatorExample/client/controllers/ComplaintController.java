package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;

import java.io.IOException;
import java.time.LocalDateTime;

public class ComplaintController
{
    public static void getAllComplaints()
    {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_ALL_COMPLIANTS);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void getAllComplaintsByUserId(String userId)
    {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_COMPLIANTS_BY_CUSTOMER_ID,userId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void getAllComplaintsByTheater(Theater theater)
    {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_COMPLIANTS_BY_THEATER,theater);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void addNewComplaint(String info, LocalDateTime creationDate, Purchase purchase, RegisteredUser registeredUser)
    {
        Complaint complaint = new Complaint(info, creationDate, purchase, false, registeredUser);
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.ADD_COMPLIANT,complaint);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void answer_Complaint(String answer, Complaint complaint)
    {
        complaint.setInfo(answer);
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.ANSWER_COMPLIANT,complaint);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void getAllOpenComplaints()
    {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_OPEN_COMPLIANTS);
        SimpleClient.getClient().sendRequest(requestMessage);
    }



}
