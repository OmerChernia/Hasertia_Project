package il.cshaifasweng.OCSFMediatorExample.client.mask;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RequieredFieldsValidators {

    private static final String MESSAGE = "Obligatory field";

    public static void toTextField(TextField txt) {
        addValidationListener(txt);
    }

    public static void toPasswordField(PasswordField txt) {
        addValidationListener(txt);
    }

    public static void toTextArea(TextArea txt) {
        addValidationListener(txt);
    }

    public static void toComboBox(ComboBox<?> comboBox) {
        addValidationListener(comboBox);
    }

    public static void toDatePicker(DatePicker datePicker) {
        addValidationListener(datePicker);
    }

    private static void addValidationListener(Control control) {
        control.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // focus lost
                if (isControlEmpty(control)) {
                    showValidationMessage(control, MESSAGE);
                } else {
                    hideValidationMessage(control);
                }
            }
        });
    }

    private static boolean isControlEmpty(Control control) {
        if (control instanceof TextField) {
            return ((TextField) control).getText().trim().isEmpty();
        } else if (control instanceof TextArea) {
            return ((TextArea) control).getText().trim().isEmpty();
        } else if (control instanceof ComboBox<?>) {
            return ((ComboBox<?>) control).getValue() == null;
        } else if (control instanceof DatePicker) {
            return ((DatePicker) control).getValue() == null;
        }
        return false;
    }

    private static void showValidationMessage(Control control, String message) {
        Text validationMessage = new Text(message);
        validationMessage.setFill(Color.RED);
        validationMessage.setUserData("validationMessage");
        if (control.getParent() instanceof Pane) {
            Pane parent = (Pane) control.getParent();
            parent.getChildren().add(validationMessage);
            AnchorPane.setTopAnchor(validationMessage, control.getLayoutY() + control.getHeight() + 2);
            AnchorPane.setLeftAnchor(validationMessage, control.getLayoutX());
        }
    }

    private static void hideValidationMessage(Control control) {
        if (control.getParent() instanceof Pane) {
            Pane parent = (Pane) control.getParent();
            parent.getChildren().removeIf(node -> "validationMessage".equals(node.getUserData()));
        }
    }
}
