package model.champions;

import controller.GameController;
import static controller.GameControllerInterface.NOTARGETFORABILITYMESSAGE;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import model.Ability;
import model.Champion;
import model.GameSquare;
import model.Mark;
import model.Passive;
import model.abilities.DarkOrb;
import model.marks.ImpureBindMark;
import static model.passives.PassiveInterface.gridLogicController;
import model.passives.ImpurityBinding;
import static view.GameView.centerPane;
import static view.GameView.gameGrid;

public class Chedipe extends Champion implements ChampionInterface {

    private static Chedipe instance = null;
    private final String imgUrl = "images/champions/chedipe.png";
    private final String TYPE = "mage";
    private final Ability ABILITY = new DarkOrb();
    private final Passive PASSIVE = new ImpurityBinding();
    private final String IMPUREBINDMARKNAME = "IMPUREBINDMARK";
    private ArrayList<Champion> bindedChampList = new ArrayList<>();
    private ArrayList<GameSquare> targetList = new ArrayList<>();

    private Chedipe() {
        this.getName().setValue("Chedipe");
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

    public static synchronized Chedipe getInstance() {
        if (instance == null) {
            instance = new Chedipe();
        }
        return instance;
    }

    @Override
    public void castAbility(GridPane gameGrid) {
        updateList();
        targetList = loadTargetList();
        if (targetList.isEmpty()) {
            GameController.getInstance().warningPaneAnimation(getGameSquare(), NOTARGETFORABILITYMESSAGE);
        }
        for (GameSquare gs : targetList) {
            enableAbilityCast(gs);
            cancelAbilityCastEffects(gs);
        }

    }

    public void enableAbilityCast(GameSquare square) {
        DropShadow DarkOrbCast = new DropShadow();
        DarkOrbCast.setOffsetX(5.0);
        DarkOrbCast.setOffsetY(5.0);
        DarkOrbCast.setColor(Color.BLUEVIOLET);
        square.setEffect(DarkOrbCast);
        square.setIsAbilityTargetted(true);
        square.setOnMouseClicked(e -> {
            if (isChampAlly() && !getInstance().getUsedAbility().getValue()) {
                castDarkOrb(GameController.getInstance().getEnemyFromSquare(square));
            } else if (!isChampAlly() && !getInstance().getUsedAbility().getValue()) {
                castDarkOrb(GameController.getInstance().getAllyFromSquare(square));
            }
        });
    }

    public void cancelAbilityCastEffects(GameSquare square) {
        getInstance().getGameContextMenu().getCANCELITEM().setOnAction(e -> {
            for (GameSquare gs : targetList) {
                gs.setIsAbilityTargetted(false);
                gs.setEffect(null);
            }
        });
    }

    public void castDarkOrb(Champion c) {
        runDarkOrbAnimation(c);
        CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
        if (!getInstance().getBindedChampList().contains(c)) {
            c.addMark(new ImpureBindMark());
            GameController.getInstance().updateHpStatsWithDeathChecker(c, getAp().getValue());
            getInstance().getBindedChampList().add(c);
        } else {
            for (Champion ch : getInstance().getBindedChampList()) {
                GameController.getInstance().updateHpStatsWithDeathChecker(ch, getAp().getValue());
            }
        }

        for (GameSquare gs : targetList) {
            gs.setIsAbilityTargetted(false);
            gs.setEffect(null);
        }
        getInstance().getUsedAbility().setValue(true);
    }

    private void runDarkOrbAnimation(Champion c) {
        double initX = getInstance().getGameSquare().getLayoutX()
                + getInstance().getGameSquare().getIVsquareImage().getFitWidth() / 2;
        double initY = getInstance().getGameSquare().getLayoutY()
                + getInstance().getGameSquare().getIVsquareImage().getFitHeight() / 2;
        double finalX = c.getGameSquare().getLayoutX()
                + c.getGameSquare().getIVsquareImage().getFitWidth() / 2;
        double finalY = c.getGameSquare().getLayoutY()
                + c.getGameSquare().getIVsquareImage().getFitHeight() / 2;
        Circle orb = new Circle(initX, initY, 20);
        orb.setFill(Color.VIOLET);
        orb.setEffect(new GaussianBlur(50));
        orb.setManaged(false);
        centerPane.getChildren().add(orb);
        PathTransition pathTransition = new PathTransition();
        Path path = new Path();
        path.setVisible(false);
        path.getElements().add(new MoveTo(orb.getCenterX(), orb.getCenterY()));
        path.getElements().add(new LineTo(finalX, finalY));
        pathTransition.setDuration(Duration.millis(1500));
        pathTransition.setNode(orb);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);

        //play sequence of animations
        Platform.runLater(() -> {
            pathTransition.play();
            pathTransition.setOnFinished(e -> {
                centerPane.getChildren().remove(orb);
                //build timeline animation features
                Timeline timeline = new Timeline();
                Glow initGlow = new Glow(0.1);
                Light.Distant light = new Light.Distant();
                light.setAzimuth(45.0);
                light.setElevation(45.0);
                light.setColor(Color.RED);
                Lighting initLighting = new Lighting();
                initLighting.setLight(light);
                initGlow.setInput(initLighting);
                if (getInstance().getBindedChampList().contains(c)) {
                    for (Champion champ : getInstance().getBindedChampList()) {
                        champ.getGameSquare().setEffect(initGlow);
                    }
                } else {
                    c.getGameSquare().setEffect(initGlow);
                }

                timeline.getKeyFrames().setAll(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(light.colorProperty(), light.getColor()),
                                new KeyValue(initGlow.levelProperty(), initGlow.getLevel())
                        ),
                        new KeyFrame(Duration.seconds(1.5),
                                new KeyValue(light.colorProperty(), Color.color(0.58, 0, 0.83, 0.9)),
                                new KeyValue(initGlow.levelProperty(), 0.9)
                        )
                );
                timeline.setAutoReverse(false);
                timeline.setCycleCount(1);
                timeline.play();
                timeline.setOnFinished(ev -> {
                    for (Champion champ : getInstance().getBindedChampList()) {
                        champ.getGameSquare().setEffect(null);
                    }
                });
            });
        });
    }

    public ArrayList<Champion> getBindedChampList() {
        return bindedChampList;
    }

    /**
     * refresh the list to ensure bind mark ownership is coherent with bind list
     * by removing champions that no longer have impure bind mark note: the
     * incoherence is likely an outcome of cleansing effect from other abilities
     */
    public void updateList() {
        for (Iterator<Champion> iterator = bindedChampList.iterator(); iterator.hasNext();) {
            Champion c = iterator.next();
            if (!championHasImpureBind(c)) {
                iterator.remove();
            }
        }

    }

    /**
     * returns true if the champion has impure bind mark already, false
     * otherwise
     *
     * @return
     */
    public boolean championHasImpureBind(Champion champion) {
        boolean hasimpurebind = false;
        for (Mark m : champion.getMarkList()) {
            if (m.getName().equals(IMPUREBINDMARKNAME)) {
                hasimpurebind = true;
            }
        }
        return hasimpurebind;
    }

    /**
     * returns an array list of enemy's squares that are at the range of attack
     * for Chedipe
     *
     * @return
     */
    public ArrayList<GameSquare> loadTargetList() {
        int row = GridPane.getRowIndex(getInstance().getGameSquare());
        int col = GridPane.getColumnIndex(getInstance().getGameSquare());
        int range = getInstance().getRange().getValue();
        ArrayList<GameSquare> targetSquares = new ArrayList<>();
        if (isChampAlly()) {
            for (Integer[] ints : gridLogicController.getAllReachableEnemyCoordsAtRange(row, col, range)) {
                if (GameController.getInstance().isEnemySquare((GameSquare) GameController.getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))) {
                    targetSquares.add((GameSquare) GameController.getNodeByRowColumnIndex(ints[0], ints[1], gameGrid));
                }
            }
        } else {
            for (Integer[] ints : gridLogicController.getAllReachableEnemyCoordsAtRange(row, col, range)) {
                if (GameController.getInstance().isAllySquare((GameSquare) GameController.getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))) {
                    targetSquares.add((GameSquare) GameController.getNodeByRowColumnIndex(ints[0], ints[1], gameGrid));
                }
            }
        }

        return targetSquares;
    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(getInstance());
    }

}
