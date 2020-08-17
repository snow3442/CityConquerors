package view;

import javafx.beans.binding.Bindings;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class InGameButton extends Pane {
    private Text text;
    private Effect shadow = new DropShadow(5, Color.GOLD);
    private Effect blur = new BoxBlur(1, 1, 3);
    private final double BUTTONWIDTH = 130;
    private final double BUTTONHEIGHT = 24;

    public InGameButton(String name) {
        Polygon bg = new Polygon(
                0, 0,
                (BUTTONWIDTH-15), 0,
                BUTTONWIDTH, 12,
                BUTTONWIDTH-15, BUTTONHEIGHT,
                0,BUTTONHEIGHT
        );       
        bg.setStroke(Color.WHITE);
        bg.setStrokeWidth(3);
        bg.setStrokeType(StrokeType.OUTSIDE);
        bg.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.color(0.05, 0.05, 0.05, 1))
                        .otherwise(Color.color(0.1, 0.1, 0.1, 0.8))
        );

        text = new Text(name);
        text.setTranslateX(5);
        text.setTranslateY(17);
        text.setFont(Font.font("Algerian", FontWeight.BOLD, 17));
        text.setFill(Color.WHITE);
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );

        getChildren().addAll(bg, text);
        setTranslateX(15);
        setTranslateY(10);
    }
}
