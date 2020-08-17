package view;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class LoginPage extends AnchorPane {

    private final double WIDTH = 1200;
    private final double HEIGHT = 680;
    private VBox loginPane;
    private final GameMenuItem btLogin = new GameMenuItem("Log In", 150, 30);
    private final GameMenuItem btRegister = new GameMenuItem("Register", 150, 30);
    private final GameMenuItem btExit = new GameMenuItem("Exit", 150, 30);
    private final double WIDTHSPLITRATIO = 0.25;
    private File backgroundSoundFile;
    private Media backgroundSoundMedia;
    private MediaPlayer backgroundSoundMediaPlayer;

    public LoginPage() {
        setPrefSize(WIDTH, HEIGHT);      
        try {
            loadBackgroundSound("loginbackground.mp3",5);
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        addBackground();
        addLoginPane();
    }

    private void addBackground() {
        ImageView iv = new ImageView(new Image("images/background/sample14.jpg"));
        iv.setFitWidth(WIDTH);
        iv.setFitHeight(HEIGHT);
        getChildren().add(iv);
    }

    private void addLoginPane() {
        loginPane = new VBox(30);
        loginPane.setPadding(new Insets(20, 35, 20, 35));
        loginPane.setPrefSize(WIDTH * WIDTHSPLITRATIO, HEIGHT * 0.5);
        AnchorPane.setTopAnchor(loginPane, HEIGHT * 0.1);
        loginPane.setLayoutX(WIDTH * (1 - WIDTHSPLITRATIO));
        loginPane.setLayoutY(HEIGHT * 0.1);
        Label lblLandsOfWar = new Label("CITY CONQUERORS");
        VBox loginBox = new VBox(7);

        //username
        TextField tfUserName = new TextField();
        tfUserName.setPromptText("username");
        Label lblUserName = new Label("UserName");
        //password
        TextField tfPassword = new TextField();
        tfPassword.setPromptText("password");
        Label lblPassword = new Label("password");
        lblLandsOfWar.getStyleClass().add("loginLabel");
        lblPassword.getStyleClass().add("loginLabel");
        lblUserName.getStyleClass().add("loginLabel");
        loginBox.getChildren().addAll(lblUserName, tfUserName,
                lblPassword, tfPassword);
        btExit.setOnAction(() -> {
            Platform.exit();
        });
        btExit.setPadding(new Insets(0, 0, 20, 0));
        loginPane.setAlignment(Pos.CENTER_LEFT);
        loginPane.getChildren().addAll(lblLandsOfWar, loginBox, btLogin, btRegister, btExit);
        loginPane.setClip(buildClip());
        loginPane.getStyleClass().add("loginPane");
        getChildren().add(loginPane);
    }

    public GameMenuItem getBtLogin() {
        return btLogin;
    }

    public GameMenuItem getBtRegister() {
        return btRegister;
    }

    public Polygon buildClip() {
        Polygon bg = new Polygon(
                0, 0,
                WIDTH * WIDTHSPLITRATIO * 0.9, 0,
                WIDTH * WIDTHSPLITRATIO, 30,
                WIDTH * WIDTHSPLITRATIO, HEIGHT,
                0, HEIGHT
        );
        bg.setStroke(Color.color(1, 1, 1, 0.75));
        bg.setEffect(new GaussianBlur());
        return bg;
    }

    //GAME SOUND AND BACKGROUND MUSIC
    protected void loadBackgroundSound(String gameSoundString, double startTime) throws MalformedURLException {

        /*stop the previous selected song*/
        if (backgroundSoundFile != null) {
            String path = backgroundSoundFile.getPath();
            if (backgroundSoundMediaPlayer != null) {
                backgroundSoundMediaPlayer.stop();
            }
        }
        //load newly selected song
        backgroundSoundFile = new File("src/audio/" + gameSoundString);
        backgroundSoundMedia = new Media(backgroundSoundFile.toURI().toURL().toExternalForm());
        backgroundSoundMediaPlayer = new MediaPlayer(backgroundSoundMedia);
        backgroundSoundMediaPlayer.setStartTime(Duration.seconds(startTime));
        backgroundSoundMediaPlayer.setVolume(70);
        backgroundSoundMediaPlayer.setAutoPlay(true);
        backgroundSoundMediaPlayer.setCycleCount(AudioClip.INDEFINITE);
        backgroundSoundMediaPlayer.play();
    }

    public MediaPlayer getBackgroundSoundMediaPlayer() {
        return backgroundSoundMediaPlayer;
    }
    
    

}
