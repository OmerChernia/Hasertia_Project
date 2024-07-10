package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer_service")
public class CustomerService extends ABS_Employee{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "customerService")
    private List<Complaint> complaints;

    public CustomerService() {
    }

    public CustomerService(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    // Getters and setters

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
