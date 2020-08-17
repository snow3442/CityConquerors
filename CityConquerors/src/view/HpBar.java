
package view;

import static controller.GameControllerInterface.BOARDNUMOFCOLS;
import javafx.scene.control.ProgressBar;

public class HpBar extends ProgressBar{
    public HpBar(){
        setHeight(21-BOARDNUMOFCOLS);
        setWidth(780/BOARDNUMOFCOLS-2.5);
    }
}
