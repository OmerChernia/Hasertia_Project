package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.PurchaseController;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private TextField expirationDateField;

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
    private Label pricePaidLabel;

    @FXML
    private Spinner<Integer> ticketsSpinner; // רכיב לבחירת מספר כרטיסים

    private MovieInstance currentMovieInstance;
    private Seat selectedSeat;
    private Timeline timer;
    private int timeRemaining;
    private double ticketPrice;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(seatSelectionPane);
        highlightStep(1);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        ticketsSpinner.setValueFactory(valueFactory); // הגדרת טווח לבחירת מספר כרטיסים
    }

    public void setMovieInstance(MovieInstance movieInstance) {
        this.currentMovieInstance = movieInstance;
        updateMovieDetails();
        updateSeats();
    }

    private void updateMovieDetails() {
        movieTitle.setText(currentMovieInstance.getMovie().getHebrewName() + " | " + currentMovieInstance.getMovie().getEnglishName());
        movieDate.setText("Date: " + currentMovieInstance.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        movieTime.setText("Time: " + currentMovieInstance.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        movieHall.setText("Hall: " + currentMovieInstance.getHall().getId());
        movieLocation.setText("Theater: " + currentMovieInstance.getHall().getTheater().getLocation());
        movieImage.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MOVIE_PACKAGE + currentMovieInstance.getMovie().getImage())));
    }

    private void updateSeats() {
        seatGrid.getChildren().clear();
        List<Seat> seats = currentMovieInstance.getHall().getSeats();
        for (Seat seat : seats) {
            Button seatButton = new Button(String.valueOf(seat.getCol()));
            seatButton.setStyle("-fx-background-color: #4CAF50FF;");
            seatButton.setOnAction(event -> selectSeat(seat, seatButton));
            seatGrid.add(seatButton, seat.getCol(), seat.getRow());
        }
    }

    private void selectSeat(Seat seat, Button seatButton) {
        if (selectedSeat != null) {
            Button prevButton = (Button) seatGrid.lookup("#" + selectedSeat.getRow() + "-" + selectedSeat.getCol());
            prevButton.setStyle("-fx-background-color: #4CAF50FF;");
        }

        if (isSingleSeatLeft(seat)) { // בדיקה אם נשאר מושב בודד
            System.out.println("Cannot leave a single seat!");
            return;
        }

        selectedSeat = seat;
        seatButton.setStyle("-fx-background-color: #e72241;");
        resetTimer(); // איפוס הסטופר בבחירת כיסא אחר
    }

    private boolean isSingleSeatLeft(Seat seat) {
        // בדיקה אם בחירת הכיסא תשאיר מושב בודד
        // יש לממש לוגיקה מותאמת לפי הצורך
        return false;
    }

    private void resetTimer() {
        if (timer != null) {
            timer.stop();
        }
        startTimer();
    }

    @FXML
    private void goToTicketSelection() {
        if (selectedSeat != null) {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(ticketSelectionPane);
            highlightStep(2);
        } else {
            System.out.println("Please select a seat.");
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
        stackPane.getChildren().clear();
        stackPane.getChildren().add(paymentDetailsPane);
        highlightStep(3);
    }

    @FXML
    private void goToSeatSelection() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(seatSelectionPane);
        highlightStep(1);
    }

    @FXML
    private void goToNextStep() {
        if (stackPane.getChildren().contains(seatSelectionPane)) {
            goToTicketSelection();
        } else if (stackPane.getChildren().contains(ticketSelectionPane)) {
            goToPaymentDetails();
        }
    }

    @FXML
    private void goToPreviousStep() {
        if (stackPane.getChildren().contains(paymentDetailsPane)) {
            goToTicketSelection();
        } else if (stackPane.getChildren().contains(ticketSelectionPane)) {
            goToSeatSelection();
        }
    }

    @FXML
    private void purchaseTicket() {
        if (selectedSeat != null) {
            int ticketCount = ticketsSpinner.getValue();
            String purchaseValidation = generatePurchaseValidation();
            RegisteredUser loggedInUser = getLoggedInUser();
            PurchaseController.AddMovieTicket(loggedInUser.getId(), LocalDateTime.now(), loggedInUser, purchaseValidation, currentMovieInstance, selectedSeat);
            // יש לשנות את הבקשה כדי לטפל ברכישת מספר כרטיסים
        } else {
            System.out.println("Please select a seat.");
        }
    }

    @Subscribe
    public void onPurchaseMessageReceived(PurchaseMessage message) {
        Platform.runLater(() -> {
            if (message.responseType == PurchaseMessage.ResponseType.PURCHASE_ADDED) {
                System.out.println("Ticket purchased successfully!");
                stopTimer();
                showConfirmation();
            } else {
                System.out.println("Failed to purchase ticket.");
            }
        });
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
    private void showCreditCardFields() {
        creditCardPane.setVisible(true);
    }

    @FXML
    private void submitPayment() {
        // יש לממש את הלוגיקה של תשלום
        System.out.println("Payment submitted with card number: " + cardNumberField.getText());
        creditCardPane.setVisible(false);
        showConfirmation();
    }

    @FXML
    private void cancelPayment() {
        creditCardPane.setVisible(false);
    }

    private void highlightStep(int step) {
        step1Label.setStyle("-fx-text-fill: white;");
        step1Text.setStyle("-fx-text-fill: white;");
        step2Label.setStyle("-fx-text-fill: white;");
        step2Text.setStyle("-fx-text-fill: white;");
        step3Label.setStyle("-fx-text-fill: white;");
        step3Text.setStyle("-fx-text-fill: white;");

        switch (step) {
            case 1:
                step1Label.setStyle("-fx-text-fill: #ffc500;");
                step1Text.setStyle("-fx-text-fill: #ffc500;");
                break;
            case 2:
                step2Label.setStyle("-fx-text-fill: #ffc500;");
                step2Text.setStyle("-fx-text-fill: #ffc500;");
                break;
            case 3:
                step3Label.setStyle("-fx-text-fill: #ffc500;");
                step3Text.setStyle("-fx-text-fill: #ffc500;");
                break;
        }

        // הוספת זום לסטפ הנבחר
        Animations.hover(step1Label, 200, 1.2);
        Animations.hover(step2Label, 200, 1.2);
        Animations.hover(step3Label, 200, 1.2);
    }

    private void showConfirmation() {
        confirmationDetails.setText(
                "Movie: " + currentMovieInstance.getMovie().getHebrewName() + " | " + currentMovieInstance.getMovie().getEnglishName() + "\n" +
                        "Date: " + currentMovieInstance.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                        "Time: " + currentMovieInstance.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" +
                        "Hall: " + currentMovieInstance.getHall().getId() + "\n" +
                        "Theater: " + currentMovieInstance.getHall().getTheater().getLocation() + "\n" +
                        "Seat: Row " + selectedSeat.getRow() + ", Seat " + selectedSeat.getCol() + "\n" +
                        "Price Paid: ₪" + ticketPrice
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

    private String generatePurchaseValidation() {
        return "validation-code";
    }

    private RegisteredUser getLoggedInUser() {
        return new RegisteredUser(); // יש לשנות למימוש נכון שמחזיר את המשתמש המחובר
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
}
