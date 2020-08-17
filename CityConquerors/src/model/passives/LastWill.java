package model.passives;

import controller.GameController;
import static controller.GameController.championStack;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Passive;
import model.champions.Nagamasa;
import model.marks.BleedMark;

public class LastWill extends Passive implements PassiveInterface {

    private final String NAME = "Last Will";
    private final String DESCRIPTION = "when Magamasa dies, he gives the killer a bleed mark";
    private final String TYPE = "DEATH";
    private boolean passiveTriggered = false;
    private final String VOICELINE = "passive/lastwill.wav";

    @Override
    public void unwind() {
        if (Nagamasa.getInstance().getHp().getValue() == 0 && !passiveTriggered) {
            passiveTriggered = true;
            passiveHelper.runPassiveCastAnimation(Nagamasa.getInstance());
            try {
                GameController.getInstance().loadGameSound(VOICELINE, 0.5);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Nagamasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            Nagamasa.getInstance().runPreciseStrikeAnimation(championStack.peek());
            championStack.peek().addMark(new BleedMark());
        }
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
