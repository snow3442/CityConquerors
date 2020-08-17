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
import model.Passive;
import model.ParticleSystem.Emitter;
import model.ParticleSystem.ExplosiveArrowEmitter;
import model.ParticleSystem.Particle;
import model.abilities.ExplosiveArrow;
import static model.champions.ChampionInterface.GRIDCONTROLLER;
import model.marks.IgniteMark;
import model.passives.AimedFocus;
import static view.GameView.centerPane;
import static view.GameView.gameGrid;

public class Houyi extends Champion implements ChampionInterface {

    private static Houyi instance = null;
    private final String imgUrl = "images/champions/houyi.png";
    private final String TYPE = "ranger";
    private final Ability ABILITY = new ExplosiveArrow();
    private final Passive PASSIVE = new AimedFocus();
    private ArrayList<GameSquare> targetList = new ArrayList<>();
    private ArrayList<GameSquare> adjacentSquares = new ArrayList<>();
    private List<Particle> particles = new ArrayList<>();
    private GraphicsContext g;
    private long startTime;
    private AnimationTimer timer;
    private Canvas canvas;
    private Emitter emitter = new ExplosiveArrowEmitter();
    private final double trajectoryTime = 2500;
    private double initX = 0.0;
    private double initY = 0.0;
    private double destX = 0.0;
    private double destY = 0.0;
    private double xTravel = 0.0;
    private double yTravel = 0.0;
    private final double VELOCITY = 1.5;
    private Timeline timeline = new Timeline(); 
    private final String VOICELINE = "ability/explosivearrow.wav";
    
    private Houyi() {
        this.getName().setValue("Houyi");
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

    public static synchronized Houyi getInstance() {
        if (instance == null) {
            instance = new Houyi();
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

        //add extra range
        for (Integer[] ints : GRIDCONTROLLER.getAllReachableWithExtraRange(getRange().getValue(), 1, row, col)) {
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

        //enablers
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
        DropShadow dsBleedCast = new DropShadow();
        dsBleedCast.setOffsetX(5.0);
        dsBleedCast.setOffsetY(5.0);
        dsBleedCast.setColor(Color.CRIMSON);
        square.setEffect(dsBleedCast);
        square.setIsAbilityTargetted(true);
        square.setOnMouseClicked(e -> {
            if (isAlly && !getInstance().getUsedAbility().getValue()) {
                castExplosiveArrow(GameController.getInstance().getEnemyFromSquare(square));
            } else if (!isAlly && !getInstance().getUsedAbility().getValue()) {
                castExplosiveArrow(GameController.getInstance().getAllyFromSquare(square));
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

    public void castExplosiveArrow(Champion champion) {
        CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
        //Animator.getInstance().NotificationAnimation();
        int row = GridPane.getRowIndex(champion.getGameSquare());
        int col = GridPane.getColumnIndex(champion.getGameSquare());
        ArrayList<Champion> igniteList = new ArrayList<Champion>();
        igniteList.add(champion);
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
        System.out.println("xTravel: " + xTravel);
        System.out.println("yTravel: " + yTravel);
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
                        for (Integer[] ints : GRIDCONTROLLER.getAllAdjacentSquares(row, col)) {
                            adjacentSquares.add(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))));
                        }
                        if (isChampAlly()) {
                            for (Champion ch : GameController.enemyChampList) {
                                if (adjacentSquares.contains(ch.getGameSquare())) {
                                    igniteList.add(ch);
                                }
                            }
                        } else {
                            for (Champion ch : GameController.allyChampList) {
                                if (adjacentSquares.contains(ch.getGameSquare())) {
                                    igniteList.add(ch);
                                }
                            }
                        }
                        for (GameSquare gs : adjacentSquares) {
                            gs.setEffect(initGlow);
                        }
                        timeline.play();
                        timeline.setOnFinished(ev -> {
                            for (Champion ch : igniteList) {
                                ch.addMark(new IgniteMark());
                            }

                            for (GameSquare gs : adjacentSquares) {
                                gs.setIsAbilityTargetted(false);
                                gs.setEffect(null);
                            }
                            for(GameSquare gs: targetList){
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
            try {
                GameController.getInstance().loadGameSound(VOICELINE, 1);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Houyi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //burning Timeline
        //end of animation
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
     * getting rid of the trace left in UI by using a rectangle that follows dead 
     * particles positions
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

    public double absDiff(double i, double j) {
        if (i > j) {
            return i - j;
        } else if (i < j) {
            return j - i;
        } else {
            return 0.0;
        }
    }

    public double min(double i, double j) {
        if (i < j) {
            return i;
        } else if (i > j) {
            return j;
        } else {
            return i;
        }
    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(getInstance());
    }

}
