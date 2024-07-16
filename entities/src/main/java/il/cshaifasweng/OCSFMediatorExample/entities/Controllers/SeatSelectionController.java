package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    private Purchase purchase;

    @Column(nullable = false)
    private boolean isClosed;

    @ManyToOne
    private RegisteredUser registeredUser;

    public Complaint(String info, LocalDateTime creationDate, Purchase purchase, boolean isClosed, RegisteredUser registeredUser) {
        this.info = info;
        this.creationDate = creationDate;
        this.purchase = purchase;
        this.isClosed = isClosed;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
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


}