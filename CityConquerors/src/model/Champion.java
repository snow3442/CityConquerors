package model;

import static controller.GameController.turnCounterQueue;
import static controller.GameController.turnNumber;
import controller.GameControllerInterface;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import view.ChampionPane;
import view.GameContextMenu;
import static view.GameView.centerPane;
import view.HpBar;
import view.MovementContextMenu;

public abstract class Champion {

    private StringProperty name;
    private String type;
    private DoubleProperty maxHp;
    private DoubleProperty hp;
    private IntegerProperty ad;
    private IntegerProperty ap;
    private IntegerProperty movingDistance;
    private IntegerProperty range;
    private Ability ability;
    private BooleanProperty isAlive;
    private BooleanProperty usedAbility;
    private BooleanProperty autoAttacked;
    private BooleanProperty canMove;
    private BooleanProperty isDone;
    private ImageView imageView;
    private StringProperty imgUrl;
    private Passive passive;
    private StringProperty unitCollisionType;
    private GameSquare gameSquare = new GameSquare();
    private HpBar hpBar = new HpBar();
    private ChampionPane championPane = new ChampionPane();
    private String passiveDescription;
    private String abilityDescription;
    private GameContextMenu gameContextMenu = new GameContextMenu();
    private MovementContextMenu movementContextMenu = new MovementContextMenu();
    private ArrayList<Mark> markList = new ArrayList<Mark>();
    private HBox markBox = new HBox(3);
    private Text txtHp = new Text("");

    public Champion() {
        this.name = new SimpleStringProperty(" ");
        this.type = "";
        this.hp = new SimpleDoubleProperty(0.0);
        this.ad = new SimpleIntegerProperty(0);
        this.ap = new SimpleIntegerProperty(0);
        this.maxHp = new SimpleDoubleProperty(0.0);
        this.movingDistance = new SimpleIntegerProperty(0);
        this.range = new SimpleIntegerProperty(0);
        this.imgUrl = new SimpleStringProperty("images/gameIcons/champion.png");
        imageView = new ImageView(new Image("images/gameIcons/champion.png"));
        unitCollisionType = new SimpleStringProperty("");
        isAlive = new SimpleBooleanProperty(true);
        canMove = new SimpleBooleanProperty(true);
        usedAbility = new SimpleBooleanProperty(false);
        autoAttacked = new SimpleBooleanProperty(false);
        isDone = new SimpleBooleanProperty(false);
        hpBar.setManaged(false);
        txtHp.setManaged(false);
        markBox.setManaged(false);
        markBox.layoutXProperty().bind(gameSquare.layoutXProperty());
        markBox.layoutYProperty().bind(gameSquare.layoutYProperty().add(10));
        championPane.txtName.textProperty().bindBidirectional(getName());
        txtHp.textProperty().bindBidirectional(getHp(), new NumberStringConverter());
        championPane.txtAd.textProperty().bindBidirectional(getAd(), new NumberStringConverter());
        championPane.txtAp.textProperty().bindBidirectional(getAp(), new NumberStringConverter());
        championPane.txtRange.textProperty().bindBidirectional(getRange(), new NumberStringConverter());
        championPane.txtmovingDistance.textProperty().bindBidirectional(getMovingDistance(), new NumberStringConverter());
        championPane.setVisible(false);
        getHp().addListener(
                (observable, oldvalue, newvalue) -> {
                    playHpChangingAnimation((double) oldvalue, (double) newvalue);
                });
        getUsedAbility().addListener(
                (observable, oldvalue, newvalue)->{
                    if(newvalue){
                        getGameContextMenu().getCASTABILITYITEM().setVisible(false);
                    }
                    else{
                        getGameContextMenu().getCASTABILITYITEM().setVisible(true);
                    }
                }
        );
    }

    public abstract void castAbility(GridPane gameGrid);

    public StringProperty getName() {
        return name;
    }

