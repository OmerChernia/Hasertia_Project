/*
 * Copyright 2020-2021 LaynezCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laynezcoder.estfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.mask.RequieredFieldsValidators;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.Constants;
import com.laynezcoder.estfx.util.CropImageProfile;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
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

public class SettingsController implements Initializable {

    private final long LIMIT = 1000000;

    private final String MESSAGE_PROFILE_IMAGE_SAVED = "Success, profile picture saved.";

    @FXML
    private StackPane stckSettings;

    @FXML
    private AnchorPane rootSettings;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtConfirmPassword;

    @FXML
    private JFXComboBox<String> cmbDialogTransition;

    @FXML
    private JFXTextArea txtBio;

    @FXML
    private JFXButton btnSave;

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

    @FXML
    private MaterialDesignIconView icon;

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
        effectEditImageProfile();
    }

    private void effectEditImageProfile() {
        Animations.fade(parentImage, imageProfileContainer, icon);
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
        initializeJFXComboBox("LEFT");
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

    private void initializeJFXComboBox(String dialogTransition) {
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
        TextFieldMask.selectText(txtName);
        TextFieldMask.selectText(txtUser);
        TextFieldMask.selectText(txtPassword);
        TextFieldMask.selectText(txtConfirmPassword);
        TextFieldMask.selectTextToJFXTextArea(txtBio);
    }

    private void validations() {
        RequieredFieldsValidators.toJFXTextArea(txtBio);
        RequieredFieldsValidators.toJFXComboBox(cmbDialogTransition);
        RequieredFieldsValidators.toJFXTextField(txtName);
        RequieredFieldsValidators.toJFXTextField(txtUser);
        RequieredFieldsValidators.toJFXPasswordField(txtPassword);
        RequieredFieldsValidators.toJFXPasswordField(txtConfirmPassword);
    }

    private void setMask() {
        TextFieldMask.onlyLetters(txtName, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtUser, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtPassword, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtConfirmPassword, 40);
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
        Image image = new Image(getClass().getResourceAsStream("/path/to/dummy/image.png"), 100, 100, true, true);
        imageViewProfile.setImage(image);
    }

    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }

    private File getImageFromFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterImages = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().addAll(extFilterImages);
        fileChooser.setTitle("Select an image");

        File selectedImage = fileChooser.showOpenDialog(stage);
        return selectedImage;
    }
}
