package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "home_viewing_package_instances")
public class HomeViewingPackageInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Movie movie;

    @Temporal(TemporalType.DATE)
    private Date activationDate;

    @Column(nullable = false)
    private int price;

    public HomeViewingPackageInstance() {
    }

    public HomeViewingPackageInstance(Movie movie, Date viewingDate, int price) {
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

    public Date getViewingDate() {
        return activationDate;
    }

    public void setViewingDate(Date viewingDate) {
        this.activationDate = viewingDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
