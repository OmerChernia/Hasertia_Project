package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Hall;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.HallMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HallHandler extends MessageHandler {
    private HallMessage message;

    public HallHandler(HallMessage message, ConnectionToClient client, Session session) {
        super(client, session);
        this.message = message;
    }

    public void handleMessage() {
        switch (message.requestType) {
            case GET_AVAILABLE_DATES -> get_available_dates();
            case GET_AVAILABLE_TIMES -> get_available_times();
        }
    }

    @Override
    public void setMessageTypeToResponse() {
        message.messageType = HallMessage.MessageType.RESPONSE;
    }

    private void get_available_dates() {
        try {
            Hall hall = session.get(Hall.class, message.getHall().getId());
            List<LocalDate> availableDates = get_available_dates_for_hall(hall);
            message.setAvailableDates(availableDates);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle transaction rollback if necessary
        }
    }

    private List<LocalDate> get_available_dates_for_hall(Hall hall) {
        if (hall.getMovieInstances() == null) {
            hall.setMovieInstances(new ArrayList<>());
        }

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusWeeks(2);

        return today.datesUntil(endDate)
                .filter(date -> hall.getMovieInstances().stream().noneMatch(
                        instance -> instance.getTime().toLocalDate().equals(date)
                ))
                .collect(Collectors.toList());
    }

    private void get_available_times() {
        try {
            Hall hall = session.get(Hall.class, message.getHall().getId());
            LocalDate date = message.getDate();
            List<LocalTime> availableTimes = get_available_times_for_hall(hall, date);
            message.setAvailableTimes(availableTimes);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle transaction rollback if necessary
        }
    }

    private List<LocalTime> get_available_times_for_hall(Hall hall, LocalDate date) {
        if (hall.getMovieInstances() == null) {
            hall.setMovieInstances(new ArrayList<>());
        }

        LocalTime startTime = LocalTime.of(12, 0);
        LocalTime endTime = LocalTime.MIDNIGHT;

        List<LocalTime> availableTimes = new ArrayList<>();

        for (LocalTime time = startTime; !time.equals(endTime); time = time.plusHours(1)) {
            LocalTime finalTime = time;
            boolean isTimeAvailable = hall.getMovieInstances().stream().noneMatch(
                    instance -> instance.getTime().toLocalDate().equals(date) &&
                            instance.getTime().toLocalTime().equals(finalTime)
            );
            if (isTimeAvailable) {
                availableTimes.add(time);
            }
        }

        return availableTimes;
    }
}
