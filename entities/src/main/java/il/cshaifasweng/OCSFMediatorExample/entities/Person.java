package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;


@MappedSuperclass //means we don't need a table for it, but we want its fields in employee
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column
    protected String name;
    @Column
    protected boolean isOnline;

    public Person( String name, boolean isOnline) {
        this.name = name;
        this.isOnline = isOnline;
    }

    public Person() {
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