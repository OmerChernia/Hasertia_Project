package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.ReportConfiguration;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.util.List;

public class TicketSalesReportConfiguration extends ReportConfiguration {
    private List<Purchase> purchases;

    public TicketSalesReportConfiguration(List<Purchase> purchases) {
        super("Ticket Sales Report", "Days", "Sales", null, null);
        this.purchases = purchases;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }
}
