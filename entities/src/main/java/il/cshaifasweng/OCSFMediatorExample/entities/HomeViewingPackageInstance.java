package il.cshaifasweng.OCSFMediatorExample.entities;

public class HomeViewingPackageInstance {
    Movie movie;
    Date viewingDate;
    int price;

    public HomeViewingPackageInstance(Movie movie, Date viewingDate, int price) {
        this.movie = movie;
        this.viewingDate = viewingDate;
        this.price = price;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Date getViewingDate() {
        return viewingDate;
    }

    public void setViewingDate(Date viewingDate) {
        this.viewingDate = viewingDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
