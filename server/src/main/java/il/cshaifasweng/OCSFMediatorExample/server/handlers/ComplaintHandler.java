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
            case GET_COMPLIANT_BY_THEATER -> get_complaint_by_theater();
            case GET_COMPLIANT_BY_CUSTOMER_ID -> get_complaint_by_customer_id();
        }
    }

    @Override
    public void setMessageTypeToResponse()
    {

        message.messageType= Message.MessageType.RESPONSE;
    }

    private void add_complaint()
    {
        session.save(message.compliants.getFirst());
        session.flush();
        message.responseType = ComplaintMessage.ResponseType.COMPLIANT_ADDED;
    }
    private void answer_compliant()
    {
        // Create an HQL query to fetch all complaints
        Query<Complaint> query = session.createQuery("FROM Complaint WHERE id = :id_compliant", Complaint.class);
        query.setParameter("id_compliant", message.compliants.getFirst().getId());

        Complaint complaint = query.uniqueResult();
        complaint.setInfo(message.compliants.getFirst().getInfo());
        session.update(complaint);
        session.flush();
        message.responseType = ComplaintMessage.ResponseType.COMPLIANT_WES_ANSWERED;
    }
    private void get_all_complaints() {

        try {
            System.out.println("Executing get_all_complaints");
            // Create an HQL query to fetch all complaints
            System.out.println("Creating query to fetch all complaints");
            Query<Complaint> query = session.createQuery("FROM Complaint", Complaint.class);

            // Execute the query and get the result list
            System.out.println("Executing query to fetch all complaints");
            List<Complaint> complaints = query.getResultList();
            List<Complaint> res = new ArrayList<Complaint>();


            message.compliants = complaints;
            // Set the response type
            message.responseType = ComplaintMessage.ResponseType.FILLTERD_COMPLIANTS_LIST;

            System.out.println("get_all_complaints executed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = ComplaintMessage.ResponseType.COMPLIANT_ADDED_FAILED;
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

        // Create an HQL query to fetch complaints by customer ID
        Query<Complaint> query_compliants = session.createQuery("FROM Complaint WHERE registeredUser.id = :customerId", Complaint.class);
        query_compliants.setParameter("customerId", registeredUser.getId());

        // Execute the query and get the result list
        message.compliants = query_compliants.getResultList();
        message.responseType = ComplaintMessage.ResponseType.FILLTERD_COMPLIANTS_LIST;
    }
}
