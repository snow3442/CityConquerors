package model.champions;

import controller.GameController;
import static controller.GameController.getNodeByRowColumnIndex;
import javafx.scene.layout.GridPane;
import model.Ability;
import model.Champion;
import model.Passive;
import static controller.GameControllerInterface.NOTARGETFORABILITYMESSAGE;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import model.GameSquare;
import model.abilities.PreciseStrike;
import model.marks.BleedMark;
import model.passives.LastWill;
import static view.GameView.centerPane;

public class Nagamasa extends Champion implements ChampionInterface {

    private static Nagamasa instance = null;
    private final String imgUrl = "images/champions/nagamasa.png";
    private final String TYPE = "assassin";
    private final Ability ABILITY = new PreciseStrike();
    private final Passive PASSIVE = new LastWill();
    private ArrayList<GameSquare> targetList = new ArrayList<>();
    private final String VOICELINE = "ability/precisestrike.wav";


    private Nagamasa() {
        this.getName().setValue("Nagamasa");
        this.setAbility(ABILITY);
        this.setPassive(PASSIVE);
        this.getImgUrl().setValue(imgUrl);
        this.getHp().setValue(55.0);
        this.getMaxHp().setValue(55.0);
        this.getAd().setValue(18);
        this.getAp().setValue(0);
        this.getRange().setValue(1);
        this.getMovingDistance().setValue(3);
        this.setType(TYPE);
        this.setPassiveDescription(getPassive().getDescription());
        this.setAbilityDescription(getAbility().getDescription());
    }

    public static synchronized Nagamasa getInstance() {
        if (instance == null) {
            instance = new Nagamasa();
        }
        return instance;
    }

    /**
     * only needs to be determined upon ability and passive casting request
     *
     * @return
     */
    public boolean isChampAlly() {
        return GameController.allyChampList.contains(getInstance());
    }

    @Override
    public void castAbility(GridPane gameGrid) {

        boolean castable = false;
        int row = GridPane.getRowIndex(this.getGameSquare());
        int col = GridPane.getColumnIndex(this.getGameSquare());
        for (Integer[] ints : GRIDCONTROLLER.getAllAdjacentSquares(row, col)) {           
            if (isChampAlly()
                    && GameController.getInstance().isEnemySquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))))) {
                targetList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
                castable = true;
            } else if (!isChampAlly()
                    && GameController.getInstance().isAllySquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))))) {
                targetList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
                castable = true;
            }
        }

        for (GameSquare gs : targetList) {
            enableAbilityCast(gs, isChampAlly());
            cancelAbilityCastEffects(gs, isChampAlly());
        }
        if (!castable) {
            GameController.getInstance().warningPaneAnimation(getGameSquare(), NOTARGETFORABILITYMESSAGE);
        }

    }

    public void enableAbilityCast(GameSquare square, boolean isAlly) {
        DropShadow dsBleedCast = new DropShadow();
        dsBleedCast.setOffsetX(5.0);
        dsBleedCast.setOffsetY(5.0);
        dsBleedCast.setColor(Color.RED);
        square.setEffect(dsBleedCast);
        square.setIsAbilityTargetted(true);
        square.setOnMouseClicked(e -> {
            if (isAlly && !getInstance().getUsedAbility().getValue()) {
                castBleedMark(GameController.getInstance().getEnemyFromSquare(square));
            } else if (!isAlly && !getInstance().getUsedAbility().getValue()) {
                castBleedMark(GameController.getInstance().getAllyFromSquare(square));
            }
        });
    }

    public void cancelAbilityCastEffects(GameSquare square, boolean isAlly) {
        getInstance().getGameContextMenu().getCANCELITEM().setOnAction(e -> {
            for (GameSquare gs : targetList) {
                gs.setIsAbilityTargetted(false);
                gs.setEffect(null);
            }
        });
    }

    public void castBleedMark(Champion champion) {
        CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
        try {
            GameController.getInstance().loadGameSound(VOICELINE, 0);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Nagamasa.class.getName()).log(Level.SEVERE, null, ex);
        }
        runPreciseStrikeAnimation(champion);
        champion.addMark(new BleedMark());
        for (GameSquare gs : targetList) {
            gs.setIsAbilityTargetted(false);
            gs.setEffect(null);
        }
        getInstance().getUsedAbility().setValue(true);
    }
    
    public void runPreciseStrikeAnimation(Champion champion){
         double startX = champion.getGameSquare().getLayoutX();
         double startY = champion.getGameSquare().getLayoutY()+20;
         double midX = champion.getGameSquare().getLayoutX()+
                 champion.getGameSquare().getIVsquareImage().getFitWidth()/2;
         double midY = champion.getGameSquare().getLayoutY()+
                 champion.getGameSquare().getIVsquareImage().getFitHeight()/2;
         double endX = champion.getGameSquare().getLayoutX()+
                 champion.getGameSquare().getIVsquareImage().getFitWidth();
         double endY = startY+40;
         Polygon bg = new Polygon(
                startX, startY,
                midX+5, midY-5,
                endX, endY,
                midX-5, midY+5
        );
        bg.setManaged(false);
        bg.setStroke(Color.CRIMSON); 
        bg.setStrokeWidth(5);
        bg.setEffect(new GaussianBlur(10));
        bg.setFill(Color.RED);
        centerPane.getChildren().add(bg);
        FadeTransition ft = new FadeTransition(Duration.millis(2000), bg);
        ft.setFromValue(0.4);
        ft.setToValue(1);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        Platform.runLater(()->{
            ft.play();
            ft.setOnFinished(e->{
                centerPane.getChildren().remove(bg);              
            });          
        });
    }

}
