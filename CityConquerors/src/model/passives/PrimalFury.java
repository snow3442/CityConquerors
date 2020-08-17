package model.passives;

import controller.GameController;
import static controller.GameController.turnNumber;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import model.Passive;
import model.abilities.Animations.Animator;
import model.champions.Pyrubo;
import static view.GameView.centerPane;

public class PrimalFury extends Passive implements PassiveInterface{

    private final String TYPE = "DEATH";
    private final String NAME = "Primal Fury";
    private final String DESCRIPTION = "Upon reaching 0 health, Pyrubo drops to 1 hp and refuses to \n"
            + "die for the entire turn. This can only happen once per combat";
    private final int MAXPARTICLE = 75;
    private Group group;
    private ArrayList<Circle> burns = new ArrayList<>();
    private ParallelTransition pt1;
    private final String VOICELINE = "passive/primalfury.wav";
    @Override
    public void unwind() {
        if (Pyrubo.getInstance().getHp().getValue() == 0 && !Pyrubo.getInstance().isPassiveUsed()) {
            passiveHelper.runPassiveCastAnimation(Pyrubo.getInstance());
            primalFuryAnimation();
            try {
                GameController.getInstance().loadGameSound(VOICELINE, 0);
            } catch (MalformedURLException ex) {
                Logger.getLogger(PrimalFury.class.getName()).log(Level.SEVERE, null, ex);
            }
            Pyrubo.getInstance().getIsAlive().setValue(true);
            Pyrubo.getInstance().getHp().setValue(1);
            turnNumber.addListener(
                    (observable, oldvalue, newvalue) -> {
                        Pyrubo.getInstance().setPassiveUsed(true);
                    });
        }

    }

    private void primalFuryAnimation() {
        //ability cast notification animation
        Animator.getInstance().NotificationAnimation(getName());
        //main animation
        group = new Group();
        group.setManaged(false);
        pt1 = new ParallelTransition();
        double animationWidth = Pyrubo.getInstance().getGameSquare().getIVsquareImage().getFitHeight();
        double initX = Pyrubo.getInstance().getGameSquare().getLayoutX();
        double initY = Pyrubo.getInstance().getGameSquare().getLayoutY()
                + Pyrubo.getInstance().getGameSquare().getIVsquareImage().getFitHeight();
        for (int i = 0; i < MAXPARTICLE; i++) {
            Circle burn = new Circle(initX + Pyrubo.getInstance().getGameSquare().getIVsquareImage().getFitWidth() * Math.random(),
                    initY, 10);
            burn.setManaged(false);
            burn.setFill(Color.CRIMSON);
            burn.setEffect(new GaussianBlur(400));
            burn.setCache(true);
            burn.setCacheHint(CacheHint.SPEED);
            burns.add(burn);
            group.getChildren().add(burn);
        }
        centerPane.getChildren().add(group);

        for (Circle circle : burns) {
            Path path = new Path();
            path.setVisible(false);
            path.getElements().add(new MoveTo(circle.getCenterX(), circle.getCenterY()));
            path.getElements().add(new CubicCurveTo(circle.getCenterX(), circle.getCenterY(),
                    initX + animationWidth * Math.random(),
                    initY - animationWidth / 2,
                    initX+25 + animationWidth * Math.random(),
                    initY - animationWidth*3/4));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(3000));
            pathTransition.setNode(circle);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pt1.getChildren().add(pathTransition);
        }
        pt1.play();
        pt1.setOnFinished(e1 -> {
                for (Circle burn : burns) {
                    burn.setVisible(false);
                    burn = null;
                }
                centerPane.getChildren().remove(group);
        });

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
