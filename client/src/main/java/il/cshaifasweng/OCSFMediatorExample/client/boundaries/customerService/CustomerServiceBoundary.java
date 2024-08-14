package il.cshaifasweng.OCSFMediatorExample.client.boundaries.customerService;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.ComplaintController;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerServiceBoundary implements Initializable {

    private ObservableList<Complaint> listComplaints;
    private DialogTool dialogAddProduct;

    @FXML
    private TableColumn<Complaint, Integer> colId;
    @FXML
    private TableColumn<Complaint, String> colCustomerName;
    @FXML
    private TableColumn<Complaint, Button> colPurchase;
    @FXML
    private TableColumn<Complaint, String> colDate;
    @FXML
    private TableColumn<Complaint, String> colDescription;
    @FXML
    private TableColumn<Complaint, Button> colStatus;
    @FXML
    private AnchorPane containerDeleteComplaint;
    @FXML
    private AnchorPane containerHandleComplaint;
    @FXML
    private AnchorPane rootCustomerService;
    @FXML
    private HBox rootSearch;
    @FXML
    private StackPane stckCustomerService;
    @FXML
    private TableView<Complaint> tblComplaints;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        setActionToggleButton();
        Platform.runLater(() -> ComplaintController.getAllComplaints());  // Request to load data from the server
        animateNodes();

        tblComplaints.setRowFactory(tv -> {
            TableRow<Complaint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Complaint rowData = row.getItem();
                    Platform.runLater(this::showDialogDetailsQuote);
                }
            });
            return row;
        });
    }

    @Subscribe
    public void onComplaintMessageReceived(ComplaintMessage message) {
        if (message.responseType == ComplaintMessage.ResponseType.FILLTERD_COMPLIANTS_LIST) {
            Platform.runLater(() -> loadTableData(message.compliants));
        }
    }

    private void loadTableData(List<Complaint> complaints) {
        Platform.runLater(() -> {
            listComplaints = FXCollections.observableArrayList(complaints);
            tblComplaints.setItems(listComplaints);
            tblComplaints.setFixedCellSize(30);

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("info"));
            colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                    cellData.getValue().getCreationDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            ));
            colCustomerName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                    cellData.getValue().getRegisteredUser().getName()
            ));

            ButtonFactory buttonFactory = new ButtonFactory();
            colPurchase.setCellValueFactory(new ButtonFactory.ButtonTypeOrderCellValueFactory());
            colStatus.setCellValueFactory(new ButtonFactory.ButtonExistsCellValueFactory());
        });
    }

    private void animateNodes() {
        Platform.runLater(() -> {
            Animations.fadeInUp(rootSearch);
            Animations.fadeInUp(tblComplaints);
        });
    }

    @FXML
    private void showDialogAddQuotes() {
        Platform.runLater(() -> {
            disableTable();
            Complaint selectedComplaint = tblComplaints.getSelectionModel().getSelectedItem();
            if (selectedComplaint == null) {
                AlertsBuilder.create(AlertType.ERROR, stckCustomerService, rootCustomerService, tblComplaints, "No complaint selected");
                tblComplaints.setDisable(false);
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ConstantsPath.DIALOG_CUSTOMER_SERVICE_VIEW));
                AnchorPane ticketPane = loader.load();

                DialogCustomerService dialogCustomerService = loader.getController();
                dialogCustomerService.setCustomerServiceController(this);
                dialogCustomerService.setComplaint(selectedComplaint);

                containerHandleComplaint.getChildren().clear();
                containerHandleComplaint.getChildren().add(ticketPane);
                containerHandleComplaint.setVisible(true);

                dialogAddProduct = new DialogTool(containerHandleComplaint, stckCustomerService);
                dialogAddProduct.show();

                dialogAddProduct.setOnDialogClosed(ev -> {
                    tblComplaints.setDisable(false);
                    rootCustomerService.setEffect(null);
                    containerHandleComplaint.setVisible(false);
                });

                rootCustomerService.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

            } catch (IOException e) {
                e.printStackTrace();
                tblComplaints.setDisable(false);
            }
        });
    }

    @FXML
    public void closeDialogAddQuotes() {
        if (dialogAddProduct != null) {
            dialogAddProduct.close() ;
        }
    }

    @FXML
    private void showDialogDetailsQuote() {
        Platform.runLater(() -> {
            if (tblComplaints.getSelectionModel().isEmpty()) {
                AlertsBuilder.create(AlertType.ERROR, stckCustomerService, rootCustomerService, tblComplaints, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
                return;
            }
            showDialogAddQuotes();
            selectedRecord();
        });
    }

    private void selectedRecord() {
        Complaint complaint = tblComplaints.getSelectionModel().getSelectedItem();
        Platform.runLater(() -> {
            DialogCustomerService dialogCustomerService = new DialogCustomerService();
            dialogCustomerService.setComplaint(complaint);
        });
    }

    @FXML
    private void setActionToggleButton() {
        // Implement action handling for toggle buttons if necessary
    }

    @FXML
    private void loadData() {
        ComplaintController.getAllComplaints() ;  // Request to load data from the server
    }

    @FXML
    private void newQuote() {
        Platform.runLater(() -> {
            loadData();
            closeDialogAddQuotes();
            AlertsBuilder.create(AlertType.SUCCESS, stckCustomerService, rootCustomerService, tblComplaints, ConstantsPath.MESSAGE_ADDED);
        });
    }

    @FXML
    private void deleteQuotes() {
        Platform.runLater(() -> {
            Complaint selectedQuote = tblComplaints.getSelectionModel().getSelectedItem();
            if (selectedQuote != null) {
                listComplaints.remove(selectedQuote);
                tblComplaints.refresh();
                loadData();
                closeDialogDeleteQuote();
                AlertsBuilder.create(AlertType.SUCCESS, stckCustomerService, rootCustomerService, tblComplaints, ConstantsPath.MESSAGE_ADDED);
            }
        });
    }

    @FXML
    private void updateQuotes() {
        Platform.runLater(() -> {
            loadData();
            closeDialogAddQuotes();
            AlertsBuilder.create(AlertType.SUCCESS, stckCustomerService, rootCustomerService, tblComplaints, ConstantsPath.MESSAGE_ADDED);
        });
    }

    private void disableTable() {
        Platform.runLater(() -> tblComplaints.setDisable(true));
    }

    @FXML
    private void closeDialogDeleteQuote() {
        // Implement close dialog logic here
    }
}
