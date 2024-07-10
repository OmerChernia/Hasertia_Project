package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name = "movie_tickets")
public class MovieTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private MovieInstance movieInstance;

    @OneToOne
    private Seat seat;

    public MovieTicket() {
    }

    public MovieTicket(MovieInstance movieInstance, Seat seat) {
        this.movieInstance = movieInstance;
        this.seat = seat;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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