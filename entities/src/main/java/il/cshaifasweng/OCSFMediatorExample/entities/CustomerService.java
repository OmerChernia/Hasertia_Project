package il.cshaifasweng.OCSFMediatorExample.entities;

public class CustomerService {
    List<Complaint> complaints;

    public CustomerService(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}
