package il.cshaifasweng.OCSFMediatorExample.client.boundaries.registeredUser;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.PurchaseController;
import il.cshaifasweng.OCSFMediatorExample.client.boundaries.user.DialogTicketController;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.MultiEntryTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class OrdersBoundary implements Initializable {

    private final String CANNOT_DELETED = "This user cannot be deleted";

    private final String ADMINISTRATOR_ONLY = "This user can only be administrator type";

    private final String UNABLE_TO_CHANGE = "Unable to change user type";

    @FXML
    private AnchorPane OrderContainer;

    @FXML
    private Button btnNewUser;

    @FXML
    private TableColumn<Purchase, Integer> colId;

    @FXML
    private TableColumn<Purchase, Button> colStatus;

    @FXML
    private TableColumn<Purchase, Button> colTypePurchase;

    @FXML
    private TableColumn<Purchase, String> colpurchaseDate;

    @FXML
    private AnchorPane deleteUserContainer;

    @FXML
    private HBox hboxSearch;

    @FXML
    private ImageView image;

    @FXML
    private AnchorPane rootUsers;

    @FXML
    private StackPane stckUsers;

    @FXML
    private TableView<Purchase> tblOrders;

    @FXML
    private Text txtRefund;

    @FXML
    private Text titleOrder;

    @FXML
    private TextField txtSearchName;

    @FXML
    private TextField txtSearchUser;

    @FXML
    private ToggleButton toggleButtonStatus;

    @FXML
    private ToggleButton toggleButtonType;

    private DialogTool dialogAddUser;

    private DialogTool dialogDeleteUser;

    private ObservableList<Purchase> listOrders;

    private ObservableList<Purchase> filterOrders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listOrders = FXCollections.observableArrayList();
        filterOrders = FXCollections.observableArrayList();
        EventBus.getDefault().register(this);
        PurchaseController.GetAllPurchases(); // מבקש את כל ההזמנות מהשרת
        animateNodes();
        setContextMenu();

        tblOrders.setRowFactory(tv -> {
            TableRow<Purchase> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Purchase rowData = row.getItem();
                    showDialogDetailsUser();
                }
            });
            return row;
        });
    }

    @Subscribe
    public void onPurchaseMessageReceived(PurchaseMessage message) {
        Platform.runLater(() -> {
            if (message.requestType == PurchaseMessage.RequestType.GET_ALL_PURCHASES) {
                listOrders.setAll(message.purchases);
                tblOrders.setItems(listOrders);
            }
        });
    }

    private void setContextMenu() {
        CustomContextMenu contextMenu = new CustomContextMenu(tblOrders);

        contextMenu.setActionEdit(ev -> {
            showDialogEditUser();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDeleteUser();
            contextMenu.hide();
        });

        contextMenu.show();
    }

    private void animateNodes() {
        Animations.fadeInUp(tblOrders);
        Animations.fadeInUp(hboxSearch);
        Animations.fadeInUp(btnNewUser);
    }

    @FXML
    private void showDialogAddUser() {
        disableTable();
        Purchase selectedPurchase = tblOrders.getSelectionModel().getSelectedItem();

        if (selectedPurchase == null) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblOrders, "No purchase selected");
            tblOrders.setDisable(false);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/boundaries/user/dialogTicket.fxml"));
            AnchorPane ticketPane = loader.load();

            DialogTicketController dialogTicketController = loader.getController();
            dialogTicketController.setOrdersController(this);

            dialogTicketController.setTicketInfo(selectedPurchase);

            OrderContainer.getChildren().clear();
            OrderContainer.getChildren().add(ticketPane);
            OrderContainer.setVisible(true);

            dialogAddUser = new DialogTool(OrderContainer, stckUsers);
            dialogAddUser.show();

            dialogAddUser.setOnDialogOpened(ev -> {
                // פעולות נוספות שניתן לבצע בעת פתיחת הדיאלוג, אם יש צורך
            });

            dialogAddUser.setOnDialogClosed(ev -> {
                tblOrders.setDisable(false);
                rootUsers.setEffect(null);
                OrderContainer.setVisible(false);
            });

            rootUsers.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

        } catch (IOException e) {
            e.printStackTrace();
            tblOrders.setDisable(false);
        }
    }

    @FXML
    public void closeDialogAddUser() {
        if (dialogAddUser != null) {
            dialogAddUser.close();
        }
    }

    @FXML
    private void showDialogDeleteUser() {
        if (tblOrders.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblOrders, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        deleteUserContainer.setVisible(true);
        rootUsers.setEffect(ConstantsPath.BOX_BLUR_EFFECT);
        disableTable();

        dialogDeleteUser = new DialogTool(deleteUserContainer, stckUsers);
        dialogDeleteUser.show();
        dialogDeleteUser.setOnDialogClosed(ev -> {
            tblOrders.setDisable(false);
            rootUsers.setEffect(null);
        });
    }

    @FXML
    private void closeDialogDeleteUser() {
        if (dialogDeleteUser != null) {
            dialogDeleteUser.close();
        }
    }

    @FXML
    private void showDialogEditUser() {
        disableTable();
        Purchase selectedPurchase = tblOrders.getSelectionModel().getSelectedItem();

        if (selectedPurchase == null) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblOrders, "No purchase selected");
            tblOrders.setDisable(false);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/boundaries/registeredUser/dialogComplaint.fxml"));
            AnchorPane ticketPane = loader.load();

            DialogComplaintController dialogComplaintController = loader.getController();
            dialogComplaintController.setOrdersController(this);

            dialogComplaintController.setPurchase(selectedPurchase);

            OrderContainer.getChildren().clear();
            OrderContainer.getChildren().add(ticketPane);
            OrderContainer.setVisible(true);

            dialogAddUser = new DialogTool(OrderContainer, stckUsers);
            dialogAddUser.show();

            dialogAddUser.setOnDialogOpened(ev -> {
                // פעולות נוספות שניתן לבצע בעת פתיחת הדיאלוג, אם יש צורך
            });

            dialogAddUser.setOnDialogClosed(ev -> {
                tblOrders.setDisable(false);
                rootUsers.setEffect(null);
                OrderContainer.setVisible(false);
            });

            rootUsers.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

        } catch (IOException e) {
            e.printStackTrace();
            tblOrders.setDisable(false);
        }
    }

    @FXML
    private void showDialogDetailsUser() {
        if (tblOrders.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblOrders, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddUser();
        selectedRecord();
    }

    @FXML
    private void loadData() {
        loadTable();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colpurchaseDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                cellData.getValue().getPurchaseDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        ));

        ButtonFactory buttonFactory = new ButtonFactory();
        colStatus.setCellValueFactory(buttonFactory.new ButtonStatusCellValueFactory());
        colTypePurchase.setCellValueFactory(buttonFactory.new ButtonTypePurchaseCellValueFactory(colTypePurchase));
    }

    private void loadTable() {
        PurchaseController.GetAllPurchases(); // מבקש את כל ההזמנות מהשרת
    }

    @FXML
    private void deleteUser() {
        int id = tblOrders.getSelectionModel().getSelectedItem().getId();

        if (id == 1) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, CANNOT_DELETED);
            return;
        }

        PurchaseController.RemovePurchase(id); // מחיקת ההזמנה מהשרת
        listOrders.remove(tblOrders.getSelectionModel().getSelectedItem());
        loadData();
        closeDialogDeleteUser();
        handleRefund();
    }

    private void selectedRecord() {
        Purchase purchase = tblOrders.getSelectionModel().getSelectedItem();
        DialogTicketController dialogTicketController = new DialogTicketController();
        dialogTicketController.setPurchase(purchase);
    }

    private void disableTable() {
        tblOrders.setDisable(true);
    }

    @FXML
    private void filterName() {
        // Implement filter logic here
    }

    @FXML
    private void filterUser() {
        // Implement filter logic here
    }

    @FXML
    private void handleRefund() {
        Purchase selectedPurchase = tblOrders.getSelectionModel().getSelectedItem();
        if (selectedPurchase == null) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblOrders, "No purchase selected");
            return;
        } else if (selectedPurchase instanceof MultiEntryTicket) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblOrders, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        String refundDetails = processRefund(selectedPurchase);
        txtRefund.setText(refundDetails);

        AlertsBuilder.create(AlertType.SUCCESS, stckUsers, rootUsers, tblOrders, refundDetails);
    }

    private String processRefund(Purchase purchase) {
        if (purchase instanceof MovieTicket) {
            MovieTicket movieTicket = (MovieTicket) purchase;
            int price = movieTicket.getMovieInstance().getMovie().getTheaterPrice();
            LocalDateTime screeningTime = purchase.getPurchaseDate();
            long hoursUntilScreening = ChronoUnit.HOURS.between(LocalDateTime.now(), screeningTime);
            if (hoursUntilScreening > 3) {
                return "You will refund Full Price Ticket: " + price + "₪";
            } else if (hoursUntilScreening > 1) {
                return "You will refund 50% of the Price Ticket: " + price * 0.5 + "₪";
            } else {
                return "You won't be refunded!";
            }
        } else {
            HomeViewingPackageInstance movieTicket = (HomeViewingPackageInstance) purchase;
            return "You will refund 50% of the Price Ticket: " + movieTicket.getMovie().getHomeViewingPrice() * 0.5 + "₪";
        }
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
}
