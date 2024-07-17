
package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.models.Customers;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CustomersController implements Initializable {

    private static final String NOT_AVAILABLE = "N/A";
    private static final String INVALID_EMAIL = "Invalid email";

    private ObservableList<Customers> listCustomers;
    private ObservableList<Customers> filterCustomers;

    @FXML
    private StackPane stckCustomers;

    @FXML
    private AnchorPane rootCustomers;

    @FXML
    private AnchorPane containerDeleteCustomer;

    @FXML
    private AnchorPane containerAddCustomer;

    @FXML
    private Button btnUpdateCustomer;

    @FXML
    private Button btnSaveCustomer;

    @FXML
    private TableView<Customers> tblCustomers;

    @FXML
    private TableColumn<Customers, Integer> colCodigoCliente;

    @FXML
    private TableColumn<Customers, String> colNombreCliente;

    @FXML
    private TableColumn<Customers, String> colTelefonoCliente;

    @FXML
    private TableColumn<Customers, String> colCorreoCliente;

    @FXML
    private TableColumn<Customers, String> colNitCliente;

    @FXML
    private Button btnAddCustomer;

    @FXML
    private HBox rootSearchCustomers;

    @FXML
    private TextField txtSearchNumber;

    @FXML
    private TextField txtSearchCustomer;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerNumber;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtIt;

    @FXML
    private Text titleAddCustomer;

    private DialogTool dialogAddCustomer;
    private DialogTool dialogDeleteCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listCustomers = FXCollections.observableArrayList();
        filterCustomers = FXCollections.observableArrayList();
        selectText();
        animateNodes();
        closeDialogWithTextFields();
        closeDialogWithEscapeKey();
    }

    private void animateNodes() {
        Animations.fadeInUp(rootSearchCustomers);
        Animations.fadeInUp(btnAddCustomer);
        Animations.fadeInUp(tblCustomers);
    }

    private void selectText() {
        txtCustomerNumber.setOnMouseClicked(event -> txtCustomerNumber.selectAll());
        txtCustomerName.setOnMouseClicked(event -> txtCustomerName.selectAll());
        txtEmail.setOnMouseClicked(event -> txtEmail.selectAll());
        txtIt.setOnMouseClicked(event -> txtIt.selectAll());
        txtSearchCustomer.setOnMouseClicked(event -> txtSearchCustomer.selectAll());
        txtSearchNumber.setOnMouseClicked(event -> txtSearchNumber.selectAll());
    }

    @FXML
    private void showDialogAddCustomer() {
        resetValidation();
        enableEditControls();
        rootCustomers.setEffect(Constants.BOX_BLUR_EFFECT);

        titleAddCustomer.setText("Add customer");
        btnUpdateCustomer.setVisible(false);
        btnSaveCustomer.setVisible(true);
        containerAddCustomer.setVisible(true);
        btnSaveCustomer.toFront();

        dialogAddCustomer = new DialogTool(containerAddCustomer, stckCustomers);
        dialogAddCustomer.show();

        dialogAddCustomer.setOnDialogOpened(ev -> {
            txtCustomerName.requestFocus();
        });

        dialogAddCustomer.setOnDialogClosed(ev -> {
            containerAddCustomer.setVisible(false);
            rootCustomers.setEffect(null);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogAddCustomer() {
        if (dialogAddCustomer != null) {
            dialogAddCustomer.close();
        }
    }

    @FXML
    private void showDialogDeleteCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        containerDeleteCustomer.setVisible(true);
        rootCustomers.setEffect(Constants.BOX_BLUR_EFFECT);

        dialogDeleteCustomer = new DialogTool(containerDeleteCustomer, stckCustomers);
        dialogDeleteCustomer.show();

        dialogDeleteCustomer.setOnDialogClosed(ev -> {
            containerDeleteCustomer.setVisible(false);
            rootCustomers.setEffect(null);
            cleanControls();
        });

    }

    @FXML
    private void closeDialogDeleteCustomer() {
        if (dialogDeleteCustomer != null) {
            dialogDeleteCustomer.close();
        }
    }

    @FXML
    private void showDialogEditCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddCustomer();
        titleAddCustomer.setText("Update customer");
        btnUpdateCustomer.toFront();
        selectedRecord();
    }

    @FXML
    private void showDialogDetailsCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddCustomer();
        titleAddCustomer.setText("Customer details");
        btnUpdateCustomer.setVisible(false);
        btnSaveCustomer.setDisable(true);
        btnSaveCustomer.toFront();
        disableEditControls();
        selectedRecord();
    }

    private void selectedRecord() {
        Customers customers = tblCustomers.getSelectionModel().getSelectedItem();
        txtCustomerName.setText(customers.getCustomerName());
        txtCustomerNumber.setText(customers.getCustomerNumber());
        txtEmail.setText(customers.getCustomerEmail());
        txtIt.setText(customers.getIt());
    }

    @FXML
    private void newCustomer() {
        String name = txtCustomerName.getText().trim();
        String phoneNumber = txtCustomerNumber.getText().trim();
        String email = txtEmail.getText().trim();
        String it = txtIt.getText().trim();

        if (name.isEmpty()) {
            txtCustomerName.requestFocus();
            Animations.shake(txtCustomerName);
            return;
        }

        if (phoneNumber.isEmpty()) {
            txtCustomerNumber.requestFocus();
            Animations.shake(txtCustomerNumber);
            return;
        }

        if (!validateEmailAddress(email) && !email.isEmpty()) {
            txtEmail.requestFocus();
            Animations.shake(txtEmail);
            NotificationsBuilder.create(NotificationType.ERROR, INVALID_EMAIL);
            return;
        }

        Customers customers = new Customers();
        customers.setCustomerName(name);
        customers.setCustomerNumber(phoneNumber);

        if (email.isEmpty()) {
            customers.setCustomerEmail(NOT_AVAILABLE);
        } else {
            customers.setCustomerEmail(email);
        }

        if (it.isEmpty()) {
            customers.setIt(NOT_AVAILABLE);
        } else {
            customers.setIt(it);
        }

        listCustomers.add(customers);
        tblCustomers.setItems(listCustomers);
        cleanControls();
        closeDialogAddCustomer();
        AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, "Customer added successfully");
    }

    @FXML
    private void deleteCustomer() {
        Customers selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            listCustomers.remove(selectedCustomer);
            tblCustomers.setItems(listCustomers);
            cleanControls();
            closeDialogDeleteCustomer();
            AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, "Customer deleted successfully");
        }
    }

    @FXML
    private void updateCustomer() {
        String name = txtCustomerName.getText().trim();
        String phoneNumber = txtCustomerNumber.getText().trim();
        String email = txtEmail.getText().trim();
        String it = txtIt.getText().trim();

        if (name.isEmpty()) {
            txtCustomerName.requestFocus();
            Animations.shake(txtCustomerName);
            return;
        }

        if (phoneNumber.isEmpty()) {
            txtCustomerNumber.requestFocus();
            Animations.shake(txtCustomerNumber);
            return;
        }

        if (!validateEmailAddress(email) && !email.isEmpty() && !email.equals(NOT_AVAILABLE)) {
            txtEmail.requestFocus();
            Animations.shake(txtEmail);
            NotificationsBuilder.create(NotificationType.ERROR, INVALID_EMAIL);
            return;
        }

        Customers customers = tblCustomers.getSelectionModel().getSelectedItem();
        customers.setCustomerName(name);
        customers.setCustomerNumber(phoneNumber);

        if (email.isEmpty()) {
            customers.setCustomerEmail(NOT_AVAILABLE);
        } else {
            customers.setCustomerEmail(email);
        }

        if (it.isEmpty()) {
            customers.setIt(NOT_AVAILABLE);
        } else {
            customers.setIt(it);
        }

        tblCustomers.refresh();
        cleanControls();
        closeDialogAddCustomer();
        AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, "Customer updated successfully");
    }

    private void cleanControls() {
        txtEmail.clear();
        txtIt.clear();
        txtCustomerName.clear();
        txtCustomerNumber.clear();
    }

    private void disableEditControls() {
        txtEmail.setEditable(false);
        txtIt.setEditable(false);
        txtCustomerName.setEditable(false);
        txtCustomerNumber.setEditable(false);
    }

    private void enableEditControls() {
        txtEmail.setEditable(true);
        txtIt.setEditable(true);
        txtCustomerName.setEditable(true);
        txtCustomerNumber.setEditable(true);
    }

    private void resetValidation() {
        // Add any reset validation logic here if needed
    }

    private boolean validateEmailAddress(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private void closeDialogWithEscapeKey() {
        rootCustomers.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogDeleteCustomer();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                AlertsBuilder.close();
            }
        });
    }

    private void closeDialogWithTextFields() {
        txtCustomerName.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }
        });

        txtCustomerNumber.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }
        });

        txtEmail.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }
        });

        txtIt.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }
        });
    }

    @FXML
    private void filterNameCustomer() {
        String filterName = txtSearchCustomer.getText().trim();
        if (filterName.isEmpty()) {
            tblCustomers.setItems(listCustomers);
        } else {
            filterCustomers.clear();
            for (Customers c : listCustomers) {
                if (c.getCustomerName().toLowerCase().contains(filterName.toLowerCase())) {
                    filterCustomers.add(c);
                }
            }
            tblCustomers.setItems(filterCustomers);
        }
    }

    @FXML
    private void filterNumberCustomer() {
        String filterNumber = txtSearchNumber.getText().trim();
        if (filterNumber.isEmpty()) {
            tblCustomers.setItems(listCustomers);
        } else {
            filterCustomers.clear();
            for (Customers c : listCustomers) {
                if (c.getCustomerNumber().toLowerCase().contains(filterNumber.toLowerCase())) {
                    filterCustomers.add(c);
                }
            }
            tblCustomers.setItems(filterCustomers);
        }
    }


}
