package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @ManyToOne
    Hall hall;

    // needs to be fixed!!!!!!!
    @OneToMany
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

    public void setRow(int row) {
        this.row = row;
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

    public void setMovies(List<MovieInstance> movies) {
        this.taken = movies;
    }
}