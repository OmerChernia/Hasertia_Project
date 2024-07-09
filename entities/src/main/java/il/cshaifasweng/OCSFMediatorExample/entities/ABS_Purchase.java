package il.cshaifasweng.OCSFMediatorExample.entities;

public class ABS_Purchase {
    Date purchaseDate;
    RegisteredUser owner;
    String purchaseValidation;
    String attribute;

    public ABS_Purchase(Date purchaseDate, RegisteredUser owner, String purchaseValidation, String attribute) {
        this.purchaseDate = purchaseDate;
        this.owner = owner;
        this.purchaseValidation = purchaseValidation;
        this.attribute = attribute;
    }

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
