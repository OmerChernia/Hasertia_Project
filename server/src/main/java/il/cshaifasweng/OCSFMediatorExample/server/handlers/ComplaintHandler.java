package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

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
            case GET_COMPLIANTS_BY_THEATER -> get_complaint_by_theater();
            case GET_COMPLIANTS_BY_CUSTOMER_ID -> get_complaint_by_customer_id();
        }
    }

    @Override
    public void setMessageTypeToResponse()
    {

        message.messageType= Message.MessageType.RESPONSE;
    }

    private void add_complaint()
    {
        if(message.compliants.getFirst() != null)
        {
            session.save(message.compliants.getFirst());
            session.flush();
            message.responseType = ComplaintMessage.ResponseType.COMPLIANT_ADDED;
        }
        else
            message.responseType = ComplaintMessage.ResponseType.COMPLIANT_MESSAGE_FAILED;
    }
    private void answer_compliant()
    {

        // Create an HQL query to fetch all complaints
        Query<Complaint> query = session.createQuery("FROM Complaint WHERE id = :id_compliant", Complaint.class);
        query.setParameter("id_compliant", message.compliants.getFirst().getId());


        Complaint complaint = query.uniqueResult();

        if(complaint != null)
        {
            complaint.setInfo(message.compliants.getFirst().getInfo());
            session.update(complaint);
            session.flush();
            message.responseType = ComplaintMessage.ResponseType.COMPLIANT_WES_ANSWERED;
        }
        else
            message.responseType = ComplaintMessage.ResponseType.COMPLIANT_MESSAGE_FAILED;
    }
    private void get_all_complaints() {

        try {
            // Create an HQL query to fetch all complaints
            Query<Complaint> query = session.createQuery("FROM Complaint", Complaint.class);

            // Execute the query and get the result list
            message.compliants = query.getResultList();
            // Set the response type
            message.responseType = ComplaintMessage.ResponseType.FILLTERD_COMPLIANTS_LIST;

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = ComplaintMessage.ResponseType.COMPLIANT_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    private void get_complaint_by_theater()
    {
        //need field of theater/theater_name in compliant
    }
    private void get_complaint_by_customer_id()
    {
        // Create a query to find the user by ID
        Query<RegisteredUser> query_user = session.createQuery("FROM RegisteredUser WHERE id_number = :id", RegisteredUser.class);
        query_user.setParameter("id", message.customerId);

        RegisteredUser registeredUser = query_user.getSingleResult();

        if(registeredUser != null) {

            // Create an HQL query to fetch complaints by customer ID
            Query<Complaint> query_compliants = session.createQuery("FROM Complaint WHERE registeredUser.id = :customerId", Complaint.class);
            query_compliants.setParameter("customerId", registeredUser.getId());

            // Execute the query and get the result list
            message.compliants = query_compliants.getResultList();
            message.responseType = ComplaintMessage.ResponseType.FILLTERD_COMPLIANTS_LIST;
        }
        else
            message.responseType = ComplaintMessage.ResponseType.COMPLIANT_MESSAGE_FAILED;
    }
}
