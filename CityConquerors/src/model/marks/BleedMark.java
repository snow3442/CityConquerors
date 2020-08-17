package model.marks;

import controller.GameController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Champion;
import model.Mark;

public class BleedMark extends Mark implements MarkInterface {

    private IntegerProperty numOfTurn = new SimpleIntegerProperty(2);
    private final int damage = 5;
    private final String imgUrl = "images/marks/bleed.png";
    private ImageView ivMark = new ImageView(new Image(imgUrl));
    private final String TYPE = "PRE";
    private final String NAME = "Bleed Mark";
    private final String DESCRIPTION = "At next turn, this champion will suffer 5hp damage";

    public BleedMark() {
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
        GameController.getInstance().updateHpStatsWithDeathChecker(champion, damage);
        //if markList does not contain the mark, it won't call takeEffect()
        if (getNumOfTurn().getValue() > 1) {
            getNumOfTurn().setValue(getNumOfTurn().getValue() - 1);
        }
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
