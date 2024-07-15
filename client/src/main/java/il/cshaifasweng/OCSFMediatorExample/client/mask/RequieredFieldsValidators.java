package il.cshaifasweng.OCSFMediatorExample.client.mask;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RequieredFieldsValidators {

    private static final String MESSAGE = "Obligatory field";

    public static void toTextField(TextField txt) {
        Text warningText = createWarningText();
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal && txt.getText().trim().isEmpty()) {
                txt.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                if (!txt.getParent().getChildrenUnmodifiable().contains(warningText)) {
                    ((VBox) txt.getParent()).getChildren().add(warningText);
                }
            } else {
                txt.setStyle(null);
                ((VBox) txt.getParent()).getChildren().remove(warningText);
            }
        });
    }

    public static void toPasswordField(PasswordField txt) {
        Text warningText = createWarningText();
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal && txt.getText().trim().isEmpty()) {
                txt.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                if (!txt.getParent().getChildrenUnmodifiable().contains(warningText)) {
                    ((VBox) txt.getParent()).getChildren().add(warningText);
                }
            } else {
                txt.setStyle(null);
                ((VBox) txt.getParent()).getChildren().remove(warningText);
            }
        });
    }

    public static void toTextArea(TextArea txt) {
        Text warningText = createWarningText();
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal && txt.getText().trim().isEmpty()) {
                txt.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                if (!txt.getParent().getChildrenUnmodifiable().contains(warningText)) {
                    ((VBox) txt.getParent()).getChildren().add(warningText);
                }
            } else {
                txt.setStyle(null);
                ((VBox) txt.getParent()).getChildren().remove(warningText);
            }
        });
    }

    public static void toComboBox(ComboBox<?> comboBox) {
        Text warningText = createWarningText();
        comboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal && comboBox.getSelectionModel().isEmpty()) {
                comboBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                if (!comboBox.getParent().getChildrenUnmodifiable().contains(warningText)) {
                    ((VBox) comboBox.getParent()).getChildren().add(warningText);
                }
            } else {
                comboBox.setStyle(null);
                ((VBox) comboBox.getParent()).getChildren().remove(warningText);
            }
        });
    }

    public static void toDatePicker(DatePicker datePicker) {
        Text warningText = createWarningText();
        datePicker.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal && datePicker.getValue() == null) {
                datePicker.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                if (!datePicker.getParent().getChildrenUnmodifiable().contains(warningText)) {
                    ((VBox) datePicker.getParent()).getChildren().add(warningText);
                }
            } else {
                datePicker.setStyle(null);
                ((VBox) datePicker.getParent()).getChildren().remove(warningText);
            }
        });
    }

    private static Text createWarningText() {
        Text warningText = new Text(MESSAGE);
        warningText.setFill(Color.RED);
        warningText.setFont(Font.font("Arial", 12));
        return warningText;
    }

    public static void resetValidation(TextField txt) {
        txt.setStyle("");
    }

    public static void resetValidation(PasswordField txt) {
        txt.setStyle("");
    }

    public static void resetValidation(TextArea txt) {
        txt.setStyle("");
    }

    public static void resetValidation(ComboBox<?> comboBox) {
        comboBox.setStyle("");
    }

    public static void resetValidation(DatePicker datePicker) {
        datePicker.setStyle("");
    }

}







