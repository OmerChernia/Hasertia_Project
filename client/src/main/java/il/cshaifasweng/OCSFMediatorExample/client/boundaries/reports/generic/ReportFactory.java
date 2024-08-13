package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.ComplaintReport;
import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.HomeViewReport;
import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.MultiEntryReport;
import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.TicketSalesReport;

public class ReportFactory {

    public static Report createReport(String reportType, ReportConfiguration configuration) {
        switch (reportType) {
            case "Generic":
                return new GenericReport(configuration);
            case "TicketSales":
                return new TicketSalesReport(configuration.getPurchases());
            case "HomeViewSales":
                return new HomeViewReport(configuration.getPurchases());
            case "MultiEntrySales":
                return new MultiEntryReport(configuration.getPurchases());
            case "Complaint":
                return new ComplaintReport(configuration.getComplaints());
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }
    }
}
