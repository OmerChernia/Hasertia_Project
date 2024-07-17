package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.models.Users;
import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.client.mask.RequieredFieldsValidators;
import il.cshaifasweng.OCSFMediatorExample.client.mask.TextFieldMask;
import il.cshaifasweng.OCSFMediatorExample.client.util.CustomContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.DefaultProfileImage;
import java.io.FileNotFoundException;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;

public class UsersController implements Initializable {

    private final String CANNOT_DELETED = "This user cannot be deleted";

    private final String ADMINISTRATOR_ONLY = "This user can only be administrator type";

    private final String UNABLE_TO_CHANGE = "Unable to change user type";

    @FXML
    private StackPane stckUsers;

    @FXML
    private AnchorPane rootUsers;

    @FXML
    private AnchorPane addUserContainer;

    @FXML
    private HBox hboxSearch;

    @FXML
    private TableView<Users> tblUsers;

    @FXML
    private TableColumn<Users, Integer> colId;

    @FXML
    private TableColumn<Users, String> colName;

    @FXML
    private TableColumn<Users, String> colUser;

    @FXML
    private TableColumn<Users, PasswordField> colPassword;

    @FXML
    private TableColumn<Users, Button> colTypeUser;

    @FXML
    private TextField txtSearchName;

    @FXML
    private TextField txtSearchUser;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtPassword;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private ComboBox<String> cmbTypeUser;

    @FXML
    private Button btnNewUser;

    @FXML
    private AnchorPane deleteUserContainer;

    @FXML
    private Button btnSaveUser;

    @FXML
    private Button btnUpdateUser;

    @FXML
    private Text titleAddUser;

    private DialogTool dialogAddUser;

    private DialogTool dialogDeleteUser;

    private ObservableList<Users> listUsers;

