package il.cshaifasweng.OCSFMediatorExample.client.util.mask;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RequieredFieldsValidators {

    private static final String MESSAGE = "Obligatory field";
    private static final String ICON_PATH = "/il/cshaifasweng/OCSFMediatorExample/client/media/warning.png"; // Update the path to your icon file

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
        ImageView icon = new ImageView(new Image(ICON_PATH));
        icon.setFitHeight(16);
        icon.setFitWidth(16);

        Text validationMessage = new Text(message);
        validationMessage.setFill(Color.RED);

        HBox hbox = new HBox(5); // 5 is the spacing between icon and text
        hbox.getChildren().addAll(icon, validationMessage);
        hbox.setUserData("validationMessage");

        if (control.getParent() instanceof Pane) {
            Pane parent = (Pane) control.getParent();
            parent.getChildren().add(hbox);
            AnchorPane.setTopAnchor(hbox, control.getLayoutY() + control.getHeight() + 2);
            AnchorPane.setLeftAnchor(hbox, control.getLayoutX());
        }
    }

    private static void hideValidationMessage(Control control) {
        if (control.getParent() instanceof Pane) {
            Pane parent = (Pane) control.getParent();
            parent.getChildren().removeIf(node -> "validationMessage".equals(node.getUserData()));
        }
    }

    public static void addLabelBehavior(Control control, String labelText) {
        Text label = new Text(labelText);
        label.setStyle("-fx-font-weight: bold");

        if (control.getParent() instanceof Pane) {
            Pane parent = (Pane) control.getParent();

            control.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) { // Focus gained
                    if (!parent.getChildren().contains(label)) {
                        parent.getChildren().add(label);
                    }
                    AnchorPane.setTopAnchor(label, control.getLayoutY() -20); // Move up
                    AnchorPane.setLeftAnchor(label, control.getLayoutX() +7); // Adjust position
                } else { // Focus lost
                    parent.getChildren().remove(label);
                }
            });
        }
    }



}
