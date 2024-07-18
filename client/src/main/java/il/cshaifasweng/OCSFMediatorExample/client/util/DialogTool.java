package il.cshaifasweng.OCSFMediatorExample.client.util;

import javafx.scene.control.DialogEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.event.EventHandler;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class DialogTool {

    private final Dialog<Boolean> dialog;

    public DialogTool(Region region, StackPane container) {
        dialog = new Dialog<Boolean>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().setContent(region);
        dialog.getDialogPane().setBackground(Background.EMPTY);

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
        System.out.println("Closing dialog");
        dialog.setResult(Boolean.TRUE);
        dialog.close();
    }
}
