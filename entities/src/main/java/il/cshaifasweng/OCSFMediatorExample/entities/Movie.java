package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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
    private long id;

    @Column(nullable = false)
    private String hebrewName;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private String producer;

    @Column(nullable = false)
    private String englishName;

    @ElementCollection
    private List<String> mainActors;

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


    public Movie() {
    }

    public Movie(String hebrewName, String info, String producer, String englishName, List<String> mainActors, String image, StreamingType streamingType, int duration, int theaterPrice, int homeViewingPrice) {
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

    public List<String> getMainActors() {
        return mainActors;
    }

    public void setMainActors(List<String> mainActors) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("hebrewName", hebrewName);
        jsonObject.put("info", info);
        jsonObject.put("producer", producer);
        jsonObject.put("englishName", englishName);
        jsonObject.put("image", image);
        jsonObject.put("streamingType", streamingType.name());
        jsonObject.put("duration", duration);
        jsonObject.put("homeViewingPrice", homeViewingPrice);
        jsonObject.put("theaterPrice", theaterPrice);

        JSONArray mainActorsArray = new JSONArray();
        for (String actor : mainActors) {
            mainActorsArray.put(actor);
        }
        jsonObject.put("mainActors", mainActorsArray);

        return jsonObject.toString();
    }

    public static Movie fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        Movie movie = new Movie();
        movie.setId(jsonObject.getLong("id"));
        movie.setHebrewName(jsonObject.getString("hebrewName"));
        movie.setInfo(jsonObject.getString("info"));
        movie.setProducer(jsonObject.getString("producer"));
        movie.setEnglishName(jsonObject.getString("englishName"));
        movie.setImage(jsonObject.getString("image"));
        movie.setStreamingType(StreamingType.valueOf(jsonObject.getString("streamingType")));
        movie.setDuration(jsonObject.getInt("duration"));
        movie.setHomeViewingPrice(jsonObject.getInt("homeViewingPrice"));
        movie.setTheaterPrice(jsonObject.getInt("theaterPrice"));

        JSONArray mainActorsArray = jsonObject.getJSONArray("mainActors");
        List<String> mainActorsList = new ArrayList<>();
        for (int i = 0; i < mainActorsArray.length(); i++) {
            mainActorsList.add(mainActorsArray.getString(i));
        }
        movie.setMainActors(mainActorsList);

        return movie;
    }

}