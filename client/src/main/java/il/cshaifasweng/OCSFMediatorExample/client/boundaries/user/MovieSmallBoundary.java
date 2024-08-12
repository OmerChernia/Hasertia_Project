package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieInstanceController;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MovieSmallBoundary {

    private HomeBoundary homeController;
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

    private String theaterName;
    private LocalDate date;
    private LocalTime time;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);

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

    public void setHomeController(HomeBoundary homeController) {
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
                MovieInstanceController.requestMovieInstancesByMovieIdAndTheaterName(movie.getId(), newValue);
                theaterName=newValue;
            }
        });


        cmbDate.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                disableAndResetComboBox(cmbHour);
            } else {
                enableComboBox(cmbHour);
                date = LocalDate.from(LocalDate.parse(newValue, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay());
                MovieInstanceController.requestMovieInstancesByMovieIdTheaterNameDate(movie.getId(), theaterName,LocalDate.parse(newValue, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay());
            }
        });


        cmbHour.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            btnBook.setDisable(newValue == null);
            time = LocalTime.from(LocalTime.parse(newValue, DateTimeFormatter.ofPattern("HH:mm")));
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

    public void setMovieShort(Movie  movie) {
        this.movie = movie;
        info.setText(movie.getInfo());
        title.setText(movie.getEnglishName());
        txtMovieTheater.setText(movie.getEnglishName());

        try {
            String imagePath = ConstantsPath.MOVIE_PACKAGE + movie.getImage();
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

    }

    @Subscribe
    public void onMovieInstanceMessageReceived(MovieInstanceMessage message) {
        Platform.runLater(() -> {
            switch (message.requestType) {
                case GET_ALL_MOVIE_INSTANCES_BY_MOVIE_ID:
                    populateCinemasComboBox(message.movies);
                    break;
                case GET_ALL_MOVIE_INSTANCES_BY_MOVIE_ID_AND_THEATER_NAME:
                    populateDatesComboBox(message.movies);
                    break;
                case GET_ALL_MOVIE_INSTANCES_BY_MOVIE_ID_THEATER_ID_DATE:
                    populateHoursComboBox(message.movies);
                    break;
                case GET_MOVIE_INSTANCE_AFTER_SELECTION:
                    loadSeatSelectionPage(message.movies.get(0));
                    break;
                default:
                    break;
            }
        });
    }

    private void populateCinemasComboBox(List<MovieInstance> movieInstances) {
        Set<String> cinemas = movieInstances.stream()
                .map(instance -> instance.getHall().getTheater().getLocation())
                .collect(Collectors.toSet());

        cmbCinema.getItems().setAll(cinemas);
        disableAndResetComboBox(cmbDate);
        disableAndResetComboBox(cmbHour);
    }

    private void populateDatesComboBox(List<MovieInstance> movieInstances) {
        Set<String> dates = movieInstances.stream()
                .map(instance -> instance.getTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .collect(Collectors.toSet());

        cmbDate.getItems().setAll(dates);
        disableAndResetComboBox(cmbHour);
    }

    private void populateHoursComboBox(List<MovieInstance> movieInstances) {
        Set<String> hours = movieInstances.stream()
                .map(instance -> instance.getTime().toLocalTime().minusHours(3).format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toSet());

        cmbHour.getItems().setAll(hours);
    }

    private void handlePayButton() {
        String selectedCinema = cmbCinema.getSelectionModel().getSelectedItem();
         String selectedDate = cmbDate.getSelectionModel().getSelectedItem();
         String selectedHour = cmbHour.getSelectionModel().getSelectedItem();
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        System.out.println(dateTime);
        if (selectedCinema != null && selectedDate != null && selectedHour != null) {
            MovieInstanceController.requestMovieInstanceAfterSelection(movie.getId(), selectedCinema, dateTime);
        } else {
            System.out.println("Cinema, date or hour not selected!");
        }
    }
    private void loadSeatSelectionPage(MovieInstance movieInstance)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ConstantsPath.THEATER_PURCHASE_VIEW));
            Parent root = loader.load();
            TheaterPurchaseBoundary purchaseController = loader.getController();
            purchaseController.setMovieInstance(movieInstance);
            homeController.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onMovieInstanceReceived(MovieInstance movieInstance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ConstantsPath.THEATER_PURCHASE_VIEW));
            Parent root = loader.load();
            TheaterPurchaseBoundary purchaseController = loader.getController();
            purchaseController.setMovieInstance(movieInstance);

            homeController.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCloseButton() {
        imagePanel.setVisible(true);
        selectTheaterPane.setVisible(false);
    }

    public void goToSelect(ActionEvent actionEvent) {
        MovieInstanceController.requestMovieInstancesByMovieId(movie.getId());
        selectTheaterPane.setVisible(true);
    }

    public void goToInfo(ActionEvent actionEvent) {
        homeController.showInfo(getMovie());
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }


}
