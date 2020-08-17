package model.passives;

import controller.GameController;
import static controller.GameController.allyTurn;
import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import model.Champion;
import model.GameSquare;
import model.Passive;
import model.champions.Selna;

public class HealingBeacon extends Passive implements PassiveInterface {

    private final String TYPE = "ENDTURN";
    private final String NAME = "Healing Beacon";
    private final String DESCRIPTION = "At start of your turn, any ally within Selna's range who is \n"
            + "below 20 hp is healed for 5 hp";
    private ArrayList<GameSquare> healList = new ArrayList<>();
    private PassiveHelper passiveHelper = new PassiveHelper();
    private final double HEALAMOUNT = 5;

    @Override
    public void unwind() {
        int row = GridPane.getRowIndex(Selna.getInstance().getGameSquare());
        int col = GridPane.getColumnIndex(Selna.getInstance().getGameSquare());
        if (allyTurn == (GameController.allyChampList.contains(Selna.getInstance()))) {
            if (passiveHelper.isChampAlly(Selna.getInstance())) {
                for (Champion c : GameController.allyChampList) {
                    if (c.getHp().getValue() < 20
                            && passiveHelper.checkTwoChampionsDistanceWithOutCollision(Selna.getInstance(), c) != 0
                            && passiveHelper.checkTwoChampionsDistanceWithOutCollision(Selna.getInstance(), c) <= Selna.getInstance().getRange().getValue()) {
                        c.getHp().setValue(c.getHp().getValue()+HEALAMOUNT);
                        passiveHelper.runPassiveCastAnimation(Selna.getInstance());
                        runHealingBeaconAnimation();
                        break;
                    }
                }
            } else {
                for (Champion c : GameController.enemyChampList) {
                    if (c.getHp().getValue() < 20
                            && passiveHelper.checkTwoChampionsDistanceWithOutCollision(Selna.getInstance(), c) != 0
                            && passiveHelper.checkTwoChampionsDistanceWithOutCollision(Selna.getInstance(), c) <= Selna.getInstance().getRange().getValue()) {
                        c.getHp().setValue(c.getHp().getValue() + HEALAMOUNT);
                        passiveHelper.runPassiveCastAnimation(Selna.getInstance());
                        runHealingBeaconAnimation();
                        break;
                    }
                }
            }
        }
    }
    
    private void runHealingBeaconAnimation(){
        
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
