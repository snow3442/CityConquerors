package view;

import javafx.geometry.HPos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

public class ChampionPane extends Pane {

    private final double HGAP = 10;
    private final double VGAP = 15;
    private final double ElEMENTWIDTH = 15;
    private final double ELEMENTHEIGHT = 15;
    public final ImageView hpIcon = new ImageView(new Image("images/gameIcons/hp.png"));
    public final ImageView bootsIcon = new ImageView(new Image("images/gameIcons/boots.png"));
    public final ImageView apIcon = new ImageView(new Image("images/gameIcons/ap.png"));
    public final ImageView adIcon = new ImageView(new Image("images/gameIcons/ad.png"));
    public final ImageView rangeIcon = new ImageView(new Image("images/gameIcons/range.png"));
    public Text txtName;
    public Text txtAd = new Text("");
    public Text txtAp = new Text("");
    public Text txtRange = new Text("");
    public Text txtmovingDistance = new Text("");
    private GridPane gridPane = new GridPane();
    private final Image imgAssassin = new Image("images/gameIcons/assassin.png");
    private final Image imgTank = new Image("images/gameIcons/tank.png");
    private final Image imgSupport = new Image("images/gameIcons/support.png");
    private final Image imgRanger = new Image("images/gameIcons/ranger.png");
    private final Image imgMage = new Image("images/gameIcons/mage.png");    
    private Circle avatar = new Circle();
    private final double AVATARRADIUS = 7;

    public ChampionPane() {
        buildTitle();
        gridPane.setPrefSize(100, 120);
        getStyleClass().add("hoverPane");
        gridPane.setHgap(HGAP);
        gridPane.setVgap(VGAP);
        //ad icon
        adIcon.setFitWidth(ElEMENTWIDTH);
        adIcon.setFitHeight(ELEMENTHEIGHT);
        gridPane.add(adIcon, 0, 0);
        //ad text
        gridPane.add(txtAd, 1, 0);
        //ap icon
        apIcon.setFitHeight(ELEMENTHEIGHT);
        apIcon.setFitWidth(ElEMENTWIDTH);
        gridPane.add(apIcon, 2, 0);
        //ap text
        gridPane.add(txtAp, 3, 0);
        //boots icon
        bootsIcon.setFitHeight(ELEMENTHEIGHT);
        bootsIcon.setFitWidth(ElEMENTWIDTH);
        gridPane.add(bootsIcon, 0, 1);
        // moving distance text
        gridPane.add(txtmovingDistance, 1, 1);
        rangeIcon.setFitHeight(ELEMENTHEIGHT);
        rangeIcon.setFitWidth(ElEMENTWIDTH);
        gridPane.add(rangeIcon, 2, 1);
        //range text
        gridPane.add(txtRange, 3, 1);

        //add styles
        txtAd.getStyleClass().add("hoverText");
        txtAp.getStyleClass().add("hoverText");
        txtRange.getStyleClass().add("hoverText");
        txtmovingDistance.getStyleClass().add("hoverText");

        //layout constraints
        ColumnConstraints colConstraint1 = new ColumnConstraints(20);
        ColumnConstraints colConstraint2 = new ColumnConstraints(20);
        ColumnConstraints colConstraint3 = new ColumnConstraints(20);
        ColumnConstraints colConstraint4 = new ColumnConstraints(20);
        colConstraint1.setHalignment(HPos.LEFT);
        colConstraint2.setHalignment(HPos.LEFT);
        colConstraint3.setHalignment(HPos.LEFT);
        colConstraint4.setHalignment(HPos.LEFT);
        gridPane.getColumnConstraints().addAll(colConstraint1, colConstraint2, colConstraint3, colConstraint4);
        //global layout
        gridPane.setLayoutX(20);
        gridPane.setLayoutY(50);
        getChildren().add(gridPane);
    }

    private void buildTitle() {
        addName();
        addSep();
    }

    private void addName() {
        txtName=new Text("");
        txtName.setLayoutX(45);
        txtName.setLayoutY(25);
        txtName.getStyleClass().add("txtChampName");
        getChildren().add(txtName);
    }

    public void buildAvatar(String s) {
        if (s.equals("assassin")) {
            avatar = new Circle(AVATARRADIUS);
            avatar.setStrokeWidth(2);
            avatar.setStrokeLineJoin(StrokeLineJoin.ROUND);
            avatar.setStrokeType(StrokeType.CENTERED);
            avatar.setStroke(Color.CRIMSON);
            avatar.setFill(new ImagePattern(imgAssassin));
            avatar.setLayoutX(20);
            avatar.setLayoutY(20);
            getChildren().add(avatar);
        }
        
        else if(s.equals("tank")){
            avatar = new Circle(AVATARRADIUS);
            avatar.setStrokeWidth(2);
            avatar.setStrokeLineJoin(StrokeLineJoin.ROUND);
            avatar.setStrokeType(StrokeType.CENTERED);
            avatar.setStroke(Color.GRAY);
            avatar.setFill(new ImagePattern(imgTank));
            avatar.setLayoutX(20);
            avatar.setLayoutY(20);
            getChildren().add(avatar);
        }
        
        else if(s.equals("support")){
            avatar = new Circle(AVATARRADIUS);
            avatar.setStrokeWidth(2);
            avatar.setStrokeLineJoin(StrokeLineJoin.ROUND);
            avatar.setStrokeType(StrokeType.CENTERED);
            avatar.setStroke(Color.SEAGREEN);
            avatar.setFill(new ImagePattern(imgSupport));
            avatar.setLayoutX(20);
            avatar.setLayoutY(20);
            getChildren().add(avatar);
        }
        
        else if(s.equals("ranger")){
            avatar = new Circle(AVATARRADIUS);
            avatar.setStrokeWidth(2);
            avatar.setStrokeLineJoin(StrokeLineJoin.ROUND);
            avatar.setStrokeType(StrokeType.CENTERED);
            avatar.setStroke(Color.GOLD);
            avatar.setFill(new ImagePattern(imgRanger));
            avatar.setLayoutX(20);
            avatar.setLayoutY(20);
            getChildren().add(avatar);
        }
        
        else if(s.equals("mage")){
            avatar = new Circle(AVATARRADIUS);
            avatar.setStrokeWidth(2);
            avatar.setStrokeLineJoin(StrokeLineJoin.ROUND);
            avatar.setStrokeType(StrokeType.CENTERED);
            avatar.setStroke(Color.DEEPSKYBLUE);
            avatar.setFill(new ImagePattern(imgMage));
            avatar.setLayoutX(20);
            avatar.setLayoutY(20);
            getChildren().add(avatar);
        }
    }

    private void addSep() {
        Line line = new Line(20, 40, 130, 40);
        line.setStrokeWidth(2);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(3, Color.GOLDENROD));
        getChildren().add(line);
    }

}
