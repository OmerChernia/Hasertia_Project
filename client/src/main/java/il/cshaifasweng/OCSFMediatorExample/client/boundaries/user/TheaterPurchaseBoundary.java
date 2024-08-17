package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleChatClient;
import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.*;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;
import il.cshaifasweng.OCSFMediatorExample.server.events.MovieInstanceCanceledEvent;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TheaterPurchaseBoundary {
    @FXML
    private StackPane stackPane;

    @FXML
    private VBox seatSelectionPane;

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
    private Label movieDate;

    @FXML
    private Label movieHall;

    @FXML
    private Label movieLocation;

    @FXML
    private GridPane seatGrid;

    @FXML
    private Label timerLabel;

    @FXML
    private TextField cardNumberField;


    @FXML
    private TextField cvvField;

    @FXML
    private TextField idField;

    @FXML
    private TextField phoneField;

    @FXML
    private Label step1Label;

    @FXML
    private Label step1Text;

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
    private ComboBox<String> expirationMonthCombo;

    @FXML
    private ComboBox<String> expirationYearCombo;

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

    // Regular expression for validating an email address
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @FXML
    private Spinner<Integer> ticketsSpinner; // רכיב לבחירת מספר כרטיסים

    @FXML
    private Button cardPackageBTN;


    private MovieInstance currentMovieInstance;

    private List<Seat> selectedSeats;

    private Timeline timer;
    private int timeRemaining;
    private int numberOfTickets;

    private RegisteredUser user=null;
    private boolean isCardPackageOn;
    private boolean isReserved;
    private Hall hall;;

    @FXML
    public void initialize()
    {
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        stackPane.getChildren().clear();
        stackPane.getChildren().add(ticketSelectionPane);
        highlightStep(1);

        isCardPackageOn= false;
        isReserved = false;

        if(!SimpleClient.user.isEmpty())
            RegisteredUserController.getUserByID(SimpleClient.user);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        ticketsSpinner.setValueFactory(valueFactory); // הגדרת טווח לבחירת מספר כרטיסים

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

    public void setMovieInstance(MovieInstance movieInstance) {
        this.currentMovieInstance = movieInstance;
        selectedSeats = new ArrayList<>();
        updateMovieDetails();
        //updateSeats();
    }

    private void updateMovieDetails() {
        movieTitle.setText(currentMovieInstance.getMovie().getHebrewName() + " | " + currentMovieInstance.getMovie().getEnglishName());
        movieDate.setText("Date: " + currentMovieInstance.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        movieTime.setText("Time: " + currentMovieInstance.getTime().minusHours(3).format(DateTimeFormatter.ofPattern("HH:mm")));
        movieHall.setText("Hall: " + currentMovieInstance.getHall().getId());
        movieLocation.setText("Theater: " + currentMovieInstance.getHall().getTheater().getLocation());
        movieImage.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MOVIE_PACKAGE + currentMovieInstance.getMovie().getImage())));
    }

    //send a message to get an updated movie instance
    private void updateSeats()
    {
        HallController.requestHallByID(currentMovieInstance.getHall().getId());
    }

    @Subscribe
    public void onHallMessage(HallMessage hallMessage) {
        hall = hallMessage.halls.get(0);
        Platform.runLater(this::updateSeatsGrid);
    }


    private void updateSeatsGrid() {
        seatGrid.getChildren().clear();
        List<Seat> seats = hall.getSeats();
        for (Seat seat : seats)
        {
            Button seatButton = new Button(String.valueOf(seat.getCol()));
            seatButton.setId(seat.getId() + ""); // Set a unique ID for each button
            if(seat.getMoviesIds().contains(currentMovieInstance.getId()))
            {
                seatButton.setStyle("-fx-background-color:  #838f97;");
                seatButton.setDisable(true);
            }
            else
                seatButton.setStyle("-fx-background-color: #4CAF50FF;");

            seatButton.setOnAction(event -> selectSeat(seat, seatButton));
            seatGrid.add(seatButton, seat.getCol(), seat.getRow());
        }
    }

    //need to finish!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Subscribe
    public void onMovieInstanceEvent(MovieInstanceCanceledEvent movieInstanceCanceledEvent)
    {
        if(movieInstanceCanceledEvent.movieInstance.getId()==currentMovieInstance.getId())
        {
            AlertsBuilder.create(AlertType.CANCELLATION, stackPane, stackPane, stackPane, "The selected screening has been canceled/modified, Please choose a new screening");
            Platform.runLater(() -> {
                SimpleChatClient.mainBoundary.homeWindowsInitialize();
            });


        }
    }

    private void selectSeat(Seat seat, Button seatButton)
    {
        if(selectedSeats.contains(seat))
        {
            Button prevButton = (Button) seatGrid.lookup("#" + seat.getId());
            prevButton.setStyle("-fx-background-color: #4CAF50FF;");
            numberOfTickets++;
            selectedSeats.remove(seat);
        }
        else
        {
            if (numberOfTickets > 0) {
                selectedSeats.add(seat);
                numberOfTickets--;
                seatButton.setStyle("-fx-background-color: #e72241;");
            }
            else
                NotificationsBuilder.create(NotificationType.ERROR, "You can't select more seats!.");
        }

    }


    @FXML
    private void goToTicketSelection() {

        stackPane.getChildren().clear();
        stackPane.getChildren().add(ticketSelectionPane);
        highlightStep(1);
        initializeSeatSelection();
    }

    private void initializeSeatSelection()
    {
        stopTimer();
        updateSeats();
        selectedSeats.clear();
        numberOfTickets = ticketsSpinner.getValue();
    }

    @FXML
    private void goToPaymentDetails()
    {
        if(numberOfTickets > 0)
        {
            NotificationsBuilder.create(NotificationType.ERROR,"You didn't selected all the seats you asked.");
        }
        else
        {
            System.out.println(currentMovieInstance.getId());
            SeatController.reserveSeats(selectedSeats, currentMovieInstance);
        }
    }

    @Subscribe
    public void onSeatMessageReceived(SeatMessage message)
    {
        Platform.runLater(() -> {
            System.out.println(message.responseType);

            if (message.responseType.equals(SeatMessage.ResponseType.SEATS_IS_ALREADY_TAKEN)) {
                NotificationsBuilder.create(NotificationType.ERROR, "Somebody ordered those tickets now, try again.");
                initializeSeatSelection();
            }

            if (message.responseType.equals(SeatMessage.ResponseType.SEATS_WAS_RESERVED)) {

                isReserved = true; // flag is now says the seats been reserved

                stackPane.getChildren().clear();
                highlightStep(3);

                if(isCardPackageOn)
                {
                    String purchaseValidation = "Card Package";
                    for (Seat seat : selectedSeats) {
                        PurchaseController.AddMovieTicket(LocalDateTime.now(), user, purchaseValidation, currentMovieInstance, seat);
                    }
                    RegisteredUserController.lowerCardPackageOfUser(user.getId_number(),selectedSeats.size());
                    showConfirmation();
                }
                else {

                    startTimer();  //start the timer

                    if (user != null)
                        showCreditCardFields();
                    else
                        stackPane.getChildren().add(paymentDetailsPane);
                }
            }

            if(message.responseType.equals(SeatMessage.ResponseType.SEATS_HAS_BEEN_CANCELED))
            {
                isReserved = false;
                stackPane.getChildren().clear();
                stackPane.getChildren().add(seatSelectionPane);
                highlightStep(2);
                stopTimer();  //stop the timer
            }
        });
    }

    @FXML
    private void goToSeatSelection()
    {
        if (stackPane.getChildren().contains(paymentDetailsPane))
        {
            SeatController.cancelSeatReservation(selectedSeats, currentMovieInstance);
        }
        else
        {
            updateSeats();
            numberOfTickets = ticketsSpinner.getValue();
            stackPane.getChildren().clear();
            stackPane.getChildren().add(seatSelectionPane);
            highlightStep(2);
        }
    }

    @FXML
    private void goToSeatSelectionWithCardPackage()
    {
        if(ticketsSpinner.getValue() > user.getTicket_counter())
        {
            NotificationsBuilder.create(NotificationType.ERROR, "You can't order more tickets then what you have in card package.");
        }
        else
        {
            updateSeats();
            isCardPackageOn = true;
            numberOfTickets = ticketsSpinner.getValue();
            stackPane.getChildren().clear();
            stackPane.getChildren().add(seatSelectionPane);
            highlightStep(2);
        }
    }


    @Subscribe
    public void onPurchaseMessageReceived(PurchaseMessage message) {
        System.out.println(message.responseType);

        Platform.runLater(() -> {
            if (message.responseType == PurchaseMessage.ResponseType.PURCHASE_ADDED) {
                System.out.println("Ticket purchased successfully!");
            } else {
                System.out.println("Failed to purchase ticket.");
            }
        });
    }

    @Subscribe
    public void onRegisteredUserReceivedMessage(RegisteredUserMessage message)
    {
        if(message.requestType == RegisteredUserMessage.RequestType.GET_USER_BY_ID)
        {
            user = message.registeredUser;
            if(user != null) {
                cardPackageBTN.setVisible(true);
                cardPackageBTN.setText("Card Package\nNumber of remaining tickets: " + user.getTicket_counter());
            }
        }
        else if (message.requestType == RegisteredUserMessage.RequestType.ADD_NEW_USER){
            Platform.runLater(() -> {
                String purchaseValidation = cardNumberField.getText() + " " + expirationMonthCombo.getValue() + "/" + expirationYearCombo.getValue() + " " + cvvField.getText();
                for (Seat seat : selectedSeats) {
                    PurchaseController.AddMovieTicket(LocalDateTime.now(), message.registeredUser, purchaseValidation, currentMovieInstance, seat);
                }
                stopTimer();
                showConfirmation();
            });
        }
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }

        timeRemaining = 600; // 10 דקות בשניות
        timerLabel.setText(formatTime(timeRemaining));

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            timerLabel.setText(formatTime(timeRemaining));

            if (timeRemaining <= 0) {
                timer.stop();
                NotificationsBuilder.create(NotificationType.ERROR, "Timer has passed!, seats canceled, order again.");
                System.out.println("Time is up! Please select a seat again.");
                goToSeatSelection();
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
    private void submitPayment() {

        if(isValidCreditCardInfo())
        {
            System.out.println("Payment submitted with card number: " + cardNumberField.getText());
            creditCardPane.setVisible(false);
            stopTimer(); // stop the time

            if (user == null)
                RegisteredUserController.addNewUser(idNumberTF.getText(), firstNameTF.getText(), lastNameTF.getText(), emailTF.getText());
            else {
                RegisteredUserController.addNewUser(user.getId_number(), "", "", "");
            }
        }
    }

    @FXML
    private void showCreditCardFields()
    {
        if(user != null)
        {
            stackPane.getChildren().add(creditCardPane);
            creditCardPane.setVisible(true);
        }
        else if (checkDetails())
            creditCardPane.setVisible(true);
    }


    @FXML
    private void cancelPayment() {
        cardNumberField.clear();
        cvvField.clear();

        if(user == null)
            creditCardPane.setVisible(false);
        else
        {
            SeatController.cancelSeatReservation(selectedSeats, currentMovieInstance);
        }
    }

    private void highlightStep(int step) {
        step1Label.setStyle("-fx-text-fill: white;");
        step1Text.setStyle("-fx-text-fill: white;");
        step2Label.setStyle("-fx-text-fill: white;");
        step2Text.setStyle("-fx-text-fill: white;");
        step3Label.setStyle("-fx-text-fill: white;");
        step3Text.setStyle("-fx-text-fill: white;");
        step1Label.setStyle("-fx-background-color: #1d1d48");
        step2Label.setStyle("-fx-background-color: #1d1d48");
        step3Label.setStyle("-fx-background-color: #1d1d48");

        switch (step) {
            case 1:
                step1Label.setStyle("-fx-text-fill: #ffc500;");
                step1Text.setStyle("-fx-text-fill: #ffc500;");
                step1Label.setStyle("-fx-background-color: #ffc500");

                break;
            case 2:
                step2Label.setStyle("-fx-text-fill: #ffc500;");
                step2Text.setStyle("-fx-text-fill: #ffc500;");
                step2Label.setStyle("-fx-background-color: #ffc500");

                break;
            case 3:
                step3Label.setStyle("-fx-text-fill: #ffc500;");
                step3Text.setStyle("-fx-text-fill: #ffc500;");
                step3Label.setStyle("-fx-background-color: #ffc500");

                break;
        }

        // הוספת זום לסטפ הנבחר
        Animations.hover(step1Label, 200, 1.2);
        Animations.hover(step2Label, 200, 1.2);
        Animations.hover(step3Label, 200, 1.2);
    }

    private void showConfirmation() {
        isReserved = false;
        StringBuilder seats_info= new StringBuilder("Seats:\n");
        for(Seat seat : selectedSeats)
        {
            seats_info.append("Row: ").append(seat.getRow()).append(", Col: ").append(seat.getCol()).append("\n");
        }
        String text = "Movie: " + currentMovieInstance.getMovie().getHebrewName() + " | " + currentMovieInstance.getMovie().getEnglishName() + "\n" +
                "Date: " + currentMovieInstance.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                "Time: " + currentMovieInstance.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" +
                "Hall: " + currentMovieInstance.getHall().getId() + "\n" +
                "Theater: " + currentMovieInstance.getHall().getTheater().getLocation() + "\n" +
                seats_info;
        if(!isCardPackageOn)
                text += "Price Paid: ₪" + currentMovieInstance.getMovie().getTheaterPrice() *selectedSeats.size();
        String Email;
        if(user!=null)
            Email= user.getEmail();
        else
            Email = emailTF.getText();
        EmailSender.sendEmail(Email, "New Movie Ticket Purchase From Hasertia", text);
        EmailSender.sendEmail("hasertiaproject@gmail.com", "New Movie Ticket Purchase From Hasertia", text);
        confirmationDetails.setText(text);
        confirmationMovieImage.setImage(movieImage.getImage());
        stackPane.getChildren().clear();
        stackPane.getChildren().add(ticketConfirmationPane);
    }

    @FXML
    private void closeApplication() {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.close();
    }

    public void cleanup()
    {
        EventBus.getDefault().unregister(this);
        System.out.println("cleanup: is reserved-> "+isReserved);
        if (isReserved)
        {
            stopTimer();
            SeatController.cancelSeatReservation(selectedSeats, currentMovieInstance);
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
