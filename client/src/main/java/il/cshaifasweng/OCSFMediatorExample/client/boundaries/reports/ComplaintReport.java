package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintReport implements Report {
    private List<Complaint> complaints;

    public ComplaintReport(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    @Override
    public Node generateReport() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Complaint Report");
        xAxis.setLabel("Day of Month");
        yAxis.setLabel("Complaints");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Complaints");

        Map<String, Integer> complaintData = new HashMap<>();
        for (Complaint complaint : complaints) {
            String day = complaint.getCreationDate().format(DateTimeFormatter.ofPattern("dd"));
            complaintData.put(day, complaintData.getOrDefault(day, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : complaintData.entrySet()) {
            System.out.println(entry);
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        // Force a layout pass
        barChart.applyCss();
        barChart.layout();

        return barChart;
    }
}
