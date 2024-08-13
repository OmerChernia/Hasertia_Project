package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.RegisteredUserMessage;

public class RegisteredUserController
{
    public static void addNewUser(String id,String username, String lastName, String email)
    {
        RegisteredUserMessage registeredUserMessage =
                new RegisteredUserMessage(Message.MessageType.REQUEST,id,username,lastName,email, RegisteredUserMessage.RequestType.ADD_NEW_USER);
        SimpleClient.getClient().sendRequest(registeredUserMessage);
    }

    public static void getUserByID(String id)
    {
        RegisteredUserMessage registeredUserMessage =
                new RegisteredUserMessage(Message.MessageType.REQUEST,id, RegisteredUserMessage.RequestType.GET_USER_BY_ID);
        SimpleClient.getClient().sendRequest(registeredUserMessage);
    }


}
