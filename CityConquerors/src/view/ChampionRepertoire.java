package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.util.Pair;
import model.Champion;
import model.champions.*;

public class ChampionRepertoire extends AnchorPane {

    private static final double WIDTH = 1200;
    private static final double HEIGHT = 680;
    private static final double REPERTOIREWIDTH = WIDTH * 0.8;
    public GameMenuItem gameMenuBack = new GameMenuItem("Back", 200, 30);
    private GameMenuItem gameMenuExit = new GameMenuItem("Exit", 200, 30);
    private VBox selectionBox;
    private VBox radioButtonBox;
    private VBox leftMenuBox;
    private Pane repertoire = new Pane();
    private Line line = new Line();
    private GameTitle title = new GameTitle("ALL CHAMPIONS");
    private final double lineX = REPERTOIREWIDTH / 2 - title.getTitleWidth() / 2;
    private final double lineY = HEIGHT / 5 + 35;
    private GridPane avatars = new GridPane();
    private static final ArrayList<Champion> ALLCHAMPIONS = new ArrayList<>(Arrays.asList(
            Wukong.getInstance(),
            Nagamasa.getInstance(),
            Alery.getInstance(),
            Pyrubo.getInstance(),
            Nywa.getInstance(),
            Selna.getInstance(),
            Chedipe.getInstance(),
            Rathmore.getInstance(),
            Dun.getInstance(),
            Houyi.getInstance()
    ));

    private final List<Pair<String, Runnable>> radioButtonData = Arrays.asList(
            new Pair<String, Runnable>("All Champions", () -> {
                loadTitleAndAvatars("all");
            }),
            new Pair<String, Runnable>("Tanks", () -> {
                loadTitleAndAvatars("tank");
            }),
            new Pair<String, Runnable>("Assassins", () -> {
                loadTitleAndAvatars("assassin");
            }),
            new Pair<String, Runnable>("Supports", () -> {
                loadTitleAndAvatars("support");
            }),
            new Pair<String, Runnable>("Mages", () -> {
                loadTitleAndAvatars("mage");
            }),
            new Pair<String, Runnable>("Rangers", () -> {
                loadTitleAndAvatars("ranger");
            })
    );

    public ChampionRepertoire() throws MalformedURLException {
        addBackground();
        getChildren().addAll(createSelectionBox(), createRepertoire(), createAvatars());
    }

    private VBox createSelectionBox() {
        selectionBox = new VBox(30);
        selectionBox.setAlignment(Pos.CENTER_LEFT);
        selectionBox.setPrefWidth(WIDTH * 0.2);
        radioButtonBox = new VBox(15);
        radioButtonBox.setAlignment(Pos.TOP_LEFT);
        radioButtonBox.setPrefWidth(WIDTH * 0.2);
        leftMenuBox = new VBox(15);
        leftMenuBox.setAlignment(Pos.CENTER_LEFT);
        leftMenuBox.setPrefWidth(WIDTH * 0.2);
        TextField tfSearch = new TextField();
        Label lblSearch = new Label("Search");
        lblSearch.getStyleClass().add("searchText");
        radioButtonBox.getChildren().add(lblSearch);
        radioButtonBox.getChildren().add(tfSearch);
        VBox.setMargin(lblSearch, new Insets(10, 10, 0, 20));
        VBox.setMargin(tfSearch, new Insets(10, 10, 20, 10));
        ToggleGroup group = new ToggleGroup();
        radioButtonData.forEach(data -> {
            GameRadioButton rb = new GameRadioButton(data.getKey());
            rb.setOnAction(data.getValue());
            rb.getRb().setToggleGroup(group);
            radioButtonBox.getChildren().add(rb);
        });
        leftMenuBox.getChildren().add(gameMenuBack);
        leftMenuBox.getChildren().add(gameMenuExit);
        gameMenuExit.setOnAction(() -> {
            Platform.exit();
        });
        selectionBox.getChildren().addAll(radioButtonBox, leftMenuBox);
        selectionBox.getStyleClass().add("selectionBox");
        selectionBox.setPadding(new Insets(0, 20, 0, 20));
        AnchorPane.setBottomAnchor(selectionBox, 0.0);
        AnchorPane.setTopAnchor(selectionBox, 0.0);
        return selectionBox;
    }

