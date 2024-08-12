package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie implements Serializable {

    public enum StreamingType {
        HOME_VIEWING,
        THEATER_VIEWING,
        BOTH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String hebrewName;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private String producer;

    @Column(nullable = false)
    private String englishName;

    @Column
    private String mainActors;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private StreamingType streamingType;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int homeViewingPrice;

    @Column(nullable = false)
    private int theaterPrice;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private boolean isActive;

    public Movie()
    {

    }

    public Movie(String hebrewName, String info, String producer, String englishName, String mainActors, String image, StreamingType streamingType, int duration, int theaterPrice, int homeViewingPrice, String genre, boolean isActive) {
        this.hebrewName = hebrewName;
        this.info = info;
        this.producer = producer;
        this.englishName = englishName;
        this.mainActors = mainActors;
        this.image = image;
        this.streamingType = streamingType;
        this.duration = duration;
        this.homeViewingPrice = homeViewingPrice;
        this.theaterPrice = theaterPrice;
        this.genre = genre;
        this.isActive = isActive;
    }

    // Getters and setters

    public String getHebrewName() {
        return hebrewName;
    }

    public void setHebrewName(String hebrewName) {
        this.hebrewName = hebrewName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }


    public List<String> getMainActors()
    {
        return Arrays.stream(mainActors.split("_")).toList();
    }

    public void setMainActors(String mainActors) {
        this.mainActors = mainActors;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public StreamingType getStreamingType() {
        return streamingType;
    }

    public void setStreamingType(StreamingType streamingType) {
        this.streamingType = streamingType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHomeViewingPrice() {
        return homeViewingPrice;
    }

    public void setHomeViewingPrice(int homeViewingPrice) {
        this.homeViewingPrice = homeViewingPrice;
    }

    public int getTheaterPrice() {
        return theaterPrice;
    }

    public void setTheaterPrice(int theaterPrice) {
        this.theaterPrice = theaterPrice;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {}

    public boolean isActive() {return isActive;}

    public void setActive(boolean isActive) {this.isActive = isActive;}


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", hebrewName='" + hebrewName + '\'' +
                ", info='" + info + '\'' +
                ", producer='" + producer + '\'' +
                ", englishName='" + englishName + '\'' +
                ", mainActors=" + mainActors +
                ", image='" + image + '\'' +
                ", streamingType=" + streamingType +
                ", duration=" + duration +
                ", homeViewingPrice=" + homeViewingPrice +
                ", theaterPrice=" + theaterPrice +
                ", genre=" + genre +
                ", isActive=" + isActive +
                '}';
    }

}