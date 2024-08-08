package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.TheaterMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.SeatMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;


public class SeatController
{
    public static void GetAllSeatByHallId(int hallId)
    {
        SeatMessage requestMessage = new SeatMessage(hallId, Message.MessageType.REQUEST, SeatMessage.RequestType.GET_ALL_SEAT_BY_HALL);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void SeatReserved(MovieInstance movieInstance)
    {
        SeatMessage requestMessage = new SeatMessage(movieInstance, Message.MessageType.REQUEST, SeatMessage.RequestType.SEAT_RESERVED);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void SeatCancelation(MovieInstance movieInstance)
    {
        SeatMessage requestMessage = new SeatMessage(movieInstance, Message.MessageType.REQUEST, SeatMessage.RequestType.SEAT_CANCELATION);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

}