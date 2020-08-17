package model.abilities;

import model.Ability;

public class ExplosiveArrow extends Ability {

    private final String NAME = "Explosive Arrow";
    private final String DESCRIPTION = "HouYi fires an inflammed arrow onto an enemy dealing\n"
            + "1*ad dmg and giving it and all enemies on the adjacents squares a Ignite Mark each. \n"
            + "During enemy's next turn, ignite mark each deals 5 dmg";

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
