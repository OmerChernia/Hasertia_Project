package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Multi_Entry_Ticket")
public class MultiEntryTicket extends Purchase
{
    public MultiEntryTicket(){}


    public MultiEntryTicket(LocalDateTime purchaseDate, RegisteredUser owner, String purchaseValidation, boolean isActive) {
        super(purchaseDate, owner, purchaseValidation,isActive);
    }

    @Override
    protected String getPurchaseType() {
        return "MultiEntryTicket";
    }

}
