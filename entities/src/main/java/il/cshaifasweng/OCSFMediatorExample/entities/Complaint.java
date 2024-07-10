package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "complaints")
public class Complaint {
    @Id
    private int id;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne
    private ABS_Purchase purchase;

    @Column(nullable = false)
    private boolean isClosed;

    @ManyToOne
    private CustomerService customerService;

    @ManyToOne
    @MapsId
    private RegisteredUser registeredUser;

    public Complaint(String info, Date creationDate, ABS_Purchase purchase, boolean isClosed, CustomerService customerService, RegisteredUser registeredUser) {
        this.info = info;
        this.creationDate = creationDate;
        this.purchase = purchase;
        this.isClosed = isClosed;
        this.customerService = customerService;
        this.registeredUser = registeredUser;
    }

    public Complaint() {
    }

    // Getters and setters

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ABS_Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(ABS_Purchase purchase) {
        this.purchase = purchase;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

}
