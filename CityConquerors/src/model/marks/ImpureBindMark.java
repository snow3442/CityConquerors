
package model.marks;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Champion;
import model.Mark;
import static model.marks.MarkInterface.IMGHEIGHT;
import static model.marks.MarkInterface.IMGWIDTH;

public class ImpureBindMark extends Mark {
    private IntegerProperty numOfTurn = new SimpleIntegerProperty(-1);
    private final int damage = 0;
    private final String imgUrl = "images/marks/impurebind.png";
    private ImageView ivMark = new ImageView(new Image(imgUrl));
    private final String TYPE = "NEVER";
    private final String NAME = "IMPUREBINDMARK";
    private final String DESCRIPTION = "this champion will suffer the same damage each time any "
            + "champion with an Impure Bind Mark is hit by Chepide";
    
    public ImpureBindMark(){
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
    public void takeEffect(Champion champion) {
        
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
