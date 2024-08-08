package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.ReportConfiguration;
import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.ReportFactory;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.ReportsPageController;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsBoundary implements Initializable {

    @FXML
    private AnchorPane rootStatistics;

    @FXML
    private VBox vbox;

    @FXML
    private TabPane tabPane;

    @FXML
    private BarChart<String, Number> ticketSalesBarChart;

    @FXML
    private PieChart ticketSalesPieChart;

    @FXML
    private BarChart<String, Number> packageSalesBarChart;

    @FXML
    private PieChart packageSalesPieChart;

    @FXML
    private BarChart<String, Number> multiEntryTicketSalesBarChart;

    @FXML
    private PieChart multiEntryTicketSalesPieChart;

    @FXML
    private BarChart<String, Number> complaintStatusBarChart;

    @FXML
    private PieChart complaintStatusPieChart;

    @FXML
    private BarChart<String, Number> complaintStatusHistogram;

    @FXML
    private ToggleButton toggleTicketSalesChartType;

    @FXML
    private ToggleButton togglePackageSalesChartType;

    @FXML
    private ToggleButton toggleMultiEntryTicketSalesChartType;

    @FXML
    private ToggleButton toggleComplaintStatusChartType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        ReportsPageController.requestAllPurchases();
        ReportsPageController.requestAllComplaints();
    }

    @Subscribe
    public void onPurchaseMessageReceived(PurchaseMessage message) {
        Platform.runLater(() -> {
            if (message.responseType == PurchaseMessage.ResponseType.PURCHASES_LIST) {
                createSalesReports(message.purchases);
            }
        });
    }

    @Subscribe
    public void onComplaintMessageReceived(ComplaintMessage message) {
        Platform.runLater(() -> {
            if (message.responseType == ComplaintMessage.ResponseType.FILLTERD_COMPLIANTS_LIST) {
                createComplaintReports(message.compliants);
            }
        });
    }

    private void createSalesReports(List<Purchase> purchases) {
        // Creating and setting Ticket Sales Report
        Map<String, Map<String, Integer>> ticketSalesByCinemaAndDay = organizeSalesData(purchases, MovieTicket.class);
        ReportConfiguration ticketSalesConfig = createBarChartReportConfig(
                "Ticket Sales",
                "Days in Month",
                "Sales",
                ticketSalesByCinemaAndDay
        );
        ticketSalesBarChart.setData(((BarChart<String, Number>) ReportFactory.createReport("BarChart", ticketSalesConfig).generateReport()).getData());

        // Creating and setting Package Sales Report
        Map<String, Map<String, Integer>> packageSalesByCinemaAndDay = organizeSalesData(purchases, HomeViewingPackageInstance.class);
        ReportConfiguration packageSalesConfig = createBarChartReportConfig(
                "Package Sales",
                "Days in Month",
                "Sales",
                packageSalesByCinemaAndDay
        );
        packageSalesBarChart.setData(((BarChart<String, Number>) ReportFactory.createReport("BarChart", packageSalesConfig).generateReport()).getData());

        // Creating and setting Multi-Entry Ticket Sales Report
        Map<String, Map<String, Integer>> multiEntryTicketSalesByCinemaAndDay = organizeSalesData(purchases, MultiEntryTicket.class);
        ReportConfiguration multiEntryTicketSalesConfig = createBarChartReportConfig(
                "Multi-Entry Ticket Sales",
                "Days in Month",
                "Sales",
                multiEntryTicketSalesByCinemaAndDay
        );
        multiEntryTicketSalesBarChart.setData(((BarChart<String, Number>) ReportFactory.createReport("BarChart", multiEntryTicketSalesConfig).generateReport()).getData());

        // Optionally, create pie charts based on the same data
        setPieChartDataFromNestedMap(ticketSalesPieChart, ticketSalesByCinemaAndDay);
        setPieChartDataFromNestedMap(packageSalesPieChart, packageSalesByCinemaAndDay);
        setPieChartDataFromNestedMap(multiEntryTicketSalesPieChart, multiEntryTicketSalesByCinemaAndDay);
    }

    private Map<String, Map<String, Integer>> organizeSalesData(List<Purchase> purchases, Class<? extends Purchase> purchaseType) {
        Map<String, Map<String, Integer>> salesByCinemaAndDay = new HashMap<>();

        for (Purchase purchase : purchases) {
            if (purchaseType.isInstance(purchase)) {
                String cinema = purchaseType.cast(purchase).getOwner().getName();
                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
                salesByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());
                Map<String, Integer> salesByDay = salesByCinemaAndDay.get(cinema);
                salesByDay.put(day, salesByDay.getOrDefault(day, 0) + 1);
            }
        }

        return salesByCinemaAndDay;
    }

    private ReportConfiguration createBarChartReportConfig(String title, String xAxisLabel, String yAxisLabel, Map<String, Map<String, Integer>> salesData) {
        return new ReportConfiguration(
                title,
                xAxisLabel,
                yAxisLabel,
                new ArrayList<>(salesData.keySet()),
                salesData.values().stream().map(daySales -> daySales.values().stream().reduce(0, Integer::sum)).collect(Collectors.toList())
        );
    }

    private void createComplaintReports(List<Complaint> complaints) {
        Map<String, Integer> complaintsByDay = complaints.stream().collect(
                Collectors.groupingBy(complaint -> complaint.getCreationDate().format(DateTimeFormatter.ofPattern("dd")), Collectors.summingInt(c -> 1))
        );

        // Creating and setting Complaint Status Report
        ReportConfiguration complaintStatusConfig = new ReportConfiguration(
                "Complaint Status",
                "Days in Month",
                "Complaints",
                new ArrayList<>(complaintsByDay.keySet()),
                new ArrayList<>(complaintsByDay.values())
        );
        complaintStatusBarChart.setData(((BarChart<String, Number>) ReportFactory.createReport("BarChart", complaintStatusConfig).generateReport()).getData());

        // Optionally, create histograms or pie charts for complaints
        setPieChartData(complaintStatusPieChart, complaintsByDay);
    }

    private void setPieChartDataFromNestedMap(PieChart pieChart, Map<String, Map<String, Integer>> salesData) {
        pieChart.getData().clear();

        for (Map.Entry<String, Map<String, Integer>> entry : salesData.entrySet()) {
            String category = entry.getKey();
            int totalSales = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();

            if (totalSales > 0) {
                PieChart.Data data = new PieChart.Data(category, totalSales);
                pieChart.getData().add(data);
            }
        }
    }

    private void setPieChartData(PieChart pieChart, Map<String, Integer> data) {
        pieChart.getData().clear();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            PieChart.Data pieData = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(pieData);
        }
    }

    // Existing methods for handling chart type toggling and downloading PDF remain the same
}
