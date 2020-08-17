package model;

/**
 * Ability leftover effect type instruction
 * 0 -> detect at enemy turn's start
 * 1 -> detect checker when an enemy moves
 * 2 -> detect checker when an enemy auto or cast
 */
public abstract class Ability {

    public Ability() {

    }
    
    public abstract void runAnim();
    
    public abstract String getDescription();

    public abstract String getName();
    

}
