package view;

import javafx.animation.TranslateTransition;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import model.Champion;
import static view.ClickableAvatar.RADIUS;
import static view.GameView.CHAMPCHATSPLITRATIO;
import static view.GameView.LEFTPANEHEIGHT;
import static view.GameView.LEFTPANEWIDTH;

public class ChampionInfoPane extends AnchorPane {

    private static ChampionInfoPane instance = null;
    private Pane titlePane = new Pane();
    private StackPane containerPane;
    private static final double WIDTH = LEFTPANEWIDTH;
    private static final double HEIGHT = LEFTPANEHEIGHT * CHAMPCHATSPLITRATIO;
    private static final double TITLESPLITRATIO = 0.3;
    private ImageView ChampIv = new ImageView(new Image("images/ingame/hero.jpg"));
    private Image imgUnknown = new Image("images/gameIcons/unknown.png");
    private final Image imgAssassin = new Image("images/gameIcons/assassin.png");
    private final Image imgTank = new Image("images/gameIcons/tank.png");
    private final Image imgSupport = new Image("images/gameIcons/support.png");
    private final Image imgRanger = new Image("images/gameIcons/ranger.png");
    private final Image imgMage = new Image("images/gameIcons/mage.png");
    private Label lblType;
    private Label lblName;
    private ClickableAvatar passiveAvatar = new ClickableAvatar("PASSIVE");
    private ClickableAvatar abilityAvatar = new ClickableAvatar("ABILITY");
    private AbilityPane APane = AbilityPane.getInstance();
    private PassivePane PPane = PassivePane.getInstance();

    private ChampionInfoPane() {
        getStyleClass().add("championInfoPane");
        buildTitlePane();
        buildContainerPane();
    }

    public static synchronized ChampionInfoPane getInstance() {
        if (instance == null) {
            instance = new ChampionInfoPane();
        }

        return instance;
    }

    private void buildTitlePane() {
        titlePane.setPrefSize(WIDTH, HEIGHT * TITLESPLITRATIO);
        titlePane.setLayoutX(0);
        titlePane.setLayoutY(0);
        AnchorPane.setTopAnchor(titlePane, 0.0);
        buildAvatar("unknown");
        lblType = new Label("City God");
        lblType.setLayoutX(65);
        lblType.setLayoutY(HEIGHT * TITLESPLITRATIO * 0.5 - 30);
        lblType.getStyleClass().add("lblType");
        //Labels 
        lblName = new Label("Thyrokus");
        lblName.setLayoutX(85);
        lblName.setLayoutY(HEIGHT * TITLESPLITRATIO * 0.5 - 18);
        lblName.getStyleClass().add("lblName");
        addSep();
        titlePane.getChildren().addAll(lblType, lblName);
        getChildren().addAll(titlePane);
    }

