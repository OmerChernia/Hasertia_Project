package il.cshaifasweng.OCSFMediatorExample.client.connect;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.user.MainBoundary;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.LoginPageController;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    public static Scene scene;
    public static SimpleClient client;
    private static Stage primaryStage;
    private static MainBoundary mainBoundary;


    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        EventBus.getDefault().register(this);

        // Load initial screen
        scene = new Scene(loadFXML("StartView"));
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource(ConstantsPath.USER_PACKAGE + fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        scene.setRoot(root);

        // Register the appropriate controller
        switch (fxml) {
            case "MainView":
                mainBoundary = fxmlLoader.getController();
                EventBus.getDefault().register(mainBoundary);
                break;

            default:
                break;
        }
    }

    public static Parent loadFXML(String fxml) throws IOException {
        String fxmlPath = ConstantsPath.USER_PACKAGE + fxml + ".fxml";
        URL fxmlUrl = SimpleChatClient.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            System.err.println("FXML file not found at: " + fxmlPath);
            throw new IOException("FXML file not found: " + fxmlPath);
        }
        System.out.println("Loading FXML from path: " + fxmlUrl);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        EventBus.getDefault().unregister(this);
        if (mainBoundary != null) {
            EventBus.getDefault().unregister(mainBoundary);
        }
        //if there is a user that login, we need to log him out
        if(SimpleClient.user !=null)
        {
            if(!SimpleClient.user.isEmpty())
            {
                LoginPageController.requestUserLogOut(SimpleClient.user);
                LoginPageController.requestEmployeeLogOut(SimpleClient.user); // maybe we can do it better
            }
        }
        System.out.println("Stop was successful");
        super.stop();
    }


    @Subscribe
    public void onMessageEvent(Object event) {
        System.out.println("Received Message in Client");

    }

    public void sendRequest(String action) {

    }

    public static SimpleClient getClient() {
        return client;
    }





    public static void main(String[] args) {
        launch();
    }


}