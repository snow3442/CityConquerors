package model.passives;

import controller.GameController;
import static controller.GameController.allyChampList;
import static controller.GameController.enemyChampList;
import static controller.GameController.turnNumber;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import model.GameSquare;
import static model.GameSquare.FIELDURL;
import model.Passive;
import model.champions.Rathmore;
import view.ChampionInfoPane;
import static view.GameView.centerPane;
import static view.GameView.gameGrid;

public class LingeringSoul extends Passive implements PassiveInterface {

    private final String TYPE = "DEATH";
    private final String NAME = "Lingering Soul";
    private final String DESCRIPTION = "when you die, you can still play 1 more turn called dead turn, during "
            + "your dead turn your move without unit collision";
    private ArrayList<Integer> turnList = new ArrayList<>();
    private final String DEADLORD = "images/ingame/deadlord.jpg";
    private ArrayList<Circle> particles = new ArrayList<>();
    private Group group = new Group();
    private ParallelTransition pt = new ParallelTransition();
    private double xOffSet;
    private double yOffSet;
    private double imgWidth;
    private final String VOICELINE = "passive/lingeringsoul.wav";

    @Override
    public void unwind() {
        if (Rathmore.getInstance().getHp().getValue() == 0 && turnList.isEmpty()) {
            turnList.add(turnNumber.getValue());
            try {
                GameController.getInstance().loadGameSound(VOICELINE, 0);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Rathmore.class.getName()).log(Level.SEVERE, null, ex);
            }
            passiveHelper.runPassiveCastAnimation(Rathmore.getInstance());
            runLingeringSoulAnimation();
            Rathmore.getInstance().getIsAlive().setValue(true);
            Rathmore.getInstance().getGameSquare().setElement(DEADLORD);
            Rathmore.getInstance().getImgUrl().setValue(DEADLORD);
            Rathmore.getInstance().getUnitCollisionType().setValue("ALL");
            Rathmore.getInstance().getGameSquare().setIsChampTargetted(false);
            Rathmore.getInstance().getGameSquare().setEffect(null);
            Rathmore.getInstance().getGameSquare().setOnMouseClicked(e -> {
                ChampionInfoPane.getInstance().loadSpecificChampionPane(Rathmore.getInstance());
            });
            turnNumber.addListener(
                    (observable, oldvalue, newvalue) -> {
                        turnList.add((int) newvalue);
                        if (turnList.size() == 3 && Rathmore.getInstance().getIsAlive().getValue()) {
                            Rathmore.getInstance().getIsAlive().setValue(false);
                            //Logically remove Rathmore
                            if (isChampAlly()) {
                                allyChampList.remove(Rathmore.getInstance());
                            } else if (enemyChampList.contains(Rathmore.getInstance()) && !Rathmore.getInstance().getIsAlive().getValue()) {
                                enemyChampList.remove(Rathmore.getInstance());
                            }

                            //visually remove Rathmore
                            GameSquare newSquare = new GameSquare();
                            newSquare.setElement(FIELDURL);
                            //clean up square of the dead champ by replacement
                            GameController.getInstance().replaceNodeByColRowIndex(newSquare, GridPane.getRowIndex(Rathmore.getInstance().getGameSquare()),
                                    GridPane.getColumnIndex(Rathmore.getInstance().getGameSquare()), gameGrid);
                            Rathmore.getInstance().getMarkList().clear();
                            Rathmore.getInstance().getMarkBox().getChildren().clear();
                            centerPane.getChildren().removeAll(Rathmore.getInstance().getTxtHp(), Rathmore.getInstance().getHpBar(), Rathmore.getInstance().getChampionPane());
                        }

                    });

        }

    }

    private void runLingeringSoulAnimation() {
        Rathmore.getInstance().getGameSquare().setEffect(null);
        xOffSet = Rathmore.getInstance().getGameSquare().getLayoutX();
        yOffSet = Rathmore.getInstance().getGameSquare().getLayoutY();
        imgWidth = Rathmore.getInstance().getGameSquare().getIVsquareImage().getFitWidth();
        Image snapshot = Rathmore.getInstance().getGameSquare().getIVsquareImage().snapshot(null, null);
        group = new Group();
        group.setManaged(false);
        for (int y = 0; y < snapshot.getHeight(); y = y + 3) {
            for (int x = 0; x < snapshot.getWidth(); x = x + 3) {
                Circle circle = new Circle(xOffSet + x + 1, yOffSet + y + 1.5, 1);
                circle.setFill(snapshot.getPixelReader().getColor(x, y));
                particles.add(circle);
                circle.setManaged(false);
                centerPane.getChildren().add(circle);
            }
        }
        centerPane.getChildren().add(group);
        //animations
        FadeTransition ft = new FadeTransition(Duration.millis(1500), Rathmore.getInstance().getGameSquare().getIVsquareImage());
        ft.setFromValue(0.0);
        ft.setToValue(1);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        addParticlesAnimation();
        pt.getChildren().add(ft);
        Platform.runLater(() -> {
            pt.play();
            pt.setOnFinished(e -> {
                for (Circle circle : particles) {
                    centerPane.getChildren().remove(circle);
                }
                centerPane.getChildren().remove(group);
            });
        });
    }

    private void addParticlesAnimation() {
        for (Circle circle : particles) {
            Path path = new Path();
            path.setVisible(false);
            path.getElements().add(new MoveTo(circle.getCenterX(), circle.getCenterY()));
            path.getElements().add(new LineTo(xOffSet + imgWidth * Math.random(), 0));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(3000));
            pathTransition.setNode(circle);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pt.getChildren().add(pathTransition);
        }

    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(Rathmore.getInstance());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
