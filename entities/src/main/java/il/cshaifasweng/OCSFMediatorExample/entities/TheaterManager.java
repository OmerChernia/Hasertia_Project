package il.cshaifasweng.OCSFMediatorExample.entities;

public class TheaterManager {
    Theater theater;

    public TheaterManager(Theater theater) {
        this.theater = theater;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }
}
