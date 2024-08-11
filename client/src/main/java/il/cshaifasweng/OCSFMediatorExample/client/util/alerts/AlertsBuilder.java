package il.cshaifasweng.OCSFMediatorExample.client.util.alerts;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertsBuilder {

    private static String title;

    private static String buttonStyle;

    private static String titleStyle;

    private static String bodyStyle;

    public static void create(AlertType type, StackPane dialogContainer, Node nodeToBlur, Node nodeToDisable, String body) {
        create(type, dialogContainer, nodeToBlur, nodeToDisable, body, "Okay", null, null);
    }

    public static void create(AlertType type, StackPane dialogContainer, Node nodeToBlur, Node nodeToDisable, String body, String primaryButtonText, Runnable primaryAction, String secondaryButtonText) {

        setFunction(type);

        Stage alertStage = new Stage();
        alertStage.initStyle(StageStyle.UNDECORATED);
        alertStage.initModality(Modality.APPLICATION_MODAL);

        AnchorPane content = new AnchorPane();
        content.setPrefSize(390, 230);

        Button primaryButton = new Button(primaryButtonText);
        primaryButton.getStyleClass().add(buttonStyle);

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutY(115);
        buttonContainer.setPrefSize(390, 115);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().add(primaryButton);

        if (secondaryButtonText != null) {
            Button secondaryButton = new Button(secondaryButtonText);
            secondaryButton.getStyleClass().add("alert-warning-button ");
            buttonContainer.getChildren().add(secondaryButton);
            secondaryButton.setOnMouseClicked(e -> alertStage.close());
        }

        Text textTitle = new Text(title);
        textTitle.getStyleClass().add(titleStyle);

        Text textBody = new Text(body);
        textBody.getStyleClass().add(bodyStyle);

        VBox textContainer = new VBox();
        textContainer.setSpacing(5);
        textContainer.setPrefSize(390, 115);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.setPadding(new Insets(0, 0, 0, 30));
        textContainer.getChildren().addAll(textTitle, textBody);
        content.getChildren().addAll(buttonContainer, textContainer);

        StackPane root = new StackPane(content);
        root.getStyleClass().add("jfx-dialog-overlay-pane");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(AlertsBuilder.class.getResource(ConstantsPath.CSS_LIGHT_THEME).toExternalForm());
        alertStage.setScene(scene);

        nodeToDisable.setDisable(true);
        nodeToBlur.setEffect(ConstantsPath.BOX_BLUR_EFFECT);

        alertStage.show();

        primaryButton.setOnMouseClicked(e -> {
            if (primaryAction != null) {
                primaryAction.run();
            }
            alertStage.close();
        });

        alertStage.setOnShown(e -> {
            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(ConstantsPath.BOX_BLUR_EFFECT);
        });

        alertStage.setOnHidden(e -> {
            nodeToDisable.setDisable(false);
            nodeToBlur.setEffect(null);
        });

        // Close the alert by clicking outside
        root.setOnMouseClicked(e -> alertStage.close());
        content.setOnMouseClicked(e -> e.consume());
    }

    private static void setFunction(AlertType type) {
        switch (type) {
            case SUCCESS:
                title = "Success!";
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

            case WARNING:
                title = "Warning!";
                buttonStyle = "alert-warning-button";
                titleStyle = "alert-warning-title";
                bodyStyle = "alert-warning-body";
                break;

            case INFO:
                title = "Information";
                buttonStyle = "alert-info-button";
                titleStyle = "alert-info-title";
                bodyStyle = "alert-info-body";
                break;
        }
    }
}
