package il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.DBGenerate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieSmallController {

    private final DBGenerate db = new DBGenerate();
    private HomeController homeController;
    public Movie movie;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnBook;

    @FXML
    private Button btnCloseHV;

    @FXML
    private Button btnCloseTheater;

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnPayHV;

    @FXML
    private Button btnPayTheater;

    @FXML
    private ComboBox<String> cmbCinema;

    @FXML
    private ComboBox<String> cmbDate;

    @FXML
    private ComboBox<String> cmbDateHv;

    @FXML
    private ComboBox<String> cmbHour;

    @FXML
    private ComboBox<String> cmbMovie;

    @FXML
    private Pane contentPanel;

    @FXML
    private ImageView image;

    @FXML
    private Pane imagePanel;

    @FXML
    private Label info;

    @FXML
    private Pane selectHvPanel;

    @FXML
    private Pane selectTheaterPane;

    @FXML
    private Text title;

    @FXML
    private Text txtMovieHV;

    @FXML
    private Text txtMovieTheater;

    @FXML
    public void initialize() {
        if (imagePanel != null) {
            imagePanel.setOnMouseEntered(event -> handleMouseEnter());
            imagePanel.setOnMouseExited(event -> handleMouseExit());
        }

        if (contentPanel != null) {
            contentPanel.setOnMouseEntered(event -> handleMouseEnter());
            contentPanel.setOnMouseExited(event -> handleMouseExit());
        }

        if (btnPayTheater != null) {
            btnPayTheater.setOnAction(event -> handlePayButton());
        }

        if (btnCloseTheater != null) {
            btnCloseTheater.setOnAction(event -> handleCloseButton());
        }
        if (cmbCinema != null && cmbDate != null && cmbHour != null) {
            setupComboBoxes();
        } else {
            System.err.println("One or more ComboBox elements are not initialized.");
        }

    }

    public Movie getMovie() {
        return movie;
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    private void setupComboBoxes() {
        disableAndResetComboBox(cmbDate);
        disableAndResetComboBox(cmbHour);

        cmbCinema.setCellFactory(comboBox -> new ListCell<String>() {
            @Override
            protected void updateItem(String location, boolean empty) {
                super.updateItem(location, empty);
                setText(empty || location == null ? null : location);
            }
        });

        cmbCinema.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String location, boolean empty) {
                super.updateItem(location, empty);
                setText(empty || location == null ? null : location);
            }
        });

        cmbCinema.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                disableAndResetComboBox(cmbDate);
                disableAndResetComboBox(cmbHour);
            } else {
                enableComboBox(cmbDate);
                populateDatesComboBox(newValue);
            }
        });

        cmbDate.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                disableAndResetComboBox(cmbHour);
            } else {
                enableComboBox(cmbHour);
                populateHoursComboBox(cmbCinema.getSelectionModel().getSelectedItem(), newValue);
            }
        });

        cmbHour.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            btnBook.setDisable(newValue == null);
        });
    }

    private void enableComboBox(ComboBox<String> comboBox) {
        comboBox.setDisable(false);
        comboBox.setStyle("-fx-background-color: #cae8fb;");
    }

    private void disableAndResetComboBox(ComboBox<String> comboBox) {
        comboBox.setDisable(true);
        comboBox.getItems().clear();
        comboBox.setStyle("");
    }

    private void handleMouseEnter() {
        if (!contentPanel.isVisible()) {
            contentPanel.setVisible(true);
        }
    }

    private void handleMouseExit() {
        if (contentPanel.isVisible()) {
            contentPanel.setVisible(false);
        }
    }

    public void setMovieShort(Movie movie) {
        this.movie = movie;
        info.setText(movie.getInfo());
        title.setText(movie.getEnglishName());
        txtMovieTheater.setText(movie.getEnglishName());

        try {
            String imagePath = "/il/cshaifasweng/OCSFMediatorExample/client/media/Movie/" + movie.getImage();
            URL imageUrl = getClass().getResource(imagePath);

            if (imageUrl == null) {
                throw new FileNotFoundException("Image not found: " + imagePath);
            }

            Image image = new Image(imageUrl.toExternalForm());
            this.image.setImage(image);

        } catch (Exception e) {
            System.out.println("Failed to load image: " + movie.getImage());
            e.printStackTrace();
        }

        populateCinemasComboBox(movie);
    }

    private void populateCinemasComboBox(Movie movie) {
        List<MovieInstance> allMovieInstances = db.getMovieInstances();
        List<MovieInstance> filteredInstances = allMovieInstances.stream()
                .filter(instance -> instance.getMovie().getId() == movie.getId())
                .collect(Collectors.toList());

        Set<String> cinemas = filteredInstances.stream()
                .map(instance -> instance.getHall().getTheater().getLocation())
                .collect(Collectors.toSet());

        cmbCinema.getItems().setAll(cinemas);
        disableAndResetComboBox(cmbDate);
        disableAndResetComboBox(cmbHour);
    }

    private void populateDatesComboBox(String selectedCinema) {
        List<MovieInstance> filteredInstances = db.getMovieInstances().stream()
                .filter(instance -> instance.getHall().getTheater().getLocation().equals(selectedCinema))
                .collect(Collectors.toList());

        Set<String> dates = filteredInstances.stream()
                .map(instance -> instance.getTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .collect(Collectors.toSet());

        cmbDate.getItems().setAll(dates);
        disableAndResetComboBox(cmbHour);
    }

    private void populateHoursComboBox(String selectedCinema, String selectedDate) {
        List<MovieInstance> filteredInstances = db.getMovieInstances().stream()
                .filter(instance -> instance.getHall().getTheater().getLocation().equals(selectedCinema))
                .filter(instance -> instance.getTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).equals(selectedDate))
                .collect(Collectors.toList());

        Set<String> hours = filteredInstances.stream()
                .map(instance -> instance.getTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toSet());

        cmbHour.getItems().setAll(hours);
    }

    private void handlePayButton() {
        String selectedCinema = cmbCinema.getSelectionModel().getSelectedItem();
        String selectedDate = cmbDate.getSelectionModel().getSelectedItem();
        String selectedHour = cmbHour.getSelectionModel().getSelectedItem();

        if (selectedCinema != null && selectedDate != null && selectedHour != null) {
            List<MovieInstance> filteredInstances = db.getMovieInstances().stream()
                    .filter(instance -> instance.getHall().getTheater().getLocation().equals(selectedCinema))
                    .filter(instance -> instance.getTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).equals(selectedDate))
                    .filter(instance -> instance.getTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).equals(selectedHour))
                    .collect(Collectors.toList());

            if (!filteredInstances.isEmpty()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/boundriesCustomer/TheaterPurchaseView.fxml"));
                    Parent root = loader.load();
                    TheaterPurchaseController purchaseController = loader.getController();
                    purchaseController.setMovieInstance(filteredInstances.get(0));

                    // Set the new root in HomeController
                    homeController.setRoot(root);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No screenings available for selected date and time!");
            }
        } else {
            System.out.println("Cinema, date or hour not selected!");
        }
    }

    private void handleCloseButton() {
        imagePanel.setVisible(true);
        selectTheaterPane.setVisible(false);
    }

    public void goToSelect(ActionEvent actionEvent) {
        populateCinemasComboBox(movie);
        selectTheaterPane.setVisible(true);
    }

    public void goToInfo(ActionEvent actionEvent) {
        homeController.showInfo(getMovie());
    }
}
