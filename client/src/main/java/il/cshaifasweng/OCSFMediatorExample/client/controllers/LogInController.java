package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button enterButton;

    @FXML
    private Button closeButton;

    private ViewMoviesController parentController;

    @FXML
    void initialize() {
        enterButton.setOnAction(this::handleLogin);
        closeButton.setOnAction(this::handleClose);
    }

    public void setParentController(ViewMoviesController parentController) {
        this.parentController = parentController;
    }

    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Simple validation (should be replaced with actual authentication logic)
        if (!email.isEmpty() && !password.isEmpty()) {
            parentController.handleSuccessfulLogin();
            ((Stage) enterButton.getScene().getWindow()).close(); // Close the login window
        } else {
            System.out.println("Login failed");
        }
    }

    private void handleClose(ActionEvent event) {
        ((Stage) closeButton.getScene().getWindow()).close(); // Close the login window
    }
}
