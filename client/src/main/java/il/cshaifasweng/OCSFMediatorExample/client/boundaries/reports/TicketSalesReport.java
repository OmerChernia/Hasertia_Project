package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTicket;
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

public class TicketSalesReport implements Report {
    private List<Purchase> purchases;

    public TicketSalesReport(List<Purchase> purchases) {
        this.purchases = purchases != null ? purchases : new ArrayList<>();
    }

    @Override
    public Node generateReport() {
        // Initialize axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // Set chart titles and labels
        barChart.setTitle("Ticket Sales Report");
        xAxis.setLabel("Day of Month");
        yAxis.setLabel("Sales");

        // Ensure the y-axis shows only whole numbers
        yAxis.setTickUnit(1);
        yAxis.setMinorTickCount(0);
        yAxis.setForceZeroInRange(true);

        // Map to hold sales data by cinema and day
        Map<String, Map<String, Integer>> salesData = new HashMap<>();

        for (Purchase purchase : purchases) {
            if (purchase instanceof MovieTicket) {
                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
                String theaterName = ((MovieTicket) purchase).getMovieInstance().getHall().getTheater().getLocation();

                // Initialize the inner map if it doesn't exist
                salesData.putIfAbsent(theaterName, new HashMap<>());

                // Get the inner map and update the sales count
                Map<String, Integer> cinemaSales = salesData.get(theaterName);
                cinemaSales.put(day, cinemaSales.getOrDefault(day, 0) + 1);
            }
        }

        // Create a series for each cinema and order the days correctly
        for (Map.Entry<String, Map<String, Integer>> cinemaEntry : salesData.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(cinemaEntry.getKey());

            // Sort days in numerical order before adding to series
            List<Map.Entry<String, Integer>> sortedDays = new ArrayList<>(cinemaEntry.getValue().entrySet());
            sortedDays.sort(Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey())));

            for (Map.Entry<String, Integer> dayEntry : sortedDays) {
                series.getData().add(new XYChart.Data<>(dayEntry.getKey(), dayEntry.getValue()));
            }

            barChart.getData().add(series);
        }

        return barChart;
    }
}