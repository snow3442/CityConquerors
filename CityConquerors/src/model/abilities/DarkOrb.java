package model.abilities;

import model.Ability;

public class DarkOrb extends Ability {
    private final String NAME = "Dark Orb";
    private final String DESCRIPTION  = "Chedipe fires a powerful magical shot onto 1 enemy at exactly her range that deals her ap damage";

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getName() {
        return NAME; 
    }

    @Override
    public void runAnim() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
