package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import animatefx.animation.Shake;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.mask.RequieredFieldsValidators;
import il.cshaifasweng.OCSFMediatorExample.client.mask.TextFieldMask;
import il.cshaifasweng.OCSFMediatorExample.client.models.Customers;
import il.cshaifasweng.OCSFMediatorExample.client.models.Quotes;
import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationsBuilder;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
        RequieredFieldsValidators.toTextArea(txtDescription);
        RequieredFieldsValidators.toComboBox(cmbIdCustomer);
        RequieredFieldsValidators.toDatePicker(dtpDate);
    }

    private void selectText() {
        TextFieldMask.selectText(txtSearchCustomer);
        TextFieldMask.selectTextToJFXTextArea(txtDescription);
        TextFieldMask.selectText(txtPrice);
    }

    private void setMask() {
        TextFieldMask.onlyDoubleNumbers5Integers(txtPrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtPrice);
    }

    @FXML
    private void showDialogAddQuotes() {
        System.out.println("showDialogAddQuotes called");
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
            System.out.println("Dialog Add Quote opened");
        });

        dialogAddQuote.setOnDialogClosed(ev -> {
            containerAddQuotes.setVisible(false);
            tblQuotes.setDisable(false);
            rootQuotes.setEffect(null);
            cleanControls();
            System.out.println("Dialog Add Quote closed");
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
        System.out.println("closeDialogAddQuotes called");
        if (dialogAddQuote != null) {
            dialogAddQuote.close();
        }
    }

    @FXML
    private void showDialogDeleteQuotes() {
        System.out.println("showDialogDeleteQuotes called");
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
            System.out.println("Dialog Delete Quote closed");
        });
    }

    @FXML
    private void closeDialogDeleteQuote() {
        System.out.println("closeDialogDeleteQuote called");
        if (dialogDeleteQuote != null) {
            dialogDeleteQuote.close();
        }
    }

    @FXML
    private void showDialogEditQuote() {
        System.out.println("showDialogEditQuote called");
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
        System.out.println("showDialogDetailsQuote called");
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
        System.out.println("selectedRecord called");
        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();
        if (quotes == null) {
            System.out.println("No quote selected");
            return;
        }

        Integer customerId = quotes.getCustomerId();
        if (customerId == null) {
            System.out.println("Customer ID is null");
            customerId=0;
        }

        txtPrice.setText(String.valueOf(quotes.getPrice()));
        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(quotes.getRequestDate())));
        txtDescription.setText(quotes.getDescriptionQuote());
        cmbIdCustomer.getSelectionModel().select(customerId);
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
        System.out.println("setActionToggleButton called");
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
        System.out.println("loadData called");
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
        System.out.println("loadTable called");
        ArrayList<Quotes> list = new ArrayList<>();
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
        System.out.println("loadComboBox called");
        ArrayList<Customers> list = new ArrayList<>();
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
        System.out.println("newQuote called");
        String description = txtDescription.getText().trim();

        if (dtpDate.getEditor().getText().isEmpty()) {
            dtpDate.requestFocus();
            Animations.shake(dtpDate);
            return;
        }

        if (cmbIdCustomer.getSelectionModel().isEmpty()) {
            Animations.shake(cmbIdCustomer);
            return;
        }

        if (description.isEmpty()) {
            new Shake(txtDescription).play();
            return;
        }

        Quotes quotes = new Quotes();

        if (txtPrice.getText().isEmpty()) {
            quotes.setPrice(Double.valueOf("0"));
        } else {
            quotes.setPrice(Double.valueOf(txtPrice.getText()));
        }

        if (toggleButtonExists.isSelected()) {
            quotes.setExistence(Constants.EXISTENT);
        } else {
            quotes.setExistence(Constants.NOT_EXISTENT);
        }

        if (toggleButtonRealized.isSelected()) {
            quotes.setRealization(Constants.REALIZED);
        } else {
            quotes.setRealization(Constants.NOT_REALIZED);
        }

        if (toggleButtonReport.isSelected()) {
            quotes.setReport(Constants.REPORTED);
        } else {
            quotes.setReport(Constants.NOT_REPORTED);
        }

        quotes.setDescriptionQuote(description);
        quotes.setRequestDate(java.sql.Date.valueOf(dtpDate.getValue()));
        quotes.setCustomerId(AutocompleteComboBox.getComboBoxValue(cmbIdCustomer).getId());

        listQuotes.add(quotes);
        loadData();
        cleanControls();
        closeDialogAddQuotes();
        AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_ADDED);
    }

    @FXML
    private void deleteQuotes() {
        System.out.println("deleteQuotes called");
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
        System.out.println("updateQuotes called");
        String description = txtDescription.getText().trim();

        if (dtpDate.getEditor().getText().isEmpty()) {
            dtpDate.requestFocus();
            Animations.shake(dtpDate);
            return;
        }

        if (description.isEmpty()) {
            new Shake(txtDescription).play();
            return;
        }

        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();

        if (txtPrice.getText().isEmpty()) {
            quotes.setPrice(Double.valueOf("0"));
        } else {
            quotes.setPrice(Double.valueOf(txtPrice.getText()));
        }

        if (toggleButtonExists.isSelected()) {
            quotes.setExistence(Constants.EXISTENT);
        } else {
            quotes.setExistence(Constants.NOT_EXISTENT);
        }

        if (toggleButtonRealized.isSelected()) {
            quotes.setRealization(Constants.REALIZED);
        } else {
            quotes.setRealization(Constants.NOT_REALIZED);
        }

        if (toggleButtonReport.isSelected()) {
            quotes.setReport(Constants.REPORTED);
        } else {
            quotes.setReport(Constants.NOT_REPORTED);
        }

        quotes.setDescriptionQuote(description);
        quotes.setRequestDate(java.sql.Date.valueOf(dtpDate.getValue()));
        quotes.setId(quotes.getId());

        loadData();
        cleanControls();
        closeDialogAddQuotes();
        AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_UPDATED);
    }

    private void closeDialogWithEscapeKey() {
        System.out.println("closeDialogWithEscapeKey called");
        rootQuotes.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogDeleteQuote();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddQuotes();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                tblQuotes.setDisable(false);
                rootQuotes.setEffect(null);
                AlertsBuilder.close();
            }
        });
    }

    private void closeDialogWithTextFields() {
        System.out.println("closeDialogWithTextFields called");
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
        System.out.println("deleteUserDeleteKey called");
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
        System.out.println("disableTable called");
        tblQuotes.setDisable(true);
    }

    private void cleanControls() {
        System.out.println("cleanControls called");
        cmbIdCustomer.getSelectionModel().clearSelection();
        txtDescription.clear();
        dtpDate.setValue(null);
        txtPrice.clear();
    }

    private void disableEditControls() {
        System.out.println("disableEditControls called");
        txtDescription.setEditable(false);
        txtPrice.setEditable(false);
        dtpDate.setEditable(false);
    }

    private void enableEditControls() {
        System.out.println("enableEditControls called");
        txtDescription.setEditable(true);
        txtPrice.setEditable(true);
        dtpDate.setEditable(true);
    }

    private void resetValidations() {
        System.out.println("resetValidations called");
        txtDescription.clear();
        cmbIdCustomer.getSelectionModel().clearSelection();
        txtPrice.clear();
        dtpDate.setValue(null);
    }

    @FXML
    private void filterQuotes() {
        System.out.println("filterQuotes called");
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
        System.out.println("filterDescriptionQuotes called");
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

            Button button = new Button();
            button.setText(item.getExistence());
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

            Button button = new Button();
            button.setText(item.getReport());
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

            Button button = new Button();
            button.setText(item.getRealization());
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
