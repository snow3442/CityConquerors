package model.champions;

import controller.GameController;
import static controller.GameController.getNodeByRowColumnIndex;
import static controller.GameControllerInterface.NOTARGETFORABILITYMESSAGE;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Ability;
import model.Champion;
import model.GameSquare;
import model.ParticleSystem.Emitter;
import model.ParticleSystem.Particle;
import model.ParticleSystem.PoisonousArrowEmitter;
import model.Passive;
import model.abilities.PoisonousArrow;
import static model.champions.ChampionInterface.GRIDCONTROLLER;
import model.marks.PoisonMark;
import model.passives.SafeShot;
import static model.passives.PassiveInterface.passiveHelper;
import static view.GameView.centerPane;

public class Dun extends Champion implements ChampionInterface {

    private static Dun instance = null;
    private final String imgUrl = "images/champions/dun.png";
    private final String TYPE = "ranger";
    private final Ability ABILITY = new PoisonousArrow();
    private final Passive PASSIVE = new SafeShot();
    private boolean passiveTakeEffect = false;
    private final int ORIGINALDMG = 11;
    private ArrayList<GameSquare> targetList = new ArrayList<>();
    private SafeShot SafeShot = new SafeShot();
    private List<Particle> particles = new ArrayList<>();
    private GraphicsContext g;
    private long startTime;
    private AnimationTimer timer;
    private Canvas canvas;
    private Emitter emitter = new PoisonousArrowEmitter();
    private final double trajectoryTime = 2500;
    private double initX = 0.0;
    private double initY = 0.0;
    private double destX = 0.0;
    private double destY = 0.0;
    private double xTravel = 0.0;
    private double yTravel = 0.0;
    private final double VELOCITY = 1.5;
    private Timeline timeline = new Timeline();
    private final String VOICELINE = "ability/poisonousarrow.wav";

    private Dun() {
        this.getName().setValue("Dun");
        this.setAbility(ABILITY);
        this.setPassive(PASSIVE);
        this.getImgUrl().setValue(imgUrl);
        this.getHp().setValue(45.0);
        this.getMaxHp().setValue(45.0);
        this.getAd().setValue(11);
        this.getAp().setValue(0);
        this.getRange().setValue(3);
        this.getMovingDistance().setValue(2);
        this.setType(TYPE);
        this.setPassiveDescription(getPassive().getDescription());
        this.setAbilityDescription(getAbility().getDescription());
    }

    public static synchronized Dun getInstance() {
        if (instance == null) {
            instance = new Dun();
        }
        return instance;
    }

