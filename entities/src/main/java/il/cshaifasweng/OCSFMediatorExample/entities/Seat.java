package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    int row;

    @Column(nullable = false)
    int col;


    // needs to be fixed!!!!!!!
    @OneToMany
    @JoinTable(
        name = "seat_movie_instance",
        joinColumns = @JoinColumn(name = "seat_id"),
        inverseJoinColumns = @JoinColumn(name = "movie_instance_id")
    )
    Map<MovieInstance, boolean> movies = new HashMap<>();

    public Seat() {
    }

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
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

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Map<MovieInstance, Boolean> getMovies() {
        return movies;
    }

    public void setMovies(Map<MovieInstance, Boolean> movies) {
        this.movies = movies;
    }
}