package il.cshaifasweng.OCSFMediatorExample.client.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class CustomContextMenu {

    private final ContextMenu contextMenu;

    private final Node node;

    private MenuItem edit;

    private MenuItem delete;

    private MenuItem details;

    private MenuItem refresh;

    public CustomContextMenu(Node node) {
        this.node = node;

        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(getContent());
        // Load the CSS file
        contextMenu.getStyleClass().add("context-menu");
        contextMenu.getStyleClass().add(
                Objects.requireNonNull(getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/DialogStyle.css")).toExternalForm()
        );
    }

    public void setActionEdit(EventHandler<ActionEvent> action) {
        edit.setOnAction(action);
    }

    public void setActionDelete(EventHandler<ActionEvent> action) {
        delete.setOnAction(action);
    }

    public void setActionDetails(EventHandler<ActionEvent> action) {
        details.setOnAction(action);
    }

    public void setActionRefresh(EventHandler<ActionEvent> action) {
        refresh.setOnAction(action);
    }

    public void show() {
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                contextMenu.show(node, ev.getScreenX(), ev.getScreenY());
            }
        });
    }

    public void hide() {
        contextMenu.hide();
    }

    public MenuItem getEditButton() {
        return edit;
    }

    public MenuItem getDeleteButton() {
        return delete;
    }

    private MenuItem[] getContent() {
        edit = new MenuItem("Edit");
        style(edit);

        delete = new MenuItem("Delete");
        style(delete);

        details = new MenuItem("Details");
        style(details);

        refresh = new MenuItem("Refresh");
        style(refresh);

        return new MenuItem[]{edit, delete, details, refresh};
    }

    private void style(MenuItem menuItem) {
        menuItem.getStyleClass().add("menu-item-context-menu");
    }
}
