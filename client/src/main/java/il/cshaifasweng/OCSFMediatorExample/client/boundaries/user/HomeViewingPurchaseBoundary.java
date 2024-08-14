package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.PurchaseController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.RegisteredUserController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.SeatController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.TheaterController;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.RegisteredUserMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.EmailSender;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Pattern;

public class HomeViewingPurchaseBoundary {
    @FXML
    private StackPane stackPane;

    @FXML
    private VBox ticketSelectionPane;

    @FXML
    private VBox paymentDetailsPane;

    @FXML
    private GridPane nodetails;

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

    //the user details
    @FXML
    private TextField firstNameTF;

    @FXML
    private TextField lastNameTF;

    @FXML
    private Label connectedText;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField confirmEmailTF;

    @FXML
    private TextField idNumberTF;

    @FXML
    private TextField confirmIdNumberTF;
    //end of user details
    private RegisteredUser user=null;

    private String link;

    // Regular expression for validating an email address
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    private LocalDateTime dateTime;
    private double packagePrice;
    private Movie currentMovie;
    private MovieInstance currentMovieInstance;
    private HomeViewingPackageInstance homeViewingPackageInstance;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(ticketSelectionPane);
        highlightStep(1);
        user=null;
        if(!SimpleClient.user.isEmpty()) {
            RegisteredUserController.getUserByID(SimpleClient.user);
        }
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
        stackPane.getChildren().clear();
        if(user!=null)
        {
            stackPane.getChildren().add(creditCardPane);
            creditCardPane.setVisible(true);
        }
        else
            stackPane.getChildren().add(paymentDetailsPane);

        highlightStep(2);
    }

    @FXML
    private void goToTicketSelection() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(ticketSelectionPane);
        highlightStep(1);
    }


    @FXML
    private void showCreditCardFields()
    {
        if(checkDetails()) {
            creditCardPane.setVisible(true);
        }
    }

    @FXML
    private void submitPayment()
    {

        // Implement payment submission logic
        System.out.println("Payment submitted with card number: " + cardNumberField.getText());
        creditCardPane.setVisible(false);

        if(user == null)
            RegisteredUserController.addNewUser(idNumberTF.getText(),firstNameTF.getText(),lastNameTF.getText(),emailTF.getText());
        else
        {
            RegisteredUserController.addNewUser(user.getId_number(),"","","");
        }
    }

    @Subscribe
    public void onRegisteredUserReceivedMessage(RegisteredUserMessage message)
    {
        if(message.requestType==RegisteredUserMessage.RequestType.GET_USER_BY_ID)
            user= message.registeredUser;
        else
        {
            String purchaseValidation = cardNumberField.getText() + " " + expirationDateField.getText() + " " + cvvField.getText();
            link ="https://hasertia.com/"+ generateRandomString(10);
            PurchaseController.AddHomeViewing(LocalDateTime.now(), message.registeredUser, purchaseValidation, currentMovie, dateTime, link);
        }
    }
    public static String generateRandomString(int length) {
        // Define the characters that can be included in the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        // Generate random characters from the 'characters' string
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }
    @Subscribe
    public void onPurchaseReceivedMessage(PurchaseMessage message)
    {
        if (message.responseType == PurchaseMessage.ResponseType.PURCHASE_ADDED)
        {
            homeViewingPackageInstance = (HomeViewingPackageInstance)message.purchases.get(0);
            showConfirmation();
        }
    }

    @FXML
    private void cancelPayment()
    {
        cardNumberField.clear();
        expirationDateField.clear();
        cvvField.clear();

        if(user == null) {
            // Hide the credit card fields
            creditCardPane.setVisible(false);
        }
        else
            goToTicketSelection();
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
        Platform.runLater(() -> {
            System.out.println("Package purchased successfully.");
            String text = "Movie: " + movieTitle.getText() + ", " +
                    "Available on " + homeViewingPackageInstance.getViewingDate() + ", " +
                    "Price Paid: ₪" + homeViewingPackageInstance.getMovie().getHomeViewingPrice() + ", " +
                    "Valid until: " + homeViewingPackageInstance.getViewingDate().plusWeeks(1) + ", " +
                    "Purchase link: " + link;

            confirmationDetails.setText(text);
            EmailSender.sendEmail(homeViewingPackageInstance.getOwner().getEmail(), "New Home Viewing Purchase From Hasertia", text);
            EmailSender.sendEmail("hasertiaproject@gmail.com", "New Home Viewing Purchase From Hasertia", text);
            confirmationMovieImage.setImage(movieImage.getImage());
            stackPane.getChildren().clear();
            stackPane.getChildren().add(ticketConfirmationPane);
            EventBus.getDefault().unregister(this);
        });
    }

    @FXML
    private void closeApplication()
    {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.close();
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
}
