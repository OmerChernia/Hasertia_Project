package il.cshaifasweng.OCSFMediatorExample.entities;

import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;


@MappedSuperclass //means we don't need a table for it, but we want its fields in employee
public abstract class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column
    protected String id_number;
    @Column
    protected String name;
    @Column
    protected boolean isOnline;

    public Person(String id_number, String name, boolean isOnline) {
        this.id_number = id_number;
        this.name = name;
        this.isOnline = isOnline;
    }

    public Person() {
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
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

    public int getId(){return  id;}

    public String toJson()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("id_number", id_number);
        jsonObject.put("name", name);
        jsonObject.put("isOnline", isOnline);
        return jsonObject.toString();
    }

    public static Person fromJson(String jsonString)
    {
        JSONObject jsonObject = new JSONObject(jsonString);
        Person person = new Person() {}; // Creating an anonymous subclass of Person
        person.id = jsonObject.getInt("id");
        person.id_number = jsonObject.getString("id_number");
        person.name = jsonObject.getString("name");
        person.isOnline = jsonObject.getBoolean("isOnline");
        return person;
    }
}