package il.cshaifasweng.OCSFMediatorExample.entities;

public class PriceRequest {
    String newPrice;
    Movie movie;

    public PriceRequest(String newPrice, Movie movie) {
        this.newPrice = newPrice;
        this.movie = movie;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
