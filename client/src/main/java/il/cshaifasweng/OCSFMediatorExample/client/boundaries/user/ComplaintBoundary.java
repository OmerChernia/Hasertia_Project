package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.ComplaintController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.RegisteredUserController;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.ComplaintMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.RegisteredUserMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private Label emailHeader;

    @FXML
    private Label nameHeader;


    @FXML
    private Button btnSubmitComplaint;

    @FXML
    private Label lblTitle;

    private RegisteredUser user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComplaintForm();

        // Register this controller to listen for events
        EventBus.getDefault().register(this);
    }

    private void initializeComplaintForm() {
        if(!SimpleClient.user.isEmpty()) {
            txtCustomerName.setVisible(false);
            txtCustomerEmail.setVisible(false);
            nameHeader.setVisible(false);
            emailHeader.setVisible(false);
            RegisteredUserController.getUserByID(SimpleClient.user);
        }
    }

    @FXML
    private void submitComplaint() {
        String customerName="", customerEmail="",complaintDetails="";
        if(txtCustomerName.isVisible()) {
            customerName = txtCustomerName.getText().trim();
            customerEmail = txtCustomerEmail.getText().trim();
        }
        complaintDetails = txtComplaintDetails.getText().trim();

        if(!txtCustomerName.isVisible()) {
            if (complaintDetails.isEmpty() ) {
                Animations.shake(txtComplaintDetails);
                NotificationsBuilder.create(NotificationType.ERROR, "Please fill in all required fields.");
                return;
            }
            LocalDateTime creationDate = LocalDateTime.now();
            ComplaintController.addComplaint(complaintDetails, creationDate, null, false, user);
        }
        else {
            if (customerName.isEmpty() || customerEmail.isEmpty() || complaintDetails.isEmpty()) {
                if (customerName.isEmpty()) {
                    Animations.shake(txtCustomerName);
                }
                if (customerEmail.isEmpty()) {
                    Animations.shake(txtCustomerEmail);
                }
                if (complaintDetails.isEmpty()) {
                    Animations.shake(txtComplaintDetails);
                }
                NotificationsBuilder.create(NotificationType.ERROR, "Please fill in all required fields.");
                return;
            }


            // Code to submit the complaint to the system goes here
            LocalDateTime creationDate = LocalDateTime.now();
            ComplaintController.addComplaint(complaintDetails, creationDate, null, false, user);
        }
        AlertsBuilder.create(AlertType.SUCCESS, stckComplaint, rootComplaint, rootComplaint, "Complaint submitted successfully. A response will be sent to the customer's email within 24 hours.");
    }

    @Subscribe
    public void onComplaintMessageReceived(ComplaintMessage message) {
        // Handle the event, e.g., update the UI with complaint information
        System.out.println("Received complaint message: " + message);
    }
    @Subscribe
    public void onRegisteredUserMessageReceived(RegisteredUserMessage message) {
        user= message.registeredUser;
    }

    public void cleanup() {
        // Unregister this controller from EventBus when it's no longer needed
        EventBus.getDefault().unregister(this);
    }
}
