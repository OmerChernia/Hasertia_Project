package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;

public class RegisteredUser extends ABS_Person{
    String email;
    List<Complaint> complaints;
    List<ABS_Purchase> purchases;

    public RegisteredUser(int id, String name, boolean isOnline, String email, List<Complaint> complaints, List<ABS_Purchase> purchases) {
        super(id, name, isOnline);
        this.email = email;
        this.complaints = complaints;
        this.purchases = purchases;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<ABS_Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<ABS_Purchase> purchases) {
        this.purchases = purchases;
    }
}

