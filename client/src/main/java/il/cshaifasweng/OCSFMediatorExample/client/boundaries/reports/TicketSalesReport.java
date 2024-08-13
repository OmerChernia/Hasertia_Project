package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTicket;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        // Map to hold sales data by cinema and day
        Map<String, Map<String, Integer>> salesByCinemaAndDay = new HashMap<>();

        // Iterate through the purchases and collect sales data by cinema and day
        for (Purchase purchase : purchases) {
            if (purchase instanceof MovieTicket) {
                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
                String cinema = ((MovieTicket) purchase).getMovieInstance().getHall().getTheater().getLocation();

                // Initialize the cinema map if it doesn't exist
                salesByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());

                // Get the inner map and update the sales count
                Map<String, Integer> salesByDay = salesByCinemaAndDay.get(cinema);
                salesByDay.put(day, salesByDay.getOrDefault(day, 0) + 1);
            }
        }

        // Collect and sort all unique days across all cinemas
        List<String> sortedDays = new ArrayList<>();
        for (Map<String, Integer> salesByDay : salesByCinemaAndDay.values()) {
            for (String day : salesByDay.keySet()) {
                if (!sortedDays.contains(day)) {
                    sortedDays.add(day);
                }
            }
        }
        sortedDays.sort(Comparator.comparingInt(Integer::parseInt));

        // Debugging: Print sorted days for verification
        System.out.println("Sorted days: " + sortedDays);

        // Create a series for each cinema
        for (Map.Entry<String, Map<String, Integer>> cinemaEntry : salesByCinemaAndDay.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(cinemaEntry.getKey());

            // Add sorted data to the series
            for (String day : sortedDays) {
                int sales = cinemaEntry.getValue().getOrDefault(day, 0);
                series.getData().add(new XYChart.Data<>(day, sales));
            }

            // Add the series to the bar chart
            barChart.getData().add(series);
        }

        // Set the sorted days as categories on the x-axis
        xAxis.setCategories(FXCollections.observableArrayList(sortedDays));

        return barChart;
    }
}