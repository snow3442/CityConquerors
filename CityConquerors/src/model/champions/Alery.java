package model.champions;

import controller.GameController;
import static controller.GameController.getNodeByRowColumnIndex;
import static controller.GameControllerInterface.NOTARGETFORABILITYMESSAGE;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import model.Ability;
import model.Champion;
import model.GameSquare;
import model.Passive;
import model.abilities.FrostBlast;
import model.marks.FrozenMark;
import model.passives.HeartOfIce;
import static view.GameView.centerPane;

public class Alery extends Champion implements ChampionInterface {

    private static Alery instance = null;
    private final String imgUrl = "images/champions/alery.png";
    private final String TYPE = "tank";
    private final Ability ABILITY = new FrostBlast();
    private final Passive PASSIVE = new HeartOfIce();
    private boolean passiveTakeEffect = false;
    private ArrayList<GameSquare> targetList = new ArrayList<GameSquare>();
    private ArrayList<Champion> champList = new ArrayList<Champion>();
    private final double ANIMATIONWIDTH = 100;
    private final double ANIMATIONHEIGHT = 100;
    private final int MAXFROSTS = 25;
    private Group group;
    private ArrayList<Circle> frosts = new ArrayList<>();
    private Timeline timeline = new Timeline();
    private ParallelTransition pt1 = new ParallelTransition();
    private ParallelTransition pt2 = new ParallelTransition();

    private Alery() {
        this.getName().setValue("Alery");
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

    public static synchronized Alery getInstance() {
        if (instance == null) {
            instance = new Alery();
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
                champList.add(GameController.getInstance().getEnemyFromSquare(targetList.get(i)));
                castFrozenMark(GameController.getInstance().getEnemyFromSquare(targetList.get(i)));
            } else if (!isChampAlly() && !getInstance().getUsedAbility().getValue()) {
                champList.add(GameController.getInstance().getAllyFromSquare(targetList.get(i)));
                castFrozenMark(GameController.getInstance().getAllyFromSquare(targetList.get(i)));
            }
        }

        if (castable) {
            CHAMPIONHELPER.runAbilityCastAnimation(getInstance());
            frostsGatheringAnimation(targetList);          
        }
        
        else{
            GameController.getInstance().warningPaneAnimation(getGameSquare(), NOTARGETFORABILITYMESSAGE);
        }
    }

    public void castFrozenMark(Champion champion) {
        champion.addMark(new FrozenMark());
               
    }

