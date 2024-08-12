package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewReport implements Report {
    private List<Purchase> purchases;

    public HomeViewReport(List<Purchase> purchases) {
        this.purchases = purchases != null ? purchases : new ArrayList<>();
    }

    @Override
    public Node generateReport() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Home Viewing Report");
        xAxis.setLabel("Day of Month");
        yAxis.setLabel("Package Usages");

        // Map to hold data by day
        Map<String, Integer> usageData = new HashMap<>();

        for (Purchase purchase : purchases) {
            if (purchase instanceof HomeViewingPackageInstance) {
                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
                usageData.put(day, usageData.getOrDefault(day, 0) + 1);
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Home Viewing Package Usages");

        // Sort the days numerically before adding them to the chart
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(usageData.entrySet());
        Collections.sort(sortedEntries, Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey())));

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
        return barChart;
    }
}