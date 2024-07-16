package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "purchases")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public abstract class Purchase implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime purchaseDate;

    @ManyToOne
    private RegisteredUser owner;

    @Column
    private String purchaseValidation;


    public Purchase(LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation) {
        this.purchaseDate = purchaseDate;
        this.purchaseValidation = purchaseValidation;
        this.owner = owner;
    }


    public Purchase() {
    }

    // Getters and setters

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public RegisteredUser getOwner() {
        return owner;
    }

    public void setOwner(RegisteredUser owner) {
        this.owner = owner;
    }

    public String getPurchaseValidation() {
        return purchaseValidation;
    }

    public void setPurchaseValidation(String purchaseValidation) {
        this.purchaseValidation = purchaseValidation;
    }

    public int getId(){return this.id;}

    public void setId(int id) {
        this.id = id;
    }

    protected abstract String getPurchaseType();

    public String toJson() {
        System.out.println("entered Purchase class");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("purchaseDate", purchaseDate.toString());
        jsonObject.put("owner", owner != null ? owner.toJson() : JSONObject.NULL);
        System.out.println("passed owner of purchase");
        jsonObject.put("purchaseValidation", purchaseValidation);
        jsonObject.put("purchaseType", getPurchaseType());
        System.out.println("finished purchase");
        return jsonObject.toString();
    }

    public static Purchase fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String purchaseType = jsonObject.getString("purchaseType");

        switch (purchaseType) {
            case "HomeViewingPackageInstance":
                return HomeViewingPackageInstance.fromJson(jsonString);
            case "MultiEntryTicket":
                return MultiEntryTicket.fromJson(jsonString);
            // Add cases for other subclasses here
            default:
                throw new IllegalArgumentException("Unknown purchase type: " + purchaseType);
        }
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", purchaseDate=" + purchaseDate +
                ", owner=" + (owner != null ? owner.toString() : "null") +
                ", purchaseValidation='" + purchaseValidation + '\'' +
                '}';
    }


}