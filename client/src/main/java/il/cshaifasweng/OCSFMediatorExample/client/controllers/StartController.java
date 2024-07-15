package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class StartController implements Initializable {

    public AnchorPane rootStart;
    @FXML
    private StackPane stckStart;

    @FXML
    private Text title;

    @FXML
    private Pane paneStep1;

    @FXML
    private Text textStep1;

    @FXML
    private Pane paneControlsStep1;

    @FXML
    private Button btnStep1;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Pane paneStep2;

    @FXML
    private Text textStep2;

    @FXML
    private TextArea txtBio;

    @FXML
    private HBox hBoxStep2;

    @FXML
    private Pane paneStep3;

    @FXML
    private Text textStep3;

    @FXML
    private ComboBox<String> cmbDialogTransition;

    @FXML
    private HBox hBoxStep3;

    @FXML
    private Pane paneFinish;

    @FXML
    private Text finishText;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Text textProgressBar;

    private double x, y;

    private String name, user, password, confirmPassword, bio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startConfig();
        selectText();
        setMask();
        validations();
        StartStepOne();
        setOptionsToComboBox();
    }

    private void setOptionsToComboBox() {
        cmbDialogTransition.getItems().addAll("Left", "Right", "Top", "Bottom", "Center");
        cmbDialogTransition.setValue("Center");
    }

    private void startConfig() {
        progressBar.setProgress(0.00);
        textProgressBar.setText("1 of 3");

        Animations.fadeInUp(title);
        Animations.fadeInUp(textProgressBar);
        Animations.fadeInUp(progressBar);
    }

    @FXML
    private void StartStepOne() {
        paneStep1.setVisible(true);
        paneStep2.setVisible(false);

        textProgressBar.setText("1 of 3");
        Animations.progressAnimation(progressBar, 0.00);
        Animations.fadeInUp(paneStep1);
        Animations.fadeInUp(paneControlsStep1);
        Animations.fadeInUp(textStep1);
        Animations.fadeInUp(btnStep1);
    }

    @FXML
    private void stepOneToStepTwo() {
        name = txtName.getText().trim();
        user = txtUser.getText().trim();
        password = txtPassword.getText().trim();
        confirmPassword = txtConfirmPassword.getText().trim();

        if (name.isEmpty()) {
            txtName.requestFocus();
            Animations.shake(txtName);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (user.length() < 4) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            return;
        }

        if (password.isEmpty()) {
            txtPassword.requestFocus();
            Animations.shake(txtPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (password.length() < 4) {
            txtPassword.requestFocus();
            Animations.shake(txtPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            return;
        }

        if (confirmPassword.isEmpty()) {
            txtConfirmPassword.requestFocus();
            Animations.shake(txtConfirmPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (!confirmPassword.equals(password)) {
            Animations.shake(txtConfirmPassword);
            Animations.shake(txtPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_PASSWORDS_NOT_MATCH);
            return;
        }

        startStepTwo();
    }

    @FXML
    private void startStepTwo() {
        paneStep1.setVisible(false);
        paneStep2.setVisible(true);
        paneStep3.setVisible(false);

        textProgressBar.setText("2 of 3");
        Animations.progressAnimation(progressBar, 0.33);
        Animations.fadeInUp(paneStep2);
        Animations.fadeInUp(textStep2);
        Animations.fadeInUp(txtBio);
        Animations.fadeInUp(hBoxStep2);
    }

    @FXML
    private void stepTwoToStepThree() {
        bio = txtBio.getText().trim();

        if (bio.isEmpty()) {
            Animations.shake(txtBio);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        startStepThree();
    }

    private void startStepThree() {
        paneStep2.setVisible(false);
        paneStep3.setVisible(true);

        textProgressBar.setText("3 of 3");
        Animations.progressAnimation(progressBar, 0.66);
        Animations.fadeInUp(paneStep3);
        Animations.fadeInUp(textStep3);
        Animations.fadeInUp(cmbDialogTransition);
        Animations.fadeInUp(hBoxStep3);
    }

    @FXML
    private void finish() {
        paneStep3.setVisible(false);
        paneFinish.setVisible(true);

        textProgressBar.setText("Finalized");
        Animations.progressAnimation(progressBar, 1);
        Animations.fadeInUp(paneFinish);

        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(ev -> {
            Animations.fadeOutWithDuration(paneFinish);
            mainWindow();
        });

        pt.play();
    }

    private String getDialogTransition() {
        return cmbDialogTransition.getSelectionModel().getSelectedItem().toUpperCase();
    }

    @FXML
    private void mainWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Constants.MAIN_VIEW));
            Stage stage = new Stage();
            stage.getIcons().add(new javafx.scene.image.Image(Constants.STAGE_ICON));
            stage.initStyle(StageStyle.DECORATED);
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

            NotificationsBuilder.create(NotificationType.SUCCESS, "Welcome to the system " + name + "!");
        } catch (IOException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeStage() {
        ((Stage) txtUser.getScene().getWindow()).close();
    }

    @FXML
    private void closeWindow() {
        System.exit(0);
    }

    @FXML
    private void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    private void dragged(MouseEvent event) {
        Stage stg = (Stage) btnStep1.getScene().getWindow();
        stg.setX(event.getScreenX() - x);
        stg.setY(event.getScreenY() - y);
    }

    @FXML
    private void alert() {
        // Show alert dialog here
    }

    private void selectText() {
        txtName.selectAll();
        txtUser.selectAll();
        txtPassword.selectAll();
        txtConfirmPassword.selectAll();
        txtBio.selectAll();
    }

    private void validations() {
        // Implement validation logic here
    }

    private void setMask() {
        // Implement input masks here
    }
}
