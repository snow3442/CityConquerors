
package model.GameObject;

public abstract class NeutralGameObject {
    private final String type = "NEUTRAL";
    
    public NeutralGameObject(){
        
    }
    
    public String getType(){
        return type;
    }
}
