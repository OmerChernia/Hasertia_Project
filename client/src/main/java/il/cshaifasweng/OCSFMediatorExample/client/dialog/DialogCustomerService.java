package il.cshaifasweng.OCSFMediatorExample.client.dialog;

import il.cshaifasweng.OCSFMediatorExample.client.boundariesEmploee.CustomerServiceBoundary;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.dbDeleteLaterGenerate;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DialogCustomerService {

    @FXML
    private ComboBox<String> dateComboBox;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private ComboBox<String> movieComboBox;

    @FXML
    private ComboBox<String> actionComboBox;

    @FXML
    private VBox changeScreeningBox;

    @FXML
    private VBox changeMovieBox;

    @FXML
    private VBox refundTicketBox;

    @FXML
    private GridPane seatGridPane;



    @FXML
    private TextField seatField;

    @FXML
    private TextField movieTitleField;

    @FXML
    private TextField screeningTimeField;



    @FXML
    private TextField refundAmountField;

    @FXML
    private TextFlow finalResponsePreviewArea;

    @FXML
    private ScrollPane finalResponseScrollPane;

    private CustomerServiceBoundary customerServiceController;

    @FXML
    private Label purchaseTypeField;

    private dbDeleteLaterGenerate dbGenerate;

    private Hall hall;
    private MovieTicket selectedMovieTicket;
    private MovieInstance selectedMovieInstance;
    private HomeViewingPackageInstance selectedHomeViewingPackageInstance;
    private MultiEntryTicket multiEntryTicket;



    public void initialize() {
        // Initialize DBGenerate instance
        dbGenerate = new dbDeleteLaterGenerate();
        // Set visibility of boxes to false initially
        changeScreeningBox.setVisible(false);
        changeScreeningBox.setManaged(false);
        changeMovieBox.setVisible(false);
        changeMovieBox.setManaged(false);
        refundTicketBox.setVisible(false);
        refundTicketBox.setManaged(false);
    }

    public void setCustomerServiceController(CustomerServiceBoundary customerServiceController) {
        this.customerServiceController = customerServiceController;
    }


    public void setComplaint(Complaint selectedComplaint) {

        if (selectedComplaint.getPurchase() instanceof MovieTicket) {
            setMovieTicket( selectedComplaint);
         } else if (selectedComplaint.getPurchase() instanceof HomeViewingPackageInstance) {
            setHomeViewingTicket(selectedComplaint);
         } else if (selectedComplaint.getPurchase() instanceof MultiEntryTicket) {
            setMultiEntryTicket(selectedComplaint);
        }

    }

    private void setMovieTicket(Complaint selectedComplaint){
        this.selectedMovieInstance = ((MovieTicket) selectedComplaint.getPurchase()).getMovieInstance();
        this.selectedMovieTicket = (MovieTicket)  selectedComplaint.getPurchase();
        purchaseTypeField.setText("Movie Ticket");
        movieTitleField.setText(selectedMovieTicket.getMovieInstance().getMovie().getEnglishName());
        screeningTimeField.setText(selectedMovieTicket.getMovieInstance().getTime().toString());
        seatField.setText("Row: " + selectedMovieTicket.getSeat().getRow() + " Seat: " + selectedMovieTicket.getSeat().getCol());
    }

    private void setHomeViewingTicket(Complaint selectedComplaint){
        HomeViewingPackageInstance viewingPackage = (HomeViewingPackageInstance) selectedComplaint.getPurchase();
        // this.selectedMovieInstance =  ((HomeViewingPackageInstance) selectedComplaint.getPurchase()).getMovie();
        purchaseTypeField.setText("Home Viewing Package");
        movieTitleField.setText(viewingPackage.getMovie().getEnglishName());
        screeningTimeField.setText(viewingPackage.getViewingDate().toString());
    }



    private void setMultiEntryTicket(Complaint selectedComplaint){
        MultiEntryTicket multiEntryTicket = (MultiEntryTicket) selectedComplaint.getPurchase();
        purchaseTypeField.setText("Multi-Entry Ticket");
        // Set any fields specific to multi-entry tickets
    }


    @FXML
    public void handleActionSelection() {
        String selectedAction = actionComboBox.getValue();

        // Hide all action boxes initially
        changeScreeningBox.setVisible(false);
        changeScreeningBox.setManaged(false);
        changeMovieBox.setVisible(false);
        changeMovieBox.setManaged(false);
        refundTicketBox.setVisible(false);
        refundTicketBox.setManaged(false);

        // Show the appropriate box based on the selected action
        if (selectedAction.equals("Change Screening Details")) {
            changeScreeningBox.setVisible(true);
            changeScreeningBox.setManaged(true);
            loadScreeningDetails(); // Load current screening details
        } else if (selectedAction.equals("Change Movie")) {
            changeMovieBox.setVisible(true);
            changeMovieBox.setManaged(true);
            loadMovieOptions(); // Populate the movieComboBox with available movies
        } else if (selectedAction.equals("Refund Ticket")) {
            refundTicketBox.setVisible(true);
            refundTicketBox.setManaged(true);
        }

        updateFinalResponsePreview(); // Update preview based on selection
    }

    private void loadScreeningDetails() {
        if (selectedMovieInstance != null) {
            movieTitleField.setText(selectedMovieInstance.getMovie().getEnglishName());

            // Populate dateComboBox with relevant dates
            List<String> dates = dbGenerate.getMovieInstances().stream()
                    .filter(instance -> instance.getMovie().equals(selectedMovieInstance.getMovie()))
                    .map(instance -> instance.getTime().toLocalDate().toString())
                    .distinct()
                    .collect(Collectors.toList());
            dateComboBox.getItems().setAll(dates);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

// Set the formatted date as the value in the ComboBox
            dateComboBox.setValue(selectedMovieInstance.getTime().toLocalDate().format(dateFormatter));
            // Populate timeComboBox with relevant times
            List<String> times = dbGenerate.getMovieInstances().stream()
                    .filter(instance -> instance.getMovie().equals(selectedMovieInstance.getMovie()) &&
                            instance.getTime().toLocalDate().equals(selectedMovieInstance.getTime().toLocalDate()))
                    .map(instance -> instance.getTime().toLocalTime().toString())
                    .distinct()
                    .collect(Collectors.toList());
            timeComboBox.getItems().setAll(times);
 // Define a formatter for the time
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

// Set the formatted time as the value in the ComboBox
            timeComboBox.setValue(selectedMovieInstance.getTime().toLocalTime().format(timeFormatter));
            seatField.setText("Row: " + selectedMovieTicket.getSeat().getRow() + " Seat: " + selectedMovieTicket.getSeat().getCol());
        }
    }



    private void loadMovieOptions() {
        // Populate movieComboBox with movie titles
        List<String> movieTitles = dbGenerate.getMovies().stream()
                .map(Movie::getEnglishName)
                .collect(Collectors.toList());
        movieComboBox.getItems().setAll(movieTitles);

        // Optionally set the current movie as the default selection
        if (selectedMovieInstance != null) {
            movieComboBox.setValue(selectedMovieInstance.getMovie().getEnglishName());
        }
    }


    @FXML
    public void showSeatGrid() {
        // Toggle visibility of the seat grid
        boolean isVisible = seatGridPane.isVisible();
        seatGridPane.setVisible(!isVisible);
        seatGridPane.setManaged(!isVisible);

        if (!isVisible && selectedMovieTicket.getSeat().getHall() != null) {
            populateSeatGrid( selectedMovieTicket.getSeat().getHall().getSeats());
        }
    }

    private void populateSeatGrid(List<Seat> seats) {
        seatGridPane.getChildren().clear(); // Clear previous seats if any

        for (Seat seat : seats) {
            Button seatButton = new Button(" ");
            seatButton.setMinSize(10, 10); // Very small button size
            seatButton.setMaxSize(10, 10); // Consistent small size
            seatButton.setOnAction(event -> handleSeatSelection(seat));

            // Add seatButton to the grid at the corresponding row and column
            seatGridPane.add(seatButton, seat.getCol(), seat.getRow());

            // Optionally, you can style the button to indicate its state (e.g., occupied, available)
            if (isSeatTaken(seat)) {
                seatButton.setStyle("-fx-background-color: red;"); // Mark as taken
                seatButton.setDisable(true); // Disable if taken
            } else {
                seatButton.setStyle("-fx-background-color: green;"); // Mark as available
            }
        }
    }

    private boolean isSeatTaken(Seat seat) {
        // Implement logic to check if the seat is taken based on your movie instances
        return !seat.getMovies().isEmpty();
    }

    private void handleSeatSelection(Seat seat) {
        // Update the seatField with the selected seat details
        seatField.setText("Row: " + seat.getRow() + " Seat: " + seat.getCol());



        // Hide the seat grid after selection
        seatGridPane.setVisible(false);
        seatGridPane.setManaged(false);

        // Update the final response preview
        updateFinalResponsePreview();
    }


    private void updateFinalResponsePreview() {
        // Clear the existing content
        finalResponsePreviewArea.getChildren().clear();

        String selectedAction = actionComboBox.getValue();
        if (selectedAction.equals("Change Screening Details")) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("Screening details have been updated.\n"));
             showSeatGrid();
        } else if (selectedAction.equals("Change Movie")) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("Movie has been changed to: " + movieComboBox.getValue() + "\n"));
        } else if (selectedAction.equals("Refund Ticket")) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("Ticket has been refunded.\nAmount: " + refundAmountField.getText() + "₪\n"));
        }

        // Optionally, append other details like the selected seat
        if (!seatField.getText().isEmpty()) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("Seat has been changed to: " + seatField.getText() + "\n"));
        }
    }
    @FXML
    public void closeEmployeeArea(ActionEvent actionEvent) {
        customerServiceController.closeDialogAddQuotes();
    }

    // Methods to handle form submission, additional logic, etc.
}
