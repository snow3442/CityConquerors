package model.abilities;

import model.Ability;

public class Trickster extends Ability implements AbilityInterface {

    private final String NAME = "Trickster";
    private final String DESCRIPTION = "(only after auto attack) Wukong becomes a clone of himself, during the next\n"
            + "enemy's turn, if an enemy attack the clone, the clone disppears and Wukong reappears\n"
            + "on the same square";

    public Trickster() {

    }

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
