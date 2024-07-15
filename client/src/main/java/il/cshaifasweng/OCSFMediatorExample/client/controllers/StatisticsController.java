package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import animatefx.animation.FadeIn;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class StatisticsController implements Initializable {

    @FXML
    private AnchorPane rootStatistics;

    @FXML
    private AnchorPane rootDate;

    @FXML
    private PieChart pieChart;

    @FXML
    private Text title;

    @FXML
    private HBox hbox;

    @FXML
    private HBox hboxImage;

    @FXML
    private ImageView emptyImage;

    private final DatePicker datepicker = new DatePicker();

    private final ContextMenu contextMenu = new ContextMenu();

    private final ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGraphics();
        setDatePicker();
        setContextMenu();
        setNodeStartupConfiguration();
        setAnimations();
    }

    private void setNodeStartupConfiguration() {
        hboxImage.setVisible(true);
        hbox.setVisible(false);
        pieChart.setLegendVisible(false);
    }

    private void setAnimations() {
        Animations.fadeInUp(hboxImage);
        Animations.fadeInUp(title);
    }

    private void setDatePicker() {
        rootDate.getChildren().add(datepicker);
    }

    private void setContextMenu() {
        MenuItem menuItem = new MenuItem("Select Date");
        menuItem.setOnAction(ev -> {
            contextMenu.hide();
            rootDate.setVisible(true);
        });

        contextMenu.getItems().add(menuItem);

        pieChart.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                contextMenu.show(pieChart, ev.getScreenX(), ev.getScreenY());
            }
        });

        hboxImage.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                contextMenu.show(hboxImage, ev.getScreenX(), ev.getScreenY());
            }
        });

        emptyImage.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                contextMenu.show(emptyImage, ev.getScreenX(), ev.getScreenY());
            }
        });

        rootStatistics.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                contextMenu.show(rootStatistics, ev.getScreenX(), ev.getScreenY());
            }
        });
    }

    private void setGraphics() {
        datepicker.setOnAction(ev -> {
            rootDate.setVisible(false);

            pieChart.getData().clear();

            java.sql.Date date = java.sql.Date.valueOf(datepicker.getValue());

            // כאן תוסיף את הלוגיקה שלך לטיפול בבחירת התאריך
            int count = 5; // דוגמה לנתונים מזויפים
            int count2 = 10; // דוגמה לנתונים מזויפים
            int count3 = 15; // דוגמה לנתונים מזויפים

            if (count == 0 && count2 == 0 && count3 == 0) {
                hboxImage.setVisible(true);
                hbox.setVisible(false);
                new FadeIn(hboxImage).play();
            } else {
                hboxImage.setVisible(false);
                hbox.setVisible(true);

                PieChart.Data one = new PieChart.Data("Total customers: " + count, count);
                data.add(one);
                pieChart.setData(data);
                Animations.hover(one.getNode(), 50, 1.1);

                PieChart.Data two = new PieChart.Data("Total quotes: " + count2, count2);
                data.add(two);
                pieChart.setData(data);
                Animations.hover(two.getNode(), 50, 1.1);

                PieChart.Data three = new PieChart.Data("Total products: " + count3, count3);
                data.add(three);
                pieChart.setData(data);
                Animations.hover(three.getNode(), 50, 1.1);
            }
            pieChart.setData(data);
        });
    }
}
