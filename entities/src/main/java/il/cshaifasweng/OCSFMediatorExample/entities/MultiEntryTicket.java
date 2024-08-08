package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Multi_Entry_Ticket")
public class MultiEntryTicket extends Purchase
{
    public MultiEntryTicket(){}


    public MultiEntryTicket(LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation) {
        super(purchaseDate, owner, purchaseValidation);
    }

    @Override
    protected String getPurchaseType() {
        return "MultiEntryTicket";
    }

}
