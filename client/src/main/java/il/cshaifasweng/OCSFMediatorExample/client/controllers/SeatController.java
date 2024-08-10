package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.SeatMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;

public class SeatController {

     public static void getAllSeatsByHall(int hallId) {
        SeatMessage requestMessage = new SeatMessage(hallId, MessageType.REQUEST, SeatMessage.RequestType.GET_ALL_SEAT_BY_HALL);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

     public static void reserveSeat(MovieInstance movieInstance) {
        SeatMessage requestMessage = new SeatMessage(movieInstance, MessageType.REQUEST, SeatMessage.RequestType.SEAT_RESERVED);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

     public static void cancelSeatReservation(MovieInstance movieInstance) {
        SeatMessage requestMessage = new SeatMessage(movieInstance, MessageType.REQUEST, SeatMessage.RequestType.SEAT_CANCELATION);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
