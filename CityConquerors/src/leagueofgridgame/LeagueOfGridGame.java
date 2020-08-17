package leagueofgridgame;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import view.ChampionRepertoire;
import view.GameMenuItem;
import view.GameTitle;
import view.GameView;
import static view.GameView.gameStage;
import view.LoginPage;

public class LeagueOfGridGame extends Application {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 680;
    private Stage primaryStage;
    private Scene primaryScene;
    private final ChampionRepertoire champRep = new ChampionRepertoire();
    private final Scene repertoireScene = new Scene(champRep, WIDTH, HEIGHT);
    private final double lineX = WIDTH / 2 - 100;
    private final double lineY = HEIGHT / 3 + 50;
    private final LoginPage LOGIN = new LoginPage();

    private List<Pair<String, Runnable>> primaryMenuData = Arrays.asList(
            new Pair<String, Runnable>("New Game", () -> {
                loadNewGame();
            }),
            new Pair<String, Runnable>("Profile", () -> {
                
            }),
            new Pair<String, Runnable>("Repertoire", () -> {
                forwardToRepertoireMenu();
            }),
            new Pair<String, Runnable>("Tutorial", () -> {
            }),
            new Pair<String, Runnable>("Shop", () -> {
            }),
            new Pair<String, Runnable>("Exit to Desktop", Platform::exit)
    );

    private List<Pair<String, Runnable>> repertoireMenuData = Arrays.asList(
            new Pair<String, Runnable>("Champions", () -> {
                loadChampionRepertoire();
            }),
            new Pair<String, Runnable>("Cities", () -> {
            }),
            new Pair<String, Runnable>("Items", () -> {
            }),
            new Pair<String, Runnable>("Main Menu", () -> {
                backToMainMenu();
            }),
            new Pair<String, Runnable>("Exit to Desktop", Platform::exit)
    );

    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;


    public LeagueOfGridGame() throws MalformedURLException {
        
    }
    
    private Parent createMenus() throws MalformedURLException {
        addBackground();
        addTitle();
        addLine(lineX, lineY);
        loadMenu(primaryMenuData);
        addMenu();
        GameView gameView = new GameView();
        startAnimation();
        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image("images/background/sample12.jpg"));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);
        root.getChildren().add(imageView);
    }

    private void addTitle() {
        GameTitle title = new GameTitle("CITY CONQUERORS");
        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 4);
        root.getChildren().add(title);
    }

    private void addLine(double x, double y) {
        line = new Line(x, y, x, y + 300);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);
        root.getChildren().add(line);
    }
    

    private void startAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);
        st.setToY(1);
        st.setOnFinished(e -> {
            for (int i = 0; i < menuBox.getChildren().size(); i++) {
                Node n = menuBox.getChildren().get(i);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }
        });
        st.play();
    }

    private void loadMenu(List<Pair<String, Runnable>> menuData) {
        menuBox.getChildren().clear();
        menuBox.setTranslateX(lineX);
        menuBox.setTranslateY(lineY);
        menuData.forEach(data -> {
            GameMenuItem item = new GameMenuItem(data.getKey(),200,30);
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);
            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());
            item.setClip(clip);
            item.setCache(true);
            item.setCacheHint(CacheHint.SPEED);
            menuBox.getChildren().addAll(item);
        });

    }

    private void addMenu() {
        root.getChildren().add(menuBox);
    }

    private void loadNewGame() {
        gameStage.show();
    }

    private void forwardToRepertoireMenu() {
        loadMenu(repertoireMenuData);
        startAnimation();
    }

    public void backwardToRepertoireMenu() {
        primaryStage.setScene(primaryScene);
        loadMenu(repertoireMenuData);
        startAnimation();
    }

    private void backToMainMenu() {
        loadMenu(primaryMenuData);
        startAnimation();
    }

    @Override

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Scene scene = new Scene(LOGIN);
        scene.getStylesheets().add("css/game.css");
        LOGIN.getBtLogin().setOnAction(()->{
            try {
                LOGIN.getBackgroundSoundMediaPlayer().pause();
                logInToMenus();
            } catch (MalformedURLException ex) {
                Logger.getLogger(LeagueOfGridGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
    
    private void logInToMenus() throws MalformedURLException{
        Scene menuScene = new Scene(createMenus());
        this.primaryScene=menuScene;
        primaryStage.setScene(primaryScene);
    }

    private void loadChampionRepertoire() {
        repertoireScene.getStylesheets().add("css/game.css");
        champRep.gameMenuBack.setOnMouseClicked(e->{
            backwardToRepertoireMenu();
        });       
        primaryStage.setScene(repertoireScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    


}
