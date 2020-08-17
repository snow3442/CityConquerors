package model.champions;

import controller.GameController;
import static controller.GameController.turnNumber;
import static controller.GameControllerInterface.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.CacheHint;
import javafx.scene.Group;
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
import model.Mark;
import model.Passive;
import model.abilities.Trickster;
import model.marks.TricksterMark;
import model.passives.MonkeyAgility;
import static view.GameView.centerPane;

public class Wukong extends Champion implements ChampionInterface {
    
    private static Wukong instance = null;
    private final String imgUrl = "images/champions/wukong.png";
    private final String TYPE = "assassin";
    private final Ability ABILITY = new Trickster();
    private final Passive PASSIVE = new MonkeyAgility();
    private BooleanProperty TricksterExist = new SimpleBooleanProperty(true);
    private ArrayList<Integer> turnList = new ArrayList<>();
    private ArrayList<Circle> smokes = new ArrayList<>();
    private final int MAXSMOKE = 60;
    private final String TRICKSTER = "ability/trickster.wav";
    private final String TRICKSTERHIT = "ability/tricksterhit.wav";
    private final String TRICKSTERNOTHIT = "ability/tricksternothit.wav";

    private Wukong() {
        this.getName().setValue("Wukong");
        this.setAbility(ABILITY);
        this.setPassive(PASSIVE);
        this.getImgUrl().setValue(imgUrl);
        this.getHp().setValue(60.0);
        this.getMaxHp().setValue(60.0);
        this.getAd().setValue(18);
        this.getAp().setValue(0);
        this.getRange().setValue(1);
        this.getMovingDistance().setValue(3);
        this.setType(TYPE);
        this.setPassiveDescription(getPassive().getDescription());
        this.setAbilityDescription(getAbility().getDescription());
    }

    public static synchronized Wukong getInstance() {
        if (instance == null) {
            instance = new Wukong();
        }
        return instance;
    }

    @Override
    public void castAbility(GridPane gameGrid) {
        if (!getInstance().getAutoAttacked().getValue()) {
            GameController.getInstance().warningPaneAnimation(getInstance().getGameSquare(), TricksterNOTENABLEDMESSAGE);
        } else {
            getInstance().addMark(new TricksterMark());
            runTricksterAnimation();
            try {
                GameController.getInstance().loadGameSound(TRICKSTER,0);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Wukong.class.getName()).log(Level.SEVERE, null, ex);
            }
            getInstance().getHp().addListener(
                    (observable, oldvalue, newvalue) -> {
                        if (getInstance().getTricksterExist().getValue()) {
                            if ((double) newvalue < (double) oldvalue) {
                                CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
                                runTricksterAnimation();
                                try {
                                    GameController.getInstance().loadGameSound(TRICKSTERHIT,0);
                                } catch (MalformedURLException ex) {
                                    Logger.getLogger(Wukong.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                getInstance().getHp().setValue(oldvalue);
                                getInstance().getTricksterExist().setValue(false);
                            }
                        }
                    });
            turnNumber.addListener(
                    (observable, oldvalue, newvalue) -> {
                        turnList.add((int) newvalue);
                        if (turnList.size() == 2 && getInstance().getTricksterExist().getValue()) {
                            runTricksterAnimation();
                            try {
                                GameController.getInstance().loadGameSound(TRICKSTERNOTHIT,0.5);
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(Wukong.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            getInstance().getTricksterExist().setValue(false);
                        }
                    });
            getInstance().getUsedAbility().setValue(true);
        }

    }

    @Override
    public void addMark(Mark mark) {
        if (getInstance().getTricksterExist().getValue() && getInstance().getUsedAbility().getValue()) {

        } else {
            super.addMark(mark);
        }
    }

    /**
     * trickster Animation
     */
    public void runTricksterAnimation() {
        Group group = new Group();
        group.setManaged(false);
        double animationWidth = getInstance().getGameSquare().getIVsquareImage().getFitWidth();
        double animationHeight = getInstance().getGameSquare().getIVsquareImage().getFitHeight();
        double initX = getInstance().getGameSquare().getLayoutX();
        double initY = getInstance().getGameSquare().getLayoutY()
                + getInstance().getGameSquare().getIVsquareImage().getFitHeight();
        //build smokes
        for (int i = 0; i < MAXSMOKE; i++) {
            Circle smoke = new Circle(initX + animationWidth * Math.random(),
                    initY + animationHeight * Math.random(), 10);
            smoke.setManaged(false);
            smoke.setFill(Color.WHITESMOKE);
            smoke.setEffect(new GaussianBlur(200));
            smoke.setCache(true);
            smoke.setCacheHint(CacheHint.SPEED);
            smokes.add(smoke);
            group.getChildren().add(smoke);
            centerPane.getChildren().add(smoke);
        }
        centerPane.getChildren().add(group);
        // build parallel transition
        ParallelTransition pt = new ParallelTransition();
        ParallelTransition pt1 = new ParallelTransition();
        for (Circle circle : smokes) {
            Path path = new Path();
            path.setVisible(false);
            path.getElements().add(new MoveTo(circle.getCenterX(), circle.getCenterY()));
            path.getElements().add(new LineTo(
                    initX + animationWidth * Math.random(),
                    circle.getCenterY() - animationHeight * Math.random()));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(3000));
            pathTransition.setNode(circle);
            pathTransition.setPath(path);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.setCycleCount(1);
            pt1.getChildren().add(pathTransition);

        }

        Platform.runLater(() -> {
            pt1.play();
            pt1.setOnFinished(e -> {
                for (Circle smoke : smokes) {
                    smoke.setVisible(false);
                    smoke = null;
                }
                getInstance().getGameSquare().setEffect(null);

            });

        });

    }

    public BooleanProperty getTricksterExist() {
        return TricksterExist;
    }

    public void setTricksterExist(BooleanProperty TricksterExist) {
        this.TricksterExist = TricksterExist;
    }
	
	

}
