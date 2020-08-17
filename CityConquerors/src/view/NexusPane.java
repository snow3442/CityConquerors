package view;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

public class NexusPane extends Pane {

    public Text txtName;
    private Circle avatar;
    private final double AVATARRADIUS = 7;
    private final Image imgNexus = new Image("images/gameIcons/nexusicon.png");
    private final Text txtDescription = new Text("Responsible for summoning new champions into combat");
    public NexusPane() {
        getStyleClass().add("hoverPane");
        buildAvatar();
        addText();
        addSep();
        addDescription();
    }

    public void addText() {
        txtName = new Text("Nexus");
        txtName.setLayoutX(45);
        txtName.setLayoutY(25);
        txtName.getStyleClass().add("txtChampName");
        getChildren().add(txtName);
    }

    public void buildAvatar() {
        avatar = new Circle(AVATARRADIUS);
        avatar.setStrokeWidth(2);
        avatar.setStrokeLineJoin(StrokeLineJoin.ROUND);
        avatar.setStrokeType(StrokeType.CENTERED);
        avatar.setStroke(Color.CRIMSON);
        avatar.setFill(new ImagePattern(imgNexus));
        avatar.setLayoutX(20);
        avatar.setLayoutY(20);
        getChildren().add(avatar);
    }

    private void addSep() {
        Line line = new Line(20, 40, 130, 40);
        line.setStrokeWidth(2);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(3, Color.GOLDENROD));
        getChildren().add(line);
    }
    
    private void addDescription(){      
        txtDescription.setLayoutX(10);
        txtDescription.setLayoutY(65);
        txtDescription.getStyleClass().add("txtNexusDescription");
        txtDescription.wrappingWidthProperty().bind(widthProperty().subtract(20));
        getChildren().add(txtDescription);
    }
}