    private ObservableList<Users> filterUsers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animateNodes();
        setValidations();
        loadData();
        setMask();
        setContextMenu();
        deleteUserDeleteKey();
        closeDialogWithEscapeKey();
        initalizeComboBox();
        selectTextFromTextField();
        closeDialogWithTextFields();
        filterUsers = FXCollections.observableArrayList();
    }

    private void setContextMenu() {
        CustomContextMenu contextMenu = new CustomContextMenu(tblUsers);

        contextMenu.setActionEdit(ev -> {
            showDialogEditUser();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDeleteUser();
            contextMenu.hide();
        });

        contextMenu.setActionDetails(ev -> {
            showDialogDetailsUser();
            contextMenu.hide();
        });

        contextMenu.setActionRefresh(ev -> {
            loadData();
            contextMenu.hide();
        });

        contextMenu.show();
    }

    private void setValidations() {
        RequieredFieldsValidators.toTextField(txtName);
        RequieredFieldsValidators.toTextField(txtUser);
        RequieredFieldsValidators.toTextField(txtPassword);
        RequieredFieldsValidators.toPasswordField(pfPassword);
        RequieredFieldsValidators.toComboBox(cmbTypeUser);
    }

    private void setMask() {
        TextFieldMask.onlyLetters(txtName, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtUser, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(pfPassword, 40);
    }

    private void selectTextFromTextField() {
        TextFieldMask.selectText(txtName);
        TextFieldMask.selectText(txtUser);
        TextFieldMask.selectText(pfPassword);
    }

    private void animateNodes() {
        Animations.fadeInUp(tblUsers);
        Animations.fadeInUp(hboxSearch);
        Animations.fadeInUp(btnNewUser);
    }

    private void initalizeComboBox() {
        cmbTypeUser.getItems().addAll("Administrator", "User");
        cmbTypeUser.focusedProperty().addListener((o, oldV, newV) -> {
            if (!oldV) {
                cmbTypeUser.show();
            } else {
                cmbTypeUser.hide();
            }
        });
    }

    @FXML
    private void showDialogAddUser() {
        disableTable();
        resetValidations();
        enableEditControls();
        rootUsers.setEffect(Constants.BOX_BLUR_EFFECT);
        btnSaveUser.toFront();
        btnUpdateUser.setVisible(true);
        btnSaveUser.setDisable(false);
        addUserContainer.setVisible(true);
        titleAddUser.setText("Add user");

        dialogAddUser = new DialogTool(addUserContainer, stckUsers);
        dialogAddUser.show();

        dialogAddUser.setOnDialogOpened(ev -> {
            txtName.requestFocus();
        });

        dialogAddUser.setOnDialogClosed(ev -> {
            tblUsers.setDisable(false);
            rootUsers.setEffect(null);
            addUserContainer.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogAddUser() {
        if (dialogAddUser != null) {
            dialogAddUser.close();
        }
    }

    @FXML
    private void showDialogDeleteUser() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        deleteUserContainer.setVisible(true);
        rootUsers.setEffect(Constants.BOX_BLUR_EFFECT);
        disableTable();

        dialogDeleteUser = new DialogTool(deleteUserContainer, stckUsers);
        dialogDeleteUser.show();

        dialogDeleteUser.setOnDialogClosed(ev -> {
            tblUsers.setDisable(false);
            rootUsers.setEffect(null);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogDeleteUser() {
        if (dialogDeleteUser != null) {
            dialogDeleteUser.close();
        }
    }

    @FXML
    private void showDialogEditUser() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddUser();
        selectedRecord();
        titleAddUser.setText("Update user");
        btnUpdateUser.toFront();
    }

    @FXML
    private void showDialogDetailsUser() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddUser();
        titleAddUser.setText("User details");
        btnSaveUser.toFront();
        btnSaveUser.setDisable(true);
        btnUpdateUser.setVisible(false);
        disableEditControls();
        selectedRecord();
    }

    @FXML
    private void loadData() {
        laodTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nameUser"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new JFXPasswordCellValueFactory());
        colTypeUser.setCellValueFactory(new JFXButtonTypeUserCellValueFactory());
    }

    private void laodTable() {
        ArrayList<Users> list = new ArrayList<>();
        // כאן ניתן להוסיף נתונים באופן ידני לצורך בדיקה
        list.add(new Users(1, "Admin", "admin@example.com", "admin123", "Administrator"));
        list.add(new Users(2, "User", "user@example.com", "user123", "User"));

        listUsers = FXCollections.observableArrayList(list);
        tblUsers.setItems(listUsers);
    }

    @FXML
    private void newUser() {
        String name = txtName.getText().trim();
        String user = txtUser.getText().trim();
        String password = pfPassword.getText().trim();

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
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (password.isEmpty()) {
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (password.length() < 4) {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (cmbTypeUser.getSelectionModel().isEmpty()) {
            Animations.shake(cmbTypeUser);
            return;
        }

        // להוסיף בדיקת משתמשים קיימים
        for (Users existingUser : listUsers) {
            if (existingUser.getEmail().equals(user)) {
                NotificationsBuilder.create(NotificationType.INVALID_ACTION, Constants.MESSAGE_USER_ALREADY_EXISTS);
                txtUser.requestFocus();
                Animations.shake(txtUser);
                return;
            }
        }

        Users newUser = new Users(name, user, password, cmbTypeUser.getSelectionModel().getSelectedItem());
        try {
            newUser.setProfileImage(DefaultProfileImage.getImage(name));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        listUsers.add(newUser);
        closeDialogAddUser();
        loadData();
        cleanControls();
        AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, Constants.MESSAGE_ADDED);
    }

    @FXML
    private void updateUser() {
        String name = txtName.getText().trim();
        String user = txtUser.getText().trim();
        String password = txtPassword.getText().trim();
        int id = tblUsers.getSelectionModel().getSelectedItem().getId();
        String userFromTable = tblUsers.getSelectionModel().getSelectedItem().getEmail();
        String userType = tblUsers.getSelectionModel().getSelectedItem().getUserType();

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
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (password.isEmpty()) {
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (password.length() < 4) {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (cmbTypeUser.getSelectionModel().isEmpty()) {
            Animations.shake(cmbTypeUser);
            return;
        }

        // בדיקה האם ניתן לעדכן את המשתמש
        if (id == 1 && cmbTypeUser.getSelectionModel().getSelectedItem().equals("User")) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, ADMINISTRATOR_ONLY);
            Animations.shake(cmbTypeUser);
            return;
        }

        if (!user.equals(userFromTable)) {
            for (Users existingUser : listUsers) {
                if (existingUser.getEmail().equals(user)) {
                    NotificationsBuilder.create(NotificationType.INVALID_ACTION, Constants.MESSAGE_USER_ALREADY_EXISTS);
                    Animations.shake(txtUser);
                    return;
                }
            }
        }

        if (id == 1 && !cmbTypeUser.getSelectionModel().getSelectedItem().equals(userType)) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, UNABLE_TO_CHANGE);
            Animations.shake(cmbTypeUser);
            return;
        }

        Users updatedUser = new Users(id, name, user, password, cmbTypeUser.getSelectionModel().getSelectedItem());
        listUsers.set(listUsers.indexOf(tblUsers.getSelectionModel().getSelectedItem()), updatedUser);
        closeDialogAddUser();
        loadData();
        cleanControls();
        AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, Constants.MESSAGE_UPDATED);
    }

    @FXML
    private void deleteUser() {
        int id = tblUsers.getSelectionModel().getSelectedItem().getId();

        if (id == 1) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, CANNOT_DELETED);
            return;
        }

        listUsers.remove(tblUsers.getSelectionModel().getSelectedItem());
        loadData();
        closeDialogDeleteUser();
        AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, Constants.MESSAGE_DELETED);
    }

    private void selectedRecord() {
        Users users = tblUsers.getSelectionModel().getSelectedItem();
        txtName.setText(users.getNameUser());
        txtUser.setText(users.getEmail());
        pfPassword.setText(users.getPass());
        cmbTypeUser.setValue(users.getUserType());
    }

    private void disableEditControls() {
        txtName.setEditable(false);
        txtUser.setEditable(false);
        txtPassword.setEditable(false);
        pfPassword.setEditable(false);
    }

    private void enableEditControls() {
        txtName.setEditable(true);
        txtUser.setEditable(true);
        txtPassword.setEditable(true);
        pfPassword.setEditable(true);
    }

    private void cleanControls() {
        txtName.clear();
        txtUser.clear();
        txtPassword.clear();
        pfPassword.clear();
        cmbTypeUser.getSelectionModel().clearSelection();
    }

    private void resetValidations() {
        RequieredFieldsValidators.resetValidation(txtUser);
        RequieredFieldsValidators.resetValidation(txtName);
        RequieredFieldsValidators.resetValidation(txtPassword);
        RequieredFieldsValidators.resetValidation(pfPassword);
        RequieredFieldsValidators.resetValidation(cmbTypeUser);
    }

    private void disableTable() {
        tblUsers.setDisable(true);
    }

    private void closeDialogWithEscapeKey() {
        rootUsers.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogDeleteUser();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                tblUsers.setDisable(false);
                rootUsers.setEffect(null);
                AlertsBuilder.close();
            }
        });

        addUserContainer.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }
        });
    }

    private void closeDialogWithTextFields() {
        txtName.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }
        });

        txtPassword.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }
        });

        cmbTypeUser.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }
        });
    }

    private void deleteUserDeleteKey() {
        rootUsers.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (tblUsers.isDisable()) {
                    return;
                }

                if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
                    AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Constants.MESSAGE_NO_RECORD_SELECTED);
                    return;
                }

                deleteUser();
            }
        });
    }

    @FXML
    private void filterName() {
        String name = txtSearchName.getText().trim();
        if (name.isEmpty()) {
            tblUsers.setItems(listUsers);
        } else {
            filterUsers.clear();
            for (Users u : listUsers) {
                if (u.getNameUser().toLowerCase().contains(name.toLowerCase())) {
                    filterUsers.add(u);
                }
            }
            tblUsers.setItems(filterUsers);
        }
    }

    @FXML
    private void filterUser() {
        String user = txtSearchUser.getText().trim();
        if (user.isEmpty()) {
            tblUsers.setItems(listUsers);
        } else {
            filterUsers.clear();
            for (Users u : listUsers) {
                if (u.getEmail().toLowerCase().contains(user.toLowerCase())) {
                    filterUsers.add(u);
                }
            }
            tblUsers.setItems(filterUsers);
        }
    }

    private class JFXPasswordCellValueFactory implements Callback<TableColumn.CellDataFeatures<Users, PasswordField>, ObservableValue<PasswordField>> {

        @Override
        public ObservableValue<PasswordField> call(TableColumn.CellDataFeatures<Users, PasswordField> param) {
            Users item = param.getValue();

            PasswordField password = new PasswordField();
            password.setEditable(false);
            password.setPrefWidth(colPassword.getWidth() / 0.5);
            password.setText(item.getPass());
            password.getStyleClass().addAll("password-field-cell", "table-row-cell");

            return new SimpleObjectProperty<>(password);
        }
    }

    private class JFXButtonTypeUserCellValueFactory implements Callback<TableColumn.CellDataFeatures<Users, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Users, Button> param) {
            Users item = param.getValue();

            Button button = new Button();
            button.setPrefWidth(colTypeUser.getWidth() / 0.5);
            button.setText(item.getUserType());

            if (item.getUserType().equals("Administrator")) {
                button.getStyleClass().addAll("button-administrator", "table-row-cell");
            } else {
                button.getStyleClass().addAll("button-user", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
