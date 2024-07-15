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
package com.laynezcoder.estfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Quotes;
import com.laynezcoder.estfx.Constants;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private final String DEFAULT_WELCOME_TEXT = "¿What do you think if you start adding a new client?";

    private ObservableList<Quotes> listQuotes;
    private ObservableList<Quotes> filterQuotes;

    @FXML
    private StackPane stckHome;

    @FXML
    private AnchorPane rootSearchMain;

    @FXML
    private AnchorPane rootHome;

    @FXML
    private TextField txtSearchRecentCustomer;

    @FXML
    private AnchorPane rootWelcome;

    @FXML
    private Text textDescriptionWelcome;

    @FXML
    private Text textWelcome;

    @FXML
    private Label labelTotalCustomers;

    @FXML
    private Label labelTotalQuotes;

    @FXML
    private Label labelNowQuotes;

    @FXML
    private Label labelTotalProduct;

    @FXML
    private TableView<Quotes> tblQuotes;

    @FXML
    private AnchorPane rootTotalCustomers;

    @FXML
    private AnchorPane rootTotalQuotes;

    @FXML
    private AnchorPane rootRecentQuotes;

    @FXML
    private AnchorPane rootProducts;

    @FXML
    private TableColumn<Quotes, Integer> colId;

    @FXML
    private TableColumn<Quotes, String> colDescription;

    @FXML
    private TableColumn<Quotes, String> colPrice;

    @FXML
    private TableColumn<Quotes, String> colDate;

    @FXML
    private TableColumn<Quotes, String> colCustomerName;

    @FXML
    private TableColumn<Quotes, JFXButton> colExistence;

    @FXML
    private TableColumn<Quotes, JFXButton> colRealization;

    @FXML
    private TableColumn<Quotes, JFXButton> colReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listQuotes = FXCollections.observableArrayList();
        filterQuotes = FXCollections.observableArrayList();
        animationsNodes();
        setWelcomeText();
        counterRecords();
        loadData();
        selectText();
    }

    private void selectText() {
        TextFieldMask.selectText(txtSearchRecentCustomer);
    }

    private void animationsNodes() {
        Animations.fadeInUp(rootSearchMain);
        Animations.fadeInUp(rootWelcome);
        Animations.fadeInUp(tblQuotes);
        Animations.fadeInUp(rootTotalCustomers);
        Animations.fadeInUp(rootTotalQuotes);
        Animations.fadeInUp(rootRecentQuotes);
        Animations.fadeInUp(rootProducts);
    }

    private void counterRecords() {
        labelTotalCustomers.setText("0");
        labelTotalProduct.setText("0");
    }

    private int getTotalQuotes() {
        int count = listQuotes.size();
        labelTotalQuotes.setText(String.valueOf(count));
        return count;
    }

    private void setWelcomeText() {
        String name = "User"; // Placeholder for user name
        int total = getTotalQuotes();
        labelTotalQuotes.setText(String.valueOf(total));

        switch (total) {
            case 10:
                setText(name, 10);
                break;
            case 20:
                setText(name, 20);
                break;
            case 30:
                setText(name, 30);
                break;
            case 50:
                setText(name, 50);
                break;
            case 100:
                setText(name, 100);
                break;
            case 500:
                setText(name, 500);
                break;
            default:
                textWelcome.setText("¡Welcome back, " + name + "!");
                textDescriptionWelcome.setText(DEFAULT_WELCOME_TEXT);
                break;
        }
    }

    private void setText(String name, int total) {
        textWelcome.setText("¡Congratulations " + name + ", " + total + " new quotes have been registered!");
        textDescriptionWelcome.setText("!Nice job!");
    }

    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionQuote"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colExistence.setCellValueFactory(new JFXButtonExistsCellValueFactory());
        colRealization.setCellValueFactory(new JFXButtonRealizedCellValueFactory());
        colReport.setCellValueFactory(new JFXButtonReportCellValueFactory());
    }

    private void loadTable() {
        // Add some dummy data for testing
        listQuotes.add(new Quotes(1, "Quote 1", new java.sql.Date(System.currentTimeMillis()), 100.0, "Exist", "Realized", "Reported", "Customer 1"));
        listQuotes.add(new Quotes(2, "Quote 2", new java.sql.Date(System.currentTimeMillis()), 200.0, "Exist", "Realized", "Reported", "Customer 2"));
        listQuotes.add(new Quotes(3, "Quote 3", new java.sql.Date(System.currentTimeMillis()), 300.0, "Exist", "Realized", "Reported", "Customer 3"));
        listQuotes.add(new Quotes(4, "Quote 4", new java.sql.Date(System.currentTimeMillis()), 400.0, "Exist", "Realized", "Reported", "Customer 4"));

        labelNowQuotes.setText(String.valueOf(listQuotes.size()));
        tblQuotes.setItems(listQuotes);
    }

    @FXML
    private void filterQuotes() {
        String filter = txtSearchRecentCustomer.getText().trim();
        if (filter.isEmpty()) {
            tblQuotes.setItems(listQuotes);
        } else {
            filterQuotes.clear();
            for (Quotes q : listQuotes) {
                if (q.getDescriptionQuote().toLowerCase().contains(filter.toLowerCase())) {
                    filterQuotes.add(q);
                }
            }
            tblQuotes.setItems(filterQuotes);
        }
    }

    private class JFXButtonExistsCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getExistence());
            button.setPrefWidth(colExistence.getWidth() / 0.5);

            if (item.getExistence().equals(Constants.EXISTENT)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonReportCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getReport());
            button.setPrefWidth(colReport.getWidth() / 0.5);

            if (item.getReport().equals(Constants.REPORTED)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonRealizedCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getRealization());
            button.setPrefWidth(colRealization.getWidth() / 0.5);

            if (item.getRealization().equals(Constants.REALIZED)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
