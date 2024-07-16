package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONObject;

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

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    public String toJson() {
        System.out.println("enter");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("info", info);
        jsonObject.put("creationDate", creationDate.toString());
        jsonObject.put("purchase", purchase.toJson());
        System.out.println("passed purchase");
        jsonObject.put("isClosed", isClosed);
        jsonObject.put("registeredUser", registeredUser.toJson());
        System.out.println("passes user");
        return jsonObject.toString();
    }

    public static Complaint fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        Complaint complaint = new Complaint();
        complaint.id = jsonObject.getInt("id");
        complaint.info = jsonObject.getString("info");
        complaint.creationDate = LocalDateTime.parse(jsonObject.getString("creationDate"));
        complaint.purchase = Purchase.fromJson(jsonObject.getString("purchase"));
        complaint.isClosed = jsonObject.getBoolean("isClosed");
        complaint.registeredUser = RegisteredUser.fromJson(jsonObject.getString("registeredUser"));
        return complaint;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", info='" + info + '\'' +
                ", creationDate=" + creationDate +
                ", purchase=" + (purchase != null ? purchase.toString() : "null") +
                ", isClosed=" + isClosed +
                ", registeredUser=" + (registeredUser != null ? registeredUser.toString() : "null") +
                '}';
    }

}