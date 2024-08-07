package il.cshaifasweng.OCSFMediatorExample.client.boundariesEmploee;

import il.cshaifasweng.OCSFMediatorExample.client.dialog.DialogCustomerService;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.dbDeleteLaterGenerate;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
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

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CustomerServiceBoundary implements Initializable {

    private ObservableList<Complaint> listComplaints;
    private ObservableList<RegisteredUser> listCustomers;

    private final dbDeleteLaterGenerate db = new dbDeleteLaterGenerate();

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
         setActionToggleButton();
         loadData();
          animateNodes();

        // הוספת מאזין לאירוע דאבל קליק על שורות הטבלה
        tblComplaints.setRowFactory(tv -> {
            TableRow<Complaint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Complaint rowData = row.getItem();
                    // כאן את יכולה לקרוא לפונקציה שתפתח את הדיאלוג
                    showDialogDetailsQuote();
                }
            });
            return row;
        });
    }


    private void animateNodes() {
        Animations.fadeInUp(rootSearch);
        Animations.fadeInUp(tblComplaints);
    }



    @FXML
    private void showDialogAddQuotes() {
        // השבתת הטבלה
        disableTable();

        // קבלת ההזמנה הנבחרת
        Complaint selectedPurchase = tblComplaints.getSelectionModel().getSelectedItem();

        // אם לא נבחרה הזמנה, הצגת התראה וחזרה
        if (selectedPurchase == null) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomerService, rootCustomerService, tblComplaints, "No complaint selected");
            tblComplaints.setDisable(false);
            return;
        }

        try {
            // הטעינת ה-Ticket.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource( "/il/cshaifasweng/OCSFMediatorExample/client/dialog/dialogCustomerService.fxml"));
            AnchorPane ticketPane = loader.load();

            // השגת ה-Controller של ה-Ticket והעברת המידע הנבחר
            DialogCustomerService dialogCustomerService = loader.getController();
            dialogCustomerService.setCustomerServiceController(this);

            dialogCustomerService.setComplaint(selectedPurchase);

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

            // הוספת אפקט טשטוש ל-rootUsers
            rootCustomerService.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

        } catch (IOException e) {
            e.printStackTrace();
            // במקרה של שגיאה בטעינת ה-FXML, להפעיל מחדש את הטבלה
            tblComplaints.setDisable(false);
        }
    }

    @FXML
    public void closeDialogAddQuotes() {
        System.out.println("closeDialogAddQuotes called");
        if (dialogAddProduct != null) {
            dialogAddProduct.close();
        }
    }



    @FXML
    private void closeDialogDeleteQuote() {
        System.out.println("closeDialogDeleteQuote called");

    }



    @FXML
    private void showDialogDetailsQuote() {
        System.out.println("showDialogDetailsQuote called");
        if (tblComplaints.getSelectionModel().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomerService, rootCustomerService, tblComplaints, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        Complaint selectedComplaint = tblComplaints.getSelectionModel().getSelectedItem();
        System.out.println("Selected Complaint for Details: " + selectedComplaint);

        showDialogAddQuotes();

        selectedRecord();
     }

    private void selectedRecord() {
        Complaint complaint = tblComplaints.getSelectionModel().getSelectedItem();
        DialogCustomerService dialogCustomerService = new DialogCustomerService();
        dialogCustomerService.setComplaint(complaint);
    }

    @FXML
    private void setActionToggleButton() {
        System.out.println("setActionToggleButton called");
     }

    @FXML
    private void loadData() {
        System.out.println("loadData called");
        loadTable();

        // Set up the cell value factories
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("info"));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                cellData.getValue().getCreationDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        ));
        colCustomerName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                cellData.getValue().getRegisteredUser().getName()
        ));

        // Correct usage of ButtonFactory
        ButtonFactory buttonFactory = new ButtonFactory();
        ButtonFactory.ButtonTypeOrderCellValueFactory buttonCellFactory = buttonFactory.new ButtonTypeOrderCellValueFactory(colPurchase);
        colPurchase.setCellValueFactory(buttonCellFactory);

        // Assuming ButtonExistsCellValueFactory is correctly implemented
        colStatus.setCellValueFactory(buttonFactory.new ButtonExistsCellValueFactory(colStatus));
    }


    private void loadTable() {
        System.out.println("loadTable called");
        listComplaints = FXCollections.observableArrayList(db.getComplaints());
        tblComplaints.setItems(listComplaints);
        tblComplaints.setFixedCellSize(30);
    }



    @FXML
    private void newQuote() {
        System.out.println("newQuote called");

        loadData();
         closeDialogAddQuotes();
        AlertsBuilder.create(AlertType.SUCCESS, stckCustomerService, rootCustomerService, tblComplaints, ConstantsPath.MESSAGE_ADDED);
    }

    @FXML
    private void deleteQuotes() {
        System.out.println("deleteQuotes called");
        Complaint selectedQuote = tblComplaints.getSelectionModel().getSelectedItem();
        if (selectedQuote != null) {
            listComplaints.remove(selectedQuote);
            tblComplaints.refresh();
            loadData();
             closeDialogDeleteQuote();
            AlertsBuilder.create(AlertType.SUCCESS, stckCustomerService, rootCustomerService, tblComplaints, ConstantsPath.MESSAGE_ADDED);
        }
    }

    @FXML
    private void updateQuotes() {
        System.out.println("updateQuotes called");

        Complaint complaint = tblComplaints.getSelectionModel().getSelectedItem();

        loadData();
         closeDialogAddQuotes();
        AlertsBuilder.create(AlertType.SUCCESS, stckCustomerService, rootCustomerService, tblComplaints, ConstantsPath.MESSAGE_ADDED);
    }







    private void disableTable() {
        System.out.println("disableTable called");
        tblComplaints.setDisable(true);
    }









    }





