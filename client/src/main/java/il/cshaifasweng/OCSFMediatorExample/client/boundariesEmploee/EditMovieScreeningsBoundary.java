package il.cshaifasweng.OCSFMediatorExample.client.boundariesEmploee;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.dbDeleteLaterGenerate;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.RequieredFieldsValidators;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.TextFieldMask;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditMovieScreeningsBoundary implements Initializable {
    private final dbDeleteLaterGenerate db = new dbDeleteLaterGenerate();

    private final ColorAdjust colorAdjust = new ColorAdjust();

    private final long LIMIT = 1000000;

    private final String ALREADY_EXISTS = "There is already a product with this barcode";

    private final String IS_GREATER = "Minimum price cannot be higher than sale price";


    @FXML
    private Label txtAddProduct;

    private ObservableList<MovieInstance> listTheater;

    private ObservableList<MovieInstance> filterProducts;

    @FXML
    private Button btnCancelAddProduct;

    @FXML
    private Button btnNewProduct;

    @FXML
    private Button btnSaveProduct;

    @FXML
    private Button btnUpdateProduct;

    @FXML
    private TableColumn<MovieInstance, String> colDate;

    @FXML
    private TableColumn<MovieInstance, String> colEnglish;

    @FXML
    private TableColumn<MovieInstance, String> colHall;

    @FXML
    private TableColumn<MovieInstance, String> colHebrew;

    @FXML
    private TableColumn<MovieInstance, String> colHour;

    @FXML
    private TableColumn<MovieInstance, Integer> colId;

    @FXML
    private TableColumn<MovieInstance, String> colTheater;

    @FXML
    private AnchorPane containerAddProduct;

    @FXML
    private AnchorPane containerDeleteProducts;

    @FXML
    private HBox hBoxSearch;

    @FXML
    private HBox imageContainer;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Pane paneContainer;

    @FXML
    private AnchorPane rootProducts;

    @FXML
    private StackPane stckProducts;

    @FXML
    private TableView<MovieInstance> tblProducts;

    @FXML
    private TextField txtEnglishName;

    @FXML
    private ComboBox<String> cmbHall;

    @FXML
    private ComboBox<String> cmbHour;

    @FXML
    private ComboBox<String> cmbTheater;

    @FXML
    private TextField txtHebrewName;

    @FXML
    private DatePicker dtptDate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtSearchBarCode;

    @FXML
    private TextField txtSearchProduct;


    private DialogTool dialogAddProduct;

    private DialogTool dialogDeleteProduct;

    private static final Stage stage = new Stage();

    private File imageFile;

    private CustomContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listTheater = FXCollections.observableArrayList();
         filterProducts = FXCollections.observableArrayList();
        loadData();
         animateNodes();
        selectText();
        initalizeComboBox();
        setValidations();
        validateUser();
        characterLimiter();
        initializeImage();
        setTextIfFieldIsEmpty();
        closeDialogWithTextFields();

        closeDialogWithEscapeKey();
    }

    private void initalizeComboBox() {

        // Assuming you have predefined lists or methods to get the available options for each ComboBox.
        List<String> availableHalls = Collections.singletonList(db.getHalls().toString()); // Replace with actual method to get hall names
        List<String> availableTheaters = Collections.singletonList(db.getTheaters().toString()); // Replace with actual method to get theater names
        List<String> availableHours = Arrays.asList("10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00");

        cmbHall.setItems(FXCollections.observableArrayList(availableHalls));
        cmbTheater.setItems(FXCollections.observableArrayList(availableTheaters));
        cmbHour.setItems(FXCollections.observableArrayList(availableHours));

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
        RequieredFieldsValidators.toComboBox(cmbHall);
        RequieredFieldsValidators.toComboBox(cmbHour);
        RequieredFieldsValidators.toComboBox(cmbTheater);
          RequieredFieldsValidators.toTextField(txtId);
        RequieredFieldsValidators.addLabelBehavior(txtEnglishName, "English Name");
        RequieredFieldsValidators.addLabelBehavior(txtHebrewName, "Hebrew Name");
        RequieredFieldsValidators.addLabelBehavior(cmbHall, "Date");
        RequieredFieldsValidators.addLabelBehavior(cmbHour, "Hall");
        RequieredFieldsValidators.addLabelBehavior(cmbTheater, "Theater");
        RequieredFieldsValidators.addLabelBehavior(txtId, "ID");
     }



    private void selectText() {
        TextFieldMask.selectText(txtEnglishName);
        TextFieldMask.selectText(txtHebrewName);

        TextFieldMask.selectText(txtId);
     }

    private void setTextIfFieldIsEmpty() {

    }

    private void characterLimiter() {
        TextFieldMask.characterLimit(txtId, 20);


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

        dialogAddProduct.setOnDialogOpened(ev -> txtEnglishName.requestFocus());

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

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colEnglish.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovie().getEnglishName()));
        colHebrew.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovie().getHebrewName()));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime().toLocalDate().toString()));
        colHour.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        colTheater.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHall().getTheater().getLocation()));
      //  colHall.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHall().getName()));

    }



    private void loadTable() {
        listTheater.setAll(db.getMovieInstances());
        tblProducts.setItems(listTheater);
        tblProducts.setFixedCellSize(30);
    }

    private void selectedRecord() {
        MovieInstance movieInstance = tblProducts.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(movieInstance.getId()));
        txtEnglishName.setText(movieInstance.getMovie().getEnglishName());
        txtHebrewName.setText(movieInstance.getMovie().getHebrewName());

        // Set ComboBox values based on the selected MovieInstance
        cmbHall.setValue(movieInstance.getHall().toString());
        cmbTheater.setValue(movieInstance.getHall().getTheater().getLocation());
        cmbHour.setValue(movieInstance.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        // Also set the date picker value
        dtptDate.setValue(movieInstance.getTime().toLocalDate());

    }

    @FXML
    private void newProduct() {
        String id = txtId.getText().trim();
        String englishName = txtEnglishName.getText().trim();
        String hebrewName = txtHebrewName.getText().trim();


        if (id.isEmpty()) {
            txtId.requestFocus();
            Animations.shake(txtId);
            return;
        }

        for (MovieInstance p : listTheater) {
            if (p.getId() == Integer.parseInt(id)) {
                txtId.requestFocus();
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


        if (imageFile != null && imageFile.length() > LIMIT) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, ConstantsPath.MESSAGE_IMAGE_LARGE);
            return;
        }

        MovieInstance movie = new MovieInstance();
        movie.getMovie().setEnglishName(englishName);
        movie.getMovie().setHebrewName(hebrewName);
         movie.getMovie().setImage(getInputStream());

        listTheater.add(movie);
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
                is = EditMovieListBoundary.class.getResourceAsStream(ConstantsPath.NO_IMAGE_AVAILABLE);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EditMovieListBoundary.class.getName()).log(Level.SEVERE, null, ex);
            NotificationsBuilder.create(NotificationType.INFORMATION, ConstantsPath.MESSAGE_IMAGE_NOT_FOUND);
            is = EditMovieListBoundary.class.getResourceAsStream(ConstantsPath.NO_IMAGE_AVAILABLE);
        }
        return is.toString();
    }

    private Image getImage(int id) {
        // חפש את הסרט לפי ה-ID
        MovieInstance movie = null;
        for (MovieInstance m : listTheater) {
            if (m.getId() == id) {
                movie = m;
                break;
            }
        }

        if (movie == null || movie.getMovie().getImage() == null) {
            return new Image(ConstantsPath.NO_IMAGE_AVAILABLE, true); // Remove size constraints
        }

        String imagePath = ConstantsPath.MOVIE_PACKAGE + movie.getMovie().getImage();
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
        String id = txtId.getText().trim();
        String englishName = txtEnglishName.getText().trim();
        String hebrewName = txtHebrewName.getText().trim();

        MovieInstance selectedProduct = tblProducts.getSelectionModel().getSelectedItem();

        if (id.isEmpty()) {
            txtId.requestFocus();
            Animations.shake(txtId);
            return;
        }

        for (MovieInstance p : listTheater) {
            if (p.getId() == Integer.parseInt(id) && p != selectedProduct) {
                txtId.requestFocus();
                Animations.shake(txtId);
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


        if (imageFile != null && imageFile.length() > LIMIT) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, ConstantsPath.MESSAGE_IMAGE_LARGE);
            return;
        }

        MovieInstance movie = tblProducts.getSelectionModel().getSelectedItem();
        movie.getMovie().setEnglishName(englishName);
        movie.getMovie().setHebrewName(hebrewName);

        movie.getMovie().setImage(getInputStream());
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

        listTheater.remove(tblProducts.getSelectionModel().getSelectedItem());
        loadData();
        cleanControls();
        hideDialogDeleteProduct();
        AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_DELETED);
    }

    private void cleanControls() {
        imageFile = null;
        txtId.clear();
        txtEnglishName.clear();
        txtHebrewName.clear();

        imageProduct.setImage(new Image(ConstantsPath.NO_IMAGE_AVAILABLE));
    }

    private void disableEditControls() {
        txtId.setEditable(false);
        txtEnglishName.setEditable(false);
        txtHebrewName.setEditable(false);

     }

    private void enableEditControls() {
        txtId.setEditable(true);
        txtEnglishName.setEditable(true);
        txtHebrewName.setEditable(true);

     }

    private void disableTable() {
        tblProducts.setDisable(true);
    }

    private void resetValidation() {
        txtId.clear();
        txtEnglishName.clear();
        txtHebrewName.clear();


    }

    private void validateUser() {
        setContextMenu();
        deleteUserDeleteKey();

        btnNewProduct.setDisable(false);

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

            }
        });
    }

    private void closeDialogWithTextFields() {
        txtId.setOnKeyReleased(ev -> {
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
            tblProducts.setItems(listTheater);
        } else {
            filterProducts.clear();
            for (MovieInstance p : listTheater) {
                if (p.getMovie().getEnglishName().toLowerCase().contains(filterName.toLowerCase())) {
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
            tblProducts.setItems(listTheater);
        } else {
            filterProducts.clear();
            for (MovieInstance p : listTheater) {
                if (p.getMovie().getEnglishName().toLowerCase().contains(filterCodeBar.toLowerCase())) {
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