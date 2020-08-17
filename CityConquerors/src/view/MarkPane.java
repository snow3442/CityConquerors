package view;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class MarkPane extends Pane {
    private Label lblMarkName = new Label(" Lorem Ipsum ");
    private Label lblTurnsLeft = new Label("(0)");
    private Text txtDescription = new Text("Thy shal ethey dio shranka heimsem du mata blinco");

    public MarkPane() {
        setPrefSize(120, 140);
        getStyleClass().add("markpane");
        buildPane();
        addSep();
    }

    private void buildPane() {
        lblMarkName.getStyleClass().add("markLabel");
        lblTurnsLeft.getStyleClass().add("markLabel");
        txtDescription.getStyleClass().add("markText");
        lblMarkName.setLayoutX(15);
        lblMarkName.setLayoutY(5);
        lblTurnsLeft.setLayoutX(130);
        lblTurnsLeft.setLayoutY(5);
        txtDescription.setLayoutX(10);
        txtDescription.setLayoutY(45);
        txtDescription.wrappingWidthProperty().bind(widthProperty().subtract(20));
        getChildren().addAll(lblMarkName, lblTurnsLeft, txtDescription);
    }

    private void addSep() {
        Line line = new Line(20, 30, 130, 30);
        line.setStrokeWidth(2);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(3, Color.GOLDENROD));
        getChildren().add(line);
    }

    public Label getLblMarkName() {
        return lblMarkName;
    }

    public Label getLblTurnsLeft() {
        return lblTurnsLeft;
    }

    public Text getTxtDescription() {
        return txtDescription;
    }

}
