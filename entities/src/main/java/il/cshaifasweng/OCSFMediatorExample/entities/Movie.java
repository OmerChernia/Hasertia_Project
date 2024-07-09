package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;

public class Movie {
    String hebrewName;
    String info;
    String producer;
    String englishName;
    List<String> mainActors;
    String image;
    String streamingType;
    int duration;

    public Movie(String hebrewName, String info, String producer, String englishName, List<String> mainActors, String image, String streamingType, int duration) {
        this.hebrewName = hebrewName;
        this.info = info;
        this.producer = producer;
        this.englishName = englishName;
        this.mainActors = mainActors;
        this.image = image;
        this.streamingType = streamingType;
        this.duration = duration;
    }

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

    public String getStreamingType() {
        return streamingType;
    }

    public void setStreamingType(String streamingType) {
        this.streamingType = streamingType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
