package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;

public class ComplaintController
{
    public void getAllComplaints()
    {
        ComplaintMessage requestMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_ALL_COMPLIANTS);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

}
