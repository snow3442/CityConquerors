package view;

import controller.GameController;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class GameView {

    public static Stage gameStage;
    private Scene gameScene;
    private GameController gameController = GameController.getInstance();
    private ScrollPane gameScrollPane;
    public static AnchorPane gamePane;
    private HBox topPane;
    private VBox leftPane;
    private VBox rightPane;
    private ArrayList<InGameButton> rightPaneButtonList = new ArrayList<>();
    public static StackPane centerPane = new StackPane();
    public static GridPane gameGrid = new GridPane();
    private static final double WIDTH = 1200;
    private static final double HEIGHT = 680;
    private static final double TOPHEIGHTRATIO = 0.22;
    private static final double LEFTWIDTHRATIO = 0.2;
    private static final double RIGHTWIDTHRATIO = 0.15;
    private final double horizontalFenceRatio = 0.002;
    private final double verticalFenceRatio = 0.0;
    private final String BACKGROUNDURL = "images/background/GameBackGround.png";
    protected static final double LEFTPANEHEIGHT = HEIGHT * (1 - TOPHEIGHTRATIO);
    protected static final double LEFTPANEWIDTH = WIDTH * LEFTWIDTHRATIO;
    protected static final double CHAMPCHATSPLITRATIO = 0.65;
    private TextField userMessageField = new TextField();
    private TextArea allTextArea = new TextArea();
    private final String HOVER = "ingame/buttonhover.wav";
    private final String CLICK = "ingame/buttonclick.wav";

    public GameView() {
        //Create main pane
        gamePane = new AnchorPane();
        //build All Components
        addBackground();
        initLeftCorner();
        initRightCorner();
        initTopPane();
        initLeftPane();
        initRightPane();
        initGameGrid();
        loadGameInits();
        //scene configs      
        gameScene = new Scene(gamePane);
        gameScene.getStylesheets().add("css/game.css");
        gameStage = new Stage();
        gameStage.setScene(gameScene);
        //gameStage.initStyle(StageStyle.TRANSPARENT);
        gameStage.setOnCloseRequest(e->{
            GameController.getInstance().clear();
        });

    }

    private void addBackground() {
        ImageView iv = new ImageView(new Image(BACKGROUNDURL));
        iv.setFitWidth(WIDTH);
        iv.setFitHeight(HEIGHT);
        gamePane.getChildren().add(iv);
    }

    private void loadGameInits() {
        gameController.populateAllyChampsList();
        gameController.populateEnemyChampsList();
        gameController.populateGameBoard();
        gameController.loadChampions();
        gameController.loadPassives();
        gameController.wireUpChampHoverAndHpBar();
        gameController.loadNexus();
    }

    public void initTopPane() {
        topPane = new HBox(13);
        topPane.setPrefSize(WIDTH * (1 - LEFTWIDTHRATIO - RIGHTWIDTHRATIO), HEIGHT * TOPHEIGHTRATIO);
        topPane.setLayoutX(WIDTH * LEFTWIDTHRATIO);
        topPane.setLayoutY(0);
        AnchorPane.setTopAnchor(topPane, 0.0);
        gamePane.getChildren().add(topPane);
    }

    public void initLeftPane() {
        leftPane = new VBox();
        leftPane.getStyleClass().add("leftPane");
        leftPane.setPrefSize(WIDTH * LEFTWIDTHRATIO, HEIGHT * (1 - TOPHEIGHTRATIO));
        leftPane.setLayoutX(0);
        leftPane.setLayoutY(HEIGHT * TOPHEIGHTRATIO);
        ChampionInfoPane cp = ChampionInfoPane.getInstance();
        cp.prefWidthProperty().bind(leftPane.widthProperty());
        cp.prefHeightProperty().bind(leftPane.heightProperty().multiply(CHAMPCHATSPLITRATIO));
        //chat scroll
        allTextArea.getStyleClass().add("text-area");
        allTextArea.setWrapText(true);
        allTextArea.setText("Chat Room: ");
        allTextArea.setEditable(false);
        allTextArea.prefHeightProperty().bind(leftPane.heightProperty().multiply(1 - CHAMPCHATSPLITRATIO).multiply(0.95));
        allTextArea.prefWidthProperty().bind(leftPane.widthProperty());
        userMessageField.prefWidthProperty().bind(leftPane.widthProperty());
        userMessageField.prefHeightProperty().bind(leftPane.heightProperty().multiply(1 - CHAMPCHATSPLITRATIO)
                .multiply(0.05));
        userMessageField.getStyleClass().add("tfuserMessage");

        //end of chat
        leftPane.getChildren().addAll(cp, allTextArea, userMessageField);
        AnchorPane.setLeftAnchor(leftPane, 0.0);
        gamePane.getChildren().add(leftPane);
    }

    private void initLeftCorner() {
        Circle leftUserIcon = new Circle(20);
        leftUserIcon.setStrokeWidth(2);
        leftUserIcon.setStrokeLineJoin(StrokeLineJoin.ROUND);
        leftUserIcon.setStrokeType(StrokeType.CENTERED);
        leftUserIcon.setStroke(Color.GREEN);
        leftUserIcon.setFill(new ImagePattern(new Image("images/userIcons/sample1.jpg")));
        leftUserIcon.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        leftUserIcon.setLayoutX(40);
        leftUserIcon.setLayoutY(30);
        Label lblLeftUserName = new Label("Alex089377");
        lblLeftUserName.getStyleClass().add("lblLeftUserName");
        Label lblLeftUserRank = new Label("Beginner (897)");
        lblLeftUserRank.getStyleClass().add("lblUserRank");
        lblLeftUserName.setLayoutX(75);
        lblLeftUserName.setLayoutY(15);
        lblLeftUserRank.setLayoutX(95);
        lblLeftUserRank.setLayoutY(35);
        //add line 
        Line line = new Line(30, 65, 230, 65);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(3, Color.GOLDENROD));
        gamePane.getChildren().addAll(leftUserIcon, lblLeftUserName, lblLeftUserRank, line);
    }

    private void initRightCorner() {
        double shiftWidth = WIDTH * 0.77;
        Circle rightUserIcon = new Circle(20);
        rightUserIcon.setStrokeWidth(2);
        rightUserIcon.setStrokeLineJoin(StrokeLineJoin.ROUND);
        rightUserIcon.setStrokeType(StrokeType.CENTERED);
        rightUserIcon.setStroke(Color.CRIMSON);
        rightUserIcon.setFill(new ImagePattern(new Image("images/userIcons/sample2.jpg")));
        rightUserIcon.setEffect(new DropShadow(+25d, 0d, +2d, Color.RED));
        rightUserIcon.setLayoutX(40 + shiftWidth);
        rightUserIcon.setLayoutY(30);
        Label lblRightUserName = new Label("snowlover");
        lblRightUserName.getStyleClass().add("lblRightUserName");
        Label lblRightUserRank = new Label("Intermediate (1082)");
        lblRightUserRank.getStyleClass().add("lblUserRank");
        lblRightUserName.setLayoutX(75 + shiftWidth);
        lblRightUserName.setLayoutY(15);
        lblRightUserRank.setLayoutX(95 + shiftWidth);
        lblRightUserRank.setLayoutY(35);
        //add line 
        Line line = new Line(30 + shiftWidth, 65, 230 + shiftWidth, 65);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(3, Color.GOLDENROD));
        gamePane.getChildren().addAll(rightUserIcon, lblRightUserName, lblRightUserRank, line);
    }

    public void initRightPane() {
        rightPane = new VBox(25);
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefSize(WIDTH * RIGHTWIDTHRATIO, HEIGHT * (1 - TOPHEIGHTRATIO));
        rightPane.setLayoutX(WIDTH * (1 - RIGHTWIDTHRATIO));
        rightPane.setLayoutY(HEIGHT * TOPHEIGHTRATIO);
        InGameButton btEndTurn = new InGameButton("End Turn");
        InGameButton btResign = new InGameButton("Resign");
        InGameButton btSettings = new InGameButton("Settings");
        InGameButton btSummon = new InGameButton("Summon");
        InGameButton btOffItem = new InGameButton("Offensives");
        InGameButton btdefItem = new InGameButton("Defensives");
        InGameButton btExit = new InGameButton("Exit Game");
        rightPane.getStyleClass().add("rightPane");
        rightPane.setPadding(new Insets(10, 0, 10, 0));
        rightPaneButtonList.add(btEndTurn);
        rightPaneButtonList.add(btResign);
        rightPaneButtonList.add(btSettings);
        rightPaneButtonList.add(btSummon);
        rightPaneButtonList.add(btOffItem);
        rightPaneButtonList.add(btdefItem);
        rightPaneButtonList.add(btExit);
        for (InGameButton bt : rightPaneButtonList) {
            rightPane.getChildren().add(bt);
        }
        /*
        Gauge player1Gauge = createGaugeTimer(60);
        Gauge player2Gauge = createGaugeTimer(60);
        rightPane.getChildren().addAll(player1Gauge, player2Gauge);
         */

        //DO NOT ERASE THE NEXTLINE AS IT ALLOWS UNDO FUNCTIONALITY
        //CURRENTLY WE DO NOT ALLOW UNDO FUNCTIIONLITY IN GAME
        btSettings.setOnMouseClicked(e -> {
            try {
                GameController.getInstance().loadGameSound(CLICK, 0);
            } catch (MalformedURLException ex) {
                Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
            }
            SettingsView settingsView = new SettingsView();
        });

        btResign.setOnMouseClicked(e -> {
            try {
                GameController.getInstance().loadGameSound(CLICK, 0);
            } catch (MalformedURLException ex) {
                Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        /*
        btSummon.setOnMouseClicked(e -> {
            runGaugeTimer(player1Gauge);
        });
         */
        AnchorPane.setRightAnchor(rightPane, 0.0);
        gamePane.getChildren().add(rightPane);

        wireUpEndTurnButton(btEndTurn);
    }

    private void initGameGrid() {
        centerPane.setPrefSize(WIDTH * (1 - LEFTWIDTHRATIO - RIGHTWIDTHRATIO - 2 * horizontalFenceRatio),
                HEIGHT * (1 - TOPHEIGHTRATIO));
        centerPane.setStyle("-fx-background-color: black");
        centerPane.setLayoutX(WIDTH * (LEFTWIDTHRATIO + horizontalFenceRatio));
        centerPane.setLayoutY(HEIGHT * (TOPHEIGHTRATIO + verticalFenceRatio));
        gameGrid.prefWidthProperty().bind(centerPane.widthProperty());
        gameGrid.prefHeightProperty().bind(centerPane.heightProperty());
        centerPane.getChildren().add(gameGrid);
        gameGrid.setHgap(2);
        gameGrid.setVgap(2);
        gameGrid.setAlignment(Pos.CENTER);
        gamePane.getChildren().add(centerPane);
    }

    private void wireUpEndTurnButton(InGameButton btEndTurn) {
        btEndTurn.setOnMouseClicked(e -> {
            try {
                GameController.getInstance().loadGameSound(CLICK, 0);
            } catch (MalformedURLException ex) {
                Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
            }
            GameController.getInstance().endTurn();

        });
    }
}
