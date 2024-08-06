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
import javafx.event.ActionEvent;
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

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.loadFXML;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.scene;


public class StartController  {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField messageField;

    @FXML
    private Button connectButton;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private Button btnAutoConnect;


    @FXML
    private void autoConnect() {
        ipField.setText("localhost");
        portField.setText("3000");
        connect();
    }
    @FXML
    private void connect() {
        try {

            SimpleClient.port = Integer.parseInt(portField.getText());
            SimpleClient.host = ipField.getText();

            SimpleChatClient.client = SimpleClient.getClient();
            SimpleChatClient.client.openConnection();

            messageField.setText("Client created, host: " + SimpleClient.host + ", port: " + SimpleClient.port);


            scene = new Scene(loadFXML("MainView"));
            Stage stage = new Stage(); // Create a new Stage instance
            stage.getIcons().add(new Image(ConstantsPath.STAGE_ICON));
            stage.setTitle(ConstantsPath.TITLE);

            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException e) {
            messageField.setText("Invalid port number: " + portField.getText());
            System.err.println("Invalid port number: " + portField.getText());
        } catch (UnknownHostException e) {
            messageField.setText("Failed to connect: Invalid hostname or IP address: " + SimpleClient.host);
            System.err.println("Failed to connect: Invalid hostname or IP address: " + SimpleClient.host);
        } catch (IOException e) {
            messageField.setText("Failed to connect: " + e.getMessage());
            System.err.println("Failed to connect: " + e.getMessage());
        }

    }

    @FXML
    public void AutoConnect(ActionEvent actionEvent) {
        ipField.setText("localhost");
        portField.setText("3000");
        connect();
    }



}
