package il.cshaifasweng.OCSFMediatorExample.server.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;

public class MovieCanceledEvent extends Event
{
    public Movie movie;

    public MovieCanceledEvent(MovieInstance movieInstance)
    {
        this.movie = movie;
    }
}