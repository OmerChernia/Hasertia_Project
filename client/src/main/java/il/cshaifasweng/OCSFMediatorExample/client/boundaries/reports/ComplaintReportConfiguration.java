package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.ReportConfiguration;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;

import java.util.List;

public class ComplaintReportConfiguration extends ReportConfiguration {
    private List<Complaint> complaints;

    public ComplaintReportConfiguration(List<Complaint> complaints) {
        super("Complaint Status Report", "Days", "Number of Complaints", null, null);
        this.complaints = complaints;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }
}
