package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.MultiEntryTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.YearMonth;


public class PurchaseHandler extends MessageHandler
{
    private PurchaseMessage message;

    public PurchaseHandler(PurchaseMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }
    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case ADD_PURCHASE -> add_purchase();
            case GET_PURCHASE -> get_purchase();
            case REMOVE_PURCHASE -> remove_purchase();
            case GET_PURCHASES_BY_CUSTOMER_ID -> get_purchases_by_customer_id();
            case GET_PURCHASES_BY_THEATER_ID -> get_purchases_by_theater_id();
            case GET_ALL_MOVIE_PACKAGES_AND_MULTI_TICKETS_PURCHASES_THIS_MONTH -> get_all_movie_packages_and_multi_entry_tickets_purchases_this_month();
            case GET_ALL_PURCHASES -> get_all_purchases();

        }
    }

    private void add_purchase() {
        if (message.purchases.getFirst() != null) {
            try {
                session.save(message.purchases.getFirst());
                session.flush();
                message.responseType = PurchaseMessage.ResponseType.PURCHASE_ADDED;
            } catch (Exception e) {
                e.printStackTrace();
                message.responseType = PurchaseMessage.ResponseType.PURCHASE_FAILED;
            }
        } else {
            message.responseType = PurchaseMessage.ResponseType.PURCHASE_FAILED;
        }
    }

    private void get_purchase() {
        try {
            Purchase purchase = session.get(Purchase.class, message.purchases.getFirst().getId());
            if (purchase != null) {
                message.purchases.clear();
                message.purchases.add(purchase);
                message.responseType = PurchaseMessage.ResponseType.PURCHASE_FOUND;
            } else {
                message.responseType = PurchaseMessage.ResponseType.PURCHASE_NOT_FOUND;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = PurchaseMessage.ResponseType.PURCHASE_FAILED;
        }
    }

    private void remove_purchase() {
        try {
            Purchase purchase = session.get(Purchase.class, message.purchases.getFirst().getId());
            if (purchase != null) {
                session.delete(purchase);
                session.flush();
                message.responseType = PurchaseMessage.ResponseType.PURCHASE_REMOVED;
            } else {
                message.responseType = PurchaseMessage.ResponseType.PURCHASE_NOT_FOUND;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = PurchaseMessage.ResponseType.PURCHASE_FAILED;
        }
    }

    private void get_purchases_by_customer_id() {
        try {
            Query<Purchase> query = session.createQuery("from Purchase where owner.id = :id", Purchase.class);
            query.setParameter("id", message.key);
            List<Purchase> purchases = query.list();
            message.purchases = (ArrayList<Purchase>) purchases;
            message.responseType = PurchaseMessage.ResponseType.PURCHASES_LIST;
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = PurchaseMessage.ResponseType.PURCHASE_FAILED;
        }
    }

    private void get_purchases_by_theater_id() {
        try {
            // Query for purchases of type MovieTicket
            Query<MovieTicket> movieTicketQuery = session.createQuery(
                    "from MovieTicket  where movieInstance.hall.theater.id = :theaterId", MovieTicket.class);
            movieTicketQuery.setParameter("theaterId", message.key);
            List<MovieTicket> movieTicketPurchases = movieTicketQuery.list();
            message.purchases = (ArrayList<Purchase>) movieTicketQuery;
            message.responseType = PurchaseMessage.ResponseType.PURCHASES_LIST;
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = PurchaseMessage.ResponseType.PURCHASE_FAILED;
        }
    }



    private void get_all_movie_packages_and_multi_entry_tickets_purchases_this_month() {
        try {
            YearMonth currentMonth = YearMonth.now();
            LocalDate startOfMonth = currentMonth.atDay(1);
            LocalDate endOfMonth = currentMonth.atEndOfMonth();

            // Query for package purchases
            Query<HomeViewingPackageInstance> packageQuery = session.createQuery(
                    "from HomeViewingPackageInstance where purchaseDate between :start and :end",
                    HomeViewingPackageInstance.class);
            packageQuery.setParameter("start", startOfMonth);
            packageQuery.setParameter("end", endOfMonth);
            List<HomeViewingPackageInstance> packagePurchases = packageQuery.list();

            // Query for multiEntry purchases
            Query<MultiEntryTicket> multiEntryQuery = session.createQuery(
                    "from MultiEntryTicket where purchaseDate between :start and :end",
                    MultiEntryTicket.class);
            multiEntryQuery.setParameter("start", startOfMonth);
            multiEntryQuery.setParameter("end", endOfMonth);
            List<MultiEntryTicket> multiEntryPurchases = multiEntryQuery.list();

            // Combine both lists
            List<Purchase> allPurchases = new ArrayList<>();
            allPurchases.addAll(packagePurchases);
            allPurchases.addAll(multiEntryPurchases);

            // Set the combined list to the message
            message.purchases = (ArrayList<Purchase>) allPurchases;
            message.responseType = PurchaseMessage.ResponseType.PURCHASES_LIST;
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = PurchaseMessage.ResponseType.PURCHASE_FAILED;
        }
    }

    private void get_all_purchases() {
        try {
            Query<Purchase> query = session.createQuery("from Purchase", Purchase.class);
            List<Purchase> purchases = query.list();
            message.purchases = (ArrayList<Purchase>) purchases;
            message.responseType = PurchaseMessage.ResponseType.PURCHASES_LIST;
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = PurchaseMessage.ResponseType.PURCHASE_FAILED;
        }
    }
}
