package view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MovementContextMenu extends ContextMenu {

    private final MenuItem DONTMOVEITEM = new MenuItem("DON'T MOVE");

    public MovementContextMenu() {
        getItems().addAll(DONTMOVEITEM);
        getStyleClass().add("movement-context-menu");
        DONTMOVEITEM.getStyleClass().add("movement-menu-item");
    }

    public MenuItem getDONTMOVEITEM() {
        return DONTMOVEITEM;
    }

}
