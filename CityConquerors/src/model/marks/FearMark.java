
package model.marks;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Champion;
import model.Mark;



public class FearMark extends Mark implements MarkInterface {
    
    private IntegerProperty numOfTurn = new SimpleIntegerProperty(1);
    private final int damage = 0;
    private final String imgUrl = "images/marks/fear.png";
    private ImageView ivMark = new ImageView(new Image(imgUrl));
    private final String TYPE = "POST";
    private final String NAME = "Fear Mark";
    private final String DESCRIPTION = "At next turn, this champion will not be able to attack nor"
            + "cast abilities";
    public FearMark(){
        ivMark.setFitHeight(IMGHEIGHT);
        ivMark.setFitWidth(IMGWIDTH);
    }

    @Override
    public ImageView getIvMark() {
        return  ivMark;
    }

    @Override
    public void setIvMark(ImageView ivMark) {
        this.ivMark = ivMark;
    }


    @Override
    public void takeEffect(Champion champion) {
        champion.getAutoAttacked().setValue(true);
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

    @Override
    public IntegerProperty getNumOfTurn() {
       return numOfTurn;
    }
    
}
