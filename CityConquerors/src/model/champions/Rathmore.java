package model.champions;

import controller.GameController;
import static controller.GameController.getNodeByRowColumnIndex;
import static controller.GameControllerInterface.NOTARGETFORABILITYMESSAGE;
import java.net.MalformedURLException;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import javafx.scene.layout.GridPane;
import model.Ability;
import model.Champion;
import model.GameSquare;
import model.Passive;
import model.abilities.Animations.NecroStormAnimation;
import model.abilities.NecroStorm;
import static model.champions.ChampionInterface.GRIDCONTROLLER;
import model.passives.LingeringSoul;

public class Rathmore extends Champion implements ChampionInterface{

    private static Rathmore instance = null;
    private final String imgUrl = "images/champions/rathmore.png";
    private final String TYPE = "mage";
    private final Ability ABILITY = new NecroStorm();
    private final Passive PASSIVE = new LingeringSoul();
    private ArrayList<GameSquare> targetList = new ArrayList<>();
    private ArrayList<GameSquare> affectedSquaresList = new ArrayList<>();
    private ArrayList<Champion> affectedChampionList = new ArrayList<>();
    private boolean isReallyDead = false;
    private NecroStormAnimation necroStormAnimation = new NecroStormAnimation();
    private final String VOICELINE = "ability/necrostorm.wav";
    private final String HELPER = "ability/necrothunder.wav";

    private Rathmore() {
        this.getName().setValue("Rathmore");
        this.setAbility(ABILITY);
        this.setPassive(PASSIVE);
        this.getImgUrl().setValue(imgUrl);
        this.getHp().setValue(50.0);
        this.getMaxHp().setValue(50.0);
        this.getAd().setValue(5);
        this.getAp().setValue(9);
        this.getRange().setValue(2);
        this.getMovingDistance().setValue(2);
        this.setType(TYPE);
        this.setPassiveDescription(getPassive().getDescription());
        this.setAbilityDescription(getAbility().getDescription());
    }

    public static synchronized Rathmore getInstance() {
        if (instance == null) {
            instance = new Rathmore();
        }
        return instance;
    }

    @Override
    public void castAbility(GridPane gameGrid) {
        boolean castable = false;
        int row = GridPane.getRowIndex(this.getGameSquare());
        int col = GridPane.getColumnIndex(this.getGameSquare());
        affectedSquaresList.add(((GameSquare) (getNodeByRowColumnIndex(row, col, gameGrid))));
        for (Integer[] ints : GRIDCONTROLLER.getAllreachableCoordsWithinDistance(row, col, getRange().getValue())) {
            affectedSquaresList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));

            if (isChampAlly()
                    && GameController.getInstance().isEnemySquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))))) {
                affectedChampionList.add(GameController.getInstance().getEnemyFromSquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid)))));
                targetList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
                castable = true;
            } else if (!isChampAlly()
                    && GameController.getInstance().isAllySquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))))) {
                targetList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
                affectedChampionList.add(GameController.getInstance().getAllyFromSquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid)))));
                castable = true;
            }
        }
        if (castable) {
            try {
            GameController.getInstance().loadGameSound(VOICELINE, 0);
            GameController.getInstance().loadHelperSound(HELPER, 0.9);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Nagamasa.class.getName()).log(Level.SEVERE, null, ex);
        }          
            runNecroStormAnimation();
        } else {
            GameController.getInstance().warningPaneAnimation(getGameSquare(), NOTARGETFORABILITYMESSAGE);
        }
    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(getInstance());
    }

    public boolean isIsReallyDead() {
        return isReallyDead;
    }

    public void setIsReallyDead(boolean isReallyDead) {
        this.isReallyDead = isReallyDead;
    }

    public void runNecroStormAnimation() {
        CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
        necroStormAnimation.runAnim(affectedSquaresList);

        necroStormAnimation.getAnim().setOnFinished(e -> {
            for (GameSquare gs : affectedSquaresList) {
                gs.setEffect(null);
            }
            for (Champion ch : affectedChampionList) {
                GameController.getInstance().updateHpStatsWithDeathChecker(ch, 2 * getInstance().getAp().getValue());
            }
        });

    }
}
