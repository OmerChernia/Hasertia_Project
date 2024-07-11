package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "row_number", nullable = false) // Avoid reserved keyword
    int rowNumber;

    @Column(nullable = false)
    int col;

    @ManyToOne
    Hall hall;

    @OneToMany
    @JoinTable(
            name = "seats_movie_instances",
            joinColumns = @JoinColumn(name = "seat_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_instance_id")
    )
    List<MovieInstance> taken;

    public Seat() {
    }

    public Seat(int row, int col) {
        this.rowNumber = row;
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

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
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

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Hall getHall() {
        return hall;
    }
}
