package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Hall;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.HallMessage;

import java.time.LocalDate;
import java.util.List;

public class HallController {

    public static void requestAvailableDates(Hall hall) {
        HallMessage requestMessage = new HallMessage(HallMessage.MessageType.REQUEST, HallMessage.RequestType.GET_AVAILABLE_DATES, hall);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void requestAvailableTimes(Hall hall, LocalDate date) {
        HallMessage requestMessage = new HallMessage(HallMessage.MessageType.REQUEST, HallMessage.RequestType.GET_AVAILABLE_TIMES, hall);
        requestMessage.setDate(date);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
