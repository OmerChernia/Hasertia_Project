/*
 * Copyright 2020-2021 LaynezCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laynezcoder.estfx.notifications;

import com.laynezcoder.estfx.Constants;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class NotificationsBuilder {

    private static Image icon;

    private static String title;

    public static void create(NotificationType type, String message) {
        setFunction(type);

        Stage notificationStage = new Stage();
        notificationStage.initStyle(StageStyle.UNDECORATED);
        notificationStage.initModality(Modality.APPLICATION_MODAL);

        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px;");

        HBox hBox = new HBox(10, imageView, new VBox(titleLabel, messageLabel));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");

        StackPane root = new StackPane(hBox);
        Scene scene = new Scene(root);
        notificationStage.setScene(scene);

        notificationStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(6));
        delay.setOnFinished(event -> notificationStage.close());
        delay.play();
    }

    private static void setFunction(NotificationType type) {
        switch (type) {
            case INFORMATION:
                title = "Information";
                icon = new Image(Constants.INFORMATION_IMAGE);
                break;

            case ERROR:
                title = "Error";
                icon = new Image(Constants.ERROR_IMAGE);
                break;

            case SUCCESS:
                title = "Success";
                icon = new Image(Constants.SUCCESS_IMAGE);
                break;

            case INVALID_ACTION:
                title = "Invalid action";
                icon = new Image(Constants.ERROR_IMAGE);
                break;
        }
    }
}
