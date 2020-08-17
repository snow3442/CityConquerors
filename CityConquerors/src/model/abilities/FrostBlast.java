package model.abilities;

import model.Ability;

public class FrostBlast extends Ability {

    private final String NAME = "Frost Blast";
    private final String DESCRIPTION = "Alery deals 4 hp damage to every single adjacent enemy, further, all adjacent enemies\n"
            + "each receives a frozen mark. Enemies who has frozen mark cannot move for the next turn.";

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
