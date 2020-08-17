package model.passives;


import controller.GameController;
import static controller.GameController.allyChampList;
import static controller.GameController.enemyChampList;
import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import model.Champion;
import model.Passive;
import model.champions.Nywa;

public class AuraOfProtection extends Passive implements PassiveInterface {

    private final String TYPE = "DAMAGERECEIVE";
    private final String NAME = "Aura of Protection";
    private final String DESCRIPTION = "During enemy's turn, any ally within nywa's range takes 2 dmg less (including nywa)";
    private final int MITIGATE = 1;
    private ArrayList<Champion> protectList = new ArrayList<>();
    @Override
    public void unwind() { 
        Nywa.getInstance().setPassiveTakeEffect(false);
        int row = GridPane.getRowIndex(Nywa.getInstance().getGameSquare());
        int col = GridPane.getColumnIndex(Nywa.getInstance().getGameSquare());
        //Nywa is ally
        if (isChampAlly()) {
            for (Champion c : allyChampList) {             
                    c.getHp().addListener(
                            (observable, oldvalue, newvalue) -> {                               
                                if ((double) newvalue < (double) oldvalue && (double) newvalue>0&&!Nywa.getInstance().isPassiveTakeEffect()
                                        &&passiveHelper.checkTwoChampionsDistanceWithOutCollision(c, Nywa.getInstance())
                        <= Nywa.getInstance().getRange().getValue()) {                  
                                    Nywa.getInstance().setPassiveTakeEffect(true);
                                    c.getHp().setValue(c.getHp().getValue() + MITIGATE);
                                    passiveHelper.runPassiveCastAnimation(Nywa.getInstance());
                                }
                            });
            }
        } //Nywa is enemy
        else {
            for (Champion c : enemyChampList) {
                    c.getHp().addListener(
                            (observable, oldvalue, newvalue) -> {
                                if ((double) newvalue < (double) oldvalue&&(double) newvalue>0&&!Nywa.getInstance().isPassiveTakeEffect()
                                        &&passiveHelper.checkTwoChampionsDistanceWithOutCollision(c, Nywa.getInstance())
                        <= Nywa.getInstance().getRange().getValue()) {
                                    Nywa.getInstance().setPassiveTakeEffect(true);
                                    c.getHp().setValue(c.getHp().getValue() + MITIGATE);
                                    passiveHelper.runPassiveCastAnimation(Nywa.getInstance());
                                }
                            });
            }
        }
    }
    

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(Nywa.getInstance());
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
