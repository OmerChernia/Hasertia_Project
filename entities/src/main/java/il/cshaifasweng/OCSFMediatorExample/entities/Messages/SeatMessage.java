package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Seat;

import java.util.ArrayList;

public class SeatMessage extends Message
{
    public ArrayList<Seat> hallSeats;
    public RequestType requestType;
    public ResponseType responseType;
    public MovieInstance movieInstance;
    public int hallId;

    public SeatMessage(MovieInstance movieInstance, MessageType messageType, RequestType requestType)
    {
        //SEAT_RESERVED , SEAT_CANCELATION

        super(messageType);
        this.movieInstance = movieInstance;
        this.requestType = requestType;
    }
    public SeatMessage(int hall,MessageType messageType, RequestType requestType)
    {
        //GET_ALL_SEAT_BY_HALL
        super(messageType);
        this.hallId = hall;
        this.requestType = requestType;
    }

    public enum ResponseType
    {
        SEAT_WAS_RESERVED,
        SEAT_IS_NOW_AVAILABLE,
        SEATS_LIST
    }
    public enum RequestType
    {
        SEAT_RESERVED,
        SEAT_CANCELATION,
        GET_ALL_SEAT_BY_HALL
    }
}