    public void setName(StringProperty name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DoubleProperty getHp() {
        return hp;
    }

    public StringProperty getHpString() {
        return new SimpleStringProperty(String.valueOf(getHp().get()));
    }

    public void setHp(DoubleProperty hp) {
        this.hp = hp;
    }

    public IntegerProperty getAd() {
        return ad;
    }

    public StringProperty getAdString() {
        return new SimpleStringProperty(String.valueOf(getAd().get()));
    }

    public void setAd(IntegerProperty ad) {
        this.ad = ad;
    }

    public IntegerProperty getAp() {
        return ap;
    }

    public StringProperty getApString() {
        return new SimpleStringProperty(String.valueOf(getAp().get()));
    }

    public void setAp(IntegerProperty ap) {
        this.ap = ap;
    }

    public IntegerProperty getMovingDistance() {
        return movingDistance;
    }

    public StringProperty getMovingDistanceString() {
        return new SimpleStringProperty(String.valueOf(getMovingDistance().get()));
    }

    public void setMovingDistance(IntegerProperty movingDistance) {
        this.movingDistance = movingDistance;
    }

    public IntegerProperty getRange() {
        return range;
    }

    public StringProperty getRangeString() {
        return new SimpleStringProperty(String.valueOf(getRange().get()));
    }

    public void setRange(IntegerProperty range) {
        this.range = range;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public BooleanProperty getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(BooleanProperty isAlive) {
        this.isAlive = isAlive;
    }

    public BooleanProperty getUsedAbility() {
        return usedAbility;
    }

    public void setUsedAbility(BooleanProperty usedAbility) {
        this.usedAbility = usedAbility;
    }

    public BooleanProperty getCanMove() {
        return canMove;
    }

    public void setCanMove(BooleanProperty canMove) {
        this.canMove = canMove;
    }

    public StringProperty getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(StringProperty imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Passive getPassive() {
        return passive;
    }

    public void setPassive(Passive passive) {
        this.passive = passive;
    }

    public void setImageView() {
        this.imageView = this.getImageView();
    }

    public ImageView getImageView() {
        return new ImageView(new Image(this.getImgUrl().get()));
    }

    public GameSquare getGameSquare() {
        return gameSquare;
    }

    public void setGameSquare(GameSquare gameSquare) {
        championPane.setManaged(false);
        championPane.buildAvatar(type);
        gameSquare.hoverProperty().addListener((observable, oldValue, show) -> {
            if (show) {
                double x = gameSquare.getLayoutX();
                double y = gameSquare.getLayoutY();
                double w = championPane.prefWidth(-1);
                double h = championPane.prefHeight(w);
                if (GridPane.getRowIndex(gameSquare) == GameControllerInterface.BOARDNUMOFROWS - 1) {
                    championPane.resizeRelocate(x + gameSquare.getWidth(), y-30, 150, 110);
                } else {
                    championPane.resizeRelocate(x + gameSquare.getWidth(), y, 150, 110);
                }
                championPane.setVisible(true);
            } else {
                championPane.setVisible(false);
            }
        });
        hpBar.layoutXProperty().bind(gameSquare.layoutXProperty());
        hpBar.layoutYProperty().bind(gameSquare.layoutYProperty());
        hpBar.progressProperty().bind(getHp().divide(getMaxHp()));
        txtHp.layoutXProperty().bind(hpBar.layoutXProperty().add(hpBar.getWidth() / 2 - 5));
        txtHp.layoutYProperty().bind(hpBar.layoutYProperty().add(hpBar.getHeight() - 2));
        txtHp.getStyleClass().add("hpText");
        markBox.setManaged(false);
        markBox.layoutXProperty().bind(gameSquare.layoutXProperty());
        markBox.layoutYProperty().bind(gameSquare.layoutYProperty().add(20));
        this.gameSquare = gameSquare;
    }

    public void playHpChangingAnimation(double oldval, double newval) {
        Text hpChangingText = new Text();
        if (oldval < newval) {
            hpChangingText.setText("+" + (newval - oldval));
            hpChangingText.setFill(Color.GREEN);
            hpChangingText.setLayoutX((gameSquare.getLayoutX() - 30));
            hpChangingText.setLayoutY((gameSquare.getLayoutY() + 40));
        } else {
            hpChangingText.setText("-" + (oldval - newval));
            hpChangingText.setFill(Color.WHITE);
            hpChangingText.setLayoutX((gameSquare.getLayoutX() + 75));
            hpChangingText.setLayoutY((gameSquare.getLayoutY() + 40));
        }
        hpChangingText.setFont(Font.font("Verdana", FontPosture.REGULAR, 30));
        //set the position
        hpChangingText.setManaged(false);

        centerPane.getChildren().add(hpChangingText);
        double currentYpos = hpChangingText.getLayoutY();
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(hpChangingText.layoutYProperty(), currentYpos - 5)
                ),
                new KeyFrame(Duration.millis(2400),
                        new KeyValue(hpChangingText.layoutYProperty(), currentYpos - 30)
                )
        );
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        Platform.runLater(() -> {
            timeline.play();
            timeline.setOnFinished(e -> {
                centerPane.getChildren().remove(hpChangingText);
            });
        });

    }
 

    public void addMark(Mark mark) {
        if (getIsAlive().getValue()) {
            mark.getIvMark().setX(markBox.getLayoutX() + markList.size() * 25);
            mark.getIvMark().setY(markBox.getLayoutY());
            markBox.getChildren().add(mark.getIvMark());
            markList.add(mark);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(mark.getIvMark().fitHeightProperty(), 45),
                            new KeyValue(mark.getIvMark().fitWidthProperty(), 45)
                    ),
                    new KeyFrame(Duration.millis(1500),
                            new KeyValue(mark.getIvMark().fitHeightProperty(), 30),
                            new KeyValue(mark.getIvMark().fitWidthProperty(), 30)
                    ),
                    new KeyFrame(Duration.millis(3000),
                            new KeyValue(mark.getIvMark().fitHeightProperty(), 15),
                            new KeyValue(mark.getIvMark().fitWidthProperty(), 15)
                    )
            );

            timeline.setAutoReverse(true);
            timeline.setCycleCount(1);
            Platform.runLater(() -> {
                timeline.play();
                timeline.setOnFinished(e -> {
                    mark.buildMarkPane();
                    centerPane.getChildren().add(mark.getMarkPane());
                    mark.getMarkPane().setManaged(false);
                    mark.getIvMark().setOnMouseClicked(ev -> {
                        double x = gameSquare.getLayoutX();
                        double y = gameSquare.getLayoutY();
                        double w = mark.getMarkPane().prefWidth(-1);
                        double h = mark.getMarkPane().prefHeight(w);
                        mark.getMarkPane().resizeRelocate(x + mark.getIvMark().getFitWidth() * markList.size(), y, 150, 140);
                        mark.getMarkPane().setVisible(true);
                    });
                    mark.getMarkPane().setOnMouseClicked(ev -> {
                        mark.getMarkPane().setVisible(false);
                    });
                    if (mark.getType().equals("POST")) {
                        if (turnCounterQueue.isEmpty()) {
                            turnCounterQueue.add(turnNumber.getValue());
                        }
                    }
                });
            });

        }
    }

    public void remove(Mark mark) {
        getMarkBox().getChildren().remove(mark.getIvMark());
        markList.remove(mark);
    }

    public BooleanProperty getAutoAttacked() {
        return autoAttacked;
    }

    public void setAutoAttacked(BooleanProperty autoAttacked) {
        this.autoAttacked = autoAttacked;
    }

    public HpBar getHpBar() {
        return hpBar;
    }

    public void setHpBar(HpBar hpBar) {
        this.hpBar = hpBar;
    }

    public ChampionPane getChampionPane() {
        return championPane;
    }

    public DoubleProperty getMaxHp() {
        return maxHp;
    }

    public String getPassiveDescription() {
        return passiveDescription;
    }

    public String getAbilityDescription() {
        return abilityDescription;
    }

    public void setPassiveDescription(String passiveDescription) {
        this.passiveDescription = passiveDescription;
    }

    public void setAbilityDescription(String abilityDescription) {
        this.abilityDescription = abilityDescription;
    }

    public void setMaxHp(DoubleProperty maxHp) {
        this.maxHp = maxHp;
    }

    public GameContextMenu getGameContextMenu() {
        return gameContextMenu;
    }

    public MovementContextMenu getMovementContextMenu() {
        return movementContextMenu;
    }

    public BooleanProperty getIsDone() {
        return isDone;
    }

    public void setIsDone(BooleanProperty isDone) {
        this.isDone = isDone;
    }

    public ArrayList<Mark> getMarkList() {
        return markList;
    }

    public void setMarkList(ArrayList<Mark> markList) {
        this.markList = markList;
    }

    public HBox getMarkBox() {
        return markBox;
    }

    public void setMarkBox(HBox markBox) {
        this.markBox = markBox;
    }

    public StringProperty getUnitCollisionType() {
        return unitCollisionType;
    }

    public void setUnitCollisionType(StringProperty unitCollisionType) {
        this.unitCollisionType = unitCollisionType;
    }

    public Text getTxtHp() {
        return txtHp;
    }

    public void setTxtHp(Text txtHp) {
        this.txtHp = txtHp;
    }

}
