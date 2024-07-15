
/**
 * Sample Skeleton for 'MovieSmall.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client.controllersCustumer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MovieSmallController implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;


    @FXML
    private Button Book;

    @FXML
    private Button Info;

    private ViewMoviesController parentController;


    @FXML
    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == Info) {
            parentController.showMovieDetails();
        }
    }

    public void setParentController(ViewMoviesController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Info.setOnAction(this::handleClicks);
    }


}
