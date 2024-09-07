package il.cshaifasweng.OCSFMediatorExample.client.boundaries.contentManager;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.PriceRequestController;
import il.cshaifasweng.OCSFMediatorExample.client.util.animationAndImages.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.RequieredFieldsValidators;
import il.cshaifasweng.OCSFMediatorExample.client.util.popUp.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.popUp.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DialogEditMovie implements Initializable {

    private static final Stage stage = new Stage();
    private File imageFile;
    private Movie movie;
    private String currentMode;

    private final ColorAdjust colorAdjust = new ColorAdjust();

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> comboGenre;


    @FXML
    private AnchorPane containerAddProduct;

    @FXML
    private VBox imageContainer;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Pane paneContainer;

    @FXML
    private TextField txtActors,txtDuration,txtEnglishName,txtHVPrice,txtHebrewName, txtTheaterPrice, txtProducer;

    @FXML
    private TextArea  txtDescription;

    @FXML
    private Label  txtAddProduct;

    @FXML
    private Label lblPathImage;

    private EditMovieListBoundary editMovieListBoundary;

    @FXML
    private Label txtTitle;

    @FXML
    private ComboBox<Movie.Availability> comboAvailable;

    @FXML
    private ComboBox<Movie.StreamingType> comboType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        initializeImageHoverEffect();
        initializeComboBox();

    }

    private void initializeComboBox() {
        comboAvailable.getItems().addAll(Movie.Availability.values());
        comboType.getItems().addAll(Movie.StreamingType.values());
        comboGenre.getItems().addAll("action","comedy","drama","horror","romance","sci-Fi","documentary");

    }


    public void setEditMovieListBoundary(EditMovieListBoundary editMovieListBoundary) {
        this.editMovieListBoundary = editMovieListBoundary;
    }

    public void setDialog(String operation, Movie movie) {
        this.currentMode = operation;
        this.movie = "add".equals(operation) ? new Movie() : movie;
        if ("view".equals(operation)) {
            populateFieldsForView();
        } else if ("add".equals(operation)) {
            prepareForNewProduct();
        } else if ("edit".equals(operation)){
            populateFieldsForEdit();
        }
    }

    private void prepareForNewProduct() {
        cleanControls();
        enableEditControls();
        txtAddProduct.setText("Add Movie");
        btnSave.setVisible(true);
    }

    private void initializeImageHoverEffect() {
        imageContainer.hoverProperty().addListener((o, oldV, newV) -> {
            colorAdjust.setBrightness(newV ? 0.25 : 0);
            imageProduct.setEffect(colorAdjust);
        });
        imageContainer.setPadding(new Insets(5));
        imageProduct.setFitHeight(300);
        imageProduct.setFitWidth(200);
    }

    private void populateFieldsForEdit() {
        txtEnglishName.setText(movie.getEnglishName());
        txtHebrewName.setText(movie.getHebrewName());
        txtProducer.setText(movie.getProducer());
        txtDuration.setText(String.valueOf(movie.getDuration()));
        txtTheaterPrice.setText(String.valueOf(movie.getTheaterPrice()));
        txtHVPrice.setText(String.valueOf(movie.getHomeViewingPrice()));
        comboGenre.setValue(movie.getGenre());
        comboType.setValue(movie.getStreamingType());
        comboAvailable.setValue(movie.getAvailability());
        txtDescription.setText(movie.getInfo());
        txtActors.setText(String.join(", ", movie.getMainActors()));
        imageProduct.setImage(getImage(movie));
        expandImage(movie, movie.getEnglishName());
        lblPathImage.setText(movie.getImage());
        txtAddProduct.setText("Update Movie");
        enableEditControls();
    }

    private void populateFieldsForView() {
        txtEnglishName.setText(movie.getEnglishName());
        txtHebrewName.setText(movie.getHebrewName());
        txtProducer.setText(movie.getProducer());
        txtDuration.setText(String.valueOf(movie.getDuration()));
        txtTheaterPrice.setText(String.valueOf(movie.getTheaterPrice()));
        txtHVPrice.setText(String.valueOf(movie.getHomeViewingPrice()));
        comboGenre.setValue(movie.getGenre());
        comboType.setValue(movie.getStreamingType());
        comboAvailable.setValue(movie.getAvailability());
        txtDescription.setText(movie.getInfo());
        txtActors.setText(String.join(", ", movie.getMainActors()));
        imageProduct.setImage(getImage(movie));

        expandImage(movie, movie.getEnglishName());

        txtAddProduct.setText("View Movie");
        disableEditControls();
        btnSave.setVisible(false);
    }

    private void disableEditControls() {
        Arrays.asList(txtEnglishName, txtHebrewName, txtProducer, txtDuration, txtTheaterPrice, txtHVPrice, txtDescription, txtActors)
                .forEach(field -> field.setEditable(false));
        comboGenre.setDisable(true);
        comboType.setDisable(true);
        comboAvailable.setDisable(true);
    }

    private void enableEditControls() {
        Arrays.asList(txtEnglishName, txtHebrewName, txtProducer, txtDuration, txtTheaterPrice, txtHVPrice, txtDescription, txtActors)
                .forEach(field -> field.setEditable(true));
        comboGenre.setDisable(false);
        comboType.setDisable(false);
        comboAvailable.setDisable(false);

    }

    private void expandImage(Movie movie, String title) {
        paneContainer.hoverProperty().addListener((o, oldV, newV) -> {
            colorAdjust.setBrightness(newV ? 0.25 : 0);
            imageProduct.setEffect(colorAdjust);
        });

        paneContainer.setOnMouseClicked(ev -> {
            final Image image = getImage(movie);
            final ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(550);

            final BorderPane borderPane = new BorderPane(imageView);
            borderPane.setStyle("-fx-background-color: white");
            borderPane.setCenter(imageView);

            final ScrollPane root = new ScrollPane(borderPane);
            root.setStyle("-fx-background-color: white");
            root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            root.getStylesheets().add(ConstantsPath.LIGHT_THEME);
            root.getStyleClass().add("scroll-bar");

            root.setFitToHeight(true);
            root.setFitToWidth(true);

            stage.getIcons().add(new Image(ConstantsPath.STAGE_ICON));
            stage.setScene(new Scene(root, 550, 550));
            stage.setTitle(title);
            stage.show();
        });
    }

    private Image getImage(Movie movie) {
        return Optional.ofNullable(movie)
                .map(Movie::getImage)
                .map(img -> ConstantsPath.MOVIE_PACKAGE + img)
                .map(path -> getClass().getResource(path))
                .map(URL::toExternalForm)
                .map(Image::new)
                .orElse(new Image(ConstantsPath.NO_IMAGE_AVAILABLE, true));
    }




    @FXML
    private void handleSave(ActionEvent event) {

        if (!validateInputs()) return;

        String englishName = txtEnglishName.getText().trim();
        String hebrewName = txtHebrewName.getText().trim();
        String producer = txtProducer.getText().trim();
        String duration = txtDuration.getText().trim();
        String theaterPrice = txtTheaterPrice.getText().trim();
        String hvPrice = txtHVPrice.getText().trim();
        String genre = comboGenre.getSelectionModel().getSelectedItem();
        Movie.StreamingType streaming = comboType.getSelectionModel().getSelectedItem();
        Movie.Availability availability = comboAvailable.getSelectionModel().getSelectedItem();
        String description = txtDescription.getText().trim();
        List<String> actors = Arrays.asList(txtActors.getText().trim().split(", "));


        // Check for changes in details
        boolean detailsChanged = !englishName.equals(movie.getEnglishName()) ||
                !hebrewName.equals(movie.getHebrewName()) ||
                !producer.equals(movie.getProducer()) ||
                !description.equals(movie.getInfo()) ||
                !genre.equals(movie.getGenre()) ||
                !(streaming==movie.getStreamingType()) ||
                !(availability==movie.getAvailability())||
                !actors.equals(movie.getMainActors()) ||
                Integer.parseInt(duration) != movie.getDuration();

        // Check for changes in prices
        boolean priceChanged = Integer.parseInt(theaterPrice) != movie.getTheaterPrice() ||
                Integer.parseInt(hvPrice) != movie.getHomeViewingPrice();

        // If no changes detected, do nothing
        if (!detailsChanged && !priceChanged) {
            NotificationsBuilder.create(NotificationType.INFORMATION, "No changes detected, nothing was saved.",containerAddProduct);
            return;
        }

        // If there are changes, proceed with the update
        if ("add".equals(currentMode)) {
            MovieController.addMovie(hebrewName, description, producer, englishName, String.valueOf(actors), "empty-image.jpg", streaming, Integer.parseInt(duration), Integer.parseInt(theaterPrice), Integer.parseInt(hvPrice), genre);
        } else {
            if (detailsChanged) {
                MovieController.updateMovie(movie, hebrewName, description, producer, englishName, String.valueOf(actors),  "", streaming, Integer.parseInt(duration), genre, availability);

            }

            // Create a price update request only if prices have changed
            if (priceChanged) {
                if (!theaterPrice.isEmpty() && Integer.parseInt(theaterPrice) != movie.getTheaterPrice()) {
                    PriceRequestController.createNewPriceRequest(Integer.parseInt(theaterPrice), movie, Movie.StreamingType.THEATER_VIEWING);
                }

                if (!hvPrice.isEmpty() && Integer.parseInt(hvPrice) != movie.getHomeViewingPrice()) {
                    PriceRequestController.createNewPriceRequest(Integer.parseInt(hvPrice), movie, Movie.StreamingType.HOME_VIEWING);
                }
            }
        }

        cleanControls();
        closeDialog();
    }


    @FXML
    private void handleClose(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        editMovieListBoundary.closeDialogAddProduct();
    }

    // Method to validate the inputs of the form
    private boolean validateInputs() {

        // Check if the English name text field is empty
        if (txtEnglishName.getText().trim().isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR, "English name cannot be empty", containerAddProduct);
            showErrorAndFocus(txtEnglishName); // Show error and focus on the field
            return false;
        }

        // Check if only English letters are allowed
        if (!txtEnglishName.getText().trim().matches("[a-zA-Z0-9 ,.!:-]+")) {// Allows only English letters and spaces
            NotificationsBuilder.create(NotificationType.ERROR, "Only English letters are allowed", containerAddProduct);
            showErrorAndFocus(txtEnglishName);
            return false;
        }

        // Check if the Hebrew name text field is empty
        if (txtHebrewName.getText().trim().isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR, "Hebrew name cannot be empty", containerAddProduct);
            showErrorAndFocus(txtHebrewName); // Show error and focus on the field
            return false;
        }

        // Check if only Hebrew letters are allowed
        if (!txtHebrewName.getText().trim().matches("[\u0590-\u05FF0-9 ,.!:-]+")) {// Allows only Hebrew letters and spaces
            NotificationsBuilder.create(NotificationType.ERROR, "Only Hebrew letters are allowed", containerAddProduct);
            showErrorAndFocus(txtHebrewName);
            return false;
        }


        // Check if the producer text field is empty
        if (txtProducer.getText().trim().isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR, "Producer cannot be empty", containerAddProduct);
            showErrorAndFocus(txtProducer); // Show error and focus on the field
            return false;
        }

        // Check if the Duration text field is empty
        if (txtDuration.getText().trim().isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR, "Duration cannot be empty", containerAddProduct);
            showErrorAndFocus(txtDuration); // Show error and focus on the field
            return false;
        }

        // Validate if the Duration contains only numbers
        if (!(txtDuration.getText().trim().matches("-?\\d+"))) {
            NotificationsBuilder.create(NotificationType.ERROR, "Duration must be a valid number", containerAddProduct);
            showErrorAndFocus(txtDuration); // Show error and focus on the field
            return false;
        }

        // Check if the Genre combo box is empty
        if (comboGenre.getValue() == null) {
            NotificationsBuilder.create(NotificationType.ERROR, "Genre cannot be empty", containerAddProduct);
            comboGenre.requestFocus();
            Animations.shake(comboGenre);
            return false;
        }

        // Check if the Available combo box is empty
        if (comboAvailable.getValue() == null) {
            NotificationsBuilder.create(NotificationType.ERROR, "Available cannot be empty", containerAddProduct);
            comboAvailable.requestFocus();
            Animations.shake(comboAvailable);
            return false;
        }

        // Check if the Type combo box is empty
        if (comboType.getValue() == null) {
            NotificationsBuilder.create(NotificationType.ERROR, "Type of movie cannot be empty", containerAddProduct);
            comboType.requestFocus();
            Animations.shake(comboType);
            return false;
        }

        // Check if the Actors text field is empty
        if (txtActors.getText().trim().isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR, "Actors cannot be empty", containerAddProduct);
            showErrorAndFocus(txtActors); // Show error and focus on the field
            return false;
        }

        // Check if the home video price text field is empty
        if (txtHVPrice.getText().trim().isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR, "Home video price cannot be empty", containerAddProduct);
            showErrorAndFocus(txtHVPrice); // Show error and focus on the field
            return false;
        }

        // Validate if the home video price contains only numbers
        if (!(txtHVPrice.getText().trim().matches("-?\\d+"))) { // Matches if the string is a valid integer
            NotificationsBuilder.create(NotificationType.ERROR, "Home video price must be a valid number", containerAddProduct);
            showErrorAndFocus(txtHVPrice); // Show error and focus on the field
            return false;
        }

        // Check if the theater price text field is empty
        if (txtTheaterPrice.getText().trim().isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR, "Theater price cannot be empty", containerAddProduct);
            showErrorAndFocus(txtTheaterPrice); // Show error and focus on the field
            return false;
        }

        // Validate if the theater price contains only numbers
        if (!(txtTheaterPrice.getText().trim().matches("-?\\d+"))) { // Matches if the string is a valid integer
            NotificationsBuilder.create(NotificationType.ERROR, "Theater price must be a valid number", containerAddProduct);
            showErrorAndFocus(txtTheaterPrice); // Show error and focus on the field
            return false;
        }

        // Check if the description text field is empty
        if (txtDescription.getText().trim().isEmpty()) {
            NotificationsBuilder.create(NotificationType.ERROR, "Description cannot be empty", containerAddProduct);
            showErrorAndFocus(txtDescription); // Show error and focus on the field
            return false;
        }


        // Check if an image file is provided and trigger an error if it exists
        if (imageFile != null) {
            Animations.shake(imageContainer); // Add a shake animation to indicate an error
            NotificationsBuilder.create(NotificationType.ERROR, ConstantsPath.MESSAGE_IMAGE_LARGE,containerAddProduct);
            return false;
        }

        // If all checks pass, return true to indicate validation success
        return true;
    }


    private void showErrorAndFocus(TextInputControl field) {
        field.requestFocus();
        Animations.shake(field);
    }

    private void cleanControls() {
        imageFile = null;
        Arrays.asList(txtEnglishName, txtHebrewName, txtProducer, txtDuration, txtTheaterPrice, txtHVPrice, txtDescription, txtActors)
                .forEach(TextInputControl::clear);
        imageProduct.setImage(new Image(ConstantsPath.NO_IMAGE_AVAILABLE));
    }


}

