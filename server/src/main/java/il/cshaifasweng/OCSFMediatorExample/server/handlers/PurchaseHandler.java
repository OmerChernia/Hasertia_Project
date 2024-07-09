package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

public class PurchaseHandler extends MessageHandler
{
    PurchaseMessage message;

    public PurchaseHandler(PurchaseMessage message, ConnectionToClient client)
    {
        super(client);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case ADD_PURCHASE -> add_purchase();
            case GET_PURCHASE -> get_purchase();
            case REMOVE_PURCHASE -> remove_purchase();
            case GET_PURCHASES_BY_CUSTOMER_ID -> get_purchase_by_customer_id();
            case GET_PURCHASES_BY_THEATER_NAME -> get_purchase_by_customer_theater_name();
            case GET_ALL_MOVIE_PACKAGES_AND_MULTI_TICKETS_PURCHASES_THIS_MONTH -> get_all_movie_packages_and_multi_entry_tickets_purchases_this_month();
            case GET_ALL_PURCHASES -> get_all_purchases();

        }
    }

    private void add_purchase()
    {

    }
    private void get_purchase()
    {

    }
    private void remove_purchase()
    {

    }
    private void get_purchase_by_customer_id()
    {

    }
    private void get_purchase_by_customer_theater_name()
    {

    }
    private void get_all_movie_packages_and_multi_entry_tickets_purchases_this_month()
    {

    }
    private void get_all_purchases()
    {

    }
}
