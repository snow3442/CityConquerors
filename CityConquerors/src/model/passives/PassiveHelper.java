package model.passives;

import controller.GameController;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Champion;
import static view.GameView.centerPane;

public class PassiveHelper implements PassiveInterface {

    private final double ANIMTIME = 3000;

    public PassiveHelper() {
    }

    /**
     * returns the distance between 2 specific champions
     *
     * @param champ1
     * @param champ2
     * @return
     */
    public int checkTwoChampionsDistanceWithOutCollision(Champion champ1, Champion champ2) {
        int row1 = GridPane.getRowIndex(champ1.getGameSquare());
        int col1 = GridPane.getColumnIndex(champ1.getGameSquare());
        int row2 = GridPane.getRowIndex(champ2.getGameSquare());
        int col2 = GridPane.getColumnIndex(champ2.getGameSquare());
        return absDiff(row1, row2) + absDiff(col1, col2);
    }

    /**
     * return a list of coordinates of all ally champion blockade squares
     *
     * @param attacker
     * @return
     */
    public ArrayList<Integer[]> getAllyBlockadesCoords(Champion attacker) {
        ArrayList<Integer[]> results = new ArrayList<>();
        int range = attacker.getRange().getValue();
        int row = GridPane.getRowIndex(attacker.getGameSquare());
        int col = GridPane.getColumnIndex(attacker.getGameSquare());

        if (isChampAlly(attacker)) {
            for (Champion ch : GameController.allyChampList) {
                int i = GridPane.getRowIndex(ch.getGameSquare());
                int j = GridPane.getColumnIndex(ch.getGameSquare());
                if ((i == row && absDiff(j, col) < range && absDiff(j, col) > 0)
                        || (j == col && absDiff(i, row) < range && absDiff(i, row) > 0)) {
                    results.add(new Integer[]{i, j});
                }
            }

        } else {
            for (Champion ch : GameController.enemyChampList) {
                int i = GridPane.getRowIndex(ch.getGameSquare());
                int j = GridPane.getColumnIndex(ch.getGameSquare());
                if ((i == row && absDiff(j, col) < range && absDiff(j, col) > 0)
                        || (j == col && absDiff(i, row) < range && absDiff(i, row) > 0)) {
                    results.add(new Integer[]{i, j});
                }
            }
        }
        return results;
    }
    
     public void runPassiveCastAnimation(Champion champion) {
        Timeline passiveTimeline = new Timeline();
        double initX = champion.getGameSquare().getLayoutX() - 100;
        double initY = champion.getGameSquare().getLayoutY() + 10;
        double finalX = champion.getGameSquare().getLayoutX() + 50;
        Text passivetext = new Text(champion.getPassive().getName());
        passivetext.setFont(Font.font("Algerian", 40));
        if (isChampAlly(champion)) {
            passivetext.setFill(Color.DARKSEAGREEN);
        } else {
            passivetext.setFill(Color.CRIMSON);
        }
        passivetext.setManaged(false);
        passivetext.setLayoutX(initX);
        passivetext.setLayoutY(initY);
        centerPane.getChildren().add(passivetext);
        passiveTimeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(passivetext.layoutXProperty(), passivetext.getLayoutX()),
                        new KeyValue(passivetext.layoutYProperty(), passivetext.getLayoutY())
                ),
                new KeyFrame(Duration.millis(ANIMTIME),
                        new KeyValue(passivetext.layoutXProperty(), finalX),
                        new KeyValue(passivetext.layoutYProperty(), passivetext.getLayoutY())
                )
        );
        passiveTimeline.setAutoReverse(false);
        passiveTimeline.setCycleCount(1);
        Platform.runLater(() -> {
            passiveTimeline.play();
            passiveTimeline.setOnFinished(e -> {
                centerPane.getChildren().remove(passivetext);
            });
        });
    }

    protected boolean isChampAlly(Champion champion) {
        return GameController.allyChampList.contains(champion);
    }

    /**
     * return absolute difference between i and j
     *
     * @param i
     * @param j
     * @return
     */
    public int absDiff(int i, int j) {
        if (i > j) {
            return i - j;
        } else if (i < j) {
            return j - i;
        } else {
            return 0;
        }
    }

    /**
     * returns the minimum of i and j
     *
     * @param i
     * @param j
     * @return
     */
    public int min(int i, int j) {
        if (i < j) {
            return i;
        } else if (i > j) {
            return j;
        } else {
            return i;
        }
    }

    /**
     * returns the maximum of i and j
     *
     * @param i
     * @param j
     * @return
     */
    public int max(int i, int j) {
        if (i < j) {
            return j;
        } else if (i > j) {
            return i;
        } else {
            return i;
        }
    }
    
}
