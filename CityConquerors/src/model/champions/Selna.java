package model.champions;

import controller.GameController;
import static controller.GameController.getNodeByRowColumnIndex;
import static controller.GameControllerInterface.NOTARGETFORABILITYMESSAGE;
import java.util.ArrayList;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
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
import model.Passive;
import model.abilities.SoundOfHealing;
import static model.champions.ChampionInterface.GRIDCONTROLLER;
import model.passives.HealingBeacon;
import static view.GameView.centerPane;

public class Selna extends Champion implements ChampionInterface {

    private static Selna instance = null;
    private final String imgUrl = "images/champions/selna.png";
    private final String TYPE = "support";
    private final Ability ABILITY = new SoundOfHealing();
    private final Passive PASSIVE = new HealingBeacon();
    private ArrayList<GameSquare> targetList = new ArrayList<>();
    private final double ANIMATIONWIDTH = 50;
    private final double ANIMATIONHEIGHT = 50;
    private final int NUMOFPARTICLES = 100;
    private Group group;
    private Group gHeal;
    private ArrayList<Circle> heals = new ArrayList<>();

    private Selna() {
        this.getName().setValue("Selna");
        this.setAbility(ABILITY);
        this.setPassive(PASSIVE);
        this.getImgUrl().setValue(imgUrl);
        this.getHp().setValue(65.0);
        this.getMaxHp().setValue(65.0);
        this.getAd().setValue(6);
        this.getAp().setValue(0);
        this.getRange().setValue(2);
        this.getMovingDistance().setValue(2);
        this.setType(TYPE);
        this.setPassiveDescription(getPassive().getDescription());
        this.setAbilityDescription(getAbility().getDescription());

    }

    public static synchronized Selna getInstance() {
        if (instance == null) {
            instance = new Selna();
        }
        return instance;
    }

    @Override
    public void castAbility(GridPane gameGrid) {
        boolean castable = false;
        int row = GridPane.getRowIndex(this.getGameSquare());
        int col = GridPane.getColumnIndex(this.getGameSquare());
        //Load squares list
        for (Integer[] ints : GRIDCONTROLLER.getAllreachableCoordsWithinDistance(row, col, getRange().getValue())) {
            if (isChampAlly()
                    && GameController.getInstance().isAllySquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))))) {
                targetList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
            } else if (!isChampAlly()
                    && GameController.getInstance().isEnemySquare(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))))) {
                targetList.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
            }
        }

        for (GameSquare gs : targetList) {
            if (isChampAlly()) {
                if (GameController.getInstance().getAllyFromSquare(gs).getHp().getValue() < 25) {
                    castable = true;
                    enableAbilityCast(gs, isChampAlly());
                    cancelAbilityCastEffects(gs, isChampAlly());
                }
            } else {
                if (GameController.getInstance().getEnemyFromSquare(gs).getHp().getValue() < 25) {
                    castable = true;
                    enableAbilityCast(gs, isChampAlly());
                    cancelAbilityCastEffects(gs, isChampAlly());
                }
            }
        }
        if (!castable) {
            GameController.getInstance().warningPaneAnimation(getGameSquare(), NOTARGETFORABILITYMESSAGE);
        }
    }

    public void enableAbilityCast(GameSquare square, boolean isAlly) {
        DropShadow dsHealCast = new DropShadow();
        dsHealCast.setOffsetX(5.0);
        dsHealCast.setOffsetY(5.0);
        dsHealCast.setColor(Color.GREEN);
        square.setEffect(dsHealCast);
        square.setIsAbilityTargetted(true);
        square.setOnMouseClicked(e -> {
            if (isAlly && !getInstance().getUsedAbility().getValue()) {
                castSoundOfHealing(GameController.getInstance().getAllyFromSquare(square));
            } else if (!isAlly && !getInstance().getUsedAbility().getValue()) {
                castSoundOfHealing(GameController.getInstance().getEnemyFromSquare(square));
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

    public void castSoundOfHealing(Champion champion) {
        CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
        //define initial and final destinations
        double initX = getInstance().getGameSquare().getLayoutX()
                + getInstance().getGameSquare().getIVsquareImage().getFitWidth() / 2
                - ANIMATIONWIDTH / 2;
        double initY = getInstance().getGameSquare().getLayoutY()
                + getInstance().getGameSquare().getIVsquareImage().getFitHeight() / 2
                - ANIMATIONHEIGHT / 2;
        double destX = champion.getGameSquare().getLayoutX()
                + champion.getGameSquare().getIVsquareImage().getFitWidth() / 2
                - ANIMATIONWIDTH / 2;
        double destY = champion.getGameSquare().getLayoutY()
                + champion.getGameSquare().getIVsquareImage().getFitHeight() / 2
                - ANIMATIONHEIGHT / 2;
        double expandX = champion.getGameSquare().getLayoutX();
        double expandY = champion.getGameSquare().getLayoutY();
        //build heal particles both ends
        //init heals
        group = new Group();
        group.setManaged(false);
        for (int i = 0; i < NUMOFPARTICLES; i++) {
            Circle heal = new Circle(initX + ANIMATIONWIDTH * Math.random(),
                    initY + ANIMATIONHEIGHT * Math.random(), 5);
            heal.setManaged(false);
            heal.setFill(Color.GREEN);
            heal.setEffect(new GaussianBlur(50));
            heal.setCache(true);
            heal.setCacheHint(CacheHint.SPEED);
            heals.add(heal);
            group.getChildren().add(heal);
        }
        centerPane.getChildren().add(group);

        //define Path transition
        // pt for moving from init to dest
        ParallelTransition ptItoD = new ParallelTransition();
        for (Circle cir1 : heals) {
            Path path = new Path();
            path.setVisible(false);            
            path.getElements().add(new MoveTo(cir1.getCenterX(), cir1.getCenterY()));
            path.getElements().add(new LineTo(
                    destX + ANIMATIONWIDTH * Math.random(),
                    destY + ANIMATIONHEIGHT * Math.random()));
            path.getElements().add(new LineTo(
                    expandX + champion.getGameSquare().getIVsquareImage().getFitWidth() * Math.random(),
                    expandY + champion.getGameSquare().getIVsquareImage().getFitHeight() * Math.random()));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(3000));
            pathTransition.setNode(cir1);
            pathTransition.setPath(path);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.setCycleCount(1);
            ptItoD.getChildren().add(pathTransition);
        }
        Platform.runLater(() -> {
            ptItoD.play();
        });

        ptItoD.setOnFinished(e -> {
            for (Circle heal : heals) {
                heal.setVisible(false);
                heal = null;
            }
            //garbage collection
            heals.clear();
            group.getChildren().clear();
            centerPane.getChildren().remove(group);
            //update logics
            champion.getHp().setValue(25);
            for (GameSquare gs : targetList) {
                gs.setIsAbilityTargetted(false);
                gs.setEffect(null);
            }
            getInstance().getUsedAbility().setValue(true);
        });

    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(getInstance());
    }

}
