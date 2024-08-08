package il.cshaifasweng.OCSFMediatorExample.client.boundaries.companyManager;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

public class GenericReport implements Report {

    private ReportConfiguration configuration;

    public GenericReport(ReportConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Node generateReport() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle(configuration.getReportTitle());
        xAxis.setLabel(configuration.getXAxisLabel());
        yAxis.setLabel(configuration.getYAxisLabel());

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName(configuration.getReportTitle());

        for (int i = 0; i < configuration.getDataCategories().size(); i++) {
            dataSeries.getData().add(new XYChart.Data<>(configuration.getDataCategories().get(i), configuration.getDataValues().get(i)));
        }

        barChart.getData().add(dataSeries);
        return barChart;
    }
}
