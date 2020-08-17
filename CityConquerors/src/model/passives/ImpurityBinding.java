package model.passives;

import controller.GameController;
import static controller.GameController.championStack;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Champion;
import model.GameSquare;
import model.Passive;
import model.champions.Chedipe;
import static model.champions.Chedipe.getInstance;
import model.marks.ImpureBindMark;

public class ImpurityBinding extends Passive implements PassiveInterface {

    private final String TYPE = "AUTOATTACK";
    private final String NAME = "ImpurityBinding";
    private final String DESCRIPTION = "Chedipe's auto attack gives an Impurity Binding mark to the target enemy who does not have any Impurity binding mark.\n"
            + "Further when Chedipe damages a target enemy who has an Impurity binding mark, any adjacent enemy with Impurity binding mark\n"
            + "suffers the same amount of damage. (Impurity binding marks don't disappear on its own)";

    @Override
    public void unwind() {
        //target Squares loaded
        Chedipe.getInstance().updateList();
        double dmg = Chedipe.getInstance().getAd().getValue();
        ArrayList<GameSquare> targetList = Chedipe.getInstance().loadTargetList();
        if (championStack.peek() == Chedipe.getInstance()) {
            for (GameSquare sq : targetList) {
                if (sq.isIsChampTargetted()) {
                    if (isChampAlly()) {
                        Champion c = GameController.getInstance().getEnemyFromSquare(sq);
                        if (!Chedipe.getInstance().championHasImpureBind(c)) {
                            c.addMark(new ImpureBindMark());
                            Chedipe.getInstance().getBindedChampList().add(c);
                        } else {
                            runImpurityBindingAnimation();
                            for (Champion ch : Chedipe.getInstance().getBindedChampList()) {
                                if (ch != c) {//need this condition to eliminate double hp updates
                                    GameController.getInstance().updateHpStatsWithDeathChecker(ch, dmg);
                                }
                            }
                        }
                    } else {
                        Champion c = GameController.getInstance().getAllyFromSquare(sq);
                        if (!Chedipe.getInstance().championHasImpureBind(c)) {
                            c.addMark(new ImpureBindMark());
                            Chedipe.getInstance().getBindedChampList().add(c);
                        } else {
                            runImpurityBindingAnimation();
                            for (Champion ch : Chedipe.getInstance().getBindedChampList()) {
                                if (ch != c) {//need this condition to eliminate double hp updates
                                    GameController.getInstance().updateHpStatsWithDeathChecker(ch, dmg);
                                }
                            }
                        }
                    }
                    break;
                }

            }
        }
    }

    private void runImpurityBindingAnimation() {
        //build timeline animation features
        Platform.runLater(() -> {
            Timeline timeline = new Timeline();
            Glow initGlow = new Glow(0.1);
            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(45.0);
            light.setColor(Color.RED);
            Lighting initLighting = new Lighting();
            initLighting.setLight(light);
            initGlow.setInput(initLighting);
            for (Champion champ : Chedipe.getInstance().getBindedChampList()) {
                System.out.println("champ is: " + champ.getName().getValue());
                champ.getGameSquare().setEffect(initGlow);
            }
            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(light.colorProperty(), light.getColor()),
                            new KeyValue(initGlow.levelProperty(), initGlow.getLevel())
                    ),
                    new KeyFrame(Duration.seconds(2),
                            new KeyValue(light.colorProperty(), Color.color(0.58, 0, 0.83, 0.9)),
                            new KeyValue(initGlow.levelProperty(), 0.9)
                    )
            );
            timeline.setAutoReverse(false);
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(ev -> {
                System.out.println("timeline is finished");
                for (Champion champ : Chedipe.getInstance().getBindedChampList()) {
                    champ.getGameSquare().setEffect(null);
                }
            });
        });

    }

    public boolean isChampAlly() {
        return GameController.allyChampList.contains(Chedipe.getInstance());
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
