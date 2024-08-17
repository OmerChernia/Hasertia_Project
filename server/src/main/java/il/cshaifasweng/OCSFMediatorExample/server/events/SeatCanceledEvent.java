package il.cshaifasweng.OCSFMediatorExample.server.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Seat;

public class SeatCanceledEvent {

    public Seat seat;

    public SeatCanceledEvent(Seat seat)
    {
        this.seat=seat;
    }
}
