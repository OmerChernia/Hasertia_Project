package il.cshaifasweng.OCSFMediatorExample.client.dialog;

import il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer.OrdersController;
import il.cshaifasweng.OCSFMediatorExample.client.boundariesEmploee.CustomerServiceController;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DialogComplaintController {

    @FXML
    private TextField activationDateField;

    @FXML
    private TextArea additionalCommentsArea;

    @FXML
    private ComboBox<String> complaintReasonComboBox;

    @FXML
    private TextArea customerServiceResponseArea;

    @FXML
    private TextField entriesField;

    @FXML
    private VBox homeViewingBox;

    @FXML
    private VBox movieTicketBox;

    @FXML
    private TextField movieTitleField;

    @FXML
    private VBox multiEntryBox;

    @FXML
    private AnchorPane pnCustomerArea;

    @FXML
    private TextField purchaseDateField;

    @FXML
    private TextField purchaseTypeField;

    @FXML
    private TextField screeningTimeField;

    @FXML
    private TextField seatField;

    @FXML
    private TextField viewingMovieField;

    private OrdersController ordersController;
    public void initialize() {
            // Example: Initialize with a sample purchase type
            setPurchaseType("MovieTicket");
        }

        public void setPurchaseType(String purchaseType) {
            purchaseTypeField.setText(purchaseType);

            // Manage visibility based on purchase type
            boolean isMovieTicket = "MovieTicket".equals(purchaseType);
            boolean isHomeViewing = "HomeViewingPackageInstance".equals(purchaseType);
            boolean isMultiEntry = "MultiEntryTicket".equals(purchaseType);

            movieTicketBox.setVisible(isMovieTicket);
            movieTicketBox.setManaged(isMovieTicket);

            homeViewingBox.setVisible(isHomeViewing);
            homeViewingBox.setManaged(isHomeViewing);

            multiEntryBox.setVisible(isMultiEntry);
            multiEntryBox.setManaged(isMultiEntry);
        }

        @FXML
        public void handleSubmitComplaint() {
            // Collect and process the complaint data
            String complaintReason =  complaintReasonComboBox.getValue();
            String additionalComments = additionalCommentsArea.getText();

            // Example: Save or send the complaint here
            System.out.println("Complaint submitted with reason: " + complaintReason);
            System.out.println("Additional comments: " + additionalComments);

            // Example response from customer service
            customerServiceResponseArea.setText("Thank you for your complaint. We are processing it and will get back to you shortly.");
        }

        @FXML
        public void closeCustomerArea() {
            // Close or hide the customer area
            System.out.println("Customer area closed.");
        }

    public void setOrdersController(OrdersController ordersController) {
        this.ordersController = ordersController;

    }

    public void setPurchase(Purchase selectedPurchase) {


    }




}
