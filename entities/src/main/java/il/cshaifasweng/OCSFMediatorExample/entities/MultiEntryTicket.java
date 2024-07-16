package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONObject;

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

    @Override
    public String toJson() {
        JSONObject jsonObject = new JSONObject(super.toJson());
        return jsonObject.toString();
    }

    public static MultiEntryTicket fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        MultiEntryTicket multiEntryTicket = new MultiEntryTicket();
        multiEntryTicket.setId(jsonObject.getInt("id"));
        multiEntryTicket.setPurchaseDate(LocalDateTime.parse(jsonObject.getString("purchaseDate")));
        multiEntryTicket.setOwner(jsonObject.isNull("owner") ? null : RegisteredUser.fromJson(jsonObject.getString("owner")));
        multiEntryTicket.setPurchaseValidation(jsonObject.getString("purchaseValidation"));
        return multiEntryTicket;
    }
}
