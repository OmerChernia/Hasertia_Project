package il.cshaifasweng.OCSFMediatorExample.server.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;

public class HomeViewingEvent extends Event{
    public Movie movie;
    public HomeViewingEvent(Movie movie)
    {
        this.movie = movie;
    }

}
