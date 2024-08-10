package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage.RequestType;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;

import java.util.List;

public class MovieController {

    public static void  requestAllMovies() {
        MovieMessage requestMessage = new MovieMessage(MessageType.REQUEST, RequestType.GET_ALL_MOVIES);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void addMovie(String hebrewName, String info, String producer, String englishName, List<String> mainActors, String image, Movie.StreamingType streamingType, int duration, int theaterPrice, int homeViewingPrice, String genre)
    {
        Movie movie = new Movie(hebrewName,info,producer,englishName,mainActors,image,streamingType,duration,theaterPrice,homeViewingPrice,genre,true);
        MovieMessage requestMessage = new MovieMessage(MessageType.REQUEST, movie, RequestType.ADD_MOVIE);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    public void updateMovie(Movie movie, String hebrewName, String info, String producer, String englishName, List<String> mainActors, String image, Movie.StreamingType streamingType, int duration, int theaterPrice, int homeViewingPrice, String genre)
    {
        movie.setHebrewName(hebrewName);
        movie.setInfo(info);
        movie.setProducer(producer);
        movie.setEnglishName(englishName);
        movie.setMainActors(mainActors);
        movie.setImage(image);
        movie.setStreamingType(streamingType);
        movie.setDuration(duration);
        movie.setTheaterPrice(theaterPrice);
        movie.setHomeViewingPrice(homeViewingPrice);
        movie.setGenre(genre);

        MovieMessage requestMessage = new MovieMessage(MessageType.REQUEST, movie, RequestType.UPDATE_MOVIE);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

    // id of the movie in mySql
    public void deleteMovie(int id)
    {
        MovieMessage requestMessage = new MovieMessage(MessageType.REQUEST, RequestType.DEACTIVATE_MOVIE, id);
        SimpleClient.getClient().sendRequest(requestMessage);
    }
}