package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "home_viewing_package_instances")
public class HomeViewingPackageInstance extends Purchase{

    @OneToOne
    private Movie movie;

    private LocalDateTime activationDate;

    @Column(nullable = false)
    private int price;

    public HomeViewingPackageInstance() {
    }

    public HomeViewingPackageInstance(LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation, String attribute,Movie movie, LocalDateTime viewingDate, int price) {
        super(purchaseDate, owner, purchaseValidation, attribute);
        this.movie = movie;
        this.activationDate = viewingDate;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}