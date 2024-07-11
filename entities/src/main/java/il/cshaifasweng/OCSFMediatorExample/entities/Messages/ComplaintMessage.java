package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;

import java.util.ArrayList;

public class ComplaintMessage extends Message
{
    public ArrayList<Complaint> compliants;
    public String customerId;
    public Theater theater;
    public ResponseType responseType;
    public RequestType requestType;

    public ComplaintMessage(MessageType messageType, RequestType requestType)
    {
        //GET_ALL_COMPLIANTS
        super(messageType);
        this.requestType = requestType;
    }
    public ComplaintMessage(MessageType messageType, RequestType requestType, String customerId)
    {
        //GET_COMPLIANT_BY_CUSTOMER_ID
        super(messageType);
        this.requestType = requestType;
        this.customerId = customerId;
    }
    public ComplaintMessage(MessageType messageType, RequestType requestType , Theater theater)
    {
        //GET_COMPLIANT_BY_THEATER
        super(messageType);
        this.requestType = requestType;
        this.theater = theater;
    }
    public ComplaintMessage(MessageType messageType, RequestType requestType, Complaint compliant)
    {
        // ADD_COMPLIANT, ANSWER_COMPLIANT
        super(messageType);
        this.requestType = requestType;
        this.compliants = new ArrayList<>();
        this.compliants.add(compliant);
    }

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
