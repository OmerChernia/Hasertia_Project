package il.cshaifasweng.OCSFMediatorExample.client.boundaries.contentManager;

import com.sun.net.httpserver.Authenticator;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.MovieInstanceController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.PriceRequestController;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieInstanceMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PriceRequestMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.PriceRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private Button btnDelete;
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
    private DialogTool dialogEditProduct;
    private CustomContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        MovieController.requestAllMovies();
        listProducts = FXCollections.observableArrayList();
        filterProducts = FXCollections.observableArrayList();
        animateNodes();
        setContextMenu();

        tblProducts.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
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
            showDialogDeleteProduct() ;
            contextMenu.hide();
        });
        contextMenu.show();
    }

    private void animateNodes() {
        Animations.fadeInUp(btnNewProduct);
        Animations.fadeInUp(tblProducts);
        Animations.fadeInUp(hBoxSearch);
    }



    @Subscribe
    public void loadData(MovieMessage movieMessage) {
        switch (movieMessage.responseType) {
            case MOVIE_UPDATED:
                Platform.runLater(() -> {
                    AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, rootProducts, "updated movie!");
                    MovieController.requestAllMovies();
                });
                break;
            case MOVIE_ADDED:
                Platform.runLater(() -> {
                    AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, rootProducts, "new movie added!");
                    MovieController.requestAllMovies();
                });
                break;
            case MOVIE_DELETED:
                Platform.runLater(() -> {
                    AlertsBuilder.create(AlertType.SUCCESS, stckProducts, rootProducts, rootProducts, "movie has been deleted!");
                    MovieController.requestAllMovies();
                });
                break;
            case MOVIE_NOT_UPDATED:
                Platform.runLater(() -> {
                    AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, rootProducts, "Failed to update movie.");
                });
                break;
            case MOVIE_NOT_ADDED:
                Platform.runLater(() -> {
                    AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, rootProducts, "Failed to add movie.");
                });
                break;
            case MOVIE_NOT_DELETED:
                Platform.runLater(() -> {
                    AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, rootProducts, "Failed to delete movie.");
                });
                break;
            case RETURN_MOVIES:
                Platform.runLater(() -> loadTableData(movieMessage.movies));
                break;
        }
    }




    private void loadTableData(List<Movie> movies) {
        listProducts.setAll(movies);
        tblProducts.setItems(listProducts);
        tblProducts.setFixedCellSize(30);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEnglish.setCellValueFactory(new PropertyValueFactory<>("englishName"));
        colHebrew.setCellValueFactory(new PropertyValueFactory<>("hebrewName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colTheaterPrice.setCellValueFactory(new PropertyValueFactory<>("theaterPrice"));
        colHVPrice.setCellValueFactory(new PropertyValueFactory<>("homeViewingPrice"));

        ButtonFactory.ButtonGenreCellValueFactory buttonGenreCellFactory = new ButtonFactory.ButtonGenreCellValueFactory();
        colGenre.setCellValueFactory(buttonGenreCellFactory);

        ButtonFactory.ButtonMovieTypeCellValueFactory buttonTypeCellFactory = new ButtonFactory.ButtonMovieTypeCellValueFactory();
        colStreamingType.setCellValueFactory(buttonTypeCellFactory);
    }



    @FXML
    private void showDialog(String operation,Movie selectedMovie) {
        disableTable();


        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ConstantsPath.DIALOG_MOVIE_VIEW));
                AnchorPane moviePane = loader.load();

                DialogEditMovie dialogEditMovie = loader.getController();
                dialogEditMovie.setEditMovieListBoundary(this);
                dialogEditMovie.setDialog(operation, selectedMovie);

                containerAddProduct.getChildren().clear();
                containerAddProduct.getChildren().add(moviePane);
                containerAddProduct.setVisible(true);

                dialogAddProduct = new DialogTool(containerAddProduct, stckProducts);
                dialogAddProduct.show();

                dialogAddProduct.setOnDialogClosed(ev -> {
                    tblProducts.setDisable(false);
                    rootProducts.setEffect(null);
                    containerAddProduct.setVisible(false);
                });

                rootProducts.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

            } catch (IOException e) {
                e.printStackTrace();
                tblProducts.setDisable(false);
            }
        });
    }





    @FXML
    public void closeDialogAddProduct() {
        if (dialogAddProduct != null) {
            Platform.runLater(dialogAddProduct::close);
        }
        if (dialogDeleteProduct !=null)
        {
            dialogDeleteProduct.close();
        }
    }





    public void handleNewMovie(ActionEvent actionEvent) {

        showDialog("add",null);
    }



    @FXML
    private void showDialogDeleteProduct() {
        Movie delete = tblProducts.getSelectionModel().getSelectedItem();
        if (delete == null) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        Platform.runLater(() -> {
            rootProducts.setEffect(ConstantsPath.BOX_BLUR_EFFECT);
            containerDeleteProducts.setVisible(true);
            disableTable();

            dialogDeleteProduct = new DialogTool(containerDeleteProducts, stckProducts);


            btnDelete.setOnAction(ev -> {

                System.out.println(delete.getId());
                MovieController.deleteMovie(delete.getId());
                dialogDeleteProduct.close();
            });

            dialogDeleteProduct.show();

            dialogDeleteProduct.setOnDialogClosed(ev -> {
                tblProducts.setDisable(false);
                rootProducts.setEffect(null);
                containerDeleteProducts.setVisible(false);
            });
        });
    }




    @FXML
    private void hideDialogDeleteProduct() {
        if (dialogDeleteProduct != null) {
            Platform.runLater(dialogDeleteProduct::close);
        }
    }

    @FXML
    private void showDialogEditProduct() {
        Movie selectedMovie = tblProducts.getSelectionModel().getSelectedItem();
        if (selectedMovie==null) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED) ;
            return;
        }

        showDialog("edit",selectedMovie);
    }

    @FXML
    private void showDialogDetailsProduct() {
        Movie selectedMovie = tblProducts.getSelectionModel().getSelectedItem();
        if (selectedMovie==null) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, ConstantsPath.MESSAGE_NO_RECORD_SELECTED) ;
            return;
        }
        showDialog("view",selectedMovie);
    }

    private void disableTable() {
        tblProducts.setDisable(true) ;
    }

    @FXML
    private void filterNameProduct() {
        Platform.runLater(() -> {
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
        });
    }

    @FXML
    private void filterCodeBar() {
        Platform.runLater(() -> {
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
        });
    }

    public void cleanup() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


}
