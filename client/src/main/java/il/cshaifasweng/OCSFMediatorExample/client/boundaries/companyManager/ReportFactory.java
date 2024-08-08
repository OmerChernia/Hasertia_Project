package il.cshaifasweng.OCSFMediatorExample.client.boundaries.companyManager;

public class ReportFactory {

    public static Report createReport(String reportType, ReportConfiguration configuration) {
        switch (reportType) {
            case "Generic":
                return new GenericReport(configuration);
            // ניתן להוסיף עוד דו"חות גנריים או מותאמים אישית לפי הצורך
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }
    }
}
