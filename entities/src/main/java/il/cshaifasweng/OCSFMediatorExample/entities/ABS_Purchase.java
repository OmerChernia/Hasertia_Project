package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "purchases")
public class ABS_Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    @ManyToOne
    private RegisteredUser owner;

    @Column
    private String purchaseValidation;

    @Column
    private String attribute;

    public ABS_Purchase(Date purchaseDate, RegisteredUser owner, String purchaseValidation, String attribute) {
        this.purchaseDate = purchaseDate;
        this.owner = owner;
        this.purchaseValidation = purchaseValidation;
        this.attribute = attribute;
    }

    public ABS_Purchase() {
    }

    // Getters and setters

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
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

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
