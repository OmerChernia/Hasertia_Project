package il.cshaifasweng.OCSFMediatorExample.client.boundaries.contentManager;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieController;
import il.cshaifasweng.OCSFMediatorExample.client.util.*;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.RequieredFieldsValidators;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.TextFieldMask;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class DialogEditMovie implements Initializable {
    private static final Stage stage = new Stage();

    private File imageFile;
    private Movie movie;

    private final ColorAdjust colorAdjust = new ColorAdjust();

    @FXML
    private Label txtAddProduct;

    @FXML
    private TextField txtStreamingType;

    @FXML
    private AnchorPane containerAddProduct;

    @FXML
    private TextField txtEnglishName;

    @FXML
    private TextField txtHebrewName;

    @FXML
    private TextField txtProducer;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtTheaterPrice;

    @FXML
    private TextField txtHVPrice;

    @FXML
    private TextField txtGenre;

    @FXML
    private TextField txtActors;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnUpdateProduct;

    @FXML
    private Button btnSaveProduct;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Pane paneContainer;

    @FXML
    private HBox imageContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFieldMasksAndValidation();
        initializeImageHoverEffect();
        characterLimiter();
        initializeTextFieldDefaults();
    }

    private void setFieldMasksAndValidation() {
        setMask();
        setValidations();
        selectText();
    }

    public void setDialog(String operation, Movie movie) {
        this.movie = "add".equals(operation) ? null : movie;
        if (this.movie == null) {
            prepareForNewProduct();
        } else {
            populateFieldsForEdit();
        }
    }

    private void prepareForNewProduct() {
        cleanControls();
        enableEditControls();
        txtAddProduct.setText("Add Movie");
        btnSaveProduct.setVisible(true);
        btnUpdateProduct.setVisible(false);
    }

    private void initializeImageHoverEffect() {
        imageContainer.hoverProperty().addListener((o, oldV, newV) -> {
            colorAdjust.setBrightness(newV ? 0.25 : 0);
            imageProduct.setEffect(colorAdjust);
        });
        imageContainer.setPadding(new Insets(5));
        imageProduct.setFitHeight(imageContainer.getPrefHeight() - 10);
        imageProduct.setFitWidth(imageContainer.getPrefWidth() - 10);
    }

    private void setValidations() {
        Arrays.asList(txtEnglishName, txtHebrewName, txtProducer, txtDuration, txtTheaterPrice, txtHVPrice, txtGenre)
                .forEach(RequieredFieldsValidators::toTextField);
        RequieredFieldsValidators.toTextArea(txtDescription);
        Arrays.asList(txtEnglishName, txtHebrewName, txtProducer, txtDuration, txtTheaterPrice, txtHVPrice, txtGenre, txtDescription, txtActors)
                .forEach(field -> RequieredFieldsValidators.addLabelBehavior((TextInputControl) field, field.getPromptText()));
    }

    private void setMask() {
        Arrays.asList(txtDuration, txtTheaterPrice, txtHVPrice).forEach(TextFieldMask::onlyNumbers);
    }

    private void selectText() {
        Arrays.asList(txtEnglishName, txtHebrewName, txtProducer, txtDuration, txtTheaterPrice, txtHVPrice, txtGenre)
                .forEach(TextFieldMask::selectText);
        TextFieldMask.selectTextToTextArea(txtDescription);
    }

    private void initializeTextFieldDefaults() {
        Arrays.asList(txtTheaterPrice, txtHVPrice, txtDuration).forEach(TextFieldMask::setTextIfFieldIsEmpty);
    }

    private void characterLimiter() {
        TextFieldMask.characterLimit(txtHVPrice, 3);
        TextFieldMask.characterLimit(txtDuration, 3);
        TextFieldMask.characterLimit(txtTheaterPrice, 3);
    }

    private void closeDialog() {
        // Implement dialog close functionality
    }

    private void populateFieldsForEdit() {
        txtEnglishName.setText(movie.getEnglishName());
        txtHebrewName.setText(movie.getHebrewName());
        txtProducer.setText(movie.getProducer());
        txtDuration.setText(String.valueOf(movie.getDuration()));
        txtTheaterPrice.setText(String.valueOf(movie.getTheaterPrice()));
        txtHVPrice.setText(String.valueOf(movie.getHomeViewingPrice()));
        txtGenre.setText(movie.getGenre());
        txtDescription.setText(movie.getInfo());
        txtActors.setText(String.join(", ", movie.getMainActors()));
        imageProduct.setImage(getImage(movie));
        expandImage(movie, movie.getEnglishName());

        btnUpdateProduct.setVisible(true);
        btnSaveProduct.setVisible(false);
        txtAddProduct.setText("Update Movie");
    }

    @FXML
    private void saveProduct() {
        if (!validateInputs()) return;

        String englishName = txtEnglishName.getText().trim();
        String hebrewName = txtHebrewName.getText().trim();
        String producer = txtProducer.getText().trim();
        String duration = txtDuration.getText().trim();
        String theaterPrice = txtTheaterPrice.getText().trim();
        String hvPrice = txtHVPrice.getText().trim();
        String genre = txtGenre.getText().trim();
        String streamingType = txtStreamingType.getText().trim();
        String description = txtDescription.getText().trim();
        List<String> actors = Arrays.asList(txtActors.getText().trim().split(", "));

        MovieController.addMovie(hebrewName, description, producer, englishName, actors, getInputStream(),
                Movie.StreamingType.valueOf(streamingType), Integer.parseInt(duration),
                Integer.parseInt(theaterPrice), Integer.parseInt(hvPrice), genre);

        cleanControls();
        closeDialog();
    }

    private boolean validateInputs() {
        if (txtEnglishName.getText().trim().isEmpty()) {
            showErrorAndFocus(txtEnglishName);
            return false;
        }
        if (txtHebrewName.getText().trim().isEmpty()) {
            showErrorAndFocus(txtHebrewName);
            return false;
        }
        if (txtProducer.getText().trim().isEmpty()) {
            showErrorAndFocus(txtProducer);
            return false;
        }
        if (txtTheaterPrice.getText().trim().isEmpty()) {
            showErrorAndFocus(txtTheaterPrice);
            return false;
        }
        if (txtHVPrice.getText().trim().isEmpty()) {
            showErrorAndFocus(txtHVPrice);
            return false;
        }
        if (txtGenre.getText().trim().isEmpty()) {
            showErrorAndFocus(txtGenre);
            return false;
        }
        if (txtDescription.getText().trim().isEmpty()) {
            showErrorAndFocus(txtDescription);
            return false;
        }
        if (imageFile != null) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, ConstantsPath.MESSAGE_IMAGE_LARGE);
            return false;
        }
        return true;
    }

    private void showErrorAndFocus(TextInputControl field) {
        field.requestFocus();
        Animations.shake(field);
    }

    private String getInputStream() {
        try (InputStream is = Optional.ofNullable(imageFile)
                .map(this::getFileInputStream)
                .orElseGet(() -> getClass().getResourceAsStream(ConstantsPath.NO_IMAGE_AVAILABLE))) {
            return is != null ? is.toString() : null;
        } catch (IOException e) {
            Logger.getLogger(EditMovieListBoundary.class.getName()).log(Level.SEVERE, null, e);
            NotificationsBuilder.create(NotificationType.INFORMATION, ConstantsPath.MESSAGE_IMAGE_NOT_FOUND);
            return null;
        }
    }

    private InputStream getFileInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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

    private void cleanControls() {
        imageFile = null;
        Arrays.asList(txtEnglishName, txtHebrewName, txtProducer, txtDuration, txtTheaterPrice, txtHVPrice, txtGenre, txtDescription, txtActors)
                .forEach(TextInputControl::clear);
        imageProduct.setImage(new Image(ConstantsPath.NO_IMAGE_AVAILABLE));
    }

    private void enableEditControls() {
        Arrays.asList(txtEnglishName, txtHebrewName, txtProducer, txtDuration, txtTheaterPrice, txtHVPrice, txtGenre, txtDescription, txtActors)
                .forEach(field -> field.setEditable(true));
    }
}
