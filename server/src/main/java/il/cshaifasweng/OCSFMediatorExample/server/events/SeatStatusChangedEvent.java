package il.cshaifasweng.OCSFMediatorExample.server.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Seat;

import java.util.List;

public class SeatStatusChangedEvent extends Event{

    public List<Seat> seats;

    public SeatStatusChangedEvent(List<Seat> seats)
    {
        isItSeatMessage = true;
        this.seats=seats;
    }
}
