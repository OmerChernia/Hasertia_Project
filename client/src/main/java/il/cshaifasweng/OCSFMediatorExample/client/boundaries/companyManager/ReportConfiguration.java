package il.cshaifasweng.OCSFMediatorExample.client.boundaries.companyManager;

import java.util.List;

public class ReportConfiguration {
    private String reportTitle;
    private String xAxisLabel;
    private String yAxisLabel;
    private List<String> dataCategories;
    private List<Integer> dataValues;

    public ReportConfiguration(String reportTitle, String xAxisLabel, String yAxisLabel, List<String> dataCategories, List<Integer> dataValues) {
        this.reportTitle = reportTitle;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.dataCategories = dataCategories;
        this.dataValues = dataValues;
    }

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
}
