package il.cshaifasweng.OCSFMediatorExample.client.boundaries.customerService;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieInstanceController;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    private MovieTicket selectedMovieTicket;
    private MovieInstance selectedMovieInstance;
    private HomeViewingPackageInstance selectedHomeViewingPackageInstance;
    private MultiEntryTicket multiEntryTicket;

    public void initialize() {
        changeScreeningBox.setVisible(false);
        changeScreeningBox.setManaged(false);
        changeMovieBox.setVisible(false);
        changeMovieBox.setManaged(false);
        refundTicketBox.setVisible(false);
        refundTicketBox.setManaged(false);

        // Register to EventBus
        EventBus.getDefault().register(this);
    }

    public void setCustomerServiceController(CustomerServiceBoundary customerServiceController) {
        this.customerServiceController = customerServiceController;
    }

    public void setComplaint(Complaint selectedComplaint) {
        if (selectedComplaint.getPurchase() instanceof MovieTicket) {
            setMovieTicket(selectedComplaint);
        } else if (selectedComplaint.getPurchase() instanceof HomeViewingPackageInstance) {
            setHomeViewingTicket(selectedComplaint);
        } else if (selectedComplaint.getPurchase() instanceof MultiEntryTicket) {
            setMultiEntryTicket(selectedComplaint);
        }
    }

    private void setMovieTicket(Complaint selectedComplaint) {
        this.selectedMovieInstance = ((MovieTicket) selectedComplaint.getPurchase()).getMovieInstance();
        this.selectedMovieTicket = (MovieTicket) selectedComplaint.getPurchase();
        purchaseTypeField.setText("Movie Ticket");
        movieTitleField.setText(selectedMovieTicket.getMovieInstance().getMovie().getEnglishName());
        screeningTimeField.setText(selectedMovieTicket.getMovieInstance().getTime().toString());
        seatField.setText("Row: " + selectedMovieTicket.getSeat().getRow() + " Seat: " + selectedMovieTicket.getSeat().getCol());
    }

    private void setHomeViewingTicket(Complaint selectedComplaint) {
        HomeViewingPackageInstance viewingPackage = (HomeViewingPackageInstance) selectedComplaint.getPurchase();
        purchaseTypeField.setText("Home Viewing Package");
        movieTitleField.setText(viewingPackage.getMovie().getEnglishName());
        screeningTimeField.setText(viewingPackage.getViewingDate().toString());
    }

    private void setMultiEntryTicket(Complaint selectedComplaint) {
        MultiEntryTicket multiEntryTicket = (MultiEntryTicket) selectedComplaint.getPurchase();
        purchaseTypeField.setText("Multi-Entry Ticket");
        // Set any fields specific to multi-entry tickets
    }

    @FXML
    public void handleActionSelection() {
        String selectedAction = actionComboBox.getValue();

        changeScreeningBox.setVisible(false);
        changeScreeningBox.setManaged(false);
        changeMovieBox.setVisible(false);
        changeMovieBox.setManaged(false);
        refundTicketBox.setVisible(false);
        refundTicketBox.setManaged(false);

        if (selectedAction.equals("Change Screening Details")) {
            changeScreeningBox.setVisible(true);
            changeScreeningBox.setManaged(true);
            MovieInstanceController.requestMovieInstancesByName(selectedMovieInstance.getMovie().getEnglishName());
        } else if (selectedAction.equals("Change Movie")) {
            changeMovieBox.setVisible(true);
            changeMovieBox.setManaged(true);
            MovieInstanceController.requestAllMovieInstances();
        } else if (selectedAction.equals("Refund Ticket")) {
            refundTicketBox.setVisible(true);
            refundTicketBox.setManaged(true);
        }

        updateFinalResponsePreview();
    }

    @Subscribe
    public void onMovieInstanceMessageReceived(MovieInstanceMessage message) {
        Platform.runLater(() -> {
            if (message.requestType == MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES ||
                    message.requestType == MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_NAME) {

                if (changeScreeningBox.isVisible()) {
                    updateScreeningDetails(message.movies);
                } else if (changeMovieBox.isVisible()) {
                    updateMovieOptions(message.movies);
                }
            }
        });
    }

    private void updateScreeningDetails(List<MovieInstance> instances) {
        List<String> dates = instances.stream()
                .map(instance -> instance.getTime().toLocalDate().toString())
                .distinct()
                .collect(Collectors.toList());
        dateComboBox.getItems().setAll(dates);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateComboBox.setValue(selectedMovieInstance.getTime().toLocalDate().format(dateFormatter));

        List<String> times = instances.stream()
                .filter(instance -> instance.getTime().toLocalDate().equals(selectedMovieInstance.getTime().toLocalDate()))
                .map(instance -> instance.getTime().toLocalTime().toString())
                .distinct()
                .collect(Collectors.toList());
        timeComboBox.getItems().setAll(times);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        timeComboBox.setValue(selectedMovieInstance.getTime().toLocalTime().format(timeFormatter));

        seatField.setText("Row: " + selectedMovieTicket.getSeat().getRow() + " Seat: " + selectedMovieTicket.getSeat().getCol());
    }

    private void updateMovieOptions(List<MovieInstance> instances) {
        List<String> movieTitles = instances.stream()
                .map(instance -> instance.getMovie().getEnglishName())
                .distinct()
                .collect(Collectors.toList());
        movieComboBox.getItems().setAll(movieTitles);

        if (selectedMovieInstance != null) {
            movieComboBox.setValue(selectedMovieInstance.getMovie().getEnglishName());
        }
    }

    @FXML
    public void showSeatGrid() {
        boolean isVisible = seatGridPane.isVisible();
        seatGridPane.setVisible(!isVisible);
        seatGridPane.setManaged(!isVisible);

        if (!isVisible && selectedMovieTicket.getSeat().getHall() != null) {
            populateSeatGrid(selectedMovieTicket.getSeat().getHall().getSeats());
        }
    }

    private void populateSeatGrid(List<Seat> seats) {
        seatGridPane.getChildren().clear();

        for (Seat seat : seats) {
            Button seatButton = new Button(" ");
            seatButton.setMinSize(10, 10);
            seatButton.setMaxSize(10, 10);
            seatButton.setOnAction(event -> handleSeatSelection(seat));

            seatGridPane.add(seatButton, seat.getCol(), seat.getRow());

            if (isSeatTaken(seat)) {
                seatButton.setStyle("-fx-background-color: red;");
                seatButton.setDisable(true);
            } else {
                seatButton.setStyle("-fx-background-color: green;");
            }
        }
    }

    private boolean isSeatTaken(Seat seat) {
        return !seat.getMoviesIds().isEmpty();
    }

    private void handleSeatSelection(Seat seat) {
        seatField.setText("Row: " + seat.getRow() + " Seat: " + seat.getCol());

        seatGridPane.setVisible(false);
        seatGridPane.setManaged(false);

        updateFinalResponsePreview();
    }

    private void updateFinalResponsePreview() {
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

        if (!seatField.getText().isEmpty()) {
            finalResponsePreviewArea.getChildren().add(new javafx.scene.text.Text("Seat has been changed to: " + seatField.getText() + "\n"));
        }
    }

    @FXML
    public void closeEmployeeArea(ActionEvent actionEvent) {
        customerServiceController.closeDialogAddQuotes();
    }



    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
}
