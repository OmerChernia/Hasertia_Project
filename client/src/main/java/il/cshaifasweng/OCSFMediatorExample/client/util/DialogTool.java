package il.cshaifasweng.OCSFMediatorExample.client.util;

import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import javafx.event.EventHandler;
import javafx.scene.control.DialogEvent;

public class DialogTool {

    private final Dialog<Void> dialog;

    public DialogTool(Region region, StackPane container) {
        dialog = new Dialog<>();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().setContent(region);
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/DialogStyle.css").toExternalForm()
        );
        dialog.getDialogPane().getStyleClass().add("jfx-dialog-overlay-pane");
        dialog.getDialogPane().setBackground(null);

        // Positioning the dialog in the center of the container
        dialog.setOnShowing(event -> {
            dialog.getDialogPane().getScene().getWindow().setX(
                    container.getScene().getWindow().getX() + container.getWidth() / 2 - dialog.getDialogPane().getWidth() / 2
            );
            dialog.getDialogPane().getScene().getWindow().setY(
                    container.getScene().getWindow().getY() + container.getHeight() / 2 - dialog.getDialogPane().getHeight() / 2
            );
        });
    }

    public void setOnDialogOpened(EventHandler<DialogEvent> action) {
        dialog.setOnShown(action);
    }

    public void setOnDialogClosed(EventHandler<DialogEvent> action) {
        dialog.setOnHidden(action);
    }

    public void show() {
        dialog.show();
    }

    public void close() {
        dialog.close();
    }
}
