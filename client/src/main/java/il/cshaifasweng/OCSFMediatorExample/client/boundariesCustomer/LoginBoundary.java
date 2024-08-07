package il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;


public class LoginBoundary {
    @FXML
    private RadioButton customerRadioButton;

    @FXML
    private RadioButton employeeRadioButton;

    @FXML
    private VBox customerLogin;

    @FXML
    private VBox employeeLogin;

    @FXML
    public void initialize() {
        handleRadioButtonAction(); // Initialize the visibility based on the default selected radio button
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
}
