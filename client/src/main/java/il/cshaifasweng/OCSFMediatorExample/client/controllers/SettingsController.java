package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.models.Users;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import il.cshaifasweng.OCSFMediatorExample.client.util.CropImageProfile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsController implements Initializable {

    private final long LIMIT = 1000000;

    private final String MESSAGE_PROFILE_IMAGE_SAVED = "Success, profile picture saved.";

    @FXML
    private StackPane stckSettings;

    @FXML
    private AnchorPane rootSettings;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private ComboBox<String> cmbDialogTransition;

    @FXML
    private TextArea txtBio;

    @FXML
    private Button btnSave;

    @FXML
    private Pane imageProfileContainer;

    @FXML
    private Group parentImage;

    @FXML
    private HBox headerContainer;

    @FXML
    private Text textName;

    @FXML
    private Text textUserType;

    @FXML
    private ImageView imageViewProfile;

    private File imageFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animationNodes();
        selectText();
        loadData();
        validations();
        setMask();
        setOptionsToComboBox();
        initializeProfileImage();
    }

    private void initializeProfileImage() {
        Circle circle = new Circle(45);
        circle.setCenterX(imageViewProfile.getFitWidth() / 2);
        circle.setCenterY(imageViewProfile.getFitHeight() / 2);
        imageViewProfile.setClip(circle);

        Circle clip = new Circle(45);
        clip.setCenterX(imageViewProfile.getFitWidth() / 2);
        clip.setCenterY(imageViewProfile.getFitHeight() / 2);
        imageProfileContainer.setClip(clip);
    }

    private void loadData() {
        // Add dummy data for testing
        txtName.setText("John Doe");
        textName.setText("John Doe");
        txtUser.setText("johndoe");
        txtPassword.setText("password");
        txtConfirmPassword.setText("password");
        txtBio.setText("Sample bio...");
        initializeComboBox("LEFT");
        textUserType.setText("Administrator");

        loadProfileImage();
    }

    @FXML
    private void updateCredentials() {
        String name = txtName.getText().trim();
        String user = txtUser.getText().trim();
        String password = txtPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();
        String bio = txtBio.getText().trim();

        if (name.isEmpty() && user.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && bio.isEmpty()) {
            Animations.shake(txtName);
            Animations.shake(txtUser);
            Animations.shake(txtPassword);
            Animations.shake(txtConfirmPassword);
            Animations.shake(txtBio);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (name.isEmpty()) {
            txtName.requestFocus();
            Animations.shake(txtName);
            return;
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
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
            return;
        }

        if (!confirmPassword.equals(password)) {
            txtConfirmPassword.requestFocus();
            Animations.shake(txtPassword);
            Animations.shake(txtConfirmPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_PASSWORDS_NOT_MATCH);
            return;
        }

        if (cmbDialogTransition.getSelectionModel().isEmpty()) {
            Animations.shake(cmbDialogTransition);
            return;
        }

        if (bio.isEmpty()) {
            txtBio.requestFocus();
            Animations.shake(txtBio);
            return;
        }

        Users users = new Users();
        users.setId(1); // Dummy ID for testing
        users.setNameUser(name);
        users.setEmail(user);
        users.setPass(password);
        users.setBiography(bio);
        users.setDialogTransition(getDialogTransition());

        loadData();
        AlertsBuilder.create(AlertType.SUCCES, stckSettings, rootSettings, rootSettings, Constants.MESSAGE_ADDED);
    }

    private String getDialogTransition() {
        return cmbDialogTransition.getSelectionModel().getSelectedItem().toUpperCase();
    }

    private void initializeComboBox(String dialogTransition) {
        switch (dialogTransition) {
            case "LEFT":
                cmbDialogTransition.setValue("Left");
                break;

            case "RIGHT":
                cmbDialogTransition.setValue("Right");
                break;

            case "TOP":
                cmbDialogTransition.setValue("Top");
                break;

            case "BOTTOM":
                cmbDialogTransition.setValue("Bottom");
                break;

            case "CENTER":
                cmbDialogTransition.setValue("Center");
                break;
        }
    }

    private void setOptionsToComboBox() {
        cmbDialogTransition.getItems().addAll("Left", "Right", "Top", "Bottom", "Center");
    }

    private void animationNodes() {
        Animations.fadeInUp(txtName);
        Animations.fadeInUp(txtUser);
        Animations.fadeInUp(txtPassword);
        Animations.fadeInUp(btnSave);
        Animations.fadeInUp(txtConfirmPassword);
        Animations.fadeInUp(txtBio);
        Animations.fadeInUp(cmbDialogTransition);
        Animations.fadeInUp(headerContainer);
    }

    private void selectText() {
        txtName.setOnMouseClicked(event -> txtName.selectAll());
        txtUser.setOnMouseClicked(event -> txtUser.selectAll());
        txtPassword.setOnMouseClicked(event -> txtPassword.selectAll());
        txtConfirmPassword.setOnMouseClicked(event -> txtConfirmPassword.selectAll());
        txtBio.setOnMouseClicked(event -> txtBio.selectAll());
    }

    private void validations() {
        // Implement any required field validations
    }

    private void setMask() {
        // Implement any required masks
    }

    @FXML
    private void showFileChooser() {
        imageFile = getImageFromFileChooser(getStage());
        if (imageFile != null) {
            if (imageFile.length() < LIMIT) {
                CropImageProfile crop = new CropImageProfile(imageFile);
                AlertsBuilder.create(AlertType.SUCCES, stckSettings, rootSettings, rootSettings, MESSAGE_PROFILE_IMAGE_SAVED);
                loadProfileImage();
            } else {
                NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_IMAGE_LARGE);
            }
        }
    }

    private void loadProfileImage() {
        // Load dummy image
        InputStream imageStream = getClass().getResourceAsStream("/media/icon.png");
        if (imageStream == null) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, "Image not found: /media/icon.png");
        } else {
            Image image = new Image(imageStream, 100, 100, true, true);
            imageViewProfile.setImage(image);
        }
    }

    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }

    private File getImageFromFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterImages = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().addAll(extFilterImages);
        fileChooser.setTitle("Select an image");

        return fileChooser.showOpenDialog(stage);
    }
}
