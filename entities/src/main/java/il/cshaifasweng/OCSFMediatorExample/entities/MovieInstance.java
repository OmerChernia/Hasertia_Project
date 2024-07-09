package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.Date;

public class MovieInstance {
    Movie movie;
    Date date;
    Hall hall;
    int price;

    public MovieInstance(Movie movie, Date date, Hall hall, int price) {
        this.movie = movie;
        this.date = date;
        this.hall = hall;
        this.price = price;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
