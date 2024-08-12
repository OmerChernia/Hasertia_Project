package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Hall;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.HallMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HallHandler extends MessageHandler
{
    private HallMessage message;

    public HallHandler(HallMessage message, ConnectionToClient client, Session session)
    {
        super(client, session);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case GET_AVAILABLE_DATES -> getAvailableDates();
            case GET_AVAILABLE_TIMES -> getAvailableTimes();
        }
    }

    @Override
    public void setMessageTypeToResponse()
    {
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
        LocalDate endDate = today.plusWeeks(2);  // Assuming we're checking the next 2 weeks

        return today.datesUntil(endDate)
                .filter(date -> hall.getMovieInstances().stream().noneMatch(
                        instance -> instance.getTime().toLocalDate().equals(date)
                ))
                .collect(Collectors.toList());
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
        LocalTime startTime = LocalTime.of(12, 0);
        LocalTime endTime = LocalTime.MIDNIGHT;  // Assuming end time is at midnight

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
