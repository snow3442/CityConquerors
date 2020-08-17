package view;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class ClickableAvatar extends Circle {

    private final Image PASSIVEICON = new Image("images/gameIcons/passive.jpg");
    private final Image ABILITYICON = new Image("images/gameIcons/ability.jpg");
    protected static final double RADIUS = 15.0;

    public ClickableAvatar(String type) {
        setManaged(false);
        setRadius(RADIUS);
        if (type.equals("ABILITY")) {
            setStrokeWidth(3);
            setStrokeLineJoin(StrokeLineJoin.ROUND);
            setStrokeType(StrokeType.CENTERED);
            setStroke(Color.CORAL);
            setFill(new ImagePattern(ABILITYICON));
            setEffect(new DropShadow(+25d, 0d, +2d, Color.CRIMSON));
        }
        else if(type.equals("PASSIVE")){
            setStrokeWidth(3);
            setStrokeLineJoin(StrokeLineJoin.ROUND);
            setStrokeType(StrokeType.CENTERED);
            setStroke(Color.DEEPSKYBLUE);
            setFill(new ImagePattern(PASSIVEICON));
            setEffect(new DropShadow(+25d, 0d, +2d, Color.AZURE));
        }
    }
    

}
