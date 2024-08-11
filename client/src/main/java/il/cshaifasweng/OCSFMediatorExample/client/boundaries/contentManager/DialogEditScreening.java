package il.cshaifasweng.OCSFMediatorExample.client.boundaries.contentManager;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieInstanceController;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.entities.Hall;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class DialogEditScreening implements Initializable {

    private int screeningId = -1;
    private String currentMode;

    @FXML
    private AnchorPane containerAddProduct;

    @FXML
    private ComboBox<Movie> cmbMovies;

    @FXML
    private DatePicker dtptDate;

    @FXML
    private ComboBox<String> cmbHour;

    @FXML
    private ComboBox<Hall> cmbHall;

    @FXML
    private ComboBox<String> cmbTheater;

    @FXML
    private Button btnSaveProduct;

    @FXML
    private Button btnUpdateProduct;

    @FXML
    private Button btnCancelAddProduct;

    @FXML
    private Button btnClose;

    @FXML
    private Label txtAddProduct;

    private EditMovieScreeningsBoundary editMovieScreeningsBoundary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setEditScreeningListBoundary(EditMovieScreeningsBoundary editMovieScreeningsBoundary) {
        this.editMovieScreeningsBoundary = editMovieScreeningsBoundary;
    }

    public void setDialog(String operation, int screeningId, Movie movie, LocalDateTime time, Hall hall) {
        this.currentMode = operation;
        this.screeningId = screeningId;

        if ("view".equals(operation)) {
            populateFieldsForView(movie, time, hall);
        } else if ("add".equals(operation)) {
            prepareForNewScreening();
        } else if ("edit".equals(operation)) {
            populateFieldsForEdit(movie, time, hall);
        }
    }

    private void prepareForNewScreening() {
        cleanControls();
        enableEditControls();
        txtAddProduct.setText("Add Screening");
        btnSaveProduct.setVisible(true);
    }

    private void populateFieldsForEdit(Movie movie, LocalDateTime time, Hall hall) {
        cmbMovies.setValue(movie);
        dtptDate.setValue(time.toLocalDate());
        cmbHour.setValue(time.toLocalTime().toString());
        cmbTheater.setValue(hall.getTheater().getLocation());
        cmbHall.setValue(hall);

        txtAddProduct.setText("Update Screening");
        enableEditControls();
    }

    private void populateFieldsForView(Movie movie, LocalDateTime time, Hall hall) {
        cmbMovies.setValue(movie);
        dtptDate.setValue(time.toLocalDate());
        cmbHour.setValue(time.toLocalTime().toString());
        cmbTheater.setValue(hall.getTheater().getLocation());
        cmbHall.setValue(hall);

        txtAddProduct.setText("View Screening");
        disableEditControls();
        btnSaveProduct.setVisible(false);
    }

    private void disableEditControls() {
        cmbMovies.setDisable(true);
        dtptDate.setDisable(true);
        cmbHour.setDisable(true);
        cmbTheater.setDisable(true);
        cmbHall.setDisable(true);
    }

    private void enableEditControls() {
        cmbMovies.setDisable(false);
        dtptDate.setDisable(false);
        cmbHour.setDisable(false);
        cmbTheater.setDisable(false);
        cmbHall.setDisable(false);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) return;

        int movieId = cmbMovies.getValue().getId();
        LocalDateTime dateTime = LocalDateTime.of(dtptDate.getValue(), LocalTime.parse(cmbHour.getValue()));
        int hallId = cmbHall.getValue().getId();

        if ("add".equals(currentMode)) {
            MovieInstanceController.addMovieInstance(movieId, dateTime, hallId);
        } else if ("edit".equals(currentMode)) {
            MovieInstanceController.updateMovieInstance(screeningId, movieId, dateTime, hallId);
        }

        cleanControls();
        closeDialog();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
      //  editMovieScreeningsBoundary.closeDialogAddProduct();
        cleanup();
    }

    private boolean validateInputs() {
        if (cmbMovies.getValue() == null) {
            showErrorAndFocus(cmbMovies);
            return false;
        }
        if (dtptDate.getValue() == null) {
            showErrorAndFocus(dtptDate);
            return false;
        }
        if (cmbHour.getValue() == null || cmbHour.getValue().isEmpty()) {
            showErrorAndFocus(cmbHour);
            return false;
        }
        if (cmbHall.getValue() == null) {
            showErrorAndFocus(cmbHall);
            return false;
        }
        return true;
    }

    private void showErrorAndFocus(Control field) {
        field.requestFocus();
        Animations.shake(field);
    }

    private void cleanControls() {
        cmbMovies.setValue(null);
        dtptDate.setValue(null);
        cmbHour.setValue(null);
        cmbTheater.setValue(null);
        cmbHall.setValue(null);
    }
}
