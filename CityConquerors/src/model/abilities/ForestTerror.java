package model.abilities;

import model.Ability;

public class ForestTerror extends Ability {
    
    private final String NAME = "Forest Terror";
    private final String DESCRIPTION = "Pyrubo roars dealing 4hp dmg and gives a Fear Mark to all adjacent enemies, \n"
            + "an enemy that has a Fear Mark cannot auto attack for the next turn. The mark then vanishes.";

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
        
    }

}
