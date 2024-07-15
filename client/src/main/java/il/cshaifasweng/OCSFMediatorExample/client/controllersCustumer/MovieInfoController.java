
/**
 * Sample Skeleton for 'MovieInfo.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client.controllersCustumer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MovieInfoController implements Initializable {
    private ViewMoviesController parentController;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Book"
    private Button Book; // Value injected by FXMLLoader

    @FXML // fx:id="Close"
    private Button Close; // Value injected by FXMLLoader

    @FXML
    public void handleClose(ActionEvent actionEvent) {
        if (parentController != null) {
            parentController.showGridPane();
        }
    }

    public void setParentController(ViewMoviesController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize any necessary components here
    }

}
