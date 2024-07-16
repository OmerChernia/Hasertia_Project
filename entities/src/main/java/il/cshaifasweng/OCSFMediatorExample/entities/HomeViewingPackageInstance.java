package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Home_Viewing_Package")
public class HomeViewingPackageInstance extends Purchase{

    @OneToOne
    private Movie movie;

    private LocalDateTime activationDate;


    public HomeViewingPackageInstance() {
    }

    public HomeViewingPackageInstance(LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation,Movie movie, LocalDateTime viewingDate, int price) {
        super(purchaseDate, owner, purchaseValidation);
        this.movie = movie;
        this.activationDate = viewingDate;
    }

    // Getters and setters

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getViewingDate() {
        return activationDate;
    }

    public void setViewingDate(LocalDateTime viewingDate) {
        this.activationDate = viewingDate;
    }


    @Override
    protected String getPurchaseType() {
        return "HomeViewingPackageInstance";
    }

    @Override
    public String toJson() {
        JSONObject jsonObject = new JSONObject(super.toJson());
        jsonObject.put("movie", movie != null ? movie.toJson() : JSONObject.NULL);
        jsonObject.put("activationDate", activationDate.toString());
        return jsonObject.toString();
    }

    public static HomeViewingPackageInstance fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        HomeViewingPackageInstance instance = new HomeViewingPackageInstance();
        instance.setId(jsonObject.getInt("id"));
        instance.setPurchaseDate(LocalDateTime.parse(jsonObject.getString("purchaseDate")));
        instance.setOwner(jsonObject.isNull("owner") ? null : RegisteredUser.fromJson(jsonObject.getString("owner")));
        instance.setPurchaseValidation(jsonObject.getString("purchaseValidation"));
        instance.setMovie(jsonObject.isNull("movie") ? null : Movie.fromJson(jsonObject.getString("movie")));
        instance.activationDate=(LocalDateTime.parse(jsonObject.getString("activationDate")));
        return instance;
    }
}