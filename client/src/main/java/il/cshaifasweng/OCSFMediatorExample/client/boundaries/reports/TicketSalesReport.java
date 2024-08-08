package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTicket;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketSalesReport implements Report {
    private List<Purchase> purchases;

    public TicketSalesReport(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public Node generateReport() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Ticket Sales Report");
        xAxis.setLabel("Day of Month");
        yAxis.setLabel("Sales");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales");

        Map<String, Integer> salesData = new HashMap<>();
        for (Purchase purchase : purchases) {
            if (purchase instanceof MovieTicket) {
                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
                salesData.put(day, salesData.getOrDefault(day, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
        return barChart;
    }
}
