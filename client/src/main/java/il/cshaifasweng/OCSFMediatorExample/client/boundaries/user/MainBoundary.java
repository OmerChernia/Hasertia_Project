
package il.cshaifasweng.OCSFMediatorExample.client.boundaries.user;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.InvocationTargetException;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.registeredUser.OrdersBoundary;
import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.LoginPageController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.PriceRequestController;
import il.cshaifasweng.OCSFMediatorExample.client.controllers.TheaterController;
import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.util.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.entities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeLoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.LoginMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Person;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainBoundary implements Initializable {

    @FXML
    private Label lblWelcome;
    private ObservableList<MovieInstance> listProducts;

    private ObservableList<MovieInstance> filterProducts;

    @FXML
    private Label Complaints;

    @FXML
    private Button btnAbout;

    @FXML
    private Button btnComplaints;

    @FXML
    private Button btnEditMovieList;

    @FXML
    private Button btnEditScreenings;

    @FXML
    private Button btnLog;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnME;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnPriceChange;
    @FXML
    private Button btnReports;

    @FXML
    private Button btnSettings;

    @FXML
    private VBox customerLogin;

    @FXML
    private RadioButton customerRadioButton;

    @FXML
    private VBox employeeLogin;

    @FXML
    private RadioButton employeeRadioButton;

    @FXML
    private AnchorPane pnLogIn;

    @FXML
    private AnchorPane rootContainer;

    @FXML
    private AnchorPane rootSideMenu;

    @FXML
    private StackPane stckMain;

    @FXML
    private AnchorPane tooltipAbout;

    @FXML
    private AnchorPane tooltipComplaints;

    @FXML
    private AnchorPane tooltipEditMovieList;



    @FXML
    private AnchorPane tooltipEditScreening;

    @FXML
    private AnchorPane tooltipHome;

    @FXML
    private AnchorPane tooltipMe;

    @FXML
    private AnchorPane tooltipOrders;

    @FXML
    private AnchorPane tooltipPrice;

    @FXML
    private AnchorPane tooltipReports;

    @FXML
    private AnchorPane tooltipSettings;

    @FXML
    private ToggleGroup userTypeToggleGroup;


    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtEmploee;


    private DialogTool dialogLogIn;
    @FXML
    private ImageView image;

    private static String loggedInUserId;
    private static Employee.EmployeeType loggedInEmploeeId;


    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        txtUser.setStyle("-fx-text-fill: #cae8fb;");
        txtEmploee.setStyle("-fx-text-fill: #cae8fb;");
        txtPassword.setStyle("-fx-text-fill: #cae8fb;");

        // Create the ToggleGroup and add the radio buttons to it
        userTypeToggleGroup = new ToggleGroup();
        customerRadioButton.setToggleGroup(userTypeToggleGroup);
        employeeRadioButton.setToggleGroup(userTypeToggleGroup);

        // Set the initial selection if needed
        customerRadioButton.setSelected(true);

        // Add a listener to clear text fields on selection change
        userTypeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            clearTextFields();
        });

        homeWindowsInitialize();
        resetButtons();
        tooltips();
    }

    private void clearTextFields() {
        txtUser.clear();
        txtEmploee.clear();
        txtPassword.clear();
        lblWelcome.setText("Welcome to 'Hasretia'!");
    }



    public void homeWindowsInitialize() {
        showFXMLWindows(ConstantsPath.HOME_VIEW);
    }

    @FXML
    private void homeWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.HOME_VIEW );
    }

    @FXML
    private void EditMovieScreeningsWindows(ActionEvent event) {

        showFXMLWindows(ConstantsPath.CONTENT_SCREENINGS_VIEW );
    }

    @FXML
    private void CustomerServiceWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.CUSTOMER_SERVICE_VIEW );
    }

    @FXML
    private void settingsWindows(ActionEvent event) {
        showFXMLWindows( ConstantsPath.COMPLAINT_VIEW );
    }

    @FXML
    private void statisticsWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.COMPANY_MANAGER_VIEW );
    }

    @FXML
    private void aboutWindows(ActionEvent event) {
        showFXMLWindows( ConstantsPath.ABOUT_VIEW  ) ;
    }

    @FXML
    private void productsWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.CONTENT_MOVIES_VIEW );
    }

    @FXML
    private void priceChangeWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.PRICE_CHANGE_VIEW);
    }


    @FXML
    private void addUserWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.ORDERS_VIEW );
    }

    @FXML
    private void MEWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.ME_PURCHASE_VIEW);
    }

    @FXML
    private void closeLoginDialog() {
        if (dialogLogIn != null) {
            dialogLogIn.close();
        }
    }

    @FXML
    private void loginWindow() {
        rootContainer.setEffect(ConstantsPath.BOX_BLUR_EFFECT); // Blur the background

        pnLogIn.setVisible(true); // Show the login pane

        dialogLogIn = new DialogTool(pnLogIn, stckMain);
        dialogLogIn.show();
        dialogLogIn.setOnDialogOpened(ev -> employeeLogin.requestFocus());
        handleRadioButtonAction();

        dialogLogIn.setOnDialogClosed(ev -> {
            rootContainer.setEffect(null); // Remove the blur effect
            pnLogIn.setVisible(false); // Hide the login pane
        });

        pnLogIn.toFront(); // Ensure the login pane is in front of other elements
    }


    @FXML
    private void handleRadioButtonAction() {
        if (customerRadioButton.isSelected()) {
            customerLogin.setVisible(true);
            customerLogin.setManaged(true);
            employeeLogin.setVisible(false);
            employeeLogin.setManaged(false);
        } else if (employeeRadioButton.isSelected()) {
            customerLogin.setVisible(false);
            customerLogin.setManaged(false);
            employeeLogin.setVisible(true);
            employeeLogin.setManaged(true);
        }
    }

    @FXML
    private void closeStage() {
        ((Stage) btnHome.getScene().getWindow()).close();
    }

    private void setDisableButtons(ActionEvent event, Button button) {
        if (event.getSource().equals(button)) {
            button.setDisable(true);
        } else {
            button.setDisable(false);
        }
    }

    private void tooltips() {
        Animations.tooltip(btnHome, tooltipHome);
        Animations.tooltip(btnEditMovieList, tooltipEditMovieList);
        Animations.tooltip(btnEditScreenings, tooltipEditScreening);
        Animations.tooltip(btnSettings, tooltipSettings);
        Animations.tooltip(btnOrders, tooltipOrders);
        Animations.tooltip(btnPriceChange, tooltipPrice);
        Animations.tooltip(btnComplaints, tooltipComplaints);
        Animations.tooltip(btnReports, tooltipReports);
        Animations.tooltip(btnAbout, tooltipAbout);
        Animations.tooltip(btnME, tooltipMe);

    }

    private static Object currentController;

    //to set Controllers that cant be reached by rootContainer: TheaterPurchaseBoundary and HomeViewingPurchaseBoundary
    public static void setCurrentController(Object new_currentController)
    {
        currentController = new_currentController;
    }

    private void showFXMLWindows(String FXMLName)  {

        if (currentController != null) {
            try {
                currentController.getClass().getMethod("cleanup").invoke(currentController);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {}
        }


        rootContainer.getChildren().clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLName));
            Parent root = loader.load();


            currentController = loader.getController();


            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);


            rootContainer.getChildren().setAll(root);

        } catch (IOException ex) {
            Logger.getLogger(MainBoundary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Button getBtnStatistics() {
        return btnReports;
    }



    public Button getBtnAbout() {
        return btnAbout;
    }


    @FXML
    private void login(ActionEvent event) {
        String UserName = txtUser.getText();
        String EmploeeName = txtEmploee.getText();
        String password = txtPassword.getText();

        if (customerRadioButton.isSelected()) {
            LoginPageController.requestUserLogin(UserName);
            System.out.println("SEND Login");
        } else if (employeeRadioButton.isSelected()) {
            LoginPageController.requestEmployeeLogin(EmploeeName, password);
            System.out.println("SEND EMPLOYEE Login");

        }
        resetButtons();
        clearTextFields();
        homeWindowsInitialize();
    }

    @Subscribe
    public void handleLoginResponse(LoginMessage loginMessage) {
        if (loginMessage instanceof EmployeeLoginMessage) {

            handleEmployeeLoginResponse((EmployeeLoginMessage) loginMessage);
            String role = "";
            if(employeeRadioButton.isSelected()) {
                role = "EMPLOYEE";
                switch (((EmployeeLoginMessage) loginMessage).employeeType) {
                    case THEATER_MANAGER:
                        role = "Theater Manager";
                        break;
                    case COMPANY_MANAGER:
                        role = "Company Manager";
                        break;
                    case CUSTOMER_SERVICE:
                        role = "Customer Service";
                        break;
                    case CONTENT_MANAGER:
                        role = "Content Manager";
                        break;
                }
            }


            String finalRole = role;
            Platform.runLater(() -> lblWelcome.setText("Welcome " + finalRole + "!"));
        } else {
            Platform.runLater(() -> handleCustomerLoginResponse(loginMessage));
        }
    }


    public static int getId(){
        return Integer.parseInt(loggedInUserId);
    }




    public void handleCustomerLoginResponse(LoginMessage loginMessage) {
        Platform.runLater(() -> {
            if (loginMessage.responseType == LoginMessage.ResponseType.LOGIN_SUCCESFUL)
            {
                lblWelcome.setText("Nice to see you again in 'Hasretia'!");

                SimpleClient.user = loginMessage.id; // Save the logged-in user ID

                this.loggedInUserId = loginMessage.id; // Save the logged-in user ID
                NotificationsBuilder.create(NotificationType.SUCCESS, "Registered User " + loginMessage.id + " Logged in!");

                // Update the UI based on the user's role
                updateUserBasedOnRole(loginMessage.id);

                // Close the login dialog or do any other post-login actions
                closeLoginDialog();
            } else if (loginMessage.responseType == LoginMessage.ResponseType.LOGIN_FAILED) {
                // Handle login failure (show error message, etc.)
                NotificationsBuilder.create(NotificationType.ERROR,"Login failed. Please check your credentials.");

            } else if (loginMessage.responseType == LoginMessage.ResponseType.ALREADY_LOGGED) {

                NotificationsBuilder.create(NotificationType.ERROR,"Registered User is already logged in.");
            }
        });
    }


    public void handleEmployeeLoginResponse(EmployeeLoginMessage loginMessage) {
        Platform.runLater(() -> {
            if (loginMessage.responseType == LoginMessage.ResponseType.LOGIN_SUCCESFUL) {
                SimpleClient.user = loginMessage.id; // Save the logged-in employee ID

                this.loggedInUserId = loginMessage.id; // Save the logged-in employee ID
                this.loggedInEmploeeId = loginMessage.employeeType; // Save the logged-in employee type


                // Update the UI based on the user's role
                updateEmployeeBasedOnRole(loginMessage.id, loginMessage.employeeType);
                NotificationsBuilder.create(NotificationType.SUCCESS, "Employee" + loginMessage.employeeType+  loginMessage.employeeType.name() + "Logged in!");

                // Close the login dialog or do any other post-login actions
                closeLoginDialog();
            } else if (loginMessage.responseType == LoginMessage.ResponseType.LOGIN_FAILED) {
                // Handle login failure (show error message, etc.)
                NotificationsBuilder.create(NotificationType.ERROR,"Login failed. Please check your credentials.");
            } else if (loginMessage.responseType == LoginMessage.ResponseType.ALREADY_LOGGED) {
                // Handle the case where the user is already logged in
                NotificationsBuilder.create(NotificationType.ERROR,"User is already logged in.");
            }
        });
    }


    private void resetButtons() {
        // Disable all buttons by default
        btnOrders.setVisible(false);
        btnEditMovieList.setVisible(false);
        btnEditScreenings.setVisible(false);
        btnComplaints.setVisible(false);
        btnReports.setVisible(false);
        btnPriceChange.setVisible(false);


        // Show common buttons
        btnAbout.setVisible(true);
        btnLog.setVisible(true);
        btnSettings.setVisible(true);
        btnHome.setVisible(true);
        btnME.setVisible(true);
    }

    private void updateUserBasedOnRole(String userId ) {
        // Disable all buttons by default
        btnEditMovieList.setVisible(false);
        btnEditScreenings.setVisible(false);
        btnComplaints.setVisible(false);
        btnReports.setVisible(false);
        btnPriceChange.setVisible(false);


        // Show common buttons
        btnAbout.setVisible(true);
        btnLog.setVisible(true);
        btnSettings.setVisible(true);
        btnHome.setVisible(true);
        btnME.setVisible(true);
        btnOrders.setVisible(true);

    }


    private void updateEmployeeBasedOnRole(String userId, Employee.EmployeeType role ) {
        // Disable all buttons by default
        btnEditMovieList.setVisible(false);
        btnEditScreenings.setVisible(false);
        btnComplaints.setVisible(false);
        btnReports.setVisible(false);
        btnPriceChange.setVisible(false);
        btnOrders.setVisible(false);


        // Show common buttons
        btnAbout.setVisible(true);
        btnLog.setVisible(true);
        btnHome.setVisible(true);
        btnME.setVisible(true);


        switch (role) {
            case CONTENT_MANAGER:
                btnEditMovieList.setVisible(true);
                btnEditScreenings.setVisible(true);
                break;
            case CUSTOMER_SERVICE:
                btnComplaints.setVisible(true);
                break;
            case THEATER_MANAGER:
                btnReports.setVisible(true);
            case  COMPANY_MANAGER:
                btnReports.setVisible(true);
                btnPriceChange.setVisible(true);
                break;

        }

    }


    @FXML
    private void logout() {
        if (loggedInUserId != null || loggedInEmploeeId != null) {
            AlertsBuilder.create(AlertType.WARNING, stckMain, stckMain, stckMain, "Are you sure you want to log out?", "Yes", () -> {
                        sendLogoutRequest();
                        loggedInUserId = null;
                        loggedInEmploeeId = null;
                        resetButtons();
                        clearTextFields();
                        closeLoginDialog();
                        homeWindowsInitialize();
                        AlertsBuilder.create(AlertType.SUCCESS, stckMain, stckMain, stckMain, "You have successfully logged out.");
                    } , "No", () -> {
                        AlertsBuilder.create(AlertType.SUCCESS, stckMain, stckMain, stckMain, "Have A nice Stay in 'Hasretia' :)");
                    }
            );
        } else {
            loginWindow();
        }
    }



    public static void sendLogoutRequest() {
        if (loggedInUserId != null )
        {
            SimpleClient.user = ""; // Save the logged-in user ID
            if(loggedInEmploeeId != null)
                LoginPageController.requestEmployeeLogOut(loggedInUserId);
            else
                LoginPageController.requestUserLogOut(loggedInUserId);
        }
    }


}
