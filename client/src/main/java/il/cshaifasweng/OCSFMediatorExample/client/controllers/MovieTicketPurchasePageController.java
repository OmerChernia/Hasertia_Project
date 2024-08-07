package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage.RequestType;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Seat;

import java.time.LocalDateTime;

public class MovieTicketPurchasePageController {

    public void requestAvailableTickets(int movieId) {
        PurchaseMessage requestMessage = new PurchaseMessage(MessageType.REQUEST, RequestType.GET_ALL_PURCHASES, movieId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void purchaseTicket(int id,LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation, MovieInstance movieInstance, Seat seat) {
        MovieTicket ticket = new MovieTicket(purchaseDate, owner, purchaseValidation, movieInstance, seat);
        PurchaseMessage requestMessage = new PurchaseMessage(MessageType.REQUEST, RequestType.ADD_PURCHASE,ticket,id);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void cancelPurchase(int purchaseId) {
        PurchaseMessage requestMessage = new PurchaseMessage(MessageType.REQUEST, RequestType.REMOVE_PURCHASE, purchaseId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}