    /**
     * load the avatar according to champion type: assassin, tank, support, mage
     * or ranger
     *
     * @param type
     * @return
     */
    private void buildAvatar(String type) {
        if (type.equals("unknown")) {
            Circle circle = new Circle(10);
            circle.setStrokeWidth(2);
            circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.BLACK);
            circle.setFill(new ImagePattern(imgUnknown));
            circle.setLayoutX(30);
            circle.setLayoutY(HEIGHT * TITLESPLITRATIO * 0.5 - 15);
            titlePane.getChildren().add(circle);
        } else if (type.equals("assassin")) {
            Circle circle = new Circle(10);
            circle.setStrokeWidth(2);
            circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.CRIMSON);
            circle.setFill(new ImagePattern(imgAssassin));
            circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            circle.setLayoutX(30);
            circle.setLayoutY(HEIGHT * TITLESPLITRATIO * 0.5 - 15);
            titlePane.getChildren().add(circle);
        } else if (type.equals("tank")) {
            Circle circle = new Circle(10);
            circle.setStrokeWidth(2);
            circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.GRAY);
            circle.setFill(new ImagePattern(imgTank));
            circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            circle.setLayoutX(30);
            circle.setLayoutY(HEIGHT * TITLESPLITRATIO * 0.5 - 15);
            titlePane.getChildren().add(circle);
        } else if (type.equals("support")) {
            Circle circle = new Circle(10);
            circle.setStrokeWidth(2);
            circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.SEAGREEN);
            circle.setFill(new ImagePattern(imgSupport));
            circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            circle.setLayoutX(30);
            circle.setLayoutY(HEIGHT * TITLESPLITRATIO * 0.5 - 15);
            titlePane.getChildren().add(circle);
        } else if (type.equals("ranger")) {
            Circle circle = new Circle(10);
            circle.setStrokeWidth(2);
            circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.GOLD);
            circle.setFill(new ImagePattern(imgRanger));
            circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            circle.setLayoutX(30);
            circle.setLayoutY(HEIGHT * TITLESPLITRATIO * 0.5 - 15);
            titlePane.getChildren().add(circle);
        } else if (type.equals("mage")) {
            Circle circle = new Circle(10);
            circle.setStrokeWidth(2);
            circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.DEEPSKYBLUE);
            circle.setFill(new ImagePattern(imgMage));
            circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            circle.setLayoutX(30);
            circle.setLayoutY(HEIGHT * TITLESPLITRATIO * 0.5 - 15);
            titlePane.getChildren().add(circle);
        }

    }

    private void addSep() {
        Line line = new Line(20, HEIGHT * TITLESPLITRATIO * 0.5 + 8, WIDTH - 20, HEIGHT * TITLESPLITRATIO * 0.5 + 8);
        line.setStrokeWidth(2);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(3, Color.GOLDENROD));
        titlePane.getChildren().add(line);
    }

    private void buildContainerPane() {
        containerPane = new StackPane();
        containerPane.setPrefSize(WIDTH, HEIGHT * (1 - TITLESPLITRATIO));
        containerPane.setLayoutX(0);
        containerPane.setLayoutY(HEIGHT * TITLESPLITRATIO);
        AnchorPane.setTopAnchor(containerPane, HEIGHT * TITLESPLITRATIO);
        addChampionImage();
        addPassiveAbilityAvatars();
        addAbilityPane();
        addPassivePane();
        getChildren().add(containerPane);
    }

    private void addChampionImage() {
        ChampIv.setFitWidth(WIDTH);
        ChampIv.setFitHeight(HEIGHT * (1 - TITLESPLITRATIO));
        ChampIv.setLayoutX(0);
        ChampIv.setLayoutY(HEIGHT * TITLESPLITRATIO);
        containerPane.getChildren().add(ChampIv);
    }

    private void addAbilityPane() {
        containerPane.getChildren().add(APane);
        APane.setLayoutX(RADIUS);
        APane.setLayoutY(HEIGHT * TITLESPLITRATIO + RADIUS);
        APane.setTranslateX(-250);

    }

    private void addPassivePane() {
        containerPane.getChildren().add(PPane);
        PPane.setLayoutX(RADIUS);
        PPane.setLayoutY(HEIGHT * TITLESPLITRATIO + RADIUS);
        PPane.setTranslateX(-250);
    }

    private void addPassiveAbilityAvatars() {
        passiveAvatar.setLayoutX(WIDTH * 1 / 5 - RADIUS - 2);
        passiveAvatar.setLayoutY(0);
        abilityAvatar.setLayoutX(WIDTH * 1 / 5 + RADIUS + 2);
        abilityAvatar.setLayoutY(0);
        containerPane.getChildren().addAll(passiveAvatar, abilityAvatar);
        //wireup avatars
        abilityAvatar.setOnMouseClicked(e -> {
            abilityPaneSlideInAnimation();
        });

        APane.getBgBack().setOnMouseClicked(e -> {
            abilityPaneSlideBackAnimation();
        });

        passiveAvatar.setOnMouseClicked(e -> {
            passivePaneSlideInAnimation();
        });

        PPane.getBgBack().setOnMouseClicked(e -> {
            passivePaneSlideBackAnimation();
        });

    }

    private void abilityPaneSlideInAnimation() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), APane);
        tt.setToX(0);
        tt.play();
        APane.setCache(true);
        APane.setCacheHint(CacheHint.SPEED);
    }

    private void abilityPaneSlideBackAnimation() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), APane);
        tt.setToX(-250);
        tt.play();
        APane.setCache(true);
        APane.setCacheHint(CacheHint.SPEED);
    }

    private void passivePaneSlideInAnimation() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), PPane);
        tt.setToX(0);
        tt.play();
        PPane.setCache(true);
        PPane.setCacheHint(CacheHint.SPEED);
    }

    private void passivePaneSlideBackAnimation() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), PPane);
        tt.setToX(-250);
        tt.play();
        PPane.setCache(true);
        PPane.setCacheHint(CacheHint.SPEED);
    }

    public void loadSpecificChampionPane(Champion champion) {
        buildAvatar(champion.getType());
        getInstance().getChampIv().setImage(new Image(champion.getImgUrl().getValue()));
        getInstance().getLblType().setText(champion.getType());
        getInstance().getLblName().setText(champion.getName().getValue());
        AbilityPane.getInstance().getLblName().setText(champion.getAbility().getName());
        AbilityPane.getInstance().getTxtDescription().setText(champion.getAbilityDescription());
        PassivePane.getInstance().getLblName().setText(champion.getPassive().getName());
        PassivePane.getInstance().getTxtDescription().setText(champion.getPassiveDescription());
    }

    public Label getLblName() {
        return lblName;
    }

    public void setLblName(Label lblName) {
        this.lblName = lblName;
    }

    public ImageView getChampIv() {
        return ChampIv;
    }

    public void setChampIv(ImageView ChampIv) {
        this.ChampIv = ChampIv;
    }

    public Label getLblType() {
        return lblType;
    }

    public void setLblType(Label lblType) {
        this.lblType = lblType;
    }

}
