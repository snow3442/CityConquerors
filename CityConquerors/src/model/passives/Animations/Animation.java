
package model.passives.Animations;


import java.util.ArrayList;
import model.GameSquare;

public abstract class Animation {
    public Animation(){
        
    }
    public abstract void runAnim(ArrayList<GameSquare> targets);
    
}
