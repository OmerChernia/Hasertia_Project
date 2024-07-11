
package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class TicketController {

    @FXML
    private ImageView ticketImage;

    @FXML
    private Text ticketText;

    public void setTicketInfo(String imagePath, String text) {
        ticketImage.setImage(new Image(imagePath));
        ticketText.setText(text);
    }
}
