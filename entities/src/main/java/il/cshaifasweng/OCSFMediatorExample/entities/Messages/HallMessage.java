package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Hall;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class HallMessage extends Message {
    private Hall hall;
    private List<LocalDate> availableDates;
    private List<LocalTime> availableTimes;
    private LocalDate date;
    public RequestType requestType;
    public ResponseType responseType;

    public HallMessage(MessageType messageType, Hall hall) {
        super(messageType);
        this.hall = hall;
    }

    public HallMessage(MessageType messageType, RequestType requestType, Hall hall) {
        super(messageType);
        this.hall = hall;
        this.requestType = requestType;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public List<LocalDate> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<LocalDate> availableDates) {
        this.availableDates = availableDates;
    }

    public List<LocalTime> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<LocalTime> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public enum RequestType {
        GET_AVAILABLE_DATES,
        GET_AVAILABLE_TIMES
    }

    public enum ResponseType {
        AVAILABLE_TIMES,
        ALL_AVAILABLE_TIMES
    }


}
