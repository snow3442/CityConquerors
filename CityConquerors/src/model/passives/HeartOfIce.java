package model.passives;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Passive;
import model.champions.Alery;
import static view.GameView.centerPane;

public class HeartOfIce extends Passive implements PassiveInterface {

    private final String TYPE = "DAMAGERECEIVE";
    private final String NAME = "Heart Of Ice";
    private final String DESCRIPTION = "Flamma takes 3 less dmg when she is below 20 hp";
    private Timeline timeline;
    private final Image imgAnim = new Image("images/animation/heartofice.jpg");

    @Override
    public void unwind() {
        Alery.getInstance().setPassiveTakeEffect(false);
        Alery.getInstance().getHp().addListener(
                (observable, oldvalue, newvalue) -> {
                    if ((double) newvalue < (double) oldvalue && (double) newvalue <= 20
                    && (double) newvalue > 0 && !Alery.getInstance().isPassiveTakeEffect()) {
                        passiveHelper.runPassiveCastAnimation(Alery.getInstance());
                        runHeartOfIceAnimation();
                        Alery.getInstance().getHp().setValue(Alery.getInstance().getHp().getValue() + 3);
                        Alery.getInstance().setPassiveTakeEffect(true);
                    }
                });
    }

    public void runHeartOfIceAnimation() {

        Platform.runLater(() -> {
            ImageView ivAnim = new ImageView(imgAnim);
            ivAnim.setFitWidth(Alery.getInstance().getGameSquare().getIVsquareImage().getFitWidth());
            ivAnim.setFitHeight(Alery.getInstance().getGameSquare().getIVsquareImage().getFitHeight());
            ivAnim.setManaged(false);
            ivAnim.setLayoutX(Alery.getInstance().getGameSquare().getLayoutX());
            ivAnim.setLayoutY(Alery.getInstance().getGameSquare().getLayoutY());
            centerPane.getChildren().add(ivAnim);
            DropShadow dsShield = new DropShadow();
            dsShield.setSpread(10.0);
            dsShield.setRadius(5.0);
            dsShield.setColor(Color.KHAKI);
            Alery.getInstance().getGameSquare().setEffect(dsShield);
            DropShadow dsFinal = new DropShadow();
            dsFinal.setSpread(5.0);
            dsFinal.setRadius(5.0);
            dsFinal.setColor(Color.GOLD);
            timeline = new Timeline();
            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(dsShield.radiusProperty(), dsShield.getRadius()),
                            new KeyValue(dsShield.spreadProperty(), dsShield.getSpread()),
                            new KeyValue(dsShield.colorProperty(), dsShield.getColor()),
                            new KeyValue(ivAnim.opacityProperty(),0.2)
                    ),
                    new KeyFrame(Duration.millis(2000),
                            new KeyValue(dsShield.radiusProperty(), dsFinal.getRadius()),
                            new KeyValue(dsShield.spreadProperty(), dsFinal.getSpread()),
                            new KeyValue(dsShield.colorProperty(), dsFinal.getColor()),
                            new KeyValue(ivAnim.opacityProperty(), 1)
                    )
            );
            timeline.setAutoReverse(true);
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(e -> {
                Alery.getInstance().getGameSquare().setEffect(null);
                centerPane.getChildren().remove(ivAnim);
            });
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
