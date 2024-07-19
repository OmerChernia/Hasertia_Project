package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class Temp {

    private static SimpleClient client;

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        // Test SeatHandler
        System.out.println("Testing SeatHandler...");
        testSeatHandler();

//        // Test PurchaseHandler
//        System.out.println("Testing PurchaseHandler...");
//        testPurchaseHandler();
//
//        // Test TheaterHandler
//        System.out.println("Testing TheaterHandler...");
//        testTheaterHandler();
//
//        System.out.println("All tests initiated.");
    }

    private static void testSeatHandler() throws IOException {
        System.out.println("Sending seat reserve message...");
        MovieInstance movieInstance = new MovieInstance();
        SeatMessage seatReserveMessage = new SeatMessage(movieInstance, Message.MessageType.REQUEST, SeatMessage.RequestType.SEAT_RESERVED);
        client.sendToServer(seatReserveMessage);

//        System.out.println("Sending seat cancel message...");
//        SeatMessage seatCancelMessage = new SeatMessage(movieInstance, Message.MessageType.REQUEST, SeatMessage.RequestType.SEAT_CANCELATION);
//        client.sendToServer(seatCancelMessage);
//
//        System.out.println("Sending get all seats message...");
//        SeatMessage getAllSeatsMessage = new SeatMessage(1, Message.MessageType.REQUEST, SeatMessage.RequestType.GET_ALL_SEAT_BY_HALL);
//        client.sendToServer(getAllSeatsMessage);
    }

    private static void testPurchaseHandler() throws IOException {
        System.out.println("Sending get all movie packages and multi-entry tickets this month message...");
        PurchaseMessage getAllMoviePackagesAndMultiEntryTicketsThisMonth = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.GET_ALL_MOVIE_PACKAGES_AND_MULTI_TICKETS_PURCHASES_THIS_MONTH);
        client.sendToServer(getAllMoviePackagesAndMultiEntryTicketsThisMonth);
    }

    private static void testTheaterHandler() throws IOException {
        System.out.println("Sending get all theaters message...");
        TheaterMessage getAllTheatersMessage = new TheaterMessage(Message.MessageType.REQUEST, TheaterMessage.RequestType.GET_ALL_THEATERS);
        client.sendToServer(getAllTheatersMessage);

        System.out.println("Sending get theater by ID message...");
        TheaterMessage getTheaterMessage = new TheaterMessage(Message.MessageType.REQUEST, 1, TheaterMessage.RequestType.GET_THEATER);
        client.sendToServer(getTheaterMessage);
    }

    public static void func1(LoginMessage message) {
        System.out.println("Login response received:");
        System.out.println(message.responseType);
    }

    public static void func2(EmployeeLoginMessage message) {
        System.out.println("Employee login response received:");
        System.out.println(message.responseType);
    }

    public static void func3(ComplaintMessage message) {
        System.out.println("Complaint response received:");
        System.out.println(message.responseType);
        for (Complaint comp : message.compliants) {
            System.out.println(comp.toString());
        }
    }

    public static void func4(MovieMessage message) {
        System.out.println("Movie response received:");
        System.out.println(message.responseType);
        for (Movie movie : message.movies) {
            System.out.println("Movie ID: " + movie.getId());
        }
    }

    public static void func5(MovieInstanceMessage message) {
        System.out.println("Movie instance response received:");
        System.out.println(message.responseType);
        for (MovieInstance movie : message.movies) {
            System.out.println("Movie location: " + movie.getHall().getTheater().getLocation());
        }
    }

    public static void func6(SeatMessage message) {
        System.out.println("Seat response received:");
        System.out.println(message.responseType);
        if (message.hallSeats != null) {
            for (Seat seat : message.hallSeats) {
                System.out.println("Seat ID: " + seat.getId());
            }
        }
    }

    public static void func7(PurchaseMessage message) {
        System.out.println("Purchase response received:");
        System.out.println(message.responseType);
        if (message.purchases != null) {
            for (Purchase purchase : message.purchases) {
                System.out.println("Purchase ID: " + purchase.getId());
            }
        }
    }

    public static void func8(TheaterMessage message) {
        System.out.println("Theater response received:");
        System.out.println(message.responseType);
        if (message.theaterList != null) {
            for (Theater theater : message.theaterList) {
                System.out.println("Theater location: " + theater.getLocation());
            }
        }
    }
}
