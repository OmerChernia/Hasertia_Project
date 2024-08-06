package il.cshaifasweng.OCSFMediatorExample.client.boundariesEmploee;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.DBGenerate;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.RequieredFieldsValidators;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.TextFieldMask;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditMovieListController implements Initializable {
    private final DBGenerate db = new DBGenerate();

    private final ColorAdjust colorAdjust = new ColorAdjust();

    private final long LIMIT = 1000000;

    private final String ALREADY_EXISTS = "There is already a product with this barcode";

    private final String IS_GREATER = "Minimum price cannot be higher than sale price";


    @FXML
    private Label txtAddProduct;

    private ObservableList<Movie> listProducts;

    private ObservableList<Movie> filterProducts;

    @FXML
    private StackPane stckProducts;

    @FXML
    private AnchorPane rootProducts;

    @FXML
    private AnchorPane containerDeleteProducts;

    @FXML
    private HBox hBoxSearch;

    @FXML
    private TextField txtSearchProduct;

    @FXML
    private TextField txtSearchBarCode;

    @FXML
    private Button btnNewProduct;

    @FXML
    private TableView<Movie> tblProducts;

    @FXML
    private TableColumn<Movie, Integer> colId;

    @FXML
    private TableColumn<Movie, String> colEnglish;

    @FXML
    private TableColumn<Movie, String> colHebrew;

    @FXML
    private TableColumn<Movie, Button> colStreamingType;

    @FXML
    private TableColumn<Movie, Integer> colDuration;

    @FXML
    private TableColumn<Movie, String> colDirector;

    @FXML
    private TableColumn<Movie, Double> colTheaterPrice;

    @FXML
    private TableColumn<Movie, Double> colHVPrice;

    @FXML
    private TableColumn<Movie, Button> colGenre;

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
    private Button btnCancelAddProduct;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Pane paneContainer;

    @FXML
    private HBox imageContainer;

    private DialogTool dialogAddProduct;

    private DialogTool dialogDeleteProduct;

    private static final Stage stage = new Stage();

    private File imageFile;

    private CustomContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listProducts = FXCollections.observableArrayList();
        filterProducts = FXCollections.observableArrayList();
        loadData();
        setMask();
        animateNodes();
        selectText();
        setValidations();
        validateUser();
        characterLimiter();
        initializeImage();
        setTextIfFieldIsEmpty();
        // הוספת מאזין לאירוע דאבל קליק על שורות הטבלה
        tblProducts.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Movie rowData = row.getItem();
                    // כאן את יכולה לקרוא לפונקציה שתפתח את הדיאלוג
                    showDialogDetailsProduct();
                }
            });
            return row;
        });
     }

    private void setContextMenu() {
        contextMenu = new CustomContextMenu(tblProducts);

        contextMenu.setActionEdit(ev -> {
            showDialogEditProduct();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDeleteProduct();
            contextMenu.hide();
        });



        contextMenu.show();
    }

    private void initializeImage() {
        imageContainer.hoverProperty().addListener((o, oldV, newV) -> {
            if (newV) {
                colorAdjust.setBrightness(0.25);
                imageProduct.setEffect(colorAdjust);
            } else {
                imageProduct.setEffect(null);
            }
        });

        imageContainer.setPadding(new Insets(5));
        filterProducts = FXCollections.observableArrayList();
        imageProduct.setFitHeight(imageContainer.getPrefHeight() - 10);
        imageProduct.setFitWidth(imageContainer.getPrefWidth() - 10);
    }

    private void animateNodes() {
        Animations.fadeInUp(btnNewProduct);
        Animations.fadeInUp(tblProducts);
        Animations.fadeInUp(hBoxSearch);
    }

    private void setValidations() {
        RequieredFieldsValidators.toTextField(txtEnglishName);
        RequieredFieldsValidators.toTextField(txtHebrewName);
        RequieredFieldsValidators.toTextField(txtProducer);
        RequieredFieldsValidators.toTextField(txtDuration);
        RequieredFieldsValidators.toTextField(txtTheaterPrice);
        RequieredFieldsValidators.toTextField(txtHVPrice);
        RequieredFieldsValidators.toTextField(txtGenre);
        RequieredFieldsValidators.toTextArea(txtDescription);
         RequieredFieldsValidators.addLabelBehavior(txtEnglishName, "English Name");
        RequieredFieldsValidators.addLabelBehavior(txtHebrewName, "Hebrew Name");
        RequieredFieldsValidators.addLabelBehavior(txtProducer, "Producer");
        RequieredFieldsValidators.addLabelBehavior(txtDuration, "Duration");
        RequieredFieldsValidators.addLabelBehavior(txtTheaterPrice, "Theater Price");
        RequieredFieldsValidators.addLabelBehavior(txtHVPrice, "HV Price");
        RequieredFieldsValidators.addLabelBehavior(txtGenre, "Genre");
        RequieredFieldsValidators.addLabelBehavior(txtDescription, "Description");
         RequieredFieldsValidators.addLabelBehavior(txtActors, "Actors");
    }

    private void setMask() {
        TextFieldMask.onlyNumbers(txtDuration);
        TextFieldMask.onlyNumbers(txtTheaterPrice);
        TextFieldMask.onlyNumbers(txtHVPrice);
    }

    private void selectText() {
        TextFieldMask.selectText(txtEnglishName);
        TextFieldMask.selectText(txtHebrewName);
        TextFieldMask.selectText(txtProducer);
        TextFieldMask.selectText(txtDuration);
        TextFieldMask.selectText(txtTheaterPrice);
        TextFieldMask.selectText(txtHVPrice);
        TextFieldMask.selectText(txtGenre);
        TextFieldMask.selectTextToTextArea(txtDescription);
    }

    private void setTextIfFieldIsEmpty() {
        TextFieldMask.setTextIfFieldIsEmpty(txtTheaterPrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtHVPrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtDuration);
    }

    private void characterLimiter() {
         TextFieldMask.characterLimit(txtHVPrice, 3);
        TextFieldMask.characterLimit(txtDuration, 3);
        TextFieldMask.characterLimit(txtTheaterPrice, 3);

    }

    @FXML
    private void showDialogAddProduct() {
        resetValidation();
        enableEditControls();
        disableTable();
        rootProducts.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

        txtAddProduct.setText("Add Movie");
        imageContainer.toFront();
        containerAddProduct.setVisible(true);
        btnSaveProduct.setDisable(false);
        btnUpdateProduct.setVisible(true);
        btnSaveProduct.toFront();

        dialogAddProduct = new DialogTool(containerAddProduct, stckProducts);

        dialogAddProduct.show();


        dialogAddProduct.setOnDialogClosed(ev -> {
            closeStage();
            tblProducts.setDisable(false);
            rootProducts.setEffect(null);
            containerAddProduct.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogAddProduct() {
        if (dialogAddProduct != null) {
            dialogAddProduct.close();
        }
    }

    @FXML
    private void showDialogDeleteProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        rootProducts.setEffect(ConstantsPath.BOX_BLUR_EFFECT);
        containerDeleteProducts.setVisible(true);
        disableTable();

        dialogDeleteProduct = new DialogTool(containerDeleteProducts, stckProducts);
        dialogDeleteProduct.show();

        dialogDeleteProduct.setOnDialogClosed(ev -> {
            tblProducts.setDisable(false);
            rootProducts.setEffect(null);
            containerDeleteProducts.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void hideDialogDeleteProduct() {
        if (dialogDeleteProduct != null) {
            dialogDeleteProduct.close();
        }
    }

    @FXML
    private void showDialogEditProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddProduct();
        btnUpdateProduct.toFront();
        txtAddProduct.setText("Update Movie");
        selectedRecord();
    }

    @FXML
    private void showDialogDetailsProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddProduct();
        txtAddProduct.setText("Movie Details");
        selectedRecord();
        paneContainer.toFront();
        btnUpdateProduct.setVisible(false);
        btnSaveProduct.setDisable(true);
        btnSaveProduct.toFront();
        disableEditControls();
    }

    @FXML
    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEnglish.setCellValueFactory(new PropertyValueFactory<>("englishName"));
        colHebrew.setCellValueFactory(new PropertyValueFactory<>("hebrewName"));
         colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
         colTheaterPrice.setCellValueFactory(new PropertyValueFactory<>("theaterPrice"));
        colHVPrice.setCellValueFactory(new PropertyValueFactory<>("homeViewingPrice"));
         ButtonFactory buttonFactory = new ButtonFactory();

         ButtonFactory.ButtonGenreCellValueFactory buttonGenreCellFactory = buttonFactory.new ButtonGenreCellValueFactory();

         colGenre.setCellValueFactory(buttonGenreCellFactory);

        ButtonFactory.ButtonMovieTypeCellValueFactory buttonTypeCellFactory = buttonFactory.new ButtonMovieTypeCellValueFactory();
        colStreamingType.setCellValueFactory(buttonTypeCellFactory);

    }

    private void loadTable() {
        listProducts.setAll(db.getMovies());
        tblProducts.setItems(listProducts);
        tblProducts.setFixedCellSize(30);
    }

    private void selectedRecord() {
        Movie movie = tblProducts.getSelectionModel().getSelectedItem();
        txtEnglishName.setText(movie.getEnglishName());
        txtHebrewName.setText(movie.getHebrewName());
        txtProducer.setText(movie.getProducer());
        txtDuration.setText(String.valueOf(movie.getDuration()));
        txtTheaterPrice.setText(String.valueOf(movie.getTheaterPrice()));
        txtHVPrice.setText(String.valueOf(movie.getHomeViewingPrice()));
        txtGenre.setText(movie.getGenre());
        txtDescription.setText(movie.getInfo());
        txtActors.setText(movie.getMainActors().toString().replace("[", "").replace("]", ""));
        imageProduct.setImage(getImage(movie.getId()));
        expandImage(movie.getId(), movie.getEnglishName());
    }

    @FXML
    private void newProduct() {
         String englishName = txtEnglishName.getText().trim();
        String hebrewName = txtHebrewName.getText().trim();
        String producer = txtProducer.getText().trim();
        String duration = txtDuration.getText().trim();
        String theaterPrice = txtTheaterPrice.getText().trim();
        String hvPrice = txtHVPrice.getText().trim();
        String genre = txtGenre.getText().trim();
        String description = txtDescription.getText().trim();
        String actors = txtActors.getText().trim();




        if (englishName.isEmpty()) {
            txtEnglishName.requestFocus();
            Animations.shake(txtEnglishName);
            return;
        }

        if (hebrewName.isEmpty()) {
            txtHebrewName.requestFocus();
            Animations.shake(txtHebrewName);
            return;
        }


        if (producer.isEmpty()) {
            txtProducer.requestFocus();
            Animations.shake(txtProducer);
            return;
        }

        if (duration.isEmpty()) {
            txtDuration.requestFocus();
            Animations.shake(txtDuration);
            return;
        }

        if (theaterPrice.isEmpty()) {
            txtTheaterPrice.requestFocus();
            Animations.shake(txtTheaterPrice);
            return;
        }

        if (hvPrice.isEmpty()) {
            txtHVPrice.requestFocus();
            Animations.shake(txtHVPrice);
            return;
        }

        if (genre.isEmpty()) {
            txtGenre.requestFocus();
            Animations.shake(txtGenre);
            return;
        }

        if (description.isEmpty()) {
            txtDescription.requestFocus();
            Animations.shake(txtDescription);
            return;
        }

        if (imageFile != null && imageFile.length() > LIMIT) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, ConstantsPath.MESSAGE_IMAGE_LARGE);
            return;
        }

        Movie movie = new Movie();
        movie.setEnglishName(englishName);
        movie.setHebrewName(hebrewName);
        movie.setProducer(producer);
        movie.setDuration(Integer.parseInt(duration));
        movie.setTheaterPrice((int) Double.parseDouble(theaterPrice));
        movie.setHomeViewingPrice((int) Double.parseDouble(hvPrice));
        movie.setGenre(genre);
        List<String> actorsList = Arrays.asList(actors.split(", "));
        movie.setMainActors(actorsList);
        movie.setInfo(description);
        movie.setImage(getInputStream());

        listProducts.add(movie);
        loadData();
        cleanControls();
        closeDialogAddProduct();
        AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_ADDED);
    }

    private String getInputStream() {
        InputStream is;
        try {
            if (imageFile != null) {
                is = new FileInputStream(imageFile);
            } else {
                is = EditMovieListController.class.getResourceAsStream(ConstantsPath.NO_IMAGE_AVAILABLE);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EditMovieListController.class.getName()).log(Level.SEVERE, null, ex);
            NotificationsBuilder.create(NotificationType.INFORMATION, ConstantsPath.MESSAGE_IMAGE_NOT_FOUND);
            is = EditMovieListController.class.getResourceAsStream(ConstantsPath.NO_IMAGE_AVAILABLE);
        }
        return is.toString();
    }

    private Image getImage(int id) {
        // חפש את הסרט לפי ה-ID
        Movie movie = null;
        for (Movie m : listProducts) {
            if (m.getId() == id) {
                movie = m;
                break;
            }
        }

        if (movie == null || movie.getImage() == null) {
            return new Image(ConstantsPath.NO_IMAGE_AVAILABLE, true); // Remove size constraints
        }

        String imagePath = ConstantsPath.MOVIE_PACKAGE + movie.getImage();
        URL imageUrl = getClass().getResource(imagePath);

        if (imageUrl == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(ConstantsPath.NO_IMAGE_AVAILABLE, true); // Remove size constraints
        }

        try {
            return new Image(imageUrl.toExternalForm(), true); // Remove size constraints
        } catch (Exception e) {
            System.out.println("Failed to load image: " + imagePath);
            e.printStackTrace();
            return new Image(ConstantsPath.NO_IMAGE_AVAILABLE, true); // Remove size constraints
        }
    }


    @FXML
    private void updateProduct() {
         String englishName = txtEnglishName.getText().trim();
        String hebrewName = txtHebrewName.getText().trim();
        String producer = txtProducer.getText().trim();
        String duration = txtDuration.getText().trim();
        String theaterPrice = txtTheaterPrice.getText().trim();
        String hvPrice = txtHVPrice.getText().trim();
        String genre = txtGenre.getText().trim();
        String description = txtDescription.getText().trim();
        String actors = txtActors.getText().trim();
        Movie selectedProduct = tblProducts.getSelectionModel().getSelectedItem();




        if (englishName.isEmpty()) {
            txtEnglishName.requestFocus();
            Animations.shake(txtEnglishName);
            return;
        }

        if (hebrewName.isEmpty()) {
            txtHebrewName.requestFocus();
            Animations.shake(txtHebrewName);
            return;
        }


        if (producer.isEmpty()) {
            txtProducer.requestFocus();
            Animations.shake(txtProducer);
            return;
        }

        if (duration.isEmpty()) {
            txtDuration.requestFocus();
            Animations.shake(txtDuration);
            return;
        }

        if (theaterPrice.isEmpty()) {
            txtTheaterPrice.requestFocus();
            Animations.shake(txtTheaterPrice);
            return;
        }

        if (hvPrice.isEmpty()) {
            txtHVPrice.requestFocus();
            Animations.shake(txtHVPrice);
            return;
        }

        if (genre.isEmpty()) {
            txtGenre.requestFocus();
            Animations.shake(txtGenre);
            return;
        }

        if (description.isEmpty()) {
            txtDescription.requestFocus();
            Animations.shake(txtDescription);
            return;
        }

        if (imageFile != null && imageFile.length() > LIMIT) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, ConstantsPath.MESSAGE_IMAGE_LARGE);
            return;
        }

        Movie movie = tblProducts.getSelectionModel().getSelectedItem();
        movie.setEnglishName(englishName);
        movie.setHebrewName(hebrewName);
        movie.setProducer(producer);
        movie.setDuration(Integer.parseInt(duration));
        movie.setTheaterPrice((int) Double.parseDouble(theaterPrice));
        movie.setHomeViewingPrice((int) Double.parseDouble(hvPrice));
        movie.setGenre(genre);
        movie.setInfo(description);
        movie.setImage(getInputStream());
        List<String> actorsList = Arrays.asList(actors.split(", "));
        movie.setMainActors(actorsList);
        loadData();
        cleanControls();
        closeDialogAddProduct();
        AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_UPDATED);
    }

    @FXML
    private void deleteProducts() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        listProducts.remove(tblProducts.getSelectionModel().getSelectedItem());
        loadData();
        cleanControls();
        hideDialogDeleteProduct();
        AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_DELETED);
    }

    private void cleanControls() {
        imageFile = null;
         txtEnglishName.clear();
        txtHebrewName.clear();
        txtProducer.clear();
        txtDuration.clear();
        txtTheaterPrice.clear();
        txtHVPrice.clear();
        txtGenre.clear();
        txtDescription.clear();
        txtActors.clear();
        imageProduct.setImage(new Image(ConstantsPath.NO_IMAGE_AVAILABLE));
    }

    private void disableEditControls() {
         txtEnglishName.setEditable(false);
        txtHebrewName.setEditable(false);
        txtProducer.setEditable(false);
        txtDuration.setEditable(false);
        txtTheaterPrice.setEditable(false);
        txtHVPrice.setEditable(false);
        txtGenre.setEditable(false);
        txtDescription.setEditable(false);
        txtActors.setEditable(false);
    }

    private void enableEditControls() {
         txtEnglishName.setEditable(true);
        txtHebrewName.setEditable(true);
        txtProducer.setEditable(true);
        txtDuration.setEditable(true);
        txtTheaterPrice.setEditable(true);
        txtHVPrice.setEditable(true);
        txtGenre.setEditable(true);
        txtDescription.setEditable(true);
        txtActors.setEditable(true);
    }

    private void disableTable() {
        tblProducts.setDisable(true);
    }

    private void resetValidation() {
         txtEnglishName.clear();
        txtHebrewName.clear();
        txtProducer.clear();
        txtDuration.clear();
        txtTheaterPrice.clear();
        txtActors.clear();
        txtHVPrice.clear();
        txtGenre.clear();
        txtDescription.clear();
    }

    private void validateUser() {
        setContextMenu();
        deleteUserDeleteKey();

        colTheaterPrice.setVisible(true);
        colHVPrice.setVisible(true);
        btnNewProduct.setDisable(false);
        txtTheaterPrice.setVisible(true);
        txtHVPrice.setVisible(true);
    }

    private void setDisableMenuItem() {
        contextMenu.getEditButton().setDisable(true);
        contextMenu.getDeleteButton().setDisable(true);
    }

    private void setEnableMenuItem() {
        contextMenu.getEditButton().setDisable(false);
        contextMenu.getDeleteButton().setDisable(false);
    }





    public static void closeStage() {
        if (stage != null) {
            stage.hide();
        }
    }

    private void deleteUserDeleteKey() {
        rootProducts.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (tblProducts.isDisable()) {
                    return;
                }

                if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
                    AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
                    return;
                }

                deleteProducts();
            }
        });
    }

    @FXML
    private void filterNameProduct() {
        String filterName = txtSearchProduct.getText().trim();
        if (filterName.isEmpty()) {
            tblProducts.setItems(listProducts);
        } else {
            filterProducts.clear();
            for (Movie p : listProducts) {
                if (p.getEnglishName().toLowerCase().contains(filterName.toLowerCase())) {
                    filterProducts.add(p);
                }
            }
            tblProducts.setItems(filterProducts);
        }
    }

    @FXML
    private void filterCodeBar() {
        String filterCodeBar = txtSearchBarCode.getText().trim();
        if (filterCodeBar.isEmpty()) {
            tblProducts.setItems(listProducts);
        } else {
            filterProducts.clear();
            for (Movie p : listProducts) {
                if (p.getEnglishName().toLowerCase().contains(filterCodeBar.toLowerCase())) {
                    filterProducts.add(p);
                }
            }
            tblProducts.setItems(filterProducts);
        }
    }

    private void expandImage(int id, String title) {
        paneContainer.hoverProperty().addListener((o, oldV, newV) -> {
            if (newV) {
                colorAdjust.setBrightness(0.25);
                imageProduct.setEffect(colorAdjust);
            } else {
                imageProduct.setEffect(null);
            }
        });

        paneContainer.setOnMouseClicked(ev -> {
            final Image image = getImage(id);
            final ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(550);

            final BorderPane boderPane = new BorderPane(imageView);
            boderPane.setStyle("-fx-background-color: white");
            boderPane.setCenter(imageView);

            final ScrollPane root = new ScrollPane(boderPane);
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
}