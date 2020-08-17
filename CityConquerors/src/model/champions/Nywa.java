package model.champions;

import controller.GameController;
import static controller.GameController.getNodeByRowColumnIndex;
import static controller.GameControllerInterface.NOTARGETFORABILITYMESSAGE;
import java.util.ArrayList;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Ability;
import model.Champion;
import model.GameSquare;
import model.Passive;
import model.abilities.ShieldOfThorns;
import static model.champions.ChampionInterface.GRIDCONTROLLER;
import model.marks.ShieldOfThornsMark;
import model.passives.AuraOfProtection;

public class Nywa extends Champion implements ChampionInterface {

    private static Nywa instance = null;
    private final String imgUrl = "images/champions/nywa.png";
    private final String TYPE = "support";
    private final Ability ABILITY = new ShieldOfThorns();
    private final Passive PASSIVE = new AuraOfProtection();
    private ArrayList<GameSquare> targetList = new ArrayList<>();
    private boolean passiveTakeEffect = false;

    private Nywa() {
        this.getName().setValue("Nywa");
        this.setAbility(ABILITY);
        this.setPassive(PASSIVE);
        this.getImgUrl().setValue(imgUrl);
        this.getHp().setValue(58.0);
        this.getMaxHp().setValue(58.0);
        this.getAd().setValue(6);
        this.getAp().setValue(0);
        this.getRange().setValue(2);
        this.getMovingDistance().setValue(2);
        this.setType(TYPE);
        this.setPassiveDescription(getPassive().getDescription());
        this.setAbilityDescription(getAbility().getDescription());
    }

    public static synchronized Nywa getInstance() {
        if (instance == null) {
            instance = new Nywa();
        }
        return instance;
    }

    @Override
    public void castAbility(GridPane gameGrid) {
        boolean castable = false;
        int row = GridPane.getRowIndex(getInstance().getGameSquare());
        int col = GridPane.getColumnIndex(getInstance().getGameSquare());
        //Load squares list
        for (Integer[] ints : GRIDCONTROLLER.getAllreachableCoordsWithinDistance(row, col, getRange().getValue())) {
            if (isChampAlly()
                    && GameController.getInstance().isAllySquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))))) {
                targetList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
                castable = true;
            } else if (!isChampAlly()
                    && GameController.getInstance().isEnemySquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))))) {
                targetList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
                castable = true;
            }
        }

        for (GameSquare gs : targetList) {
            if (isChampAlly()) {
                enableAbilityCast(gs, isChampAlly());
                cancelAbilityCastEffects(gs, isChampAlly());
            } else {
                enableAbilityCast(gs, isChampAlly());
                cancelAbilityCastEffects(gs, isChampAlly());
            }
        }
        if (!castable) {
            GameController.getInstance().warningPaneAnimation(getGameSquare(), NOTARGETFORABILITYMESSAGE);
        }
    }

    public void enableAbilityCast(GameSquare square, boolean isAlly) {
        DropShadow dsShield = new DropShadow();
        dsShield.setOffsetX(5.0);
        dsShield.setOffsetY(5.0);
        dsShield.setColor(Color.YELLOWGREEN);
        square.setEffect(dsShield);
        square.setIsAbilityTargetted(true);
        square.setOnMouseClicked(e -> {
            if (isAlly && !getInstance().getUsedAbility().getValue()) {
                castShieldOfThorns(GameController.getInstance().getAllyFromSquare(square),isAlly);
            } else if (!isAlly && !getInstance().getUsedAbility().getValue()) {
                castShieldOfThorns(GameController.getInstance().getEnemyFromSquare(square),isAlly);
            }
        });
    }

    public void cancelAbilityCastEffects(GameSquare square, boolean isAlly) {
        getInstance().getGameContextMenu().getCANCELITEM().setOnAction(e -> {
            for (GameSquare gs : targetList) {
                gs.setIsAbilityTargetted(false);
                gs.setEffect(null);
                if (isAlly) {
                    GameController.getInstance().wireUpChampSquare(gs,
                            GameController.getInstance().getAllyFromSquare(gs));
                } else {
                    GameController.getInstance().wireUpChampSquare(gs,
                            GameController.getInstance().getEnemyFromSquare(gs));
                }
            }
        });
    }

    public void castShieldOfThorns(Champion champion, boolean isAlly) {
        CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
        ShieldOfThornsMark ShieldOfThornsMark = new ShieldOfThornsMark();
        champion.addMark(ShieldOfThornsMark);
        ShieldOfThornsMark.takeEffect(champion);
        for (GameSquare gs : targetList) {
            gs.setIsAbilityTargetted(false);
            gs.setEffect(null);
            if (isAlly) {
                GameController.getInstance().wireUpChampSquare(gs,
                        GameController.getInstance().getAllyFromSquare(gs));
            } else {
                GameController.getInstance().wireUpChampSquare(gs,
                        GameController.getInstance().getEnemyFromSquare(gs));
            }
        }
        getInstance().getUsedAbility().setValue(true);

    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(getInstance());
    }

    public boolean isPassiveTakeEffect() {
        return passiveTakeEffect;
    }

    public void setPassiveTakeEffect(boolean passiveTakeEffect) {
        this.passiveTakeEffect = passiveTakeEffect;
    }

}
