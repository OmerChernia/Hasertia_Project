package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.ComplaintController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieController;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ComplaintBoundary implements Initializable {

    @FXML
    private StackPane stckComplaint;

    @FXML
    private BorderPane rootComplaint;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerEmail;



    @FXML
    private TextArea txtComplaintDetails;

    @FXML
    private Button btnSubmitComplaint;

    @FXML
    private Label lblTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Register this controller to listen for MovieMessage events
        EventBus.getDefault().register(this);

          initializeComplaintForm();
    }

    private void initializeComplaintForm() {
        // Initialize form with default data if necessary
    }

    @FXML
    private void submitComplaint() {
        String customerName = txtCustomerName.getText().trim();
        String customerEmail = txtCustomerEmail.getText().trim();
         String complaintDetails = txtComplaintDetails.getText().trim();

        if (customerName.isEmpty() || customerEmail.isEmpty()  || complaintDetails.isEmpty()) {
            Animations.shake(txtCustomerName);
            Animations.shake(txtCustomerEmail);
             Animations.shake(txtComplaintDetails);
            NotificationsBuilder.create(NotificationType.ERROR, "Please fill in all required fields.");
            return;
        }

        // Code to submit the complaint to the system goes here
       // ComplaintController.addComplaint(String info, LocalDateTime creationDate, Purchase purchase, boolean isClosed, RegisteredUser registeredUser);
        AlertsBuilder.create(AlertType.SUCCESS, stckComplaint, rootComplaint, rootComplaint, "Complaint submitted successfully. A response will be sent to the customer's email within 24 hours.");
    }

    public void cleanup() {
        // Unregister this controller from EventBus when it's no longer needed
        EventBus.getDefault().unregister(this);
    }

}
