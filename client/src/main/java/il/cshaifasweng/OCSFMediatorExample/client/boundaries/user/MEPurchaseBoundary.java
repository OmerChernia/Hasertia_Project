package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MEPurchaseBoundary {

    @FXML
    private StackPane stackPane;

    @FXML
    private VBox packageSelectionPane;

    @FXML
    private VBox paymentDetailsPane;

    @FXML
    private VBox creditCardPane;

    @FXML
    private VBox packageConfirmationPane;

    @FXML
    private ImageView confirmationPackageImage;

    @FXML
    private Label packageName;

    private double packagePrice;

    @FXML
    private Label packageType;

    @FXML
    private Label packageQuantity;

    @FXML
    private Label timerLabel;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expirationDateField;

    @FXML
    private TextField cvvField;

    @FXML
    private Label step1Label;

    @FXML
    private Label step1Text;

    @FXML
    private Label step2Label;

    @FXML
    private Label step2Text;

    @FXML
    private Label confirmationDetails;

    @FXML
    private Label pricePaidLabel;

    private Timeline timer;
    private int timeRemaining;

    @FXML
    public void initialize() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(packageSelectionPane);
        highlightStep(1);
    }

    private void highlightStep(int step) {
        step1Label.setStyle("-fx-text-fill: white;");
        step1Text.setStyle("-fx-text-fill: white;");
        step2Label.setStyle("-fx-text-fill: white;");
        step2Text.setStyle("-fx-text-fill: white;");

        switch (step) {
            case 1:
                step1Label.setStyle("-fx-text-fill: yellow;");
                step1Text.setStyle("-fx-text-fill: yellow;");
                break;
            case 2:
                step2Label.setStyle("-fx-text-fill: yellow;");
                step2Text.setStyle("-fx-text-fill: yellow;");
                break;
        }
    }

    @FXML
    private void goToPaymentDetails() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(paymentDetailsPane);
        highlightStep(2);
    }

    @FXML
    private void goToPackageSelection() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(packageSelectionPane);
        highlightStep(1);
    }

    @FXML
    private void purchasePackage() {
        packagePrice = Double.parseDouble(String.valueOf(packagePrice));
        System.out.println("Package purchased successfully.");
        showConfirmation();
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
        System.out.println("Payment submitted with card number: " + cardNumberField.getText());
        creditCardPane.setVisible(false);
        showConfirmation();
    }

    @FXML
    private void cancelPayment() {
        creditCardPane.setVisible(false);
    }

    @FXML
    private void goToNextStep() {
        if (stackPane.getChildren().contains(packageSelectionPane)) {
            goToPaymentDetails();
        }
    }

    @FXML
    private void goToPreviousStep() {
        if (stackPane.getChildren().contains(paymentDetailsPane)) {
            goToPackageSelection();
        }
    }

    private void showConfirmation() {
        confirmationDetails.setText(
                "Package: " +"10 tickets" + "\n" +
                        "Price Paid: ₪" + packagePrice
        );
        confirmationPackageImage.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.ICON_PACKAGE+"logo.png")));
        stackPane.getChildren().clear();
        stackPane.getChildren().add(packageConfirmationPane);
    }

    @FXML
    private void closeApplication() {

    }

}
