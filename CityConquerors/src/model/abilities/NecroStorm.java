package model.abilities;

import model.Ability;

public class NecroStorm extends Ability {

    private final String NAME = "Necro Storm";
    private final String DESCRIPTION = "Voldemort casts lightening onto all enemy units within his range and deals\n"
            + "2*ap to each of them.";

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
