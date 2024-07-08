package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Compliant;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;

import java.util.ArrayList;

public class ComplaintMessage extends Message
{
    ArrayList<Compliant> compliants;
    String customerId;
    Theater theater;
    String response;

    public enum ResponseType
    {
        FILLTERD_COMPLIANTS_LIST,
        COMPLIANT_ADDED,
        COMPLIANT_ADDED_FAILED,//???
        COMPLIANT_WES_ANSWERED,
    }
    public enum RequestType
    {
        GET_ALL_COMPLIANTS,
        GET_COMPLIANT_BY_CUSTOMER_ID,
        GET_COMPLIANT_BY_THEATER,
        ADD_COMPLIANT,
        ANSWER_COMPLIANT,
    }
}
