
package il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import il.cshaifasweng.OCSFMediatorExample.client.util.DialogTool;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.util.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.client.util.animations.Animations;
import il.cshaifasweng.OCSFMediatorExample.client.util.generators.DBGenerate;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {
    private final DBGenerate db = new DBGenerate();

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
    private Button btnExit;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnME;

    @FXML
    private Button btnOrders;

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
    private AnchorPane tooltipEditScreenings;

    @FXML
    private AnchorPane tooltipExit;

    @FXML
    private AnchorPane tooltipHome;

    @FXML
    private AnchorPane tooltipMe;

    @FXML
    private AnchorPane tooltipOrders;

    @FXML
    private AnchorPane tooltipReports;

    @FXML
    private AnchorPane tooltipSettings;

    private DialogTool dialogLogIn;
    @FXML
    private ImageView image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeWindowsInitialize();

        tooltips();
    }



    @FXML
    private void setDisableButtons(ActionEvent event) {
        setDisableButtons(event, btnHome);
        setDisableButtons(event, btnComplaints);

        setDisableButtons(event, btnAbout);
//        setDisableButtons(event, btnSettings);
//        setDisableButtons(event, btnExit);
    }

    private void homeWindowsInitialize() {
        btnHome.setDisable(true);
        showFXMLWindows(ConstantsPath.COSTUMER_PACKAGE +"HomeView.fxml");
    }

    @FXML
    private void homeWindows(ActionEvent event) {
        btnHome.setDisable(true);
        showFXMLWindows(ConstantsPath.COSTUMER_PACKAGE +"HomeView.fxml" );
        setDisableButtons(event);
    }

    @FXML
    private void EditMovieScreeningsWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.EMPLOEE_PACKAGE +"EditMovieScreeningsView.fxml" );
        setDisableButtons(event);
    }

    @FXML
    private void CustomerServiceWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.EMPLOEE_PACKAGE +"CustomerService.fxml" );
        setDisableButtons(event);
    }

    @FXML
    private void settingsWindows(ActionEvent event) {
        showFXMLWindows( ConstantsPath.EMPLOEE_PACKAGE +"ComplaintView.fxml" );
//        setDisableButtons(event);
    }

    @FXML
    private void statisticsWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.EMPLOEE_PACKAGE + "ReportsView.fxml" );
        setDisableButtons(event);
    }

    @FXML
    private void aboutWindows(ActionEvent event) {
        showFXMLWindows( ConstantsPath.EMPLOEE_PACKAGE +  "AboutView.fxml"  ) ;
        setDisableButtons(event);
    }

    @FXML
    private void productsWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.EMPLOEE_PACKAGE +  "EditMovieListView.fxml" );
        setDisableButtons(event);
    }

    @FXML
    private void addUserWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.EMPLOEE_PACKAGE +  "OrdersView.fxml" );
        setDisableButtons(event);
    }

    @FXML
    private void MEWindows(ActionEvent event) {
        showFXMLWindows(ConstantsPath.COSTUMER_PACKAGE +  "MEPurchaseView.fxml" );
        setDisableButtons(event);
    }



    @FXML
    private void closeLoginDialog() {
        if (dialogLogIn != null) {
            dialogLogIn.close();
        }
    }


    @FXML
    private void loginWindow() {
        rootContainer.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

        pnLogIn.setVisible(true);

        dialogLogIn = new DialogTool(pnLogIn, stckMain);

        dialogLogIn.show();
        dialogLogIn.setOnDialogOpened(ev -> employeeLogin.requestFocus());
        handleRadioButtonAction();

        dialogLogIn.setOnDialogClosed(ev -> {

            rootContainer.setEffect(null);
            pnLogIn.setVisible(false);
        });

        pnLogIn.toFront();
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
        Animations.tooltip(btnEditScreenings, tooltipEditScreenings);
        Animations.tooltip(btnExit, tooltipExit);
        Animations.tooltip(btnOrders, tooltipOrders);
        Animations.tooltip(btnComplaints, tooltipComplaints);
        Animations.tooltip(btnReports, tooltipReports);
        Animations.tooltip(btnAbout, tooltipAbout);
     }



    private void showFXMLWindows(String FXMLName) {
        rootContainer.getChildren().clear();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(FXMLName));
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            rootContainer.getChildren().setAll(root);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Button getBtnStatistics() {
        return btnReports;
    }



    public Button getBtnAbout() {
        return btnAbout;
    }


}
