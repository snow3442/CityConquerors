package model.GameObject;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.GameSquare;
import static view.GameView.centerPane;
import view.HpBar;

public abstract class AttackableGameObject {

    private DoubleProperty hp;

    public AttackableGameObject() {
        this.hp = new SimpleDoubleProperty(0.0);
        hp.addListener(
                (observable, oldvalue, newvalue) -> {
                    playHpChangingAnimation((double) oldvalue, (double) newvalue);
                });
    }

    public void playHpChangingAnimation(double oldval, double newval) {
        Text hpChangingText = new Text();
        if (oldval < newval) {
            hpChangingText.setText("+" + (newval - oldval));
            hpChangingText.setFill(Color.GREEN);
            hpChangingText.setLayoutX((getGameSquare().getLayoutX() + 10));
            hpChangingText.setLayoutY((getGameSquare().getLayoutY() + 40));
        } else {
            hpChangingText.setText("-" + (oldval - newval));
            hpChangingText.setFill(Color.WHITE);
            hpChangingText.setLayoutX((getGameSquare().getLayoutX() + 75));
            hpChangingText.setLayoutY((getGameSquare().getLayoutY() + 40));
        }
        hpChangingText.setFont(Font.font("Verdana", FontPosture.REGULAR, 16));
        //set the position
        hpChangingText.setManaged(false);

        centerPane.getChildren().add(hpChangingText);
        double currentYpos = hpChangingText.getLayoutY();
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(hpChangingText.layoutYProperty(), currentYpos - 5)
                ),
                new KeyFrame(Duration.millis(2400),
                        new KeyValue(hpChangingText.layoutYProperty(), currentYpos - 30)
                )
        );
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        Platform.runLater(() -> {
            timeline.play();
            timeline.setOnFinished(e -> {
                centerPane.getChildren().remove(hpChangingText);
            });
        });

    }

    public abstract String getSide();

    public abstract void setSide(String side);

    public abstract DoubleProperty getMaxHp();

    public DoubleProperty getHp() {
        return hp;
    }

    public void setHp(DoubleProperty hp) {
        this.hp = hp;
    }

    public abstract BooleanProperty getIsDestroyed();

    public abstract void setIsDestroyed(BooleanProperty isDestroyed);

    public abstract ImageView getImageView();

    public abstract void setImageView(ImageView imageView);

    public abstract StringProperty getImgUrl();

    public abstract void setImgUrl(StringProperty imgUrl);

    public abstract GameSquare getGameSquare();

    public abstract void setGameSquare(GameSquare gameSquare);

    public abstract HpBar getHpBar();

    public abstract String getDescription();

    public abstract Text getTxtHp();

    public abstract void setTxtHp(Text txtHp);

    public abstract Pane getPane();

}
