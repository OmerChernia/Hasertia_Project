package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name = "theater_managers")
public class TheaterManager extends Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "theater")
    private Theater theater;

    public TheaterManager() {
    }

    // Getters and Setters

    public TheaterManager(int id, String name, boolean isOnline, String password, EmployeeType employeeType, Theater theater) {
        super(id, name, isOnline, password, employeeType);
        this.theater = theater;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }
}