
package model.passives;

import model.Passive;
import model.champions.Wukong;

public class MonkeyAgility extends Passive {
    private final String TYPE = "MOVEMENT";
    private final String NAME = "Money Agility";
    private final String DESCRIPTION = "Wukong ignores unit collisions";
    @Override
    public void unwind() {
       Wukong.getInstance().getUnitCollisionType().setValue("ALL");
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getType() {
        return TYPE;
    }
    
}