    @Override
    public void castAbility(GridPane gameGrid) {
        boolean castable = false;
        int row = GridPane.getRowIndex(getGameSquare());
        int col = GridPane.getColumnIndex(getGameSquare());
        //Load squares list
        for (Integer[] ints : GRIDCONTROLLER.getAllReachableEnemyCoordsAtRange(row, col, getRange().getValue())) {
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
        DropShadow dsPoisonCast = new DropShadow();
        dsPoisonCast.setOffsetX(5.0);
        dsPoisonCast.setOffsetY(5.0);
        dsPoisonCast.setColor(Color.VIOLET);
        square.setEffect(dsPoisonCast);
        square.setIsAbilityTargetted(true);
        square.setOnMouseClicked(e -> {
            if (isAlly && !getInstance().getUsedAbility().getValue()) {
                castPoisonousDart(GameController.getInstance().getEnemyFromSquare(square));
            } else if (!isAlly && !getInstance().getUsedAbility().getValue()) {
                castPoisonousDart(GameController.getInstance().getAllyFromSquare(square));
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

    public void castPoisonousDart(Champion champion) {
        CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
        try {
            GameController.getInstance().loadGameSound(VOICELINE, 0.5);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Dun.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dun.getInstance().getAd().setValue(ORIGINALDMG);
        //get a list of all enemies (including target)

        initX = getInstance().getGameSquare().getLayoutX() + getInstance().getGameSquare().
                getIVsquareImage().getFitWidth() / 2;
        initY = getInstance().getGameSquare().getLayoutY() + getInstance().getGameSquare().
                getIVsquareImage().getFitHeight() / 2;
        destX = champion.getGameSquare().getLayoutX() + champion.getGameSquare().
                getIVsquareImage().getFitWidth() / 2;
        destY = champion.getGameSquare().getLayoutY() + champion.getGameSquare().
                getIVsquareImage().getFitHeight() / 2;
        double dx = getInstance().getGameSquare().getLayoutX() - champion.getGameSquare().getLayoutX();
        double dy = getInstance().getGameSquare().getLayoutY() - champion.getGameSquare().getLayoutY();
        double velx;
        double vely;
        //setting velocity components
        if (dx > 0) {
            velx = VELOCITY;
        } else if (dx == 0) {
            velx = 0;
        } else {
            velx = -VELOCITY;
        }

        if (dy > 0) {
            vely = VELOCITY;
        } else if (dy == 0) {
            vely = 0;
        } else {
            vely = -VELOCITY;
        }
        xTravel = -dx / trajectoryTime;
        yTravel = -dy / trajectoryTime;
        //animation
        //animation preps  
        canvas = new Canvas(800, 600);
        canvas.setManaged(false);

        canvas.setLayoutX(0);
        canvas.setLayoutY(0);
        g = canvas.getGraphicsContext2D();
        centerPane.getChildren().add(canvas);
        canvas.setVisible(true);
        startTime = System.currentTimeMillis();
        Platform.runLater(() -> {
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (System.currentTimeMillis() - startTime > trajectoryTime) {
                        timer.stop();
                        centerPane.getChildren().remove(canvas);
                        Glow initGlow = new Glow(0.1);
                        Light.Distant light = new Light.Distant();
                        light.setAzimuth(45.0);
                        light.setElevation(45.0);
                        light.setColor(Color.color(0.9, 0.16, 0.18, 0.5));
                        Lighting initLighting = new Lighting();
                        initLighting.setLight(light);
                        initGlow.setInput(initLighting);
                        champion.getGameSquare().setEffect(initGlow);
                        timeline.getKeyFrames().setAll(
                                new KeyFrame(Duration.ZERO,
                                        new KeyValue(light.colorProperty(), light.getColor()),
                                        new KeyValue(initGlow.levelProperty(), initGlow.getLevel())
                                ),
                                new KeyFrame(Duration.seconds(1),
                                        new KeyValue(light.colorProperty(), Color.rgb(240, 40, 45)),
                                        new KeyValue(initGlow.levelProperty(), 0.9)
                                )
                        );
                        timeline.setAutoReverse(true);
                        timeline.setCycleCount(1);

                        timeline.play();
                        timeline.setOnFinished(ev -> {
                            champion.getGameSquare().setEffect(null);
                            champion.addMark(new PoisonMark());
                            ArrayList<Champion> SafeShotTargets = SafeShot.getSafeShotTargets(Dun.getInstance(),
                                    passiveHelper.getAllyBlockadesCoords(Dun.getInstance()));
                            if (SafeShotTargets.contains(champion)) {
                                GameController.getInstance().updateHpStatsWithDeathChecker(champion, Dun.getInstance().getAd().getValue() + 4);

                            } else {
                                GameController.getInstance().updateHpStatsWithDeathChecker(champion, Dun.getInstance().getAd().getValue());
                            }
                            for (GameSquare gs : targetList) {
                                gs.setIsAbilityTargetted(false);
                                gs.setEffect(null);
                            }
                            getInstance().getUsedAbility().setValue(true);
                        });
                    }
                    onUpdate(new Point2D(velx, vely));
                }
            };
            timer.start();
        });

    }

    private void onUpdate(Point2D velocity) {
        g.setGlobalAlpha(1);
        g.setGlobalBlendMode(BlendMode.SRC_OVER);
        g.setFill(Color.TRANSPARENT);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        particles.addAll(emitter.emit(initX + (System.currentTimeMillis() - startTime) * xTravel,
                initY + (System.currentTimeMillis() - startTime) * yTravel, new Point2D(Math.random() * velocity.getX(),
                Math.random() * velocity.getY())));
        clearParticles(initX, initY, xTravel, yTravel);
        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
            Particle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
                continue;
            }
            p.render(g);
        }
    }

    /**
     * getting rid of the trace left in UI by using a rectangle that follows
     * dead particles positions
     *
     * @param initX
     * @param initY
     * @param deltaX
     * @param deltaY
     */
    private void clearParticles(double initX, double initY, double deltaX, double deltaY) {
        if (deltaX >= 0 && deltaY > 0) {
            g.clearRect(initX - 20, initY - 20, initX - 20 + (System.currentTimeMillis() - startTime) * xTravel,
                    initY + (System.currentTimeMillis() - startTime) * yTravel);
        } else if (deltaX < 0 && deltaY >= 0) {
            g.clearRect(initX + (System.currentTimeMillis() - startTime) * xTravel, initY - 20, initX + (System.currentTimeMillis() - startTime) * xTravel,
                    initY + (System.currentTimeMillis() - startTime) * yTravel);
        } else if (deltaX > 0 && deltaY <= 0) {
            g.clearRect(initX - 20, initY + (System.currentTimeMillis() - startTime) * yTravel, initX + (System.currentTimeMillis() - startTime) * xTravel,
                    initY - 20);
        } else if (deltaX <= 0 && deltaY < 0) {
            g.clearRect(initX + (System.currentTimeMillis() - startTime) * xTravel, initY + (System.currentTimeMillis() - startTime) * yTravel, initX + 20,
                    initY + 20);
        }

    }

    public boolean isPassiveTakeEffect() {
        return passiveTakeEffect;
    }

    public void setPassiveTakeEffect(boolean passiveTakeEffect) {
        this.passiveTakeEffect = passiveTakeEffect;
    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(getInstance());
    }

}
