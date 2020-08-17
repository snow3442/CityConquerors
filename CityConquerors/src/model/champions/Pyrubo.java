package model.champions;

import controller.GameController;
import static controller.GameController.getNodeByRowColumnIndex;
import static controller.GameControllerInterface.NOTARGETFORABILITYMESSAGE;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Ability;
import model.Champion;
import model.GameSquare;
import model.Passive;
import model.abilities.ForestTerror;
import static model.champions.ChampionInterface.GRIDCONTROLLER;
import model.marks.FearMark;
import model.passives.PrimalFury;
import static view.GameView.centerPane;

public class Pyrubo extends Champion implements ChampionInterface {

    private static Pyrubo instance = null;
    private final String imgUrl = "images/champions/pyrubo.png";
    private final String TYPE = "tank";
    private final Ability ABILITY = new ForestTerror();
    private final Passive PASSIVE = new PrimalFury();
    private ArrayList<GameSquare> targetList = new ArrayList<GameSquare>();
    private Timeline timeline;
    private boolean passiveUsed = false;
    private final Image imgAnim = new Image("images/animation/forestterror.png");
    private ArrayList<ImageView> animImages = new ArrayList<ImageView>();
    private final String VOICELINE = "ability/forestterror.wav";

    private Pyrubo() {
        this.getName().setValue("Pyrubo");
        this.setAbility(ABILITY);
        this.setPassive(PASSIVE);
        this.getImgUrl().setValue(imgUrl);
        this.getHp().setValue(80.0);
        this.getMaxHp().setValue(80.0);
        this.getAd().setValue(8);
        this.getAp().setValue(0);
        this.getRange().setValue(1);
        this.getMovingDistance().setValue(2);
        this.setType(TYPE);
        this.setPassiveDescription(getPassive().getDescription());
        this.setAbilityDescription(getAbility().getDescription());
    }

    public static synchronized Pyrubo getInstance() {
        if (instance == null) {
            instance = new Pyrubo();
        }
        return instance;
    }

    @Override
    public void castAbility(GridPane gameGrid) {
        boolean castable = false;
        int row = GridPane.getRowIndex(getInstance().getGameSquare());
        int col = GridPane.getColumnIndex(getInstance().getGameSquare());
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

        for (int i = 0; i < targetList.size(); i++) {
            if (isChampAlly() && !getInstance().getUsedAbility().getValue()) {
                GameController.getInstance().updateHpStatsWithDeathChecker(GameController.getInstance().getEnemyFromSquare(targetList.get(i)),
                        4);
                castFearMark(GameController.getInstance().getEnemyFromSquare(targetList.get(i)));
            } else if (!isChampAlly() && !getInstance().getUsedAbility().getValue()) {

                GameController.getInstance().updateHpStatsWithDeathChecker(GameController.getInstance().getAllyFromSquare(targetList.get(i)),
                        4);
                castFearMark(GameController.getInstance().getAllyFromSquare(targetList.get(i)));
            }

            if (i == targetList.size() - 1) {
                getInstance().getUsedAbility().setValue(true);
            }
        }

        if (castable) {
            CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
        } else {
            GameController.getInstance().warningPaneAnimation(getGameSquare(), NOTARGETFORABILITYMESSAGE);
        }

    }

    public void castFearMark(Champion champion) {
        FearMark fearMark = new FearMark();
        champion.addMark(fearMark);
        runForestTerrorAnimation(champion, fearMark);
    }

    public void runForestTerrorAnimation(Champion champion, FearMark fearMark) {
        Platform.runLater(() -> {
            ImageView ivAnim = new ImageView(imgAnim);
            ivAnim.setFitWidth(getInstance().getGameSquare().getIVsquareImage().getFitWidth());
            ivAnim.setFitHeight(getInstance().getGameSquare().getIVsquareImage().getFitHeight());
            ivAnim.setManaged(false);
            ivAnim.setLayoutX(getInstance().getGameSquare().getLayoutX());
            ivAnim.setLayoutY(getInstance().getGameSquare().getLayoutY());
            centerPane.getChildren().add(ivAnim);
            Glow initGlow = new Glow(0.3);
            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(45.0);
            light.setColor(Color.GREEN);
            Lighting initLighting = new Lighting();
            initLighting.setLight(light);
            initGlow.setInput(initLighting);
            champion.getGameSquare().setEffect(initGlow);
            timeline = new Timeline();
            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(light.colorProperty(), light.getColor()),
                            new KeyValue(initGlow.levelProperty(), initGlow.getLevel()),
                            new KeyValue(ivAnim.opacityProperty(), 0.2)
                    ),
                    new KeyFrame(Duration.millis(1500),
                            new KeyValue(light.colorProperty(), Color.DARKGREEN),
                            new KeyValue(initGlow.levelProperty(), 0.65),
                            new KeyValue(ivAnim.opacityProperty(), 0.6)
                    ),
                    new KeyFrame(Duration.millis(3000),
                            new KeyValue(light.colorProperty(), Color.DARKVIOLET),
                            new KeyValue(initGlow.levelProperty(), 0.9),
                            new KeyValue(ivAnim.opacityProperty(), 0.1)
                    )
            );
            timeline.setAutoReverse(true);
            timeline.setCycleCount(1);
            try {
                GameController.getInstance().loadGameSound(VOICELINE, 0.5);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Pyrubo.class.getName()).log(Level.SEVERE, null, ex);
            }
            timeline.play();
            timeline.setOnFinished(e -> {
                centerPane.getChildren().remove(ivAnim);
                champion.getGameSquare().setEffect(null);
            });
        });

    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(getInstance());
    }

    public boolean isPassiveUsed() {
        return passiveUsed;
    }

    public void setPassiveUsed(boolean passiveUsed) {
        this.passiveUsed = passiveUsed;
    }

}
