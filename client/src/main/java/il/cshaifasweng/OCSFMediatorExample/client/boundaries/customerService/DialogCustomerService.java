package il.cshaifasweng.OCSFMediatorExample.client.boundaries.customerService;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.PurchaseController;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DialogCustomerService implements Initializable {

    @FXML
    private Label numTicketsLabel;

    @FXML
    private Label complaintDetailsLabel;

    @FXML
    private Label movieNameLabel;

    @FXML
    private Label viewingDateLabel;

    @FXML
    private Label complainantNameLabel;

    @FXML
    private ComboBox<String> actionComboBox;

    @FXML
    private VBox compensateFinanciallyBox;

    @FXML
    private ComboBox<String> employeeResponseComboBox;

    @FXML
    private TextFlow finalResponsePreviewArea;

    @FXML
    private ScrollPane finalResponseScrollPane;

    @FXML
    private AnchorPane pnEmployeeArea;



    @FXML
    private Label purchaseTypeField;

    @FXML
    private TextField refundAmountField;



    @FXML
    private Button btnSend;

    private CustomerServiceBoundary customerServiceController;

    private MovieTicket selectedMovieTicket;
    private MovieInstance selectedMovieInstance;
    private HomeViewingPackageInstance homeViewingPackageInstance;
    private Complaint myComplaint;
    private int price;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        compensateFinanciallyBox.setVisible(false);
        compensateFinanciallyBox.setManaged(false);

        // Listen for changes to update the final response preview
        refundAmountField.textProperty().addListener((observable, oldValue, newValue) -> updateFinalResponsePreview());
        employeeResponseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateFinalResponsePreview());
    }

    public void setCustomerServiceController(CustomerServiceBoundary customerServiceController) {
        this.customerServiceController = customerServiceController;
    }

    public void setComplaint(Complaint selectedComplaint) {
        this.myComplaint = selectedComplaint;

        // Populate labels with data
        complaintDetailsLabel.setText(selectedComplaint.getInfo());
        complainantNameLabel.setText(selectedComplaint.getRegisteredUser().getName());

        if (selectedComplaint.getPurchase() instanceof MovieTicket) {
            setMovieTicket(selectedComplaint);
        } else if (selectedComplaint.getPurchase() instanceof MultiEntryTicket) {
            setMultiEntryTicket(selectedComplaint);
        }
        else if (selectedComplaint.getPurchase() instanceof HomeViewingPackageInstance) {
            setHomeViewing(selectedComplaint);
        }
    }

    private void setMovieTicket(Complaint selectedComplaint) {
        this.selectedMovieInstance = ((MovieTicket) selectedComplaint.getPurchase()).getMovieInstance();
        this.selectedMovieTicket = (MovieTicket) selectedComplaint.getPurchase();
        this.price = selectedMovieTicket.getMovieInstance().getMovie().getTheaterPrice();

        movieNameLabel.setText(selectedMovieInstance.getMovie().getEnglishName());
        viewingDateLabel.setText(selectedMovieTicket.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        numTicketsLabel.setText("1");
        purchaseTypeField.setText("Movie Ticket");
    }

    private void setHomeViewing(Complaint selectedComplaint) {
        this.homeViewingPackageInstance = (HomeViewingPackageInstance) selectedComplaint.getPurchase();
        this.price = homeViewingPackageInstance.getMovie().getHomeViewingPrice();

        movieNameLabel.setText(homeViewingPackageInstance.getMovie().getEnglishName());
        viewingDateLabel.setText(homeViewingPackageInstance.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        numTicketsLabel.setText("1");
        purchaseTypeField.setText("Home Viewing Ticket");
    }


    private void setMultiEntryTicket(Complaint selectedComplaint) {
        purchaseTypeField.setText("Multi-Entry Ticket");
        numTicketsLabel.setText(String.valueOf(selectedComplaint.getPurchase().getOwner().getTicket_counter()));
    }

    @FXML
    public void handleActionSelection() {
        String selectedAction = actionComboBox.getValue();

        compensateFinanciallyBox.setVisible(false);
        compensateFinanciallyBox.setManaged(false);

        if (selectedAction.equals("Compensate Financially") || selectedAction.equals("Ticket refund")) {
            compensateFinanciallyBox.setVisible(true);
            compensateFinanciallyBox.setManaged(true);
        }

        updateFinalResponsePreview();
    }

    @FXML
    public void handleSubmitFinalResponse() {
        String finalResponseText = getFinalResponseText();

        if (finalResponseText.contains("Ticket refund")) {
            PurchaseController.RemovePurchase(this.myComplaint.getPurchase());
        }
        EmailSender.sendEmail(myComplaint.getPurchase().getOwner().getEmail(), "Customer Service: Complain num"+ myComplaint.getId() , finalResponseText);
        customerServiceController.closeDialogAddQuotes();

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

        if (employeeResponseComboBox.getValue() != null && !employeeResponseComboBox.getValue().isEmpty()) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("Employee Response: " + employeeResponseComboBox.getValue() + "\n"));
        }
        String selectedAction = actionComboBox.getValue();
        if (selectedAction.equals("Compensate Financially")) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("\nAmount: " + refundAmountField.getText() + "₪\n"));
        }
        if (selectedAction.equals("Ticket refund")) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("\nAmount: " + refundAmountField.getText() + "₪\n"));
        }
    }

    @FXML
    public void closeEmployeeArea(ActionEvent actionEvent) {
        customerServiceController.closeDialogAddQuotes();
    }


}
