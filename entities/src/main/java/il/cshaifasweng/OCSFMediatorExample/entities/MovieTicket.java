package il.cshaifasweng.OCSFMediatorExample.entities;

public class MovieTicket {
    MovieInstance movieInstance;
    Seat seat;

    public MovieTicket(MovieInstance movieInstance, Seat seat) {
        this.movieInstance = movieInstance;
        this.seat = seat;
    }

    public MovieInstance getMovieInstance() {
        return movieInstance;
    }

    public void setMovieInstance(MovieInstance movieInstance) {
        this.movieInstance = movieInstance;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
