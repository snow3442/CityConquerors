
package model.abilities;

import model.Ability;

public class SoundOfHealing extends Ability {
    
    private final String NAME = "Sound of Healing";
    private final String DESCRIPTION = "Selna heals an ally back to 25 hp";


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
