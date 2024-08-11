package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.ButtonFactory;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieInfoBoundary implements Initializable {

    public Button btnGenre;
     @FXML
    private Pane paneContainer;
    @FXML
    private Button Close;

    @FXML
    private Label lblActors;

    @FXML
    private Label lblName;


    @FXML
    private Label lblInfo;

    @FXML
    private Label lblProducer;

    @FXML
    private HBox imageContainer;

    @FXML
    private ImageView image;


    private HomeBoundary homeController;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void setHomeController(HomeBoundary homeController) {
        this.homeController = homeController;
    }



    public void setInfo (Movie movie)
    {
        lblInfo.setText(movie.getInfo());
        lblActors.setText(movie.getMainActors().toString().replace("[", "").replace("]", ""));
        lblName.setText(movie.getEnglishName()+ "  |  " +movie.getHebrewName());
        lblProducer.setText(movie.getProducer());

        image.setImage(getImage(movie));
        image.setPreserveRatio(true);
        image.setFitWidth(250);
        image.setFitHeight(350);
            expandImage(movie);

        ButtonFactory.ButtonGenreCellValueFactory factory = new ButtonFactory().new ButtonGenreCellValueFactory();
        btnGenre = factory.configureGenreButton(btnGenre, movie);
    }

    @FXML
    void handleClose(ActionEvent event) {
        homeController.closeDialogAddUser();

    }


    private Image getImage(Movie movie) {

        if (movie.getImage() == null) {
            return new Image(ConstantsPath.NO_IMAGE_AVAILABLE, true); // Remove size constraints
        }

        String imagePath = ConstantsPath.MOVIE_PACKAGE + movie.getImage();
        URL imageUrl = getClass().getResource(imagePath);

        if (imageUrl == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(ConstantsPath.NO_IMAGE_AVAILABLE, true);
        }

        try {
            return new Image(imageUrl.toExternalForm(), true);
        } catch (Exception e) {
            System.out.println("Failed to load image: " + imagePath);
            e.printStackTrace();
            return new Image(ConstantsPath.NO_IMAGE_AVAILABLE, true);
        }
    }



    private void expandImage(Movie movie) {
        paneContainer.setOnMouseClicked(ev -> {
            Image image = getImage(movie);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(550);

            BorderPane borderPane = new BorderPane(imageView);
            borderPane.setStyle("-fx-background-color: white");

            ScrollPane root = new ScrollPane(borderPane);
            root.setStyle("-fx-background-color: white");
            root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            root.setFitToHeight(true);
            root.setFitToWidth(true);

            Stage stage = new Stage();
            stage.getIcons().add(new Image(ConstantsPath.STAGE_ICON));
            stage.setScene(new Scene(root, 550, 550));
            stage.setTitle(movie.getEnglishName());
            stage.show();
        });
    }

}
