package il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PackagePurchaseController {
    @FXML
    private StackPane stackPane;

    @FXML
    private VBox ticketSelectionPane;

    @FXML
    private VBox paymentDetailsPane;

    @FXML
    private VBox creditCardPane;

    @FXML
    private VBox idPhonePane;

    @FXML
    private VBox ticketConfirmationPane;

    @FXML
    private ImageView movieImage;

    @FXML
    private ImageView confirmationMovieImage;

    @FXML
    private Label movieTitle;

    @FXML
    private Label movieTime;

    @FXML
    private Label movieHall;

    @FXML
    private Label movieLocation;

    @FXML
    private Label timerLabel;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expirationDateField;

    @FXML
    private TextField cvvField;

    @FXML
    private TextField idField;

    @FXML
    private TextField phoneField;

    @FXML
    private Label step2Label;

    @FXML
    private Label step2Text;

    @FXML
    private Label step3Label;

    @FXML
    private Label step3Text;

    @FXML
    private Label confirmationDetails;

    @FXML
    private Label pricePaidLabel;

    private Timeline timer;
    private int timeRemaining;
    private double packagePrice;

    @FXML
    public void initialize() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(ticketSelectionPane);
        highlightStep(1);
    }

    private void highlightStep(int step) {
        step2Label.setStyle("-fx-text-fill: white;");
        step2Text.setStyle("-fx-text-fill: white;");
        step3Label.setStyle("-fx-text-fill: white;");
        step3Text.setStyle("-fx-text-fill: white;");

        switch (step) {
            case 1:
                step2Label.setStyle("-fx-text-fill: yellow;");
                step2Text.setStyle("-fx-text-fill: yellow;");
                break;
            case 2:
                step3Label.setStyle("-fx-text-fill: yellow;");
                step3Text.setStyle("-fx-text-fill: yellow;");
                break;
        }
    }

    @FXML
    private void showIdPhoneFields() {
        idPhonePane.setVisible(true);
    }

    @FXML
    private void hideIdPhoneFields() {
        idPhonePane.setVisible(false);
    }

    @FXML
    private void goToPaymentDetails() {
        startTimer();
        stackPane.getChildren().clear();
        stackPane.getChildren().add(paymentDetailsPane);
        highlightStep(2);
    }

    @FXML
    private void goToTicketSelection() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(ticketSelectionPane);
        stopTimer();
        highlightStep(1);
    }

    @FXML
    private void purchaseTicket() {
        // Implement ticket purchasing logic
        packagePrice = 40.00; // Or set this value based on the selected package
        System.out.println("Package purchased successfully.");
        stopTimer();
        showConfirmation();
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }

        timeRemaining = 600; // 10 minutes in seconds
        timerLabel.setText(formatTime(timeRemaining));

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            timerLabel.setText(formatTime(timeRemaining));

            if (timeRemaining <= 0) {
                timer.stop();
                System.out.println("Time is up! Please select a package again.");
                goToTicketSelection();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    @FXML
    private void showCreditCardFields() {
        creditCardPane.setVisible(true);
    }

    @FXML
    private void submitPayment() {
        // Implement payment submission logic
        System.out.println("Payment submitted with card number: " + cardNumberField.getText());
        creditCardPane.setVisible(false);
        showConfirmation();
    }

    @FXML
    private void cancelPayment() {
        // Hide the credit card fields
        creditCardPane.setVisible(false);
    }

    @FXML
    private void goToNextStep() {
        if (stackPane.getChildren().contains(ticketSelectionPane)) {
            goToPaymentDetails();
        }
    }

    @FXML
    private void goToPreviousStep() {
        if (stackPane.getChildren().contains(paymentDetailsPane)) {
            goToTicketSelection();
        }
    }

    private void showConfirmation() {
        confirmationDetails.setText(
                "Movie: " + movieTitle.getText() + "\n" +
                        "Time: " + movieTime.getText() + "\n" +
                        "Hall: " + movieHall.getText() + "\n" +
                        "Theater: " + movieLocation.getText() + "\n" +
                        "Price Paid: ₪" + packagePrice
        );
        confirmationMovieImage.setImage(movieImage.getImage());
        stackPane.getChildren().clear();
        stackPane.getChildren().add(ticketConfirmationPane);
    }

    @FXML
    private void closeApplication() {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.close();
    }
}
