/*
 * Copyright 2020-2021 LaynezCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXPopup;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
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

    private final JFXPopup popup = new JFXPopup();

    private final ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGraphics();
        setDatePicker();
        setPopup();
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

    private void setPopup() {
        popup.setPopupContent(rootDate);

        pieChart.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                popup.show(pieChart, ev.getScreenX(), ev.getScreenY());
            }
        });

        hboxImage.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                popup.show(hboxImage, ev.getScreenX(), ev.getScreenY());
            }
        });

        emptyImage.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                popup.show(emptyImage, ev.getScreenX(), ev.getScreenY());
            }
        });

        rootStatistics.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                popup.show(rootStatistics, ev.getScreenX(), ev.getScreenY());
            }
        });
    }

    private void setGraphics() {
        datepicker.setOnAction(ev -> {

            popup.hide();

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
