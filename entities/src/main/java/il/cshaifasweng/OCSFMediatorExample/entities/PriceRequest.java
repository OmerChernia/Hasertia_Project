package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "price_requests")
public class PriceRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int newPrice; // Changed from String to int for better representation

    @ManyToOne
    private Movie movie;

    public PriceRequest() {
    }

    public PriceRequest(int newPrice, Movie movie) {
        this.newPrice = newPrice;
        this.movie = movie;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("newPrice", newPrice);
        jsonObject.put("movie", movie != null ? movie.toJson() : JSONObject.NULL);
        return jsonObject.toString();
    }

    public static PriceRequest fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.id = jsonObject.getInt("id");
        priceRequest.newPrice = jsonObject.getInt("newPrice");
        priceRequest.movie = jsonObject.isNull("movie") ? null : Movie.fromJson(jsonObject.getString("movie"));
        return priceRequest;
    }
}