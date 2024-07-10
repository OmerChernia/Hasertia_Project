package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "movie_instances")
public class MovieInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Movie movie;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @ManyToOne
    private Hall hall;

    private int price;

    public MovieInstance() {
    }

    public MovieInstance(Movie movie, Date time, Hall hall, int price) {
        this.movie = movie;
        this.time = time;
        this.hall = hall;
        this.price = price;
    }

    // Getters and setters

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}