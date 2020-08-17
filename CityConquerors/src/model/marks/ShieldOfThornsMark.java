package model.marks;

import controller.GameController;
import static controller.GameController.championStack;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Champion;
import model.Mark;
import static model.marks.MarkInterface.IMGHEIGHT;
import static model.marks.MarkInterface.IMGWIDTH;
import static view.GameView.centerPane;

public class ShieldOfThornsMark extends Mark implements MarkInterface {

    private IntegerProperty numOfTurn = new SimpleIntegerProperty(3);
    private final int damage = 0;
    private final String imgUrl = "images/marks/shieldofthorns.png";
    private ImageView ivMark = new ImageView(new Image(imgUrl));
    private final String TYPE = "PRE";
    private final String animUrl = "images/animation/anim_shieldofthorns.jpg";
    private final String NAME = "Thorn Shield Mark";
    private final String DESCRIPTION = "attackers who deal damage onto this champion will receive half of "
            + "the damage dealt";

    public ShieldOfThornsMark() {
        ivMark.setFitHeight(IMGHEIGHT);
        ivMark.setFitWidth(IMGWIDTH);
    }

    @Override
    public ImageView getIvMark() {
        return ivMark;
    }

    @Override
    public void setIvMark(ImageView ivMark) {
        this.ivMark = ivMark;
    }

    @Override
    public IntegerProperty getNumOfTurn() {
        return numOfTurn;
    }

    @Override
    public void takeEffect(Champion champion) {
        champion.getHp().addListener(
                (observable, oldvalue, newvalue) -> {
                    if (getNumOfTurn().getValue() > 0) {
                        if ((double) newvalue < (double) oldvalue
                        && !GameController.getInstance().fromSameTeam(championStack.peek(), champion)) {
                            championStack.peek().getHp().setValue(championStack.peek().getHp().getValue() - 4);
                            //runShieldOfThornsTakeEffectAnimation(champion);
                        }
                    }
                });
        //update number of turns left
        if (getNumOfTurn().getValue() > 0) {
            getNumOfTurn().setValue(getNumOfTurn().getValue() - 1);
        }

    }

    private void runShieldOfThornsTakeEffectAnimation(Champion champion) {
        Platform.runLater(() -> {
            ImageView ivAnim = new ImageView(new Image(animUrl));
            ivAnim.setManaged(false);
            ivAnim.setLayoutX(champion.getGameSquare().getLayoutX());
            ivAnim.setLayoutY(champion.getGameSquare().getLayoutY() + 30);
            ivAnim.setFitWidth(35);
            ivAnim.setFitHeight(35);
            centerPane.getChildren().add(ivAnim);
            Glow initGlow = new Glow(0.3);
            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(45.0);
            light.setColor(Color.GREEN);
            Lighting initLighting = new Lighting();
            initLighting.setLight(light);
            initGlow.setInput(initLighting);
            champion.getGameSquare().setEffect(initGlow);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(light.colorProperty(), light.getColor()),
                            new KeyValue(initGlow.levelProperty(), initGlow.getLevel()),
                            new KeyValue(ivAnim.layoutXProperty(), ivAnim.getLayoutX())
                    ),
                    new KeyFrame(Duration.millis(3000),
                            new KeyValue(light.colorProperty(), Color.DARKVIOLET),
                            new KeyValue(initGlow.levelProperty(), 0.9),
                            new KeyValue(ivAnim.layoutXProperty(), ivAnim.getLayoutX() + 50)
                    )
            );
            timeline.setAutoReverse(true);
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(e -> {
                champion.getGameSquare().setEffect(null);
                centerPane.getChildren().remove(ivAnim);
            });
        });
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
