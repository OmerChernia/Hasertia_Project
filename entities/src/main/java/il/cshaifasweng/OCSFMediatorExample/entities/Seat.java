package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seats")
public class Seat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "row_num", nullable = false) // Avoid reserved keyword
    int row;

    @Column(nullable = false)
    int col;

    @ManyToOne
    Hall hall;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "seats_movie_instances",
            joinColumns = @JoinColumn(name = "seat_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_instance_id")
    )
    List<MovieInstance> taken;

    public Seat() {
    }

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
        taken = new ArrayList<>();
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int rowNumber) {
        this.row = rowNumber;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public List<MovieInstance> getMovies() {
        return taken;
    }

    public void addMovieInstance(MovieInstance movie) {
        this.taken.add(movie);
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Hall getHall() {
        return hall;
    }
}
