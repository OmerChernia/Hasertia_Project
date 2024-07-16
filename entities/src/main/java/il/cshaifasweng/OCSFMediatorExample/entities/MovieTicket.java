package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("movie_ticket")
public class MovieTicket extends Purchase{

    @OneToOne
    private MovieInstance movieInstance;

    @OneToOne
    private Seat seat;

    public MovieTicket() {
    }

    public MovieTicket(LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation, MovieInstance movieInstance, Seat seat) {
        super(purchaseDate, owner, purchaseValidation);
        this.movieInstance = movieInstance;
        this.seat = seat;
    }

    // Getters and setters

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

    @Override
    protected String getPurchaseType() {
        return "MovieTicket";
    }

    @Override
    public String toJson() {
        JSONObject jsonObject = new JSONObject(super.toJson());
        jsonObject.put("movieInstance", movieInstance != null ? movieInstance.toJson() : JSONObject.NULL);
        jsonObject.put("seat", seat != null ? seat.toJson() : JSONObject.NULL);
        return jsonObject.toString();
    }

    public static MovieTicket fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        MovieTicket movieTicket = new MovieTicket();
        movieTicket.setId(jsonObject.getInt("id"));
        movieTicket.movieInstance = jsonObject.isNull("movieInstance") ? null : MovieInstance.fromJson(jsonObject.getString("movieInstance"));
        movieTicket.seat = jsonObject.isNull("seat") ? null : Seat.fromJson(jsonObject.getString("seat"));
        movieTicket.setPurchaseDate(LocalDateTime.parse(jsonObject.getString("purchaseDate")));
        movieTicket.setOwner(jsonObject.isNull("owner") ? null : RegisteredUser.fromJson(jsonObject.getString("owner")));
        movieTicket.setPurchaseValidation(jsonObject.getString("purchaseValidation"));
        return movieTicket;
    }
}