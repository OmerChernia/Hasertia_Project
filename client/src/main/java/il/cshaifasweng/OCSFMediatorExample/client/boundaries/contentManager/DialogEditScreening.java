package il.cshaifasweng.OCSFMediatorExample.client.boundaries.contentManager;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.HallController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieInstanceController;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.entities.Hall;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.HallMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DialogEditScreening implements Initializable {

    private int screeningId = -1;
    private String currentMode;

    private Map<String, Integer> movieMap = new HashMap<>();

    @FXML
    private AnchorPane containerAddProduct;

    @FXML
    private Text txtMovie;

    @FXML
    private DatePicker dtptDate;

    @FXML
    private ComboBox<String> cmbHour;

    @FXML
    private Text txtHall;

    @FXML
    private Text txtTheater;

    @FXML
    private Button btnSaveProduct;

    @FXML
    private Button btnCancelAddProduct;

    @FXML
    private Button btnClose;

    @FXML
    private Label txtAddProduct;

    private EditMovieScreeningsBoundary editMovieScreeningsBoundary;

    private MovieInstance movieInstance;

    private static final DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);

        // Listener to update available hours when date changes
        dtptDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateAvailableHours(newValue);
            }
        });
    }

    private void updateAvailableHours(LocalDate date) {
        cmbHour.getItems().clear();

        // שליחת בקשה ל-HallController כדי להביא את הזמנים הפנויים לתאריך שנבחר
        Hall hall = movieInstance.getHall();
        HallController.requestAvailableTimes(hall, date);
    }

    @Subscribe
    public void onHallMessageReceived(HallMessage message) {
        if (message.requestType == HallMessage.RequestType.GET_AVAILABLE_TIMES) {
            cmbHour.getItems().clear();
            List<LocalTime> availableTimes = message.getAvailableTimes();
            for (LocalTime time : availableTimes) {
                cmbHour.getItems().add(time.format(hourFormatter));
            }
        }
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setEditScreeningListBoundary(EditMovieScreeningsBoundary editMovieScreeningsBoundary) {
        this.editMovieScreeningsBoundary = editMovieScreeningsBoundary;
    }

    public void setDialog(String operation, MovieInstance movieInstance) {
        this.currentMode = operation;
        this.movieInstance = movieInstance;

        if ("view".equals(operation)) {
            populateFieldsForView(movieInstance.getMovie(), movieInstance.getTime());
        } else if ("add".equals(operation)) {
            prepareForNewScreening();
        } else if ("edit".equals(operation)) {
            populateFieldsForEdit(movieInstance.getMovie(), movieInstance.getTime());
        }
    }

    private void prepareForNewScreening() {
        cleanControls();
        enableEditControls();
        txtAddProduct.setText("Add Screening");
        btnSaveProduct.setVisible(true);
    }

    private void populateFieldsForEdit(Movie movie, LocalDateTime time) {
        txtMovie.setText(movie.getEnglishName());
        dtptDate.setValue(time.toLocalDate());
        updateAvailableHours(time.toLocalDate());
        cmbHour.setValue(time.toLocalTime().format(hourFormatter));

        txtAddProduct.setText("Update Screening");
        enableEditControls();
    }

    private void populateFieldsForView(Movie movie, LocalDateTime time) {
        txtMovie.setText(movie.getEnglishName());
        dtptDate.setValue(time.toLocalDate());
        cmbHour.setValue(time.toLocalTime().format(hourFormatter));

        txtAddProduct.setText("View Screening");
        disableEditControls();
        btnSaveProduct.setVisible(false);
    }

    private void disableEditControls() {
        dtptDate.setDisable(true);
        cmbHour.setDisable(true);
    }

    private void enableEditControls() {
        dtptDate.setDisable(false);
        cmbHour.setDisable(false);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) return;

        String movieName = txtMovie.getText();
        LocalDateTime dateTime = LocalDateTime.of(dtptDate.getValue(), LocalTime.parse(cmbHour.getValue(), hourFormatter));

        if ("add".equals(currentMode)) {
            MovieInstanceController.addMovieInstance(movieMap.get(movieName), dateTime, movieInstance.getHall().getId());
        } else if ("edit".equals(currentMode)) {
            MovieInstanceController.updateMovieInstance(screeningId, movieMap.get(movieName), dateTime, movieInstance.getHall().getId());
        }

        cleanControls();
        closeDialog();
    }

    @Subscribe
    public void onMovieInstanceMessageReceived(MovieInstanceMessage message) {
        switch (message.requestType) {
            case UPDATE_MOVIE_INSTANCE:
                showAlert("You have updated the screening for " + message.movies.get(0).getMovie().getEnglishName() + "!", AlertType.SUCCESS);
                break;
            case ADD_MOVIE_INSTANCE:
                showAlert("You have added a new screening for " + message.movies.get(0).getMovie().getEnglishName() + "!", AlertType.SUCCESS);
                break;
            case DELETE_MOVIE_INSTANCE:
                showAlert("You have removed the screening for " + message.movies.get(0).getMovie().getEnglishName() + "!", AlertType.SUCCESS);
                break;
            default:
                showAlert("Failed to process the screening for " + message.movies.get(0).getMovie().getEnglishName() + ".", AlertType.ERROR);
                break;
        }
    }

    private void showAlert(String messageText, AlertType alertType) {
        AlertsBuilder.create(
                alertType,
                null,
                containerAddProduct,
                containerAddProduct,
                messageText
        );
    }

    @FXML
    private void handleClose(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        editMovieScreeningsBoundary.closeDialog();
        cleanup();
    }

    private boolean validateInputs() {

        if (dtptDate.getValue() == null) {
            showErrorAndFocus(dtptDate);
            return false;
        }
        if (cmbHour.getValue() == null || cmbHour.getValue().isEmpty()) {
            showErrorAndFocus(cmbHour);
            return false;
        }
        return true;
    }

    private void showErrorAndFocus(Control field) {
        field.requestFocus();
        Animations.shake(field);
    }

    private void cleanControls() {
        dtptDate.setValue(null);
        cmbHour.setValue(null);
    }
}
