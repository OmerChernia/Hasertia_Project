package il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.reports.generic.ReportFactory;
import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.ComplaintController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.PurchaseController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.TheaterController;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.TheaterMessage;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsBoundary implements Initializable {
    private String theaterLocation;

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
        EventBus.getDefault().register(this);

        // Retrieve the theater location for the logged-in manager
        System.out.println("Sending id" + SimpleClient.user);
        TheaterController.getTheaterNameByTheaterManagerID(SimpleClient.user);

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

        PurchaseController.GetAllPurchases();
        ComplaintController.getAllComplaints();
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

        List<Purchase> filteredTicketSales;
        if (this.theaterLocation == null) {
            // Company Manager - Show all ticket sales
            filteredTicketSales = purchases.stream()
                    .filter(purchase -> purchase.getPurchaseDate().getYear() == ticketSalesYear &&
                            purchase.getPurchaseDate().getMonthValue() == ticketSalesMonth &&
                            purchase instanceof MovieTicket)
                    .collect(Collectors.toList());
        } else {
            // Theater Manager - Show only their theater's ticket sales
            filteredTicketSales = purchases.stream()
                    .filter(purchase -> purchase.getPurchaseDate().getYear() == ticketSalesYear &&
                            purchase.getPurchaseDate().getMonthValue() == ticketSalesMonth &&
                            purchase instanceof MovieTicket &&
                            ((MovieTicket) purchase).getMovieInstance().getHall().getTheater().getLocation().equals(theaterLocation))
                    .collect(Collectors.toList());
        }

        // Package Sales
        List<Purchase> filteredPackageSales;
        if (this.theaterLocation == null) {
            // Company Manager - Show all package sales
            filteredPackageSales = purchases.stream()
                    .filter(purchase -> purchase instanceof HomeViewingPackageInstance &&
                            purchase.getPurchaseDate().getYear() == ticketSalesYear &&
                            purchase.getPurchaseDate().getMonthValue() == ticketSalesMonth)
                    .collect(Collectors.toList());
        } else {
            // Theater Manager - Do not show package sales
            filteredPackageSales = Collections.emptyList();
        }

        // Multi-Entry Ticket Sales
        List<Purchase> filteredMultiEntrySales;
        if (this.theaterLocation == null) {
            // Company Manager - Show all multi-entry sales
            filteredMultiEntrySales = purchases.stream()
                    .filter(purchase -> purchase instanceof MultiEntryTicket &&
                            purchase.getPurchaseDate().getYear() == ticketSalesYear &&
                            purchase.getPurchaseDate().getMonthValue() == ticketSalesMonth)
                    .collect(Collectors.toList());
        } else {
            // Theater Manager - Do not show multi-entry sales
            filteredMultiEntrySales = Collections.emptyList();
        }

        // Complaints
        if (complaints != null) {
            List<Complaint> filteredComplaints;
            int complaintsYear = ComplaintsyearComboBox.getValue();
            int complaintsMonth = ComplaintsmonthComboBox.getSelectionModel().getSelectedIndex() + 1;

            if (this.theaterLocation == null) {
                // Company Manager - Show all complaints
                filteredComplaints = complaints.stream()
                        .filter(complaint -> complaint.getCreationDate().getYear() == complaintsYear &&
                                complaint.getCreationDate().getMonthValue() == complaintsMonth)
                        .collect(Collectors.toList());
            } else {
                // Theater Manager - Show only complaints related to their theater
                filteredComplaints = complaints.stream()
                        .filter(complaint -> complaint.getCreationDate().getYear() == complaintsYear &&
                                complaint.getCreationDate().getMonthValue() == complaintsMonth &&
                                complaint.getPurchase() != null && (
                                (complaint.getPurchase() instanceof MovieTicket &&
                                        ((MovieTicket) complaint.getPurchase()).getMovieInstance().getHall().getTheater().getLocation().equals(theaterLocation)) ||
                                        (complaint.getPurchase() instanceof MultiEntryTicket && "Multi-Entry".equals(theaterLocation)) ||
                                        (complaint.getPurchase() instanceof HomeViewingPackageInstance && "Home Viewing".equals(theaterLocation)))
                        )
                        .collect(Collectors.toList());
            }

            createComplaintReports(filteredComplaints);
        }

        // Update the reports with the filtered data
        createSalesReports(filteredTicketSales, filteredPackageSales, filteredMultiEntrySales);

        // Apply layout updates to ensure proper rendering
        ticketSalesBarChart.applyCss();
        ticketSalesBarChart.layout();
        packageSalesBarChart.applyCss();
        packageSalesBarChart.layout();
        multiEntryTicketSalesBarChart.applyCss();
        multiEntryTicketSalesBarChart.layout();
        complaintStatusBarChart.applyCss();
        complaintStatusBarChart.layout();
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
    public void onTheaterMessageReceived(TheaterMessage message) {
        Platform.runLater(() -> {
            if (message.responseType == TheaterMessage.ResponseType.RETURN_THEATER) {
                this.theaterLocation = message.theaterList.get(0).getLocation();
                System.out.println("Theater Location set to: " + this.theaterLocation);
                updateFilteredData();
            }
        });
    }

    @Subscribe
    public void onComplaintMessageReceived(ComplaintMessage message) {
        Platform.runLater(() -> {
            if (message.responseType == ComplaintMessage.ResponseType.FILTERED_COMPLAINTS_LIST) {
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

    private void createComplaintReports(List<Complaint> filteredComplaints) {
        if (filteredComplaints.isEmpty()) {
            System.out.println("No Complaints to display for the selected period.");
            complaintStatusBarChart.getData().clear();
            return;
        }

        Map<String, Integer> complaintsByCinema = new HashMap<>();

        for (Complaint complaint : filteredComplaints) {
            String category;

            if (complaint.getPurchase() != null) {
                Purchase purchase = complaint.getPurchase();
                if (purchase instanceof MovieTicket) {
                    category = ((MovieTicket) purchase).getMovieInstance().getHall().getTheater().getLocation();
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
}