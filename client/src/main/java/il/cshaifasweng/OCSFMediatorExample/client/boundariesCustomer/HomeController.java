package il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer;

import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.DBGenerate;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private static final int ITEMS_PER_ROW = 4;
    private List<Movie> items;
    private final DBGenerate db = new DBGenerate();
    private DialogTool dialogInfo;

    @FXML
    private AnchorPane InfoContainer;

    @FXML
    private Button btnHV;

    @FXML
    private Button btnTheater;

    @FXML
    private StackPane stckHome;

    @FXML
    private GridPane grid;






    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setItems();
        } catch (IOException e) {
            e.printStackTrace(); // טיפול בשגיאה
        }
    }

    public void setItems() throws IOException {
        this.items = db.getMovies();
        updateGrid();
    }

    private void updateGrid() throws IOException {
        grid.getChildren().clear();
        int row = 0;
        int col = 0;
        for (Movie item : items) {
            Node normalItem = createItem(item);
            grid.add(normalItem, col, row);
            col++;
            if (col == ITEMS_PER_ROW) {
                col = 0;
                row++;
            }
        }
    }

    private Node createItem(Movie item) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ConstantsPath.SMALL_MOVIE_VIEW));
        StackPane itemBox = loader.load();
        MovieSmallController controller = loader.getController();
        if (controller != null) {
            controller.setMovieShort(item);
            controller.setHomeController(this);
        } else {
            System.err.println("Controller is null");
        }
        return itemBox;
    }

    public void setRoot(Parent root) {
        if (root != null) {
            stckHome.getChildren().setAll(root);
        } else {
            System.err.println("Root is null, cannot set.");
        }
    }
    public void showInfo(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ConstantsPath.COSTUMER_PACKAGE+"MovieInfo.fxml"));
            Parent pane = loader.load(); // Use Parent to accommodate different Pane types

            MovieInfoController movieInfoController = loader.getController();
            movieInfoController.sethomeController(this);
            movieInfoController.setInfo(movie);

            InfoContainer.getChildren().clear();
            InfoContainer.getChildren().add(pane);
            InfoContainer.setVisible(true);

            dialogInfo = new DialogTool(InfoContainer, stckHome);
            dialogInfo.show();


            dialogInfo.setOnDialogClosed(ev -> {
                stckHome.setEffect(null);
                InfoContainer.setVisible(false);
            });

            stckHome.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeDialogAddUser() {
        if (dialogInfo != null) {
            dialogInfo.close();
        }
    }




}







