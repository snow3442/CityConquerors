
package view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;


public class GameContextMenu extends ContextMenu {
    private final MenuItem ATTACKITEM= new MenuItem("ATTACK");
    private final MenuItem CASTABILITYITEM = new MenuItem("CAST ABILITY");
    private final MenuItem CANCELITEM = new MenuItem("CANCEL");
    
            
    public GameContextMenu(){
        getItems().addAll(ATTACKITEM, CASTABILITYITEM, CANCELITEM);
        getStyleClass().add("context-menu");
        ATTACKITEM.getStyleClass().add("menu-item");
        CASTABILITYITEM.getStyleClass().add("menu-item");
        CANCELITEM.getStyleClass().add("menu-item");
    }

    public MenuItem getATTACKITEM() {
        return ATTACKITEM;
    }

    public MenuItem getCASTABILITYITEM() {
        return CASTABILITYITEM;
    }


    public MenuItem getCANCELITEM() {
        return CANCELITEM;
    }
    
    
}
