package model.marks;

import controller.GameController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Champion;
import model.Mark;
import model.champions.Wukong;
import static model.marks.MarkInterface.IMGHEIGHT;
import static model.marks.MarkInterface.IMGWIDTH;

public class TricksterMark extends Mark implements MarkInterface {

    private IntegerProperty numOfTurn = new SimpleIntegerProperty(1);
    private final String imgUrl = "images/marks/trickster.png";
    private ImageView ivMark = new ImageView(new Image(imgUrl));
    private final String TYPE = "PRE";
    private final String NAME = "Trickster Mark";
    private final String DESCRIPTION = "When Wukong gets hit, trickster mark takes"
            + " the damage for Wukong and disappears.";

    public TricksterMark() {
        ivMark.setFitHeight(IMGHEIGHT);
        ivMark.setFitWidth(IMGWIDTH);
        //add Trickster listener 
        Wukong.getInstance().getTricksterExist().addListener(
                (observable, oldvalue, newvalue) -> {
                    if(!newvalue){
                        Wukong.getInstance().getMarkList().remove(this);
                        Wukong.getInstance().getMarkBox().getChildren().remove(getIvMark());
                    }
                }
        );
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
        //mark cleared by turn
        getNumOfTurn().setValue(1);
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
