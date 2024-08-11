package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_instances")
public class MovieInstance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Movie movie;

    private LocalDateTime time;

    @ManyToOne
    private Hall hall;

    public MovieInstance() {
    }

    public MovieInstance(Movie movie, LocalDateTime time, Hall hall) {
        this.movie = movie;
        this.time = time;
        this.hall = hall;}

    // Getters and setters

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }


    public int getId() {
        return id;
    }


}