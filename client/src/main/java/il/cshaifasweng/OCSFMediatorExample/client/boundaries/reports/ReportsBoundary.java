package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.ComplaintReportConfiguration;
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
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javassist.expr.Instanceof;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
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

    @FXML
    private ComboBox<Integer> TicketSalesyearComboBox;

    @FXML
    private ComboBox<String> TicketSalesmonthComboBox;

    @FXML
    private ComboBox<Integer> PackageSalesyearComboBox;

    @FXML
    private ComboBox<String> PackageSalesmonthComboBox;

    @FXML
    private ComboBox<Integer> MultiSalesyearComboBox;

    @FXML
    private ComboBox<String> MultiSalesmonthComboBox;

    @FXML
    private ComboBox<Integer> ComplaintsyearComboBox;

    @FXML
    private ComboBox<String> ComplaintsmonthComboBox;

    private List<Purchase> purchases;
    private List<Complaint> complaints;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBoxes for each tab
        initializeYearComboBox(TicketSalesyearComboBox);
        initializeMonthComboBox(TicketSalesmonthComboBox);

        initializeYearComboBox(PackageSalesyearComboBox);
        initializeMonthComboBox(PackageSalesmonthComboBox);

        initializeYearComboBox(MultiSalesyearComboBox);
        initializeMonthComboBox(MultiSalesmonthComboBox);

        initializeYearComboBox(ComplaintsyearComboBox);
        initializeMonthComboBox(ComplaintsmonthComboBox);

        // Add listeners to update the charts when selection changes
        addComboBoxListeners();

        EventBus.getDefault().register(this);
        ReportsPageController.requestAllPurchases();
        ReportsPageController.requestAllComplaints();
    }

    private void initializeYearComboBox(ComboBox<Integer> yearComboBox) {
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 10; i--) {
            yearComboBox.getItems().add(i);
        }
        yearComboBox.setValue(currentYear); // Set default value to current year
    }

    private void initializeMonthComboBox(ComboBox<String> monthComboBox) {
        for (Month month : Month.values()) {
            monthComboBox.getItems().add(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
        monthComboBox.setValue(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)); // Set default value to current month
    }

    private void addComboBoxListeners() {
        TicketSalesyearComboBox.setOnAction(e -> updateFilteredData());
        TicketSalesmonthComboBox.setOnAction(e -> updateFilteredData());

        PackageSalesyearComboBox.setOnAction(e -> updateFilteredData());
        PackageSalesmonthComboBox.setOnAction(e -> updateFilteredData());

        MultiSalesyearComboBox.setOnAction(e -> updateFilteredData());
        MultiSalesmonthComboBox.setOnAction(e -> updateFilteredData());

        ComplaintsyearComboBox.setOnAction(e -> updateFilteredData());
        ComplaintsmonthComboBox.setOnAction(e -> updateFilteredData());
    }

    private void updateFilteredData() {
        if (purchases == null || purchases.isEmpty()) {
            clearCharts();
            return;
        }

        // Ticket Sales
        int ticketSalesYear = TicketSalesyearComboBox.getValue();
        int ticketSalesMonth = TicketSalesmonthComboBox.getSelectionModel().getSelectedIndex() + 1;

        List<Purchase> filteredTicketSales = purchases.stream()
                .filter(purchase -> purchase.getPurchaseDate().getYear() == ticketSalesYear &&
                        purchase.getPurchaseDate().getMonthValue() == ticketSalesMonth)
                .collect(Collectors.toList());

        // Package Sales
        int packageSalesYear = PackageSalesyearComboBox.getValue();
        int packageSalesMonth = PackageSalesmonthComboBox.getSelectionModel().getSelectedIndex() + 1;

        List<Purchase> filteredPackageSales = purchases.stream()
                .filter(purchase -> purchase instanceof HomeViewingPackageInstance &&
                        purchase.getPurchaseDate().getYear() == packageSalesYear &&
                        purchase.getPurchaseDate().getMonthValue() == packageSalesMonth)
                .collect(Collectors.toList());

        // Multi-Entry Ticket Sales
        int multiEntrySalesYear = MultiSalesyearComboBox.getValue();
        int multiEntrySalesMonth = MultiSalesmonthComboBox.getSelectionModel().getSelectedIndex() + 1;

        List<Purchase> filteredMultiEntrySales = purchases.stream()
                .filter(purchase -> purchase instanceof MultiEntryTicket &&
                        purchase.getPurchaseDate().getYear() == multiEntrySalesYear &&
                        purchase.getPurchaseDate().getMonthValue() == multiEntrySalesMonth)
                .collect(Collectors.toList());

        // Complaints
        if (complaints != null) {
            int complaintsYear = ComplaintsyearComboBox.getValue();
            int complaintsMonth = ComplaintsmonthComboBox.getSelectionModel().getSelectedIndex() + 1;

            List<Complaint> filteredComplaints = complaints.stream()
                    .filter(complaint -> complaint.getCreationDate().getYear() == complaintsYear &&
                            complaint.getCreationDate().getMonthValue() == complaintsMonth)
                    .collect(Collectors.toList());

            createComplaintReports(filteredComplaints);
        }

        // Update the reports with the filtered data
        createSalesReports(filteredTicketSales, filteredPackageSales, filteredMultiEntrySales);
    }


    private void clearCharts() {
        ticketSalesBarChart.getData().clear();
        packageSalesBarChart.getData().clear();
        multiEntryTicketSalesBarChart.getData().clear();
        complaintStatusBarChart.getData().clear();
    }

    @Subscribe
    public void onPurchaseMessageReceived(PurchaseMessage message) {
        Platform.runLater(() -> {
            if (message.responseType == PurchaseMessage.ResponseType.PURCHASES_LIST) {
                this.purchases = message.purchases; // Update the purchases list with the data received
                // Automatically filter the data based on the currently selected year and month
                updateFilteredData();
            }
        });
    }

    @Subscribe
    public void onComplaintMessageReceived(ComplaintMessage message) {
        Platform.runLater(() -> {
            if (message.responseType == ComplaintMessage.ResponseType.FILLTERD_COMPLIANTS_LIST) {
                this.complaints = message.compliants; // Store the complaints data
                updateFilteredData(); // Update the reports based on the currently selected filters
            }
        });
    }

    private void createSalesReports(List<Purchase> filteredTicketSales, List<Purchase> filteredPackageSales, List<Purchase> filteredMultiEntrySales) {
        if (filteredTicketSales.isEmpty()) {
            ticketSalesBarChart.getData().clear();
        } else {
            TicketSalesReportConfiguration ticketSalesConfig = new TicketSalesReportConfiguration(filteredTicketSales);
            ticketSalesBarChart.setData(((BarChart<String, Number>) ReportFactory.createReport("TicketSales", ticketSalesConfig).generateReport()).getData());
        }

        if (filteredPackageSales.isEmpty()) {
            packageSalesBarChart.getData().clear();
        } else {
            TicketSalesReportConfiguration packageSalesConfig = new TicketSalesReportConfiguration(filteredPackageSales);
            packageSalesBarChart.setData(((BarChart<String, Number>) ReportFactory.createReport("HomeViewSales", packageSalesConfig).generateReport()).getData());
        }

        if (filteredMultiEntrySales.isEmpty()) {
            multiEntryTicketSalesBarChart.getData().clear();
        } else {
            TicketSalesReportConfiguration multiEntryTicketSalesConfig = new TicketSalesReportConfiguration(filteredMultiEntrySales);
            multiEntryTicketSalesBarChart.setData(((BarChart<String, Number>) ReportFactory.createReport("MultiEntrySales", multiEntryTicketSalesConfig).generateReport()).getData());
        }
    }

//    private Map<String, Map<String, Integer>> organizeSalesData(List<Purchase> purchases, Class<? extends Purchase> purchaseType) {
//        Map<String, Map<String, Integer>> salesByCinemaAndDay = new HashMap<>();
//
////        for (Purchase purchase : purchases) {
////            if (purchase instanceof MovieTicket) {
////                MovieTicket temp_movie_ticket = (MovieTicket) purchase;
////                String cinema = temp_movie_ticket.getMovieInstance().getHall().getTheater().getLocation();
////                String day = temp_movie_ticket.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
////                salesByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());
////                Map<String, Integer> salesByDay = salesByCinemaAndDay.get(cinema);
////                salesByDay.put(day, salesByDay.getOrDefault(day, 0) + 1);
////            }
////            else if (purchase instanceof MultiEntryTicket) {
////                String cinema = purchaseType.cast(purchase).getOwner().getName();
////                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
////                salesByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());
////                Map<String, Integer> salesByDay = salesByCinemaAndDay.get(cinema);
////                salesByDay.put(day, salesByDay.getOrDefault(day, 0) + 1);
////            }
////            else if (purchase instanceof HomeViewingPackageInstance) {
////                String cinema = purchaseType.cast(purchase).getOwner().getName();
////                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
////                salesByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());
////                Map<String, Integer> salesByDay = salesByCinemaAndDay.get(cinema);
////                salesByDay.put(day, salesByDay.getOrDefault(day, 0) + 1);
////            }
////        }
//
//        return salesByCinemaAndDay;
//    }

    private ReportConfiguration createBarChartReportConfig(String title, String xAxisLabel, String yAxisLabel, Map<String, Map<String, Integer>> salesData) {
        return new ReportConfiguration(
                title,
                xAxisLabel,
                yAxisLabel,
                new ArrayList<>(salesData.keySet()),
                salesData.values().stream().map(daySales -> daySales.values().stream().reduce(0, Integer::sum)).collect(Collectors.toList())
        );
    }

    private void createComplaintReports(List<Complaint> filteredComplaints) {
        if (filteredComplaints.isEmpty()) {
            System.out.println("No Complaints to display for the selected period.");
            complaintStatusBarChart.getData().clear();
            return;
        }

        // Map to hold complaints data by cinema or category
        Map<String, Integer> complaintsByCinema = new HashMap<>();

        for (Complaint complaint : filteredComplaints) {
            String category;

            if (complaint.getPurchase() != null) {
                Purchase purchase = complaint.getPurchase();
                if (purchase instanceof MovieTicket) {
                    MovieTicket movieTicket = (MovieTicket) purchase;
                    category = movieTicket.getMovieInstance().getHall().getTheater().getLocation();
                } else if (purchase instanceof HomeViewingPackageInstance) {
                    category = "Home Viewing";
                } else if (purchase instanceof MultiEntryTicket) {
                    category = "Multi-Entry";
                } else {
                    category = "Other Related Complaints";
                }
            } else {
                category = "No Cinema"; // Complaints not related to any cinema
            }

            complaintsByCinema.put(category, complaintsByCinema.getOrDefault(category, 0) + 1);
        }

        // Create the bar chart series for complaints
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Complaints");

        for (Map.Entry<String, Integer> entry : complaintsByCinema.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        complaintStatusBarChart.getData().clear();  // Clear existing data
        complaintStatusBarChart.getData().add(series);  // Add new data
    }

    private Purchase getPurchaseById(int purchaseId) {
        // Implement this method to retrieve the Purchase object from your data source
        // using the purchase_id from the Complaint object.
        // This could involve querying a database or searching through the list of purchases.
        return purchases.stream()
                .filter(purchase -> purchase.getId() == purchaseId)
                .findFirst()
                .orElse(null);
    }

//    private void setPieChartDataFromNestedMap(PieChart pieChart, Map<String, Map<String, Integer>> salesData) {
//        pieChart.getData().clear();
//
//        for (Map.Entry<String, Map<String, Integer>> entry : salesData.entrySet()) {
//            String category = entry.getKey();
//            int totalSales = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
//
//            if (totalSales > 0) {
//                PieChart.Data data = new PieChart.Data(category, totalSales);
//                pieChart.getData().add(data);
//            }
//        }
//    }
//
//    private void setPieChartData(PieChart pieChart, Map<String, Integer> data) {
//        pieChart.getData().clear();
//
//        for (Map.Entry<String, Integer> entry : data.entrySet()) {
//            PieChart.Data pieData = new PieChart.Data(entry.getKey(), entry.getValue());
//            pieChart.getData().add(pieData);
//        }
//    }

    // Existing methods for handling chart type toggling and downloading PDF remain the same
}
