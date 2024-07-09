package il.cshaifasweng.OCSFMediatorExample.entities;

public class Theater {
    int id;
    String location;
    List<Hall> halls;
    List<MovieInstance> movieInstances;
    TheaterManager manager;

    public Theater(int id, String location, List<Hall> halls, List<MovieInstance> movieInstances, TheaterManager manager) {
        this.id = id;
        this.location = location;
        this.halls = halls;
        this.movieInstances = movieInstances;
        this.manager = manager;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public void setHalls(List<Hall> halls) {
        this.halls = halls;
    }

    public List<MovieInstance> getMovieInstances() {
        return movieInstances;
    }

    public void setMovieInstances(List<MovieInstance> movieInstances) {
        this.movieInstances = movieInstances;
    }

    public TheaterManager getManager() {
        return manager;
    }

    public void setManager(TheaterManager manager) {
        this.manager = manager;
    }
}
