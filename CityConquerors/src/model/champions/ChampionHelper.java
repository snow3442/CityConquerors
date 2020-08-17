package model.champions;

import controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Champion;
import static view.GameView.centerPane;

public class ChampionHelper {

    private final double ANIMTIME = 3000;
    private Timeline timeline = new Timeline();

    public ChampionHelper() {
    }

    public void runAbilityCastAnimation(Champion champion) {
        double initX = champion.getGameSquare().getLayoutX() - 100;
        double initY = champion.getGameSquare().getLayoutY() - 20;
        double finalX = champion.getGameSquare().getLayoutX() + 50;
        Text text = new Text(champion.getAbility().getName());
        text.setFont(Font.font("Algerian", 60));      
        if (isChampAlly(champion)) {
            DropShadow ds = new DropShadow();
            ds.setOffsetY(5.0f);
            ds.setColor(Color.AQUA);
            text.setEffect(ds);
            text.setFill(Color.DARKSEAGREEN);
        } else {
            DropShadow ds = new DropShadow();
            ds.setOffsetY(5.0f);
            ds.setColor(Color.ANTIQUEWHITE);
            text.setEffect(ds);
            text.setFill(Color.CRIMSON);
        }
        text.setManaged(false);
        text.setLayoutX(initX);
        text.setLayoutY(initY);
        centerPane.getChildren().add(text);
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(text.layoutXProperty(), text.getLayoutX()),
                        new KeyValue(text.layoutYProperty(), text.getLayoutY())
                ),
                new KeyFrame(Duration.millis(ANIMTIME),
                        new KeyValue(text.layoutXProperty(), finalX),
                        new KeyValue(text.layoutYProperty(), text.getLayoutY())
                )
        );
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        Platform.runLater(() -> {
            timeline.play();
            timeline.setOnFinished(e -> {
                centerPane.getChildren().remove(text);
            });
        });
    }

   

    private boolean isChampAlly(Champion champion) {
        return GameController.allyChampList.contains(champion);
    }

    public Timeline getTimeline() {
        return timeline;
    }
      

}
