package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.json.JSONObject;

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


    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("movie", movie != null ? movie.toJson() : JSONObject.NULL);
        jsonObject.put("time", time.toString());
        jsonObject.put("hall", hall != null ? hall.toJson() : JSONObject.NULL);
        return jsonObject.toString();
    }

    public static MovieInstance fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        MovieInstance movieInstance = new MovieInstance();
        movieInstance.id = jsonObject.getInt("id");
        movieInstance.movie = jsonObject.isNull("movie") ? null : Movie.fromJson(jsonObject.getString("movie"));
        movieInstance.time = LocalDateTime.parse(jsonObject.getString("time"));
        movieInstance.hall = jsonObject.isNull("hall") ? null : Hall.fromJson(jsonObject.getString("hall"));
        return movieInstance;
    }

}