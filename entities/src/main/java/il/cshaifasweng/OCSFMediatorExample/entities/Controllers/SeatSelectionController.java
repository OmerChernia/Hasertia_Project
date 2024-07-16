package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.SeatMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Seat;

import java.io.IOException;
import java.util.ArrayList;

public class SeatSelectionController {
    private static SimpleClient client;
    private ArrayList<Seat> selectedSeats;

    public SeatSelectionController() {
        selectedSeats = new ArrayList<>();
    }

    public Seat[] getSelectedSeats() {
        return selectedSeats.toArray(new Seat[0]);
    }

    public void apply_selection() {
        SeatMessage seatSelectionMessage = new SeatMessage(Message.MessageType.REQUEST, SeatMessage.RequestType.APPLY_SELECTION, selectedSeats);
        try {
            SimpleClient.getClient().sendToServer(seatSelectionMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        SeatSelectionController controller = new SeatSelectionController();
        controller.apply_selection();
    }
}