package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;

public class Hall {
    int id;
    int seats;
    int capacity;
    Theater theater;
    List<MovieInstance> movieInstances;

    public Hall(int id, int seats, int capacity, Theater theater, List<MovieInstance> movieInstances) {
        this.id = id;
        this.seats = seats;
        this.capacity = capacity;
        this.theater = theater;
        this.movieInstances = movieInstances;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public List<MovieInstance> getMovieInstances() {
        return movieInstances;
    }

    public void setMovieInstances(List<MovieInstance> movieInstances) {
        this.movieInstances = movieInstances;
    }
}
