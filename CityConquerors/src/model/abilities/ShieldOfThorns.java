package model.abilities;

import model.Ability;

public class ShieldOfThorns extends Ability {

    private final String NAME = "Shield Of Thorns";
    private final String DESCRIPTION = "Nywa gives a Thorn Mark to an ally within her range. For the next 2 enemy turns,\n"
            + "the Thorn Mark reflects 50% of the dmg to whoever attacks the ally";

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
