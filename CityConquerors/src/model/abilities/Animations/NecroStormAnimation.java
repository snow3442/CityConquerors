package model.abilities.Animations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.GameSquare;
import static model.champions.Rathmore.getInstance;
import static view.GameView.gamePane;

public class NecroStormAnimation extends Animation {

    Group group = new Group();
    private ParallelTransition pt;
    private final double WIDTH = 240;
    private final double HEIGHT = 132;
    private List<Line> lines;

    private List<Line> createBolt(Vector2D src, Vector2D dst, float thickness) {
        ArrayList<Line> results = new ArrayList<>();
        Vector2D tangent = Vector2D.subtract(dst, src);
        Vector2D normal = new Vector2D(tangent.getY(), -tangent.getX()).normalize();
        double length = tangent.getLength();
        ArrayList<Float> positions = new ArrayList<>();
        positions.add(0.0f);
        for (int i = 0; i < length / 4; i++) {
            positions.add((float) Math.random());
        }
        Collections.sort(positions);
        float sway = 80;
        float jaggedness = 1 / sway;
        Vector2D prevPoint = src;
        float prevDisplacement = 0;
        for (int i = 1; i < positions.size(); i++) {
            float pos = positions.get(i);
            // used to prevent sharp angles by ensuring very close positions also have small perpendicular variation.
            double scale = (length * jaggedness) * (pos - positions.get(i - 1));
            // defines an envelope. Points near the middle of the bolt can be further from the central line.
            float envelope = pos > 0.95f ? 20 * (1 - pos) : 1;
            float displacement = (float) (sway * (Math.random() * 2 - 1));
            displacement -= (displacement - prevDisplacement) * (1 - scale);
            displacement *= envelope;
            Vector2D point = Vector2D.add(Vector2D.add(src, Vector2D.multiplyByScalar(tangent, pos)),
                    Vector2D.multiplyByScalar(normal, displacement));
            Line line = new Line(prevPoint.getX(), prevPoint.getY(), point.getX(), point.getY());
            line.setCache(true);
            line.setCacheHint(CacheHint.SPEED);
            line.setStrokeWidth(thickness);
            results.add(line);
            prevPoint = point;
            prevDisplacement = displacement;
        }

        Line line = new Line(prevPoint.getX(), prevPoint.getY(), dst.getX(), dst.getY());
        line.setCache(true);
        line.setCacheHint(CacheHint.SPEED);
        line.setStrokeWidth(thickness);
        results.add(line);
        return results;
    }

    @Override
    public void runAnim(ArrayList<GameSquare> targets) {
        pt = new ParallelTransition();
        Platform.runLater(() -> {
            Glow initGlow = new Glow(0.5);
            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(45.0);
            light.setColor(Color.DARKGRAY);
            Lighting initLighting = new Lighting();
            initLighting.setLight(light);
            initGlow.setInput(initLighting);
            for (GameSquare gs : targets) {
                gs.setEffect(initGlow);
            }
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(light.colorProperty(), light.getColor()),
                            new KeyValue(initGlow.levelProperty(), initGlow.getLevel())
                    ),
                    new KeyFrame(Duration.millis(500),
                            new KeyValue(light.colorProperty(), Color.AQUA),
                            new KeyValue(initGlow.levelProperty(), 0.9)
                    )
            );
            timeline.setAutoReverse(true);
            timeline.setCycleCount(6);
            timeline.setOnFinished(e -> {
                getInstance().getUsedAbility().setValue(true);
            });
            //lighting bolt animations
            group.getChildren()
                    .clear();
            for (GameSquare gs : targets) {
                lines = createBolt(new Vector2D(gs.getLayoutX()+WIDTH, 
                        gs.getLayoutY()+HEIGHT),
                        new Vector2D((float) gs.getLayoutX()+WIDTH+ gs.getIVsquareImage().getFitWidth(),
                                gs.getLayoutY() + HEIGHT + gs.getIVsquareImage().getFitHeight()), 1.5f);
                for (Line l : lines) {
                    l.setStroke(Color.AQUA);
                    DropShadow shadow = new DropShadow(20, Color.BLUE);
                    shadow.setInput(new Glow(0.7));
                    l.setEffect(shadow);
                    group.getChildren().add(l);
                }
            }
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), group);
            ft.setFromValue(
                    0);
            ft.setToValue(
                    1);
            ft.setAutoReverse(
                    true);
            ft.setCycleCount(
                    6);
            ft.play();
            ft.setOnFinished(e->{
                for(Line l: lines){
                    l.setVisible(false);
                }
                gamePane.getChildren().remove(group);
            });

            gamePane.getChildren().add(group);
            pt.getChildren().addAll(timeline, ft);
            pt.play();
           
        });
    }
    public ParallelTransition getAnim() {
        return pt;
    }
}
