package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PriceRequestMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PriceRequestMessage.RequestType;
import il.cshaifasweng.OCSFMediatorExample.entities.PriceRequest;

public class PriceRequestController {

    public static void requestAllPriceRequests() {
        PriceRequestMessage requestMessage = new PriceRequestMessage(MessageType.REQUEST, RequestType.GET_ALL_PRICE_REQUESTS);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void acceptPriceRequest(PriceRequest request) {
        PriceRequestMessage requestMessage = new PriceRequestMessage(MessageType.REQUEST, RequestType.APPROVE_PRICE_REQUEST , request); // IS request THE RIGHT PARAMETER?
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public static void denyPriceRequest(PriceRequest request) {
        PriceRequestMessage requestMessage = new PriceRequestMessage(MessageType.REQUEST, RequestType.DECLINE_PRICE_REQUEST , request); // IS request THE RIGHT PARAMETER?
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}