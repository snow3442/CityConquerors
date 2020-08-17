package model.abilities;

import model.Ability;

public class PoisonousArrow extends Ability {

    private final String NAME = "Poisonous Arrow";
    private final String DESCRIPTION = "Dun fires a poison arrow, deals 1*ad dmg(passive triggers if condition satisified). \n"
            + "The enemy shot becomes receives a Poison Mark that deals 5 hp dmg each for the next two enemy's turns";

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