    public void frostsGatheringAnimation(ArrayList<GameSquare> targets) {
        group = new Group();
        group.setManaged(false);
        //build the frosts
        double initX = getInstance().getGameSquare().getLayoutX()
                + getInstance().getGameSquare().getIVsquareImage().getFitWidth() / 2
                - ANIMATIONWIDTH / 2;
        double initY = getInstance().getGameSquare().getLayoutY()
                + getInstance().getGameSquare().getIVsquareImage().getFitHeight() / 2
                - ANIMATIONHEIGHT / 2;
        double transitionX = getInstance().getGameSquare().getLayoutX()
                + getInstance().getGameSquare().getIVsquareImage().getFitWidth() / 2
                - ANIMATIONWIDTH / 8;
        double transitionY = getInstance().getGameSquare().getLayoutY()
                + getInstance().getGameSquare().getIVsquareImage().getFitHeight() / 2
                - ANIMATIONHEIGHT / 8;
        double finalX = getInstance().getGameSquare().getLayoutX()
                + getInstance().getGameSquare().getIVsquareImage().getFitWidth() / 2
                - ANIMATIONWIDTH * 3;
        double finalY = getInstance().getGameSquare().getLayoutY()
                + getInstance().getGameSquare().getIVsquareImage().getFitHeight() / 2
                - ANIMATIONHEIGHT * 3;
        
        //build timeline animation features
        Glow initGlow = new Glow(0.1);
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(45.0);
        light.setColor(Color.color(0, 0.1, 0.9, 0.9));
        Lighting initLighting = new Lighting();
        initLighting.setLight(light);
        initGlow.setInput(initLighting);
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(light.colorProperty(), light.getColor()),
                        new KeyValue(initGlow.levelProperty(), initGlow.getLevel())
                ),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(light.colorProperty(), Color.AQUA),
                        new KeyValue(initGlow.levelProperty(), 0.9)
                )
        );
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        
        //build frosts
        for (int i = 0; i < MAXFROSTS; i++) {
            Circle frost = new Circle(initX + ANIMATIONWIDTH * Math.random(),
                    initY + ANIMATIONHEIGHT * Math.random(), 10);
            frost.setManaged(false);
            frost.setFill(Color.CYAN);
            frost.setEffect(new GaussianBlur(100));
            frost.setCache(true);
            frost.setCacheHint(CacheHint.SPEED);
            frosts.add(frost);
            group.getChildren().add(frost);
        }
        centerPane.getChildren().add(group);
        for (Circle circle : frosts) {
            Path path = new Path();
            path.setVisible(false);
            path.getElements().add(new MoveTo(circle.getCenterX(), circle.getCenterY()));
            ArcTo arcTo1 = new ArcTo();
            arcTo1.setX(transitionX - 80 + (ANIMATIONWIDTH + 80) * Math.random());
            arcTo1.setY(transitionY - 80 + (ANIMATIONHEIGHT + 80) * Math.random());
            arcTo1.setRadiusX(40 + ANIMATIONWIDTH / 8);
            arcTo1.setRadiusY(40 + ANIMATIONHEIGHT / 8);
            path.getElements().add(arcTo1);
            ArcTo arcTo2 = new ArcTo();
            arcTo2.setX(transitionX - 50 + (ANIMATIONWIDTH + 50) * Math.random());
            arcTo2.setY(transitionY - 50 + (ANIMATIONHEIGHT + 50) * Math.random());
            arcTo2.setRadiusX(25 + ANIMATIONWIDTH / 8);
            arcTo2.setRadiusY(25 + ANIMATIONHEIGHT / 8);
            path.getElements().add(arcTo2);
            ArcTo arcTo3 = new ArcTo();
            arcTo3.setX(transitionX - 20 + (ANIMATIONWIDTH + 20) * Math.random());
            arcTo3.setY(transitionY - 20 + (ANIMATIONHEIGHT + 20) * Math.random());
            arcTo3.setRadiusX(10 + ANIMATIONWIDTH / 8);
            arcTo3.setRadiusY(10 + ANIMATIONHEIGHT / 8);
            path.getElements().add(arcTo3);
            path.getElements().add(new CubicCurveTo(circle.getCenterX(), circle.getCenterY(),
                    transitionX + ANIMATIONWIDTH * Math.random() / 4,
                    transitionY + ANIMATIONHEIGHT * Math.random() / 4,
                    transitionX + ANIMATIONWIDTH * Math.random() / 4,
                    transitionY + ANIMATIONHEIGHT * Math.random() / 4));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(2500));
            pathTransition.setNode(circle);
            pathTransition.setPath(path);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.setCycleCount(1);
            pt1.getChildren().add(pathTransition);
        }

        for (Circle circle : frosts) {
            Path path = new Path();
            path.setVisible(false);
            path.getElements().add(new MoveTo(circle.getCenterX(), circle.getCenterY()));
            path.getElements().add(new LineTo(
                    finalX + ANIMATIONWIDTH * Math.random() * 6,
                    finalY + ANIMATIONHEIGHT * Math.random() * 6));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(500));
            pathTransition.setNode(circle);
            pathTransition.setPath(path);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.setCycleCount(1);
            pt2.getChildren().add(pathTransition);
        }
        //playing animations
        pt1.play();
        pt1.setOnFinished(e -> {
            pt2.play();
            pt2.setOnFinished(e1 -> {
                for (Circle frost : frosts) {
                    frost.setVisible(false);
                    frost = null;
                }
                for (GameSquare gs : targets) {
                    gs.setEffect(initGlow);
                }

                timeline.play();
                timeline.setOnFinished(ev -> {
                    for (GameSquare gs : targets) {
                        gs.setEffect(null);
                    }
                    for (Champion champ : champList) {
                        GameController.getInstance().updateHpStatsWithDeathChecker(champ, 4);
                    }
                    getInstance().getUsedAbility().setValue(true);
                });
            });

        });
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
