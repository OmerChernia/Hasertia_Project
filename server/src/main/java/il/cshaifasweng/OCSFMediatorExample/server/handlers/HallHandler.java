package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Hall;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.HallMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HallHandler extends MessageHandler {
    private HallMessage message;

    public HallHandler(HallMessage message, ConnectionToClient client, Session session) {
        super(client, session);
        this.message = message;
    }

    @Override
    public void handleMessage() {
        switch (message.requestType) {
            case GET_AVAILABLE_DATES -> getAvailableDates();
            case GET_AVAILABLE_TIMES -> getAvailableTimes();
        }
    }

    @Override
    public void setMessageTypeToResponse() {
        message.messageType = HallMessage.MessageType.RESPONSE;
    }

    private void getAvailableDates() {
        try {
            Hall hall = message.getHall();
            List<LocalDate> availableDates = getAvailableDatesForHall(hall);
            message.setAvailableDates(availableDates);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LocalDate> getAvailableDatesForHall(Hall hall) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusWeeks(2);  // Checking the next 2 weeks

        List<LocalDate> availableDates = new ArrayList<>();
        today.datesUntil(endDate).forEach(availableDates::add);

        return availableDates;
    }

    private void getAvailableTimes() {
        try {
            Hall hall = message.getHall();
            LocalDate date = message.getDate();
            List<LocalTime> availableTimes = getAvailableTimesForHall(hall, date);
            message.setAvailableTimes(availableTimes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LocalTime> getAvailableTimesForHall(Hall hall, LocalDate date) {
         return Arrays.asList(
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                LocalTime.of(22, 0),
                LocalTime.of(0, 0)
        );
    }
}
