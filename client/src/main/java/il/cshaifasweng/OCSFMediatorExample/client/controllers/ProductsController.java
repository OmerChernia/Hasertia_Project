package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import il.cshaifasweng.OCSFMediatorExample.client.GenerateDB;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.mask.RequieredFieldsValidators;
import il.cshaifasweng.OCSFMediatorExample.client.mask.TextFieldMask;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductsController implements Initializable {
    private final GenerateDB db = new GenerateDB();

    private final ColorAdjust colorAdjust = new ColorAdjust();

    private final long LIMIT = 1000000;

    private final String ALREADY_EXISTS = "There is already a product with this barcode";

    private final String IS_GREATER = "Minimum price cannot be higher than sale price";


    @FXML
    private TextArea txtAddProduct;

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
    private TableColumn<Movie, String> colStreamingType;

    @FXML
    private TableColumn<Movie, Integer> colDuration;

    @FXML
    private TableColumn<Movie, String> colDirector;

    @FXML
    private TableColumn<Movie, Double> colTheaterPrice;

    @FXML
    private TableColumn<Movie, Double> colHVPrice;

    @FXML
    private TableColumn<Movie, String> colGenre;

    @FXML
    private AnchorPane containerAddProduct;



    @FXML
    private TextField txtID;

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
        closeDialogWithTextFields();
        closeDialogWithEscapeKey();
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

        contextMenu.setActionDetails(ev -> {
            showDialogDetailsProduct();
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
        RequieredFieldsValidators.toTextField(txtID);
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
        TextFieldMask.selectTextToJFXTextArea(txtDescription);
    }

    private void setTextIfFieldIsEmpty() {
        TextFieldMask.setTextIfFieldIsEmpty(txtTheaterPrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtHVPrice);
    }

    private void characterLimiter() {
        TextFieldMask.characterLimit(txtID, 20);
    }

    @FXML
    private void showDialogAddProduct() {
        resetValidation();
        enableEditControls();
        disableTable();
        rootProducts.setEffect(Constants.BOX_BLUR_EFFECT);

        txtAddProduct.setText("Add Movie");
        imageContainer.toFront();
        containerAddProduct.setVisible(true);
        btnSaveProduct.setDisable(false);
        btnUpdateProduct.setVisible(true);
        btnSaveProduct.toFront();

        dialogAddProduct = new DialogTool(containerAddProduct, stckProducts);
        dialogAddProduct.show();

        dialogAddProduct.setOnDialogOpened(ev -> {
            txtEnglishName.requestFocus();
        });

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
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        rootProducts.setEffect(Constants.BOX_BLUR_EFFECT);
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
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
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
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
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
        colStreamingType.setCellValueFactory(new PropertyValueFactory<>("streamingType"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colTheaterPrice.setCellValueFactory(new PropertyValueFactory<>("theaterPrice"));
        colHVPrice.setCellValueFactory(new PropertyValueFactory<>("homeViewingPrice"));
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
        imageProduct.setImage(getImage(movie.getId()));
        expandImage(movie.getId(), movie.getEnglishName());
    }

    @FXML
    private void newProduct() {
        String barcode = txtID.getText().trim();
        String englishName = txtEnglishName.getText().trim();
        String hebrewName = txtHebrewName.getText().trim();
        String producer = txtProducer.getText().trim();
        String duration = txtDuration.getText().trim();
        String theaterPrice = txtTheaterPrice.getText().trim();
        String hvPrice = txtHVPrice.getText().trim();
        String genre = txtGenre.getText().trim();
        String description = txtDescription.getText().trim();

        if (barcode.isEmpty()) {
            txtID.requestFocus();
            Animations.shake(txtID);
            return;
        }

        for (Movie p : listProducts) {
            if (p.getId() == Integer.parseInt(barcode)) {
                txtID.requestFocus();
                NotificationsBuilder.create(NotificationType.ERROR, ALREADY_EXISTS);
                return;
            }
        }

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
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_IMAGE_LARGE);
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
        movie.setInfo(description);
        movie.setImage(getInputStream());

        listProducts.add(movie);
        loadData();
        cleanControls();
        closeDialogAddProduct();
        AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_ADDED);
    }

    private String getInputStream() {
        InputStream is;
        try {
            if (imageFile != null) {
                is = new FileInputStream(imageFile);
            } else {
                is = ProductsController.class.getResourceAsStream(Constants.NO_IMAGE_AVAILABLE);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
            NotificationsBuilder.create(NotificationType.INFORMATION, Constants.MESSAGE_IMAGE_NOT_FOUND);
            is = ProductsController.class.getResourceAsStream(Constants.NO_IMAGE_AVAILABLE);
        }
        return is.toString();
    }

    private Image getImage(int id) {
        return new Image(Constants.NO_IMAGE_AVAILABLE, 200, 200, true, true);
    }

    @FXML
    private void updateProduct() {
        String barcode = txtID.getText().trim();
        String englishName = txtEnglishName.getText().trim();
        String hebrewName = txtHebrewName.getText().trim();
        String producer = txtProducer.getText().trim();
        String duration = txtDuration.getText().trim();
        String theaterPrice = txtTheaterPrice.getText().trim();
        String hvPrice = txtHVPrice.getText().trim();
        String genre = txtGenre.getText().trim();
        String description = txtDescription.getText().trim();
        Movie selectedProduct = tblProducts.getSelectionModel().getSelectedItem();

        if (barcode.isEmpty()) {
            txtID.requestFocus();
            Animations.shake(txtID);
            return;
        }

        for (Movie p : listProducts) {
            if (p.getId() == Integer.parseInt(barcode) && p != selectedProduct) {
                txtID.requestFocus();
                Animations.shake(txtID);
                NotificationsBuilder.create(NotificationType.ERROR, ALREADY_EXISTS);
                return;
            }
        }

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
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_IMAGE_LARGE);
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

        loadData();
        cleanControls();
        closeDialogAddProduct();
        AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_UPDATED);
    }

    @FXML
    private void deleteProducts() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        listProducts.remove(tblProducts.getSelectionModel().getSelectedItem());
        loadData();
        cleanControls();
        hideDialogDeleteProduct();
        AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_DELETED);
    }

    private void cleanControls() {
        imageFile = null;
        txtID.clear();
        txtEnglishName.clear();
        txtHebrewName.clear();
        txtProducer.clear();
        txtDuration.clear();
        txtTheaterPrice.clear();
        txtHVPrice.clear();
        txtGenre.clear();
        txtDescription.clear();
        imageProduct.setImage(new Image(Constants.NO_IMAGE_AVAILABLE));
    }

    private void disableEditControls() {
        txtID.setEditable(false);
        txtEnglishName.setEditable(false);
        txtHebrewName.setEditable(false);
        txtProducer.setEditable(false);
        txtDuration.setEditable(false);
        txtTheaterPrice.setEditable(false);
        txtHVPrice.setEditable(false);
        txtGenre.setEditable(false);
        txtDescription.setEditable(false);
    }

    private void enableEditControls() {
        txtID.setEditable(true);
        txtEnglishName.setEditable(true);
        txtHebrewName.setEditable(true);
        txtProducer.setEditable(true);
        txtDuration.setEditable(true);
        txtTheaterPrice.setEditable(true);
        txtHVPrice.setEditable(true);
        txtGenre.setEditable(true);
        txtDescription.setEditable(true);
    }

    private void disableTable() {
        tblProducts.setDisable(true);
    }

    private void resetValidation() {
        txtID.clear();
        txtEnglishName.clear();
        txtHebrewName.clear();
        txtProducer.clear();
        txtDuration.clear();
        txtTheaterPrice.clear();
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

    private void closeDialogWithEscapeKey() {
        rootProducts.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                hideDialogDeleteProduct();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                tblProducts.setDisable(false);
                rootProducts.setEffect(null);
                AlertsBuilder.close();
            }
        });
    }

    private void closeDialogWithTextFields() {
        txtID.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtEnglishName.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtHebrewName.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });


        txtProducer.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtDuration.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtTheaterPrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtHVPrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtGenre.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtDescription.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });
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
                    AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
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
            final ImageView imageView = new ImageView("image");
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(550);

            final BorderPane boderPane = new BorderPane(imageView);
            boderPane.setStyle("-fx-background-color: white");
            boderPane.setCenter(imageView);

            final ScrollPane root = new ScrollPane(boderPane);
            root.setStyle("-fx-background-color: white");
            root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            root.getStylesheets().add(Constants.LIGHT_THEME);
            root.getStyleClass().add("scroll-bar");

            root.setFitToHeight(true);
            root.setFitToWidth(true);

            stage.getIcons().add(new Image(Constants.STAGE_ICON));
            stage.setScene(new Scene(root, 550, 555));
            stage.setTitle(title);
            stage.show();
        });
    }
}
