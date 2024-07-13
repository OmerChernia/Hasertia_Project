package il.cshaifasweng.OCSFMediatorExample.entities;

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
}