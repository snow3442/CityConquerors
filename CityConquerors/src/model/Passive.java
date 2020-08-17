package model;

/**
 * Passive Type instructions
 * 0)-> To cast in movement checker
 * 1)-> To cast at after movement Stage
 * 2)-> To cast at after damage received Stage
 * 3)-> To cast upon death
 * 4)-> To cast at end turn/start turn
 * @author JQ
 */
public abstract class Passive{
    
    public Passive() {     
        
    }    
    
    public abstract void unwind();

    public abstract String getName();

    public abstract String getDescription();
    
    public abstract String getType();
       
}
