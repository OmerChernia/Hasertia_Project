package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.models.Customers;
import il.cshaifasweng.OCSFMediatorExample.client.models.Quotes;
import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import il.cshaifasweng.OCSFMediatorExample.client.util.AutocompleteComboBox;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class QuotesController implements Initializable {

    private ObservableList<Quotes> listQuotes;
    private ObservableList<Customers> listCustomers;
    private ObservableList<Quotes> filterQuotes;

    @FXML
    private StackPane stckQuotes;

    @FXML
    private AnchorPane rootQuotes;

    @FXML
    private TableView<Quotes> tblQuotes;

    @FXML
    private TableColumn<Quotes, Integer> colId;

    @FXML
    private TableColumn<Quotes, String> colDescription;

    @FXML
    private TableColumn<Quotes, Double> colPrice;

    @FXML
    private TableColumn<Quotes, String> colDate;

    @FXML
    private TableColumn<Quotes, String> colCustomerName;

    @FXML
    private TableColumn<Quotes, Button> colExistence;

    @FXML
    private TableColumn<Quotes, Button> colRealization;

    @FXML
    private TableColumn<Quotes, Button> colReport;

    @FXML
    private HBox rootSearchQuotes;

    @FXML
    private AnchorPane containerAddQuotes;

    @FXML
    private AnchorPane containerDeleteQuotes;

    @FXML
    private TextField txtSearchCustomer;

    @FXML
    private TextField txtSearchQuotes;

    @FXML
    private TextField txtPrice;

    @FXML
    private DatePicker dtpDate;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnAddQuotes;

    @FXML
    private Button btnSaveQuotes;

    @FXML
    private Button btnUpdateQuotes;

    @FXML
    private ComboBox<Customers> cmbIdCustomer;

    @FXML
    private Text titleWindowAddQuotes;

    @FXML
    private ToggleButton toggleButtonExists;

    @FXML
    private ToggleButton toggleButtonReport;

    @FXML
    private ToggleButton toggleButtonRealized;

    private DialogTool dialogAddQuote;

    private DialogTool dialogDeleteQuote;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterQuotes = FXCollections.observableArrayList();
        setActionToggleButton();
        initializeComboBox();
        deleteUserDeleteKey();
        closeDialogWithEscapeKey();
        loadData();
        setMask();
        setContextMenu();
        animateNodes();
        setValidations();
        selectText();
        closeDialogWithTextFields();
    }

    private void setContextMenu() {
        CustomContextMenu contextMenu = new CustomContextMenu(tblQuotes);

        contextMenu.setActionEdit(ev -> {
            showDialogEditQuote();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDeleteQuotes();
            contextMenu.hide();
        });

        contextMenu.setActionDetails(ev -> {
            showDialogDetailsQuote();
            contextMenu.hide();
        });

        contextMenu.setActionRefresh(ev -> {
            loadData();
            contextMenu.hide();
        });

        contextMenu.show();
    }

    private void initializeComboBox() {
        loadComboBox();
        AutocompleteComboBox.autoCompleteComboBoxPlus(cmbIdCustomer, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.toString().equals(typedText));
    }

    private void animateNodes() {
        Animations.fadeInUp(rootSearchQuotes);
        Animations.fadeInUp(btnAddQuotes);
        Animations.fadeInUp(tblQuotes);
    }

    private void setValidations() {
        // No direct equivalent in JavaFX, will handle manually
    }

    private void selectText() {
        // No direct equivalent in JavaFX, will handle manually
    }

    private void setMask() {
        // No direct equivalent in JavaFX, will handle manually
    }

    @FXML
    private void showDialogAddQuotes() {
        cmbIdCustomer.setPromptText("Select a customer");
        cmbIdCustomer.setEditable(true);
        cmbIdCustomer.setDisable(false);

        resetValidations();
        enableEditControls();
        disableTable();
        rootQuotes.setEffect(Constants.BOX_BLUR_EFFECT);

        btnUpdateQuotes.setVisible(true);
        btnSaveQuotes.setDisable(false);
        containerAddQuotes.setVisible(true);
        btnSaveQuotes.toFront();
        titleWindowAddQuotes.setText("Add Quote");

        dialogAddQuote = new DialogTool(containerAddQuotes, stckQuotes);
        dialogAddQuote.show();

        dialogAddQuote.setOnDialogOpened(ev -> {
            txtPrice.requestFocus();
        });

        dialogAddQuote.setOnDialogClosed(ev -> {
            containerAddQuotes.setVisible(false);
            tblQuotes.setDisable(false);
            rootQuotes.setEffect(null);
            cleanControls();
        });

        dtpDate.focusedProperty().addListener((o, oldV, newV) -> {
            if (dtpDate.getEditor().getText().isEmpty() && newV) {
                dtpDate.setValue(LocalDate.now());
            }
        });

        cmbIdCustomer.focusedProperty().addListener((o, oldV, newV) -> {
            if (!oldV) {
                cmbIdCustomer.show();
            } else {
                cmbIdCustomer.hide();
            }
        });
    }

    @FXML
    private void closeDialogAddQuotes() {
        if (dialogAddQuote != null) {
            dialogAddQuote.close();
        }
    }

    @FXML
    private void showDialogDeleteQuotes() {
        if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        containerDeleteQuotes.setVisible(true);
        rootQuotes.setEffect(Constants.BOX_BLUR_EFFECT);
        disableTable();

        dialogDeleteQuote = new DialogTool(containerDeleteQuotes, stckQuotes);
        dialogDeleteQuote.show();

        dialogDeleteQuote.setOnDialogClosed(ev -> {
            containerDeleteQuotes.setVisible(false);
            tblQuotes.setDisable(false);
            rootQuotes.setEffect(null);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogDeleteQuote() {
        if (dialogDeleteQuote != null) {
            dialogDeleteQuote.close();
        }
    }

    @FXML
    private void showDialogEditQuote() {
        if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddQuotes();
        titleWindowAddQuotes.setText("Update quote");

        cmbIdCustomer.setPromptText("");
        cmbIdCustomer.setDisable(true);
        cmbIdCustomer.setEditable(true);

        selectedRecord();
        btnUpdateQuotes.toFront();
    }

    @FXML
    private void showDialogDetailsQuote() {
        if (tblQuotes.getSelectionModel().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddQuotes();

        titleWindowAddQuotes.setText("Quotes details");
        cmbIdCustomer.setPromptText("");
        cmbIdCustomer.setEditable(true);

        btnSaveQuotes.setDisable(true);
        btnUpdateQuotes.setVisible(false);
        btnSaveQuotes.toFront();

        selectedRecord();
        disableEditControls();
    }

    private void selectedRecord() {
        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();
        txtPrice.setText(String.valueOf(quotes.getPrice()));
        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(quotes.getRequestDate())));
        txtDescription.setText(quotes.getDescriptionQuote());
        cmbIdCustomer.getSelectionModel().select(quotes.getCustomerId());
        toggleButtonExists.setText(quotes.getExistence());
        toggleButtonRealized.setText(quotes.getRealization());
        toggleButtonReport.setText(quotes.getReport());

        if (quotes.getExistence().equals(Constants.EXISTENT)) {
            toggleButtonExists.selectedProperty().set(true);
        } else {
            toggleButtonExists.selectedProperty().set(false);
        }

        if (quotes.getRealization().equals(Constants.REALIZED)) {
            toggleButtonRealized.selectedProperty().set(true);
        } else {
            toggleButtonRealized.selectedProperty().set(false);
        }

        if (quotes.getReport().equals(Constants.REPORTED)) {
            toggleButtonReport.selectedProperty().set(true);
        } else {
            toggleButtonReport.selectedProperty().set(false);
        }
    }

    @FXML
    private void setActionToggleButton() {
        if (toggleButtonExists.isSelected()) {
            toggleButtonExists.setText(Constants.EXISTENT);
        } else {
            toggleButtonExists.setText(Constants.NOT_EXISTENT);
        }

        if (toggleButtonRealized.isSelected()) {
            toggleButtonRealized.setText(Constants.REALIZED);
        } else {
            toggleButtonRealized.setText(Constants.NOT_REALIZED);
        }

        if (toggleButtonReport.isSelected()) {
            toggleButtonReport.setText(Constants.REPORTED);
        } else {
            toggleButtonReport.setText(Constants.NOT_REPORTED);
        }
    }

    @FXML
    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionQuote"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colExistence.setCellValueFactory(new ButtonExistsCellValueFactory());
        colReport.setCellValueFactory(new ButtonReportCellValueFactory());
        colRealization.setCellValueFactory(new ButtonRealizedCellValueFactory());
    }

    private void loadTable() {
        listQuotes = FXCollections.observableArrayList();
        // Add some dummy data for testing
        listQuotes.add(new Quotes(1, "Quote 1", Date.valueOf(LocalDate.now()), 100.0, Constants.EXISTENT, Constants.REALIZED, Constants.REPORTED, "Customer 1"));
        listQuotes.add(new Quotes(2, "Quote 2", Date.valueOf(LocalDate.now().minusDays(1)), 200.0, Constants.NOT_EXISTENT, Constants.NOT_REALIZED, Constants.NOT_REPORTED, "Customer 2"));
        listQuotes.add(new Quotes(3, "Quote 3", Date.valueOf(LocalDate.now().minusDays(2)), 300.0, Constants.EXISTENT, Constants.NOT_REALIZED, Constants.REPORTED, "Customer 3"));
        listQuotes.add(new Quotes(4, "Quote 4", Date.valueOf(LocalDate.now().minusDays(3)), 400.0, Constants.NOT_EXISTENT, Constants.REALIZED, Constants.NOT_REPORTED, "Customer 4"));

        tblQuotes.setItems(listQuotes);
        tblQuotes.setFixedCellSize(30);
    }

    private void loadComboBox() {
        listCustomers = FXCollections.observableArrayList();
        // Add some dummy data for testing
        listCustomers.add(new Customers(1, "Customer 1"));
        listCustomers.add(new Customers(2, "Customer 2"));
        listCustomers.add(new Customers(3, "Customer 3"));
        listCustomers.add(new Customers(4, "Customer 4"));

        cmbIdCustomer.setItems(listCustomers);
    }

    @FXML
    private void newQuote() {
        if (!validateFields()) {
            return;
        }

        Quotes quotes = new Quotes();

        quotes.setPrice(Double.parseDouble(txtPrice.getText().isEmpty() ? "0" : txtPrice.getText()));

        quotes.setExistence(toggleButtonExists.isSelected() ? Constants.EXISTENT : Constants.NOT_EXISTENT);
        quotes.setRealization(toggleButtonRealized.isSelected() ? Constants.REALIZED : Constants.NOT_REALIZED);
        quotes.setReport(toggleButtonReport.isSelected() ? Constants.REPORTED : Constants.NOT_REPORTED);

        quotes.setDescriptionQuote(txtDescription.getText().trim());
        quotes.setRequestDate(Date.valueOf(dtpDate.getValue()));
        quotes.setCustomerId(cmbIdCustomer.getSelectionModel().getSelectedItem().getId());

        listQuotes.add(quotes);
        loadData();
        cleanControls();
        closeDialogAddQuotes();
        AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_ADDED);
    }

    @FXML
    private void deleteQuotes() {
        Quotes selectedQuote = tblQuotes.getSelectionModel().getSelectedItem();
        if (selectedQuote != null) {
            listQuotes.remove(selectedQuote);
            tblQuotes.refresh();
            loadData();
            cleanControls();
            closeDialogDeleteQuote();
            AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_DELETED);
        }
    }

    @FXML
    private void updateQuotes() {
        if (!validateFields()) {
            return;
        }

        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();

        quotes.setPrice(Double.parseDouble(txtPrice.getText().isEmpty() ? "0" : txtPrice.getText()));

        quotes.setExistence(toggleButtonExists.isSelected() ? Constants.EXISTENT : Constants.NOT_EXISTENT);
        quotes.setRealization(toggleButtonRealized.isSelected() ? Constants.REALIZED : Constants.NOT_REALIZED);
        quotes.setReport(toggleButtonReport.isSelected() ? Constants.REPORTED : Constants.NOT_REPORTED);

        quotes.setDescriptionQuote(txtDescription.getText().trim());
        quotes.setRequestDate(Date.valueOf(dtpDate.getValue()));
        quotes.setCustomerId(cmbIdCustomer.getSelectionModel().getSelectedItem().getId());

        tblQuotes.refresh();
        loadData();
        cleanControls();
        closeDialogAddQuotes();
        AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_UPDATED);
    }

    private boolean validateFields() {
        boolean valid = true;

        if (txtDescription.getText().isEmpty()) {
            txtDescription.getStyleClass().add("error");
            valid = false;
        } else {
            txtDescription.getStyleClass().removeAll("error");
        }

        if (cmbIdCustomer.getSelectionModel().isEmpty()) {
            cmbIdCustomer.getStyleClass().add("error");
            valid = false;
        } else {
            cmbIdCustomer.getStyleClass().removeAll("error");
        }

        if (txtPrice.getText().isEmpty()) {
            txtPrice.getStyleClass().add("error");
            valid = false;
        } else {
            txtPrice.getStyleClass().removeAll("error");
        }

        if (dtpDate.getValue() == null) {
            dtpDate.getEditor().getStyleClass().add("error");
            valid = false;
        } else {
            dtpDate.getEditor().getStyleClass().removeAll("error");
        }

        return valid;
    }

    private void closeDialogWithEscapeKey() {
        rootQuotes.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogDeleteQuote();
                closeDialogAddQuotes();
                tblQuotes.setDisable(false);
                rootQuotes.setEffect(null);
                AlertsBuilder.close();
            }
        });
    }

    private void closeDialogWithTextFields() {
        txtPrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddQuotes();
            }
        });

        txtDescription.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddQuotes();
            }
        });

        containerAddQuotes.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddQuotes();
            }
        });
    }

    private void deleteUserDeleteKey() {
        rootQuotes.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (tblQuotes.isDisable()) {
                    return;
                }

                if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
                    AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_NO_RECORD_SELECTED);
                    return;
                }
                deleteQuotes();
            }
        });
    }

    private void disableTable() {
        tblQuotes.setDisable(true);
    }

    private void cleanControls() {
        cmbIdCustomer.getSelectionModel().clearSelection();
        txtDescription.clear();
        dtpDate.setValue(null);
        txtPrice.clear();
    }

    private void disableEditControls() {
        txtDescription.setEditable(false);
        txtPrice.setEditable(false);
        dtpDate.setEditable(false);
    }

    private void enableEditControls() {
        txtDescription.setEditable(true);
        txtPrice.setEditable(true);
        dtpDate.setEditable(true);
    }

    private void resetValidations() {
        txtDescription.clear();
        cmbIdCustomer.getSelectionModel().clearSelection();
        txtPrice.clear();
        dtpDate.setValue(null);
    }

    @FXML
    private void filterQuotes() {
        String filterCustomers = txtSearchCustomer.getText().trim();
        if (filterCustomers.isEmpty()) {
            tblQuotes.setItems(listQuotes);
        } else {
            filterQuotes.clear();
            for (Quotes q : listQuotes) {
                if (q.getCustomerName().toLowerCase().contains(filterCustomers.toLowerCase())) {
                    filterQuotes.add(q);
                }
            }
            tblQuotes.setItems(filterQuotes);
        }
    }

    @FXML
    private void filterDescriptionQuotes() {
        String filter = txtSearchQuotes.getText().trim();
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

    private class ButtonExistsCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Quotes, Button> param) {
            Quotes item = param.getValue();

            Button button = new Button(item.getExistence());
            button.setPrefWidth(colExistence.getWidth() / 0.5);

            if (item.getExistence().equals(Constants.EXISTENT)) {
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class ButtonReportCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Quotes, Button> param) {
            Quotes item = param.getValue();

            Button button = new Button(item.getReport());
            button.setPrefWidth(colReport.getWidth() / 0.5);

            if (item.getReport().equals(Constants.REPORTED)) {
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class ButtonRealizedCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Quotes, Button> param) {
            Quotes item = param.getValue();

            Button button = new Button(item.getRealization());
            button.setPrefWidth(colRealization.getWidth() / 0.5);

            if (item.getRealization().equals(Constants.REALIZED)) {
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
