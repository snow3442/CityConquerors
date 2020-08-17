package model.passives;

import controller.GameController;
import static controller.GameController.allyTurn;
import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import model.GameSquare;
import model.Passive;
import model.champions.Houyi;
import static view.GameView.gameGrid;

public class AimedFocus extends Passive implements PassiveInterface {

    private final String TYPE = "RANGE";
    private final String NAME = "Aimed Focus";
    private final String DESCRIPTION = "Houyi increases his range by 1, if he fires horizontally or vertically";
    private ArrayList<GameSquare> targetList = new ArrayList<>();

    @Override
    public void unwind() {
        int row = GridPane.getRowIndex(Houyi.getInstance().getGameSquare());
        int col = GridPane.getColumnIndex(Houyi.getInstance().getGameSquare());
        if (passiveHelper.isChampAlly(Houyi.getInstance())&&allyTurn) {
            for (Integer[] ints : gridLogicController.getAllReachableWithExtraRange(Houyi.getInstance().getRange().getValue(),
                    1, row, col)) {
                GameSquare sq = (GameSquare) GameController.getNodeByRowColumnIndex(ints[0], ints[1], gameGrid);
                if (GameController.getInstance().isEnemySquare(sq)) {
                    targetList.add(sq);
                    GameController.getInstance().wireUpEffectForOneSquare(sq, Houyi.getInstance());
                }
            }

            Houyi.getInstance().getGameContextMenu().getCANCELITEM().setOnAction(e -> {
                for (GameSquare gs : targetList) {
                    GameController.getInstance().inactivateChamp(gs);
                }
            });
        } else if(!passiveHelper.isChampAlly(Houyi.getInstance())&&!allyTurn) {
            for (Integer[] ints : gridLogicController.getAllReachableWithExtraRange(Houyi.getInstance().getRange().getValue(),
                    1, row, col)) {
                GameSquare sq = (GameSquare) GameController.getNodeByRowColumnIndex(ints[0], ints[1], gameGrid);
                if (GameController.getInstance().isAllySquare(sq)) {
                    targetList.add(sq);
                    GameController.getInstance().wireUpEffectForOneSquare(sq, Houyi.getInstance());
                }
            }
            Houyi.getInstance().getGameContextMenu().getCANCELITEM().setOnAction(e -> {
                for (GameSquare gs : targetList) {
                    GameController.getInstance().inactivateChamp(gs);
                }
            });
        }
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
