package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.TheaterMessage;

import java.time.LocalDateTime;

public class PurchaseController
{
    public static void AddMovieTicket(int customerId, LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation, MovieInstance movieInstance, Seat seat)
    {
        MovieTicket newTicket = new MovieTicket(purchaseDate, owner, purchaseValidation, movieInstance, seat);
        PurchaseMessage requestMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.ADD_PURCHASE,newTicket,customerId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void AddMultiEntryTicket(int customerId, LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation)
    {
        MultiEntryTicket newTicket = new MultiEntryTicket(purchaseDate, owner, purchaseValidation);
        PurchaseMessage requestMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.ADD_PURCHASE,newTicket,customerId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void AddHomeViewing(int customerId, LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation,Movie movie, LocalDateTime viewingDate)
    {
        HomeViewingPackageInstance newTicket = new HomeViewingPackageInstance(purchaseDate, owner, purchaseValidation, movie, viewingDate);
        PurchaseMessage requestMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.ADD_PURCHASE,newTicket,customerId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void RemovePurchase(int purchaseID)
    {
        PurchaseMessage requestMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.REMOVE_PURCHASE,purchaseID);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
    public static void GetPurchasesByCustomerID(int CustomerId)
    {
        PurchaseMessage requestMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.GET_PURCHASES_BY_CUSTOMER_ID,CustomerId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
    public static void GetPurchasesByTheaterID(int TheaterId)
    {
        PurchaseMessage requestMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.GET_PURCHASES_BY_THEATER_ID,TheaterId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
    public static void GetAllPurchases()
    {
        PurchaseMessage requestMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.GET_ALL_PURCHASES);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
    public static void GetMoviePackagesByCustomerID(int CustomerId)
    {
        PurchaseMessage requestMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.GET_ALL_MOVIE_PACKAGES_BY_CUSTOMER_ID,CustomerId);
        SimpleClient.getClient().sendRequest(requestMessage);
    }



}
