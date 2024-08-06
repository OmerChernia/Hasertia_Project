package il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class StartController implements Initializable {

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private Button btnStart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Any necessary initialization can be done here
    }

    @FXML
    private void connect() {
        try {

            SimpleClient.port = Integer.parseInt(portField.getText());
            SimpleClient.host = ipField.getText();

            SimpleChatClient.client = SimpleClient.getClient();
            SimpleChatClient.client.openConnection();

           // messageField.setText("Client created, host: " + SimpleClient.host + ", port: " + SimpleClient.port);
            mainWindow();

        } catch (NumberFormatException e) {
           // messageField.setText("Invalid port number: " + portField.getText());
            System.err.println("Invalid port number: " + portField.getText());
        } catch (UnknownHostException e) {
           // messageField.setText("Failed to connect: Invalid hostname or IP address: " + SimpleClient.host);
            System.err.println("Failed to connect: Invalid hostname or IP address: " + SimpleClient.host);
        } catch (IOException e) {
         //   messageField.setText("Failed to connect: " + e.getMessage());
            System.err.println("Failed to connect: " + e.getMessage());
        }

    }





/*here u click button and connect!!!!!*/
    @FXML
    private void mainWindow() {
        String ip = ipField.getText();
        String port = portField.getText();

        // Handle IP and port logic here
        connect();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ConstantsPath.MAIN_VIEW));
            Stage stage = new Stage();
            stage.getIcons().add(new Image(ConstantsPath.STAGE_ICON));
            stage.initStyle(StageStyle.DECORATED);
            stage.setMinHeight(ConstantsPath.MIN_HEIGHT);
            stage.setMinWidth(ConstantsPath.MIN_WIDTH);
            stage.setTitle(ConstantsPath.TITLE);
            stage.setScene(new Scene(root));
            stage.show();
            closeStage();

            NotificationsBuilder.create(NotificationType.SUCCESS, "Welcome to the system!");
        } catch (IOException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeStage() {
        ((Stage) btnStart.getScene().getWindow()).close();
    }

    @FXML
    private void closeWindow() {
        System.exit(0);
    }
}
