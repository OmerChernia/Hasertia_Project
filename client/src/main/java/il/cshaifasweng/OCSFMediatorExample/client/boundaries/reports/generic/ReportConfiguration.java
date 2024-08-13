package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.util.List;

public class ReportConfiguration {
    private String reportTitle;
    private String xAxisLabel;
    private String yAxisLabel;
    private List<String> dataCategories;
    private List<Integer> dataValues;
    private List<Purchase> purchases;
    private List<Complaint> complaints;

    public ReportConfiguration(String reportTitle, String xAxisLabel, String yAxisLabel, List<String> dataCategories, List<Integer> dataValues) {
        this.reportTitle = reportTitle;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.dataCategories = dataCategories;
        this.dataValues = dataValues;
    }

    public ReportConfiguration(String reportTitle, List<Complaint> complaints) {
        this.reportTitle = reportTitle;
        this.complaints = complaints;
    }

//    public ReportConfiguration(String reportTitle, List<Purchase> purchases) {
//        this.reportTitle = reportTitle;
//        this.purchases = purchases;
//    }

    // Getters
    public String getReportTitle() {
        return reportTitle;
    }

    public String getXAxisLabel() {
        return xAxisLabel;
    }

    public String getYAxisLabel() {
        return yAxisLabel;
    }

    public List<String> getDataCategories() {
        return dataCategories;
    }

    public List<Integer> getDataValues() {
        return dataValues;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }
}
