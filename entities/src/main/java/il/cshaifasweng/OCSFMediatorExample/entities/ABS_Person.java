package il.cshaifasweng.OCSFMediatorExample.entities;

public abstract class ABS_Person {
    int id;
    String name;
    boolean isOnline;

    public ABS_Person(int id, String name, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.isOnline = isOnline;
    }

    public ABS_Person() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
