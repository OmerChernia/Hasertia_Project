package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "halls")
public class Hall implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne
    private Theater theater;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "hall")
    private List<MovieInstance> movieInstances;

    public Hall() {
    }

    public Hall(int id, List<Seat> seats, int capacity, Theater theater, List<MovieInstance> movieInstances, String name) {
        this.id = id;
        this.seats = seats;
        this.capacity = capacity;
        this.theater = theater;
        this.movieInstances = movieInstances;
        this.name = name;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("capacity", capacity);
        jsonObject.put("name", name);
        jsonObject.put("theater", theater != null ? theater.toJson() : JSONObject.NULL);

        JSONArray seatsArray = new JSONArray();
        for (Seat seat : seats) {
            seatsArray.put(seat.toJson());
        }
        jsonObject.put("seats", seatsArray);

        JSONArray movieInstancesArray = new JSONArray();
        for (MovieInstance movieInstance : movieInstances) {
            movieInstancesArray.put(movieInstance.toJson());
        }
        jsonObject.put("movieInstances", movieInstancesArray);

        return jsonObject.toString();
    }

    public static Hall fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        Hall hall = new Hall();
        hall.id = jsonObject.getInt("id");
        hall.capacity = jsonObject.getInt("capacity");
        hall.name = jsonObject.getString("name");
        hall.theater = jsonObject.isNull("theater") ? null : Theater.fromJson(jsonObject.getString("theater"));

        JSONArray seatsArray = jsonObject.getJSONArray("seats");
        List<Seat> seatsList = new ArrayList<>();
        for (int i = 0; i < seatsArray.length(); i++) {
            seatsList.add(Seat.fromJson(seatsArray.getString(i)));
        }
        hall.setSeats(seatsList);

        JSONArray movieInstancesArray = jsonObject.getJSONArray("movieInstances");
        List<MovieInstance> movieInstancesList = new ArrayList<>();
        for (int i = 0; i < movieInstancesArray.length(); i++) {
            movieInstancesList.add(MovieInstance.fromJson(movieInstancesArray.getString(i)));
        }
        hall.setMovieInstances(movieInstancesList);

        return hall;
    }


}
