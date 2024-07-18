package il.cshaifasweng.OCSFMediatorExample.client.alerts;

import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class AlertsBuilder {

    private static String title;
    private static String buttonStyle;
    private static String titleStyle;
    private static String bodyStyle;
    private static Dialog<Boolean> dialog;

    public static void create(AlertType type, StackPane dialogContainer, Node nodeToBlur, Node nodeToDisable, String body) {
        setFunction(type);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(390, 230);

        Button button = new Button("Okey");

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutY(115);
        buttonContainer.setPrefSize(390, 115);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(button);

        Text textTitle = new Text(title);

        Text textBody = new Text(body);

        VBox textContainer = new VBox();
        textContainer.setSpacing(5);
        textContainer.setPrefSize(390, 115);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.setPadding(new Insets(0, 0, 0, 30));
        textContainer.getChildren().addAll(textTitle, textBody);
        root.getChildren().addAll(buttonContainer, textContainer);

        nodeToDisable.setDisable(true);
        nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);

        dialog = new Dialog<Boolean>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(root);
        dialogPane.setBackground(null);

        button.setOnMouseClicked(e -> {
            System.out.println("Button clicked, closing dialog");
            Platform.runLater(() -> {
                dialog.setResult(Boolean.TRUE);
                dialog.close();
            });
        });

        dialog.setOnShown(e -> {
            System.out.println("Dialog shown");
            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);
        });

        dialog.setOnHidden(e -> {
            System.out.println("Dialog hidden");
            nodeToDisable.setDisable(false);
            nodeToBlur.setEffect(null);
        });

        dialog.showAndWait();
    }

    public static void close() {
        if (dialog != null) {
            System.out.println("Dialog closed programmatically");
            Platform.runLater(() -> {
                dialog.setResult(Boolean.TRUE);
                dialog.close();
            });
        }
    }

    private static void setFunction(AlertType type) {
        switch (type) {
            case SUCCES:
                title = "Succes!";
                buttonStyle = "alert-success-button";
                titleStyle = "alert-success-title";
                bodyStyle = "alert-success-body";
                break;

            case ERROR:
                title = "Oops!";
                buttonStyle = "alert-error-button";
                titleStyle = "alert-error-title";
                bodyStyle = "alert-error-body";
                break;

        }
    }
}
