package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.PurchaseController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.RegisteredUserController;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.RegisteredUserMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MultiEntryTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.regex.Pattern;

public class MEPurchaseBoundary {

    @FXML
    private BorderPane ME_pane;

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

    // credit card details start
    @FXML
    private TextField cardNumberField;

    @FXML
    private ComboBox<String> expirationMonthCombo;

    @FXML
    private ComboBox<String> expirationYearCombo;

    @FXML
    private TextField cvvField;
    // credit card details end

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

    //the user details
    @FXML
    private TextField firstNameTF;

    @FXML
    private TextField lastNameTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField confirmEmailTF;

    @FXML
    private TextField idNumberTF;

    @FXML
    private TextField confirmIdNumberTF;
    //end of user details

    @FXML
    private ComboBox<Integer>quantityCB;
    private int quantity;

    private RegisteredUser user=null;
    private MultiEntryTicket ticket;


    // Regular expression for validating an email address
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);

        stackPane.getChildren().clear();
        stackPane.getChildren().add(packageSelectionPane);
        highlightStep(1);

        if(!SimpleClient.user.isEmpty())
            RegisteredUserController.getUserByID(SimpleClient.user);

        populateExpirationMonths();
        populateExpirationYears();
    }

    private void populateExpirationMonths() {
        for (int i = 1; i <= 12; i++) {
            expirationMonthCombo.getItems().add(String.format("%02d", i));
        }
    }

    private void populateExpirationYears() {
        int currentYear = Year.now().getValue();
        for (int i = 0; i < 10; i++) {
            expirationYearCombo.getItems().add(String.valueOf(currentYear + i));
        }
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
        //quantity = (int)quantityCB.getValue();
        stackPane.getChildren().clear();
        initializePaymentDetails();
        highlightStep(2);

        if(user == null)
            stackPane.getChildren().add(paymentDetailsPane);
        else
        {
            stackPane.getChildren().add(creditCardPane);
            creditCardPane.setVisible(true);
        }

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


    @FXML
    private void showCreditCardFields()
    {
        if (checkDetails())
            creditCardPane.setVisible(true);
    }

    @FXML
    private void submitPayment() {

        // check if card info is in the right format
        if(isValidCreditCardInfo())
        {
            System.out.println("Payment submitted with card number: " + cardNumberField.getText());
            creditCardPane.setVisible(false);

            if (user == null)
                RegisteredUserController.addNewUser(idNumberTF.getText(), firstNameTF.getText(), lastNameTF.getText(), emailTF.getText());
            else {
                RegisteredUserController.addNewUser(user.getId_number(), "", "", "");
            }
        }
    }

    @FXML
    private void cancelPayment()
    {
        cardNumberField.clear();
        cvvField.clear();

        if(user == null) {
            // Hide the credit card fields
            creditCardPane.setVisible(false);
        }
        else
            goToPackageSelection();
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
        Platform.runLater(() -> {
            String text =  "Package: " + "20 tickets" + "\n" +
                    "Price Paid: 290₪";
            confirmationDetails.setText(text);
            confirmationPackageImage.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.ICON_PACKAGE + "logo.png")));
            stackPane.getChildren().clear();
            stackPane.getChildren().add(packageConfirmationPane);
            EmailSender.sendEmail(ticket.getOwner().getEmail(), "New Multi-Entry Ticket Purchase From Hasertia", text);
            EmailSender.sendEmail("hasertiaproject@gmail.com", "New Multi-Entry Ticket Purchase From Hasertia", text);
        });
    }

    @FXML
    private void closeApplication()
    {
        //EventBus.getDefault().unregister(this);
    }
    public void cleanup() {
        System.out.println("cleanup");
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onRegisteredUserReceivedMessage(RegisteredUserMessage message)
    {
        if(message.requestType == RegisteredUserMessage.RequestType.GET_USER_BY_ID)
        {
            user = message.registeredUser;
        }
        else {
            String purchaseValidation = cardNumberField.getText() + " " + expirationMonthCombo.getValue()+"/"+expirationYearCombo.getValue() + " " + cvvField.getText();
            PurchaseController.AddMultiEntryTicket(LocalDateTime.now(), message.registeredUser, purchaseValidation);
        }
    }

    @Subscribe
    public void onPurchaseReceivedMessage(PurchaseMessage message)
    {
        if (message.responseType == PurchaseMessage.ResponseType.PURCHASE_ADDED)
        {
            ticket= (MultiEntryTicket) message.purchases.get(0);
            showConfirmation();
        }
    }


    // a method that checks if the details that the user given is valid , returns true if the details are valid
    private boolean checkDetails()
    {

        if(firstNameTF.getText().isEmpty() || lastNameTF.getText().isEmpty() || emailTF.getText().isEmpty()
                || confirmEmailTF.getText().isEmpty() || idNumberTF.getText().isEmpty() || confirmIdNumberTF.getText().isEmpty())
        {
            NotificationsBuilder.create(NotificationType.ERROR,"One or more fields are missing.");
            return false;
        }

        String textFirstName = firstNameTF.getText();
        for (char c : textFirstName.toCharArray()) {
            if (Character.isDigit(c))
            {
                NotificationsBuilder.create(NotificationType.ERROR,"First name contains digits.");
                return false;
            }
        }

        String textLastName = lastNameTF.getText();
        for (char c : textLastName.toCharArray()) {
            if (Character.isDigit(c))
            {
                NotificationsBuilder.create(NotificationType.ERROR,"Last name contains digits.");
                return false;
            }
        }

        String textID = idNumberTF.getText();
        for (char c : textID.toCharArray()) {
            if (!Character.isDigit(c))
            {
                NotificationsBuilder.create(NotificationType.ERROR,"ID number contains digits.");
                return false;
            }
        }

        if(!idNumberTF.getText().equals(confirmIdNumberTF.getText()))
        {
            NotificationsBuilder.create(NotificationType.ERROR,"ID Number and confirm ID Number do not match!");
            return false;
        }

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (!pattern.matcher(emailTF.getText()).matches())
        {
            NotificationsBuilder.create(NotificationType.ERROR,"Email address is invalid.");
            return false;
        }


        if(!emailTF.getText().equals(confirmEmailTF.getText()))
        {
            NotificationsBuilder.create(NotificationType.ERROR,"Email and confirm email do not match!");
            return false;
        }
        return true;
    }

    private void initializePaymentDetails()
    {
        firstNameTF.clear();
        lastNameTF.clear();
        emailTF.clear();
        confirmEmailTF.clear();
        idNumberTF.clear();
        confirmIdNumberTF.clear();

        cardNumberField.clear();
        cvvField.clear();
        creditCardPane.setVisible(false);
    }

    // a method that checks if the details that the user given is valid , returns true if the details are valid
    public boolean isValidCreditCardInfo() {
        String cardNumber = cardNumberField.getText().replaceAll("\\s", "");
        String expirationMonth = expirationMonthCombo.getValue();
        String expirationYear = expirationYearCombo.getValue();
        String cvv = cvvField.getText();

        // Check if all fields are filled
        if (cardNumber.isEmpty() || expirationMonth == null || expirationYear == null || cvv.isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR,"All fields must be filled");
            return false;
        }

        // Validate card number (using Luhn algorithm)
        if (!isValidCardNumber(cardNumber)) {
            NotificationsBuilder.create(NotificationType.ERROR,"Invalid card number");
            return false;
        }

        // Validate expiration date
        if (!isValidExpirationDate(expirationMonth, expirationYear)) {
            NotificationsBuilder.create(NotificationType.ERROR,"Invalid expiration date");
            return false;
        }

        // Validate CVV
        if (!isValidCVV(cvv)) {
            NotificationsBuilder.create(NotificationType.ERROR,"Invalid CVV");
            return false;
        }

        return true;
    }
    private boolean isValidCardNumber(String cardNumber) {
        // Remove any spaces from the card number
        cardNumber = cardNumber.replaceAll("\\s", "");

        // Check if the card number contains only digits and hyphens
        if (!cardNumber.matches("[0-9-]+")) {
            return false;
        }

        // Remove hyphens for further processing
        String digitsOnly = cardNumber.replaceAll("-", "");

        // Check if there are exactly 16 digits
        if (digitsOnly.length() != 16) {
            return false;
        }

        // Check if the digits are in groups of 4, separated by hyphens (if hyphens are present)
        if (cardNumber.contains("-")) {
            String pattern = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";
            return cardNumber.matches(pattern);
        }

        // If no hyphens, it's valid as long as it's 16 digits
        return true;
    }

    private boolean isValidExpirationDate(String month, String year) {
        YearMonth expirationDate = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
        YearMonth currentDate = YearMonth.now();
        return expirationDate.isAfter(currentDate);
    }

    private boolean isValidCVV(String cvv) {
        // Check if the cvv number contains only digits
        if (!cvv.matches("[0-9]+")) {
            return false;
        }
        return cvv.matches("\\d{3,4}");
    }

}
