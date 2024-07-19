package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.SeatMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Seat;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class SeatHandler extends MessageHandler
{
    private SeatMessage message;

    public SeatHandler(SeatMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case SEAT_RESERVED -> seat_reserved();
            case SEAT_CANCELATION -> seat_cancellation();
            case GET_ALL_SEAT_BY_HALL -> get_all_seat_by_hall();
        }
    }
    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    private void seat_reserved() {
        try {
            MovieInstance movieInstance = message.movieInstance;
            Seat seat = (Seat) session.get(Seat.class, message.movieInstance.getId());
            if (seat != null) {
                seat.addMovieInstance(movieInstance);
                session.saveOrUpdate(seat);
                session.flush();
                message.responseType = SeatMessage.ResponseType.SEAT_WAS_RESERVED;
            } else {
                message.responseType = SeatMessage.ResponseType.SEAT_IS_NOW_AVAILABLE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = SeatMessage.ResponseType.SEAT_IS_NOW_AVAILABLE;
        }
    }
    private void seat_cancellation() {
        try {
            MovieInstance movieInstance = message.movieInstance;
            Seat seat = (Seat) session.get(Seat.class, message.movieInstance.getId());
            if (seat != null) {
                seat.getMovies().remove(movieInstance);
                session.saveOrUpdate(seat);
                session.flush();
                message.responseType = SeatMessage.ResponseType.SEAT_IS_NOW_AVAILABLE;
            } else {
                message.responseType = SeatMessage.ResponseType.SEAT_WAS_RESERVED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = SeatMessage.ResponseType.SEAT_WAS_RESERVED;
        }
    }

    private void get_all_seat_by_hall() {
        try {
            Query<Seat> query = session.createQuery("from Seat where hall.id = :hallId", Seat.class);
            query.setParameter("hallId", message.hallId);
            List<Seat> seats = query.list();
            message.hallSeats = new ArrayList<>(seats);
            message.responseType = SeatMessage.ResponseType.SEATS_LIST;
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = SeatMessage.ResponseType.SEAT_IS_NOW_AVAILABLE;
        }
    }
}
