package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.PurchaseController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.SeatController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.TheaterController;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class HomeViewingPurchaseBoundary {
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

    private LocalDateTime dateTime;
    private Timeline timer;
    private int timeRemaining;
    private double packagePrice;
    private RegisteredUser currentUser;
    private Movie currentMovie;
    private MovieInstance currentMovieInstance;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
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
        LocalDateTime purchaseDate = LocalDateTime.now();
        String purchaseValidation = "Validated";
        PurchaseController.AddHomeViewing(currentUser.getId(), purchaseDate, currentUser, purchaseValidation, currentMovie, purchaseDate);
        packagePrice = 40.00; // Set this value based on the selected package
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

    @Subscribe
    public void onPurchaseMessageReceived(PurchaseMessage message) {
        if (message.responseType == PurchaseMessage.ResponseType.PURCHASE_ADDED) {
            System.out.println("Purchase added successfully!");
        } else if (message.responseType == PurchaseMessage.ResponseType.PURCHASE_REMOVED) {
            System.out.println("Purchase removed successfully!");
        }
        // Handle other response types if necessary
    }

    public void setCurrentUser(RegisteredUser user) {
        this.currentUser = user;
    }

    public void setCurrentMovie(Movie movie) {
        this.currentMovie = movie;
        movieTitle.setText(movie.getEnglishName());
        // Set other movie-related details here
    }
    public void setCurrentdateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        //present the time?
    }

    public void setCurrentMovieInstance(MovieInstance movieInstance) {
        this.currentMovieInstance = movieInstance;
        // Set movie instance details here
    }

    // Relevant requests for additional actions
    @FXML
    public void reserveSeat() {
        if (currentMovieInstance != null) {
            SeatController.reserveSeat(currentMovieInstance);
            System.out.println("Seat reservation requested.");
        }
    }

    @FXML
    public void cancelSeatReservation() {
        if (currentMovieInstance != null) {
            SeatController.cancelSeatReservation(currentMovieInstance);
            System.out.println("Seat reservation cancellation requested.");
        }
    }

    @FXML
    public void loadTheaters() {
        TheaterController.getAllTheaters();
        System.out.println("Request to load all theaters sent.");
    }

    @FXML
    public void loadSeatsForCurrentHall() {
        if (currentMovieInstance != null) {
            SeatController.getAllSeatsByHall(currentMovieInstance.getHall().getId());
            System.out.println("Request to load all seats for the current hall sent.");
        }
    }
}
