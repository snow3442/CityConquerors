package view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class PassivePane extends Pane {

    private static PassivePane instance = null;
    private final Label LBLPASSIVE = new Label("PASSIVE");
    private Label lblName = new Label("morgada kaveto");
    private FlowPane descriptionPane;
    private Text txtDescription = new Text("Lorem ipsum, shals Shall never bitter chuim"
            + "sein parapita yibish frogada che sum");
    private final double WIDTH = 240;
    private final double HEIGHT = 225;

    private Polygon bgBack = new Polygon(
            0, 15,
            15, 0,
            15, 30
    );

    private PassivePane() {
        setPrefSize(WIDTH, HEIGHT);
        buildTitle();
        buildDescriptionArea();
    }

    public static synchronized PassivePane getInstance() {
        if (instance == null) {
            instance = new PassivePane();
        }

        return instance;
    }
    
    private void buildTitle() {
        getStyleClass().add("passivePane");
        buildAvatar();
        addPassiveLabel();
        addPassiveName();
        buildBackButton();
        addSep();

    }

    private void buildAvatar() {
        ClickableAvatar icon = new ClickableAvatar("PASSIVE");
        icon.setRadius(10);
        icon.layoutXProperty().bind(layoutXProperty().add(30));
        icon.layoutYProperty().bind(layoutYProperty().add(20));
        getChildren().add(icon);
    }

    private void addSep() {
        Line line = new Line();
        line.startXProperty().bind(layoutXProperty().add(20));
        line.startYProperty().bind(layoutYProperty().add(50));
        line.endXProperty().bind(layoutXProperty().add(WIDTH).subtract(20));
        line.endYProperty().bind(line.startYProperty());
        line.setStrokeWidth(2);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(3, Color.GOLDENROD));
        getChildren().add(line);
    }

    private void addPassiveLabel() {
        LBLPASSIVE.getStyleClass().add("lblType");
        LBLPASSIVE.layoutXProperty().bind(layoutXProperty().add(65));
        LBLPASSIVE.layoutYProperty().bind(layoutYProperty().add(10));
        getChildren().add(LBLPASSIVE);
    }

    private void addPassiveName() {
        lblName.getStyleClass().add("lblName");
        lblName.layoutXProperty().bind(layoutXProperty().add(85));
        lblName.layoutYProperty().bind(layoutYProperty().add(25));
        getChildren().add(lblName);
    }

    private void buildDescriptionArea() {
        txtDescription.getStyleClass().add("descriptionText");
        txtDescription.layoutXProperty().bind(layoutXProperty().add(20));
        txtDescription.layoutYProperty().bind(layoutYProperty().add(70));
        txtDescription.wrappingWidthProperty().bind(widthProperty().subtract(25));
        getChildren().add(txtDescription);
    }

    private void buildBackButton() {

        bgBack.setStroke(Color.WHITE);
        bgBack.setEffect(new GaussianBlur());
        bgBack.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.color(1, 1, 1, 0.9))
                        .otherwise(Color.GOLD)
        );
        bgBack.layoutXProperty().bind(layoutXProperty().add(WIDTH).subtract(20));
        bgBack.layoutYProperty().bind(layoutYProperty().add(10));
        getChildren().add(bgBack);
    }

    public Label getLblName() {
        return lblName;
    }

    public void setLblName(Label lblName) {
        this.lblName = lblName;
    }

    public Text getTxtDescription() {
        return txtDescription;
    }

    public Polygon getBgBack() {
        return bgBack;
    }
}
