package il.cshaifasweng.OCSFMediatorExample.client.boundaries.theaterManager;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.PriceRequestController;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PriceRequestMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PriceChangeBoundary implements Initializable {

    private ObservableList<PriceRequest> listPriceRequest;

    @FXML
    private TableColumn<PriceRequest, Integer> colId;

    @FXML
    private TableColumn<PriceRequest, String> colMovieName;

    @FXML
    private TableColumn<PriceRequest, String> colStreamingType;

    @FXML
    private TableColumn<PriceRequest, Button> colStatus;

    @FXML
    private AnchorPane rootPriceChange;

    @FXML
    private StackPane stckPriceChange;

    @FXML
    private TableView<PriceRequest> tblPriceRequest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        loadData();
        animateNodes();
    }

    @FXML
    private void loadData() {
        PriceRequestController.requestAllPriceRequests();
    }

    @Subscribe
    public void onPriceChangeMessageReceived(PriceRequestMessage message) {
        loadTableData(message.requests);
    }

    private void loadTableData(List<PriceRequest> priceRequests) {
        listPriceRequest = FXCollections.observableArrayList(priceRequests);
        tblPriceRequest.setItems(listPriceRequest);
        tblPriceRequest.setFixedCellSize(30);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMovieName.setCellValueFactory(new PropertyValueFactory<>("englishName")); // משתמש ב-Getter של PriceRequest
        colStreamingType.setCellValueFactory(new PropertyValueFactory<>("streamingType")); // משתמש ב-Getter של PriceRequest
        colStatus.setCellValueFactory(new ButtonFactory.ButtonRequestCellValueFactory());
    }

    private void animateNodes() {
        Animations.fadeInUp(tblPriceRequest);
    }

    @FXML
    private void setActionToggleButton() {
        PriceRequest selectedPriceRequest = tblPriceRequest.getSelectionModel().getSelectedItem();
        if (selectedPriceRequest == null) {
            return; // handle null case
        }
        int oldPrice;
        if (selectedPriceRequest.getType() == Movie.StreamingType.THEATER_VIEWING) {
            oldPrice = selectedPriceRequest.getMovie().getTheaterPrice();
        } else {
            oldPrice = selectedPriceRequest.getMovie().getHomeViewingPrice();
        }

        String text = "Are you sure you want to Change Price of "
                + selectedPriceRequest.getMovie().getEnglishName() + " in " + selectedPriceRequest.getType() + " format" +
                " from: " + oldPrice +
                " to: " + selectedPriceRequest.getNewPrice();

        AlertsBuilder.create(AlertType.WARNING, stckPriceChange, stckPriceChange, stckPriceChange, text, "Yes", () -> {
            PriceRequestController.acceptPriceRequest(selectedPriceRequest.getId());
            AlertsBuilder.create(AlertType.SUCCESS, stckPriceChange, stckPriceChange, stckPriceChange, "You Approved Price Change Request!");
        }, "No");
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
}
