package il.cshaifasweng.OCSFMediatorExample.client.boundaries.companyManager;

import il.cshaifasweng.OCSFMediatorExample.client.util.generators.PDFGenerator;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportsBoundary implements Initializable {

    @FXML
    private AnchorPane rootStatistics;

    @FXML
    private Text title;

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

    private dbDeleteLaterGenerate dbGenerate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbGenerate = new dbDeleteLaterGenerate();
        setChartData();
    }

    private void setChartData() {
        setTicketSalesData();
        setPackageSalesData();
        setMultiEntryTicketSalesData();
        setComplaintStatusData();
        setComplaintHistogramData();
    }

    private void setTicketSalesData() {
        Map<String, Map<String, Integer>> ticketSalesByCinemaAndDay = new HashMap<>();
        List<Theater> theaters = dbGenerate.getTheaters();
        for (Theater theater : theaters) {
            ticketSalesByCinemaAndDay.putIfAbsent(theater.getLocation(), new HashMap<>());
        }

        for (Purchase purchase : dbGenerate.getPurchases()) {
            if (purchase instanceof MovieTicket) {
                String cinema = ((MovieTicket) purchase).getMovieInstance().getHall().getTheater().getLocation();
                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
                ticketSalesByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());
                Map<String, Integer> ticketSalesByDay = ticketSalesByCinemaAndDay.get(cinema);
                ticketSalesByDay.put(day, ticketSalesByDay.getOrDefault(day, 0) + 1);
            }
        }

        XYChart.Series<String, Number> networkSeries = new XYChart.Series<>();
        networkSeries.setName("Network");

        for (Map.Entry<String, Map<String, Integer>> cinemaEntry : ticketSalesByCinemaAndDay.entrySet()) {
            String cinema = cinemaEntry.getKey();
            XYChart.Series<String, Number> cinemaSeries = new XYChart.Series<>();
            cinemaSeries.setName(cinema);

            for (int day = 1; day <= 31; day++) {
                String dayStr = String.format("%02d", day);
                int salesCount = cinemaEntry.getValue().getOrDefault(dayStr, 0);
                cinemaSeries.getData().add(new XYChart.Data<>(dayStr, salesCount));
                networkSeries.getData().add(new XYChart.Data<>(dayStr, salesCount));
            }

            ticketSalesBarChart.getData().add(cinemaSeries);
        }

        ticketSalesBarChart.getData().add(networkSeries);

        // Set Pie Chart Data
        setPieChartData(ticketSalesPieChart, ticketSalesByCinemaAndDay);
    }

    private void setPackageSalesData() {
        Map<String, Map<String, Integer>> packageSalesByCinemaAndDay = new HashMap<>();
        List<Theater> theaters = dbGenerate.getTheaters();
        for (Theater theater : theaters) {
            packageSalesByCinemaAndDay.putIfAbsent(theater.getLocation(), new HashMap<>());
        }

        for (Purchase purchase : dbGenerate.getPurchases()) {
            if (purchase instanceof HomeViewingPackageInstance) {
                String cinema = ((HomeViewingPackageInstance) purchase).getOwner().getName();
                if (cinema == null) {
                    cinema = "Unknown Cinema";
                }
                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
                packageSalesByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());
                Map<String, Integer> packageSalesByDay = packageSalesByCinemaAndDay.get(cinema);
                packageSalesByDay.put(day, packageSalesByDay.getOrDefault(day, 0) + 1);
            }
        }

        XYChart.Series<String, Number> networkSeries = new XYChart.Series<>();
        networkSeries.setName("Network");

        for (Map.Entry<String, Map<String, Integer>> cinemaEntry : packageSalesByCinemaAndDay.entrySet()) {
            String cinema = cinemaEntry.getKey();
            XYChart.Series<String, Number> cinemaSeries = new XYChart.Series<>();
            cinemaSeries.setName(cinema);

            for (int day = 1; day <= 31; day++) {
                String dayStr = String.format("%02d", day);
                int salesCount = cinemaEntry.getValue().getOrDefault(dayStr, 0);
                cinemaSeries.getData().add(new XYChart.Data<>(dayStr, salesCount));
                networkSeries.getData().add(new XYChart.Data<>(dayStr, salesCount));
            }

            packageSalesBarChart.getData().add(cinemaSeries);
        }

        packageSalesBarChart.getData().add(networkSeries);

        // Set Pie Chart Data
        setPieChartData(packageSalesPieChart, packageSalesByCinemaAndDay);
    }

    private void setMultiEntryTicketSalesData() {
        Map<String, Map<String, Integer>> multiEntryTicketSalesByCinemaAndDay = new HashMap<>();
        List<Theater> theaters = dbGenerate.getTheaters();
        for (Theater theater : theaters) {
            multiEntryTicketSalesByCinemaAndDay.putIfAbsent(theater.getLocation(), new HashMap<>());
        }

        for (Purchase purchase : dbGenerate.getPurchases()) {
            if (purchase instanceof MultiEntryTicket) {
                String cinema = ((MultiEntryTicket) purchase).getOwner().getName();
                if (cinema == null) {
                    cinema = "Unknown Cinema";
                }
                String day = purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd"));
                multiEntryTicketSalesByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());
                Map<String, Integer> multiEntryTicketSalesByDay = multiEntryTicketSalesByCinemaAndDay.get(cinema);
                multiEntryTicketSalesByDay.put(day, multiEntryTicketSalesByDay.getOrDefault(day, 0) + 1);
            }
        }

        XYChart.Series<String, Number> networkSeries = new XYChart.Series<>();
        networkSeries.setName("Network");

        for (Map.Entry<String, Map<String, Integer>> cinemaEntry : multiEntryTicketSalesByCinemaAndDay.entrySet()) {
            String cinema = cinemaEntry.getKey();
            XYChart.Series<String, Number> cinemaSeries = new XYChart.Series<>();
            cinemaSeries.setName(cinema);

            for (int day = 1; day <= 31; day++) {
                String dayStr = String.format("%02d", day);
                int salesCount = cinemaEntry.getValue().getOrDefault(dayStr, 0);
                cinemaSeries.getData().add(new XYChart.Data<>(dayStr, salesCount));
                networkSeries.getData().add(new XYChart.Data<>(dayStr, salesCount));
            }

            multiEntryTicketSalesBarChart.getData().add(cinemaSeries);
        }

        multiEntryTicketSalesBarChart.getData().add(networkSeries);

        // Set Pie Chart Data
        setPieChartData(multiEntryTicketSalesPieChart, multiEntryTicketSalesByCinemaAndDay);
    }

    private void setComplaintStatusData() {
        Map<String, Integer> complaintsByDay = new HashMap<>();
        Map<String, Integer> complaintsByDayPie = new HashMap<>();

        for (Complaint complaint : dbGenerate.getComplaints()) {
            String day = complaint.getCreationDate().format(DateTimeFormatter.ofPattern("dd"));
            complaintsByDay.put(day, complaintsByDay.getOrDefault(day, 0) + 1);
            complaintsByDayPie.put(day, complaintsByDayPie.getOrDefault(day, 0) + 1);
        }

        XYChart.Series<String, Number> complaintsSeries = new XYChart.Series<>();
        complaintsSeries.setName("תלונות");

        for (Map.Entry<String, Integer> entry : complaintsByDay.entrySet()) {
            complaintsSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        complaintStatusBarChart.getData().add(complaintsSeries);

        for (Map.Entry<String, Integer> entry : complaintsByDayPie.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            complaintStatusPieChart.getData().add(data);
            animatePieChartEntry(data);
            animatePieChartHover(data);
        }
    }

    private void setComplaintHistogramData() {
        Map<String, Map<String, Integer>> complaintsByCinemaAndDay = new HashMap<>();
        List<Complaint> complaints = dbGenerate.getComplaints();
        List<Theater> theaters = dbGenerate.getTheaters();

        for (Theater theater : theaters) {
            complaintsByCinemaAndDay.putIfAbsent(theater.getLocation(), new HashMap<>());
        }

        for (Complaint complaint : complaints) {
            String cinema = "Tel Aviv";
            String day = complaint.getCreationDate().format(DateTimeFormatter.ofPattern("dd"));

            complaintsByCinemaAndDay.putIfAbsent(cinema, new HashMap<>());
            Map<String, Integer> complaintsByDay = complaintsByCinemaAndDay.get(cinema);
            complaintsByDay.put(day, complaintsByDay.getOrDefault(day, 0) + 1);
        }

        XYChart.Series<String, Number> networkSeries = new XYChart.Series<>();
        networkSeries.setName("Network");

        for (Map.Entry<String, Map<String, Integer>> cinemaEntry : complaintsByCinemaAndDay.entrySet()) {
            String cinema = cinemaEntry.getKey();
            XYChart.Series<String, Number> cinemaSeries = new XYChart.Series<>();
            cinemaSeries.setName(cinema);

            for (int day = 1; day <= 31; day++) {
                String dayStr = String.format("%02d", day);
                int complaintsCount = cinemaEntry.getValue().getOrDefault(dayStr, 0);
                XYChart.Data<String, Number> data = new XYChart.Data<>(dayStr, complaintsCount);
                cinemaSeries.getData().add(data);
                animateHistogramEntry(data);
                networkSeries.getData().add(data);
            }

            complaintStatusHistogram.getData().add(cinemaSeries);
        }

        complaintStatusHistogram.getData().add(networkSeries);
    }

    private void setPieChartData(PieChart pieChart, Map<String, Map<String, Integer>> salesByCinemaAndDay) {
        pieChart.getData().clear();

        for (Map.Entry<String, Map<String, Integer>> cinemaEntry : salesByCinemaAndDay.entrySet()) {
            String cinema = cinemaEntry.getKey();
            int totalSales = cinemaEntry.getValue().values().stream().mapToInt(Integer::intValue).sum();

            if (totalSales > 0) {
                PieChart.Data data = new PieChart.Data(cinema + ": " + totalSales, totalSales);
                pieChart.getData().add(data);
                animatePieChartEntry(data);
                animatePieChartHover(data);
            }
        }
    }

    private void animatePieChartEntry(PieChart.Data data) {
        ScaleTransition st = new ScaleTransition(Duration.millis(500), data.getNode());
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    private void animatePieChartHover(PieChart.Data data) {
        data.getNode().setOnMouseEntered(event -> {
            data.getNode().setScaleX(1.1);
            data.getNode().setScaleY(1.1);
        });
        data.getNode().setOnMouseExited(event -> {
            data.getNode().setScaleX(1.0);
            data.getNode().setScaleY(1.0);
        });
    }

    private void animateHistogramEntry(XYChart.Data<String, Number> data) {
        ScaleTransition st = new ScaleTransition(Duration.millis(500), data.getNode());
        st.setFromY(0);
        st.setToY(1);
        st.play();

        TranslateTransition tt = new TranslateTransition(Duration.millis(500), data.getNode());
        tt.setFromY(100);
        tt.setToY(0);
        tt.play();
    }

    @FXML
    private void handleToggleTicketSalesChartType() {
        boolean showBarChart = toggleTicketSalesChartType.isSelected();

        ticketSalesBarChart.setVisible(showBarChart);
        ticketSalesPieChart.setVisible(!showBarChart);

        if (!showBarChart) {
            ticketSalesPieChart.getData().forEach(this::animatePieChartEntry);
        }
    }

    @FXML
    private void handleTogglePackageSalesChartType() {
        boolean showBarChart = togglePackageSalesChartType.isSelected();

        packageSalesBarChart.setVisible(showBarChart);
        packageSalesPieChart.setVisible(!showBarChart);

        if (!showBarChart) {
            packageSalesPieChart.getData().forEach(this::animatePieChartEntry);
        }
    }

    @FXML
    private void handleToggleMultiEntryTicketSalesChartType() {
        boolean showBarChart = toggleMultiEntryTicketSalesChartType.isSelected();

        multiEntryTicketSalesBarChart.setVisible(showBarChart);
        multiEntryTicketSalesPieChart.setVisible(!showBarChart);

        if (!showBarChart) {
            multiEntryTicketSalesPieChart.getData().forEach(this::animatePieChartEntry);
        }
    }

    @FXML
    private void handleToggleComplaintStatusChartType() {
        boolean showBarChart = toggleComplaintStatusChartType.isSelected();

        complaintStatusBarChart.setVisible(showBarChart);
        complaintStatusPieChart.setVisible(!showBarChart);

        if (!showBarChart) {
            complaintStatusPieChart.getData().forEach(this::animatePieChartEntry);
        }
    }

    @FXML
    private void handleDownloadButtonAction() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                if (ticketSalesPieChart.isVisible()) {
                    PDFGenerator.createPDF(file.getAbsolutePath(), ticketSalesPieChart);
                } else if (packageSalesPieChart.isVisible()) {
                    PDFGenerator.createPDF(file.getAbsolutePath(), packageSalesPieChart);
                } else if (multiEntryTicketSalesPieChart.isVisible()) {
                    PDFGenerator.createPDF(file.getAbsolutePath(), multiEntryTicketSalesPieChart);
                } else if (complaintStatusPieChart.isVisible()) {
                    PDFGenerator.createPDF(file.getAbsolutePath(), complaintStatusPieChart);
                } else if (complaintStatusHistogram.isVisible()) {
                    PDFGenerator.createPDF(file.getAbsolutePath(), complaintStatusHistogram);
                } else if (ticketSalesBarChart.isVisible()) {
                    PDFGenerator.createPDF(file.getAbsolutePath(), ticketSalesBarChart);
                } else if (packageSalesBarChart.isVisible()) {
                    PDFGenerator.createPDF(file.getAbsolutePath(), packageSalesBarChart);
                } else if (multiEntryTicketSalesBarChart.isVisible()) {
                    PDFGenerator.createPDF(file.getAbsolutePath(), multiEntryTicketSalesBarChart);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
