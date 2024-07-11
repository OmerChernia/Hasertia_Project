package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.security.PublicKey;
import java.time.LocalDateTime;

@Entity
@Table(name = "Multi_Entry_Tickets")
public class MultiEntryTicket extends Purchase
{
    public MultiEntryTicket(){}

    public MultiEntryTicket(LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation, String attribute) {
        super(purchaseDate, owner, purchaseValidation, attribute);
    }
}