    private Parent createRepertoire() throws MalformedURLException {
        repertoire.setLayoutX(WIDTH * 0.2);
        repertoire.setPrefSize(WIDTH * 0.8, HEIGHT);
        addTitle();
        addSeparator(lineX, lineY);
        createAvatars();
        return repertoire;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image("images/background/sample14.jpg"));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);
        getChildren().add(imageView);
    }

    private void addSeparator(double x, double y) {
        line = new Line(x, y, x + title.getTitleWidth(), y);
        line.setStrokeWidth(5);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));     
        repertoire.getChildren().add(line);
    }

    private void addTitle() {
        title.setTranslateX(REPERTOIREWIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 5);
        repertoire.getChildren().add(title);
    }

    public GameTitle getTitle() {
        return title;
    }

    public void setTitle(GameTitle title) {
        this.title = title;
    }

    private ScrollPane createAvatars() throws MalformedURLException {
        ScrollPane avatarsScroll = new ScrollPane();
        avatarsScroll.getStyleClass().add("scroll-bar");
        avatarsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        avatarsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        avatarsScroll.setPrefSize(REPERTOIREWIDTH, HEIGHT * 4 / 5 - 50);
        avatarsScroll.setLayoutX(WIDTH * 0.2);
        avatarsScroll.setLayoutY(HEIGHT / 5 + 55);
        avatarsScroll.setContent(avatars);
        avatars.setPadding(new Insets(50, 120, 30, 120));
        avatars.setAlignment(Pos.CENTER);
        avatars.setHgap(30);
        avatars.setVgap(50);
        for (int i = 0; i < ALLCHAMPIONS.size(); i++) {
            Image currentImage = new Image(ALLCHAMPIONS.get(i).getImgUrl().getValue());
            Circle circle = new Circle(55);
            circle.setStrokeWidth(5);
            circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.SEAGREEN);
            circle.setFill(new ImagePattern(currentImage));
            circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            Label lbl = new Label(ALLCHAMPIONS.get(i).getName().getValue().toUpperCase(), circle);
            lbl.getStyleClass().add("lblChamps");
            lbl.setContentDisplay(ContentDisplay.TOP);
            avatars.add(lbl, i % 5, (int) i / 5);
        }

        return avatarsScroll;
    }

    public void loadTitleAndAvatars(String type) {
        repertoire.getChildren().remove(title);
        title = new GameTitle("ALL CHAMPIONS");
        addTitle();
        avatars.getChildren().clear();
        if (type.equals("all")) {
            for (int i = 0; i < ALLCHAMPIONS.size(); i++) {
                Image currentImage = new Image(ALLCHAMPIONS.get(i).getImgUrl().getValue());
                Circle circle = new Circle(55);
                circle.setStrokeWidth(5);
                circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
                circle.setStrokeType(StrokeType.CENTERED);
                circle.setStroke(Color.SEAGREEN);
                circle.setFill(new ImagePattern(currentImage));
                circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
                Label lbl = new Label(ALLCHAMPIONS.get(i).getName().getValue().toUpperCase(), circle);
                lbl.getStyleClass().add("lblChamps");
                lbl.setContentDisplay(ContentDisplay.TOP);
                avatars.add(lbl, i % 5, (int) i / 5);
            }
        } else {
            repertoire.getChildren().remove(title);
            title = new GameTitle("ALL " + type.toUpperCase() + "S");
            addTitle();
            int pos = 0;
            for (Champion ch : ALLCHAMPIONS) {
                //load according to types 
                if (ch.getType().equals(type)) {
                    Image currentImage = new Image(ch.getImgUrl().getValue());
                    Circle circle = new Circle(55);
                    circle.setStrokeWidth(5);
                    circle.setStrokeLineJoin(StrokeLineJoin.ROUND);
                    circle.setStrokeType(StrokeType.CENTERED);
                    circle.setStroke(Color.SEAGREEN);
                    circle.setFill(new ImagePattern(currentImage));
                    circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
                    Label lbl = new Label(ch.getName().getValue().toUpperCase(), circle);
                    lbl.getStyleClass().add("lblChamps");
                    lbl.setContentDisplay(ContentDisplay.TOP);
                    avatars.add(lbl, pos % 5, (int) pos / 5);
                    pos++;
                }
            }
        }

    }

}
