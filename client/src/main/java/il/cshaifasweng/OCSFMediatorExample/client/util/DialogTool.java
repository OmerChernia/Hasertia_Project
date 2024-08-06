package il.cshaifasweng.OCSFMediatorExample.client.util;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

public class DialogTool {

    private final Stage dialogStage;
    private final StackPane root;

    public DialogTool(Region region, StackPane container) {

        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.TRANSPARENT);

        root = new StackPane(region);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(ConstantsPath.CSS_LIGHT_THEME).toExternalForm()); // Ensure this path is correct
        dialogStage.setScene(scene);

    }

    public void setOnDialogOpened(EventHandler<WindowEvent> action) {
        dialogStage.setOnShown(action);
    }

    public void setOnDialogClosed(EventHandler<WindowEvent> action) {
        dialogStage.setOnHidden(action);
    }

    public void show() {
        dialogStage.show();
    }

    public void close() {
        System.out.println("Closing dialog");
        dialogStage.close();
    }


}
