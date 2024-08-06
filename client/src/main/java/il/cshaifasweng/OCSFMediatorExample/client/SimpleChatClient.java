package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.stage.StageStyle;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath.EMPLOEE_PACKAGE;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    public static SimpleClient client;

    @Override
    public void start(Stage stage) throws IOException {
        startWindow(stage);
        EventBus.getDefault().register(this);

    }

    private void startWindow(Stage stage) {
        try {
            System.out.println("!!!!!!! Loading FXML");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/boundriesCustomer/StartView.fxml")));
            System.out.println("Loaded FXML successfully");

            stage.getIcons().add(new Image(ConstantsPath.STAGE_ICON));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle(ConstantsPath.TITLE);
            stage.show();
            System.out.println("Window shown successfully ######");
        } catch (IOException ex) {
            Logger.getLogger(SimpleChatClient.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            System.err.println("Could not load the FXML file. Check the resource path.");
            ex.printStackTrace();
        }
    }




    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
		super.stop();
	}



	public static void main(String[] args) {
        launch();
    }

}