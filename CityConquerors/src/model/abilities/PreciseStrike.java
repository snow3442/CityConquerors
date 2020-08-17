package model.abilities;

import model.Ability;

public class PreciseStrike extends Ability {

    private final String NAME = "Precise Strike";
    private final String DESCRIPTION = "gives the enemy that you just auto-attacked a bleed mark, at the start of enemy's turn, \n"
            + "the champion who has the bleed mark loses 6 hp for the next 2 turns";

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
