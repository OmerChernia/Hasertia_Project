package il.cshaifasweng.OCSFMediatorExample.client.boundaries.contentManager;

import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieController;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.RequieredFieldsValidators;
import il.cshaifasweng.OCSFMediatorExample.client.util.mask.TextFieldMask;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
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
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

public class EditMovieListBoundary implements Initializable {

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
    private TableColumn<Movie, Double> colTheaterPrice;

    @FXML
    private TableColumn<Movie, Double> colHVPrice;

    @FXML
    private TableColumn<Movie, Button> colGenre;

    @FXML
    private AnchorPane containerAddProduct;

    private DialogTool dialogAddProduct;

    private DialogTool dialogDeleteProduct;

    private CustomContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Register this controller to listen for MovieMessage events
        EventBus.getDefault().register(this);

        // Request the list of movies from the server
        MovieController.requestAllMovies();
        listProducts = FXCollections.observableArrayList();
        filterProducts = FXCollections.observableArrayList();
        animateNodes();

        tblProducts.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Movie rowData = row.getItem();
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


    private void animateNodes() {
        Animations.fadeInUp(btnNewProduct);
        Animations.fadeInUp(tblProducts);
        Animations.fadeInUp(hBoxSearch);
    }


    /*TABLE*/

    @Subscribe
    public void loadData(MovieMessage movieMessage) {
        listProducts.setAll(movieMessage.movies);
        tblProducts.setItems(listProducts);
        tblProducts.setFixedCellSize(30);


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




    /* DIALOG */

    @FXML
    private void showDialogAddProduct() {

        disableTable();
        rootProducts.setEffect(ConstantsPath.BOX_BLUR_EFFECT);
        containerAddProduct.setVisible(true);

        dialogAddProduct = new DialogTool(containerAddProduct, stckProducts);

        dialogAddProduct.show();


        dialogAddProduct.setOnDialogClosed(ev -> {
             tblProducts.setDisable(false);
            rootProducts.setEffect(null);
            containerAddProduct.setVisible(false);
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
         selectedRecord();
    }

    @FXML
    private void showDialogDetailsProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddProduct();
         selectedRecord();

    }





    private void selectedRecord() {
        Movie movie = tblProducts.getSelectionModel().getSelectedItem();

    }


    private void disableTable() {
        tblProducts.setDisable(true);
    }


    private void setDisableMenuItem() {
        contextMenu.getEditButton().setDisable(true);
        contextMenu.getDeleteButton().setDisable(true);
    }

    private void setEnableMenuItem() {
        contextMenu.getEditButton().setDisable(false);
        contextMenu.getDeleteButton().setDisable(false);
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



    public void cleanup() {
        // Unregister this controller from EventBus when it's no longer needed
        EventBus.getDefault().unregister(this);
    }


}