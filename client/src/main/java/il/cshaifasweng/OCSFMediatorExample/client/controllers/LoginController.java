package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {

    private final String INCORRECT_CREDENTIALS = "Incorrect user or password";

    @FXML
    private Button btnLogin;

    @FXML
    private Pane paneIcon;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtPassword;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private ImageView icon;

    @FXML
    private Text title;

    private double x, y;

    // Mock user data
    private final Map<String, String> userData = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mock user data
        userData.put("user@example.com", "password123");

        showPassword();
        selectText();
        animations();
    }

    private void animations() {
        Animations.fadeInUp(title);
        Animations.fadeInUp(txtUser);
        Animations.fadeInUp(txtPassword);
        Animations.fadeInUp(pfPassword);
        Animations.fadeInUp(btnLogin);
    }

    private void selectText() {
        txtUser.setOnMouseClicked(event -> txtUser.selectAll());
        txtPassword.setOnMouseClicked(event -> txtPassword.selectAll());
        pfPassword.setOnMouseClicked(event -> pfPassword.selectAll());
    }

    @FXML
    private void login() {
        String user = txtUser.getText().trim();
        String pass = pfPassword.getText().trim();

        if (user.isEmpty() && pass.isEmpty()) {
            Animations.shake(txtUser);
            Animations.shake(pfPassword);
            Animations.shake(paneIcon);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (pass.isEmpty()) {
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            Animations.shake(paneIcon);
            return;
        }

        if (userData.containsKey(user) && userData.get(user).equals(pass)) {
            loadMain();
            NotificationsBuilder.create(NotificationType.SUCCESS, "Welcome to the system " + user + "!");
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, INCORRECT_CREDENTIALS);
            Animations.shake(txtUser);
            Animations.shake(pfPassword);
            Animations.shake(txtPassword);
            Animations.shake(paneIcon);
        }
    }

    private void loadMain() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Constants.MAIN_VIEW));
            Parent root = loader.load();
            MainController main = loader.getController();

            // Mock user type
            String userType = "Administrator";
            if (userType.equals("Administrator")) {
                main.getBtnStatistics().setVisible(true);
                main.getBtnAddUser().setVisible(true);
                main.getBtnAbout().setVisible(true);
            } else {
                main.getBtnStatistics().setVisible(false);
                main.getBtnAddUser().setVisible(false);
                main.getBtnAbout().setVisible(false);
            }

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.getIcons().add(new Image(Constants.STAGE_ICON));
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setMinHeight(Constants.MIN_HEIGHT);
            stage.setMinWidth(Constants.MIN_WIDTH);
            stage.setTitle(Constants.TITLE);
            stage.setScene(new Scene(root));
            stage.show();
            closeStage();

            root.setOnKeyPressed((KeyEvent e) -> {
                if (e.getCode().equals(KeyCode.F11)) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });

            stage.setOnCloseRequest(ev -> {
                // Mock logout action
                System.out.println("User logged out");
            });
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showPassword() {
        txtPassword.managedProperty().bind(icon.pressedProperty());
        txtPassword.visibleProperty().bind(icon.pressedProperty());
        txtPassword.textProperty().bindBidirectional(pfPassword.textProperty());

        pfPassword.managedProperty().bind(icon.pressedProperty().not());
        pfPassword.visibleProperty().bind(icon.pressedProperty().not());

        icon.setOnMousePressed(event -> {
            icon.setImage(new Image(getClass().getResourceAsStream("/images/eye.png")));
        });

        icon.setOnMouseReleased(event -> {
            icon.setImage(new Image(getClass().getResourceAsStream("/images/eye-slash.png")));
        });
    }

    @FXML
    private void closeStage() {
        ((Stage) txtUser.getScene().getWindow()).close();
    }

    @FXML
    private void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    private void dragged(MouseEvent event) {
        Stage stg = (Stage) btnLogin.getScene().getWindow();
        stg.setX(event.getScreenX() - x);
        stg.setY(event.getScreenY() - y);
    }
}
