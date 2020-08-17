package model.passives;

import controller.GameController;
import static controller.GameController.championStack;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import model.Champion;
import model.Passive;
import model.champions.Dun;

public class SafeShot extends Passive implements PassiveInterface {

    private final String TYPE = "AUTOATTACK";
    private final String NAME = "Safe Shot";
    private final String DESCRIPTION = "Dun increases his dmg to 15 if he fires horizontally or vertically "
            + "from behind an ally";
    private final double ORIGINALDMG = 11;
    private final double DARKDMG = 15;
    private final String VOICELINE = "passive/safeshot.wav";

    @Override
    public void unwind() {
        if (championStack.peek() == Dun.getInstance()) {
            Dun.getInstance().getAd().setValue(ORIGINALDMG);
            ArrayList<Champion> safeShotTargets = getSafeShotTargets(Dun.getInstance(),
                    passiveHelper.getAllyBlockadesCoords(Dun.getInstance()));
            boolean darkShotFired = false;
            //check if a target of the safeShot was shot
            for (Champion ch : safeShotTargets) {
                if (ch.getGameSquare().isIsChampTargetted()) {
                    darkShotFired = true;
                    break;
                }
            }
            if (darkShotFired) {
                passiveHelper.runPassiveCastAnimation(Dun.getInstance());
                try {
                    GameController.getInstance().loadGameSound(VOICELINE, 0.5);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(SafeShot.class.getName()).log(Level.SEVERE, null, ex);
                }
                Dun.getInstance().getAd().setValue(DARKDMG);
                
            }
        }

    }

    /**
     * return a list of coordinates of dark arrow targets
     *
     * @param attacker
     * @param allyCoords
     * @return
     */
    public ArrayList<Champion> getSafeShotTargets(Champion attacker, ArrayList<Integer[]> allyCoords) {
        ArrayList<Champion> results = new ArrayList<>();
        if (passiveHelper.isChampAlly(Dun.getInstance())) {
            for (Champion ch : GameController.enemyChampList) {
                if (safeShotIndexDetector(attacker, ch, allyCoords)) {
                    results.add(ch);
                }
            }
        } else {
            for (Champion ch : GameController.allyChampList) {
                if (safeShotIndexDetector(attacker, ch, allyCoords)) {
                    results.add(ch);
                }
            }
        }

        return results;
    }

    /**
     * 
     *
     * @param row
     * @param col
     * @param indices
     * @return
     */
    public boolean safeShotIndexDetector(Champion attacker, Champion enemy, ArrayList<Integer[]> indices) {
        int attackerRow = GridPane.getRowIndex(Dun.getInstance().getGameSquare());
        int attackerCol = GridPane.getColumnIndex(Dun.getInstance().getGameSquare());
        int row = GridPane.getRowIndex(enemy.getGameSquare());
        int col = GridPane.getColumnIndex(enemy.getGameSquare());
        if (row != attackerRow && col != attackerCol) {
            return false;
        } else if (row == attackerRow) {
            for (Integer[] ints : indices) {
                if (ints[0] == row) {
                    if ((col > ints[1] && ints[1] > attackerCol && col - attackerCol == Dun.getInstance().getRange().getValue())
                            || (col < ints[1] && ints[1] < attackerCol && attackerCol - col == Dun.getInstance().getRange().getValue())) {
                        return true;
                    }
                }
            }
        } else if (col == attackerCol) {
            for (Integer[] ints : indices) {
                if (ints[1] == col) {
                    if ((row > ints[0] && ints[0] > attackerRow && row - attackerRow == Dun.getInstance().getRange().getValue())
                            || (row < ints[0] && ints[0] < attackerRow && attackerRow - row == Dun.getInstance().getRange().getValue())) {
                        System.out.println("vertical target: " + row + ", " + col);
                        return true;
                    }
                }
            }
        }

        return false;
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
