package il.cshaifasweng.OCSFMediatorExample.client.boundaries.customerService;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieInstanceController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.PurchaseController;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DialogCustomerService {

    @FXML
    private ComboBox<String> actionComboBox;

    @FXML
    private VBox compensateFinanciallyBox;

    @FXML
    private ComboBox<String> dateComboBox;

    @FXML
    private ComboBox<String> employeeResponseComboBox;

    @FXML
    private TextFlow finalResponsePreviewArea;

    @FXML
    private ScrollPane finalResponseScrollPane;

    @FXML
    private VBox issueNewTicketBox;

    @FXML
    private ComboBox<String> movieComboBox;

    @FXML
    private AnchorPane pnEmployeeArea;

    @FXML
    private TextField purchaseDateField;

    @FXML
    private Label purchaseTypeField;

    @FXML
    private TextField refundAmountField;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private Button btnSend;

    private CustomerServiceBoundary customerServiceController;

    private MovieTicket selectedMovieTicket;
    private MovieInstance selectedMovieInstance;
    private HomeViewingPackageInstance selectedHomeViewingPackageInstance;
    private MultiEntryTicket multiEntryTicket;
    private Complaint myComplaint;

    public void initialize() {
        compensateFinanciallyBox.setVisible(false);
        compensateFinanciallyBox.setManaged(false);
        issueNewTicketBox.setVisible(false);
        issueNewTicketBox.setManaged(false);

       // EventBus.getDefault().register(this);
        // Listen for changes to update the final response preview
        refundAmountField.textProperty().addListener((observable, oldValue, newValue) -> updateFinalResponsePreview());
        movieComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateFinalResponsePreview());
        employeeResponseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateFinalResponsePreview());
    }

    public void setCustomerServiceController(CustomerServiceBoundary customerServiceController) {
        this.customerServiceController = customerServiceController;
    }



    public void setComplaint(Complaint selectedComplaint) {
        this.myComplaint = selectedComplaint;
        if (selectedComplaint.getPurchase() instanceof MovieTicket) {
            setMovieTicket(selectedComplaint);
        } else if (selectedComplaint.getPurchase() instanceof HomeViewingPackageInstance) {
            setHomeViewingTicket(selectedComplaint);
        } else if (selectedComplaint.getPurchase() instanceof MultiEntryTicket) {
            setMultiEntryTicket(selectedComplaint);
        }
    }

    private void setMovieTicket(Complaint selectedComplaint) {
        this.selectedMovieInstance = ((MovieTicket) selectedComplaint.getPurchase()).getMovieInstance();
        this.selectedMovieTicket = (MovieTicket) selectedComplaint.getPurchase();
        purchaseTypeField.setText("Movie Ticket");
        purchaseDateField.setText(selectedMovieTicket.getPurchaseDate().toString());
    }

    private void setHomeViewingTicket(Complaint selectedComplaint) {
        HomeViewingPackageInstance viewingPackage = (HomeViewingPackageInstance) selectedComplaint.getPurchase();
        purchaseTypeField.setText("Home Viewing Package");
        purchaseDateField.setText(viewingPackage.getPurchaseDate().toString());
    }

    private void setMultiEntryTicket(Complaint selectedComplaint) {
        MultiEntryTicket multiEntryTicket = (MultiEntryTicket) selectedComplaint.getPurchase();
        purchaseTypeField.setText("Multi-Entry Ticket");
        purchaseDateField.setText(multiEntryTicket.getPurchaseDate().toString());
    }

    @FXML
    public void handleActionSelection() {
        String selectedAction = actionComboBox.getValue();

        compensateFinanciallyBox.setVisible(false);
        compensateFinanciallyBox.setManaged(false);
        issueNewTicketBox.setVisible(false);
        issueNewTicketBox.setManaged(false);

        if (selectedAction.equals("Compensate Financially")) {
            compensateFinanciallyBox.setVisible(true);
            compensateFinanciallyBox.setManaged(true);
        } else if (selectedAction.equals("Issue New Ticket")) {
            issueNewTicketBox.setVisible(true);
            issueNewTicketBox.setManaged(true);

        }

        updateFinalResponsePreview();
    }


    @FXML
    public void handleSubmitFinalResponse() {
        String finalResponseText = getFinalResponseText();

        if (finalResponseText.contains("Ticket has been refunded")) {

            PurchaseController.RemovePurchase(this.myComplaint.getPurchase());
        }

    }


    private String getFinalResponseText() {
        StringBuilder finalResponse = new StringBuilder();
        finalResponsePreviewArea.getChildren().forEach(node -> {
            if (node instanceof javafx.scene.text.Text) {
                finalResponse.append(((javafx.scene.text.Text) node).getText());
            }
        });
        return finalResponse.toString();
    }


    private void updateFinalResponsePreview() {
        finalResponsePreviewArea.getChildren().clear();

        String selectedAction = actionComboBox.getValue();
        if (selectedAction.equals("Compensate Financially")) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("Ticket has been refunded.\nAmount: " + refundAmountField.getText() + "₪\n"));
        } else if (selectedAction.equals("Issue New Ticket")) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("New ticket has been issued for: " + movieComboBox.getValue() + "\n"));
        }

        if (employeeResponseComboBox.getValue() != null && !employeeResponseComboBox.getValue().isEmpty()) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("Employee Response: " + employeeResponseComboBox.getValue() + "\n"));
        }
    }

    @FXML
    public void closeEmployeeArea(ActionEvent actionEvent) {
        customerServiceController.closeDialogAddQuotes();
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
}