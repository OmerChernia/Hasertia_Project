package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

public class ComplaintHandler extends MessageHandler
{
    private ComplaintMessage message;

    public ComplaintHandler(ComplaintMessage message,ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case ADD_COMPLIANT -> add_complaint();
            case ANSWER_COMPLIANT -> answer_compliant();
            case GET_ALL_COMPLIANTS -> get_all_complaints();
            case GET_COMPLIANT_BY_THEATER -> get_complaint_by_theater();
            case GET_COMPLIANT_BY_CUSTOMER_ID -> get_complaint_by_customer_id();
        }
    }

    private void add_complaint()
    {

    }
    private void answer_compliant()
    {

    }
    private void get_all_complaints()
    {

    }
    private void get_complaint_by_theater()
    {

    }
    private void get_complaint_by_customer_id()
    {

    }
}
