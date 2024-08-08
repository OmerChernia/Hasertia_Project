package il.cshaifasweng.OCSFMediatorExample.client.boundaries.companyManager;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;

public class MainReportController {

    @FXML
    private BorderPane reportContainer;

    public void showGenericReport() {
        ReportConfiguration config = new ReportConfiguration(
                "Generic Sales Report",
                "Categories",
                "Sales",
                Arrays.asList("Category 1", "Category 2", "Category 3"),
                Arrays.asList(100, 200, 300)
        );

        reportContainer.setCenter(ReportFactory.createReport("Generic", config).generateReport());
    }

    // פונקציות אחרות להציג דו"חות מותאמים אישית נוספים
}
