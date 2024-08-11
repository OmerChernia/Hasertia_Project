package il.cshaifasweng.OCSFMediatorExample.client.boundaries.contentManager;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieInstanceController;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class EditMovieScreeningsBoundary implements Initializable {

    private final ColorAdjust colorAdjust = new ColorAdjust();
    private ObservableList<MovieInstance> listTheater;
    private ObservableList<MovieInstance> filterProducts;
    private DialogTool dialogAddProduct;
    private DialogTool dialogDeleteProduct;
    private static final Stage stage = new Stage();
    private CustomContextMenu contextMenu;

    @FXML
    private Label txtAddProduct;

    @FXML
    private Button btnCancelAddProduct, btnNewProduct, btnSaveProduct, btnUpdateProduct;

    @FXML
    private TableColumn<MovieInstance, String> colDate, colEnglish, colHall, colHebrew, colHour, colTheater;

    @FXML
    private TableColumn<MovieInstance, Integer> colId;

    @FXML
    private AnchorPane containerAddProduct, containerDeleteProducts, rootProducts;

    @FXML
    private HBox hBoxSearch, imageContainer;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Pane paneContainer;

    @FXML
    private StackPane stckProducts;

    @FXML
    private TableView<MovieInstance> tblProducts;

    @FXML
    private TextField txtEnglishName, txtHebrewName, txtId, txtSearchBarCode, txtSearchProduct;

    @FXML
    private ComboBox<String> cmbHall, cmbHour, cmbTheater;

    @FXML
    private DatePicker dtptDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Register this controller to listen for MovieInstanceMessage events
        EventBus.getDefault().register(this);

        // Request the list of movie instances from the server
        MovieInstanceController.requestAllMovieInstances();

        listTheater = FXCollections.observableArrayList();
        filterProducts = FXCollections.observableArrayList();

        animateNodes();
        selectText();
        initializeComboBox();
        setValidations();
        validateUser();
        characterLimiter();
         closeDialogWithTextFields();
        closeDialogWithEscapeKey();
    }

    private void initializeComboBox() {
        List<String> availableHours = Arrays.asList("10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00");
        cmbHour.setItems(FXCollections.observableArrayList(availableHours));
    }

    @Subscribe
    public void loadData(MovieInstanceMessage movieInstanceMessage) {
        listTheater.setAll(movieInstanceMessage.movies);
        tblProducts.setItems(listTheater);
        tblProducts.setFixedCellSize(30);

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colEnglish.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovie().getEnglishName()));
        colHebrew.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovie().getHebrewName()));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime().toLocalDate().toString()));
        colHour.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        colTheater.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHall().getTheater().getLocation()));
        colHall.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getHall().getId())));

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
                NotificationsBuilder.create(NotificationType.ERROR, "There is already a product with this barcode");
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

        // Code to handle adding the new product goes here
        MovieInstance movieInstance = new MovieInstance();
        movieInstance.getMovie().setEnglishName(englishName);
        movieInstance.getMovie().setHebrewName(hebrewName);
        movieInstance.getMovie().setImage(getInputStream());

        listTheater.add(movieInstance);
        cleanControls();
        closeDialogAddProduct();
        AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_ADDED);
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
                NotificationsBuilder.create(NotificationType.ERROR, "There is already a product with this barcode");
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

        selectedProduct.getMovie().setEnglishName(englishName);
        selectedProduct.getMovie().setHebrewName(hebrewName);
        selectedProduct.getMovie().setImage(getInputStream());

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
        cleanControls();
        hideDialogDeleteProduct();
        AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_DELETED);
    }

    private String getInputStream() {
        // Implementation to handle image input stream
        // This method should return the path to the image or null if none exists
        return null; // Placeholder, replace with actual logic
    }

    private void cleanControls() {
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

    private void animateNodes() {
        Animations.fadeInUp(btnNewProduct);
        Animations.fadeInUp(tblProducts);
        Animations.fadeInUp(hBoxSearch);
    }

    private void selectText() {
        // Implementation to select text
    }

    private void setValidations() {
        // Implementation to set validations
    }

    private void characterLimiter() {
        // Implementation to limit characters
    }

    public static void closeStage() {
        if (stage != null) {
            stage.hide();
        }
    }
}
