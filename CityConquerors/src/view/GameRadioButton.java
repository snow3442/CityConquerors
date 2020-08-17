package view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;

public class GameRadioButton extends Pane {

    private RadioButton rb;
    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1, 1, 3);

    public GameRadioButton(String name) {
        Polygon bg = new Polygon(
                0, 0,
                175, 0,
                190, 12,
                175, 24,
                0, 24
        );
        bg.setStroke(Color.WHITE);
        bg.setStrokeWidth(3);
        bg.setStrokeType(StrokeType.OUTSIDE);
        bg.setEffect(new GaussianBlur());
        bg.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.color(0.05, 0.05, 0.05, 1))
                        .otherwise(Color.color(0.1, 0.1, 0.1, 0.8))
        );

        rb = new RadioButton(name);
        rb.setTranslateX(5);
        rb.getStyleClass().add("radiobutton");
        getChildren().addAll(bg, rb);
    }

    public void setOnAction(Runnable action) {
        getRb().selectedProperty().addListener((observable, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                action.run();
            }
        });
    }

    public RadioButton getRb() {
        return rb;
    }

}
