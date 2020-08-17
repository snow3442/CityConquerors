package model;

import javafx.animation.FadeTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import static view.GameView.gameGrid;

//lighting for death
//opacity and glow for enabled selection
//dropshadow for marks
public class GameSquare extends Pane {

    private String specName;
    private String urlName;
    private String affectType;
    private ImageView IVsquareImage;
    private boolean isChampActive;
    private boolean isFieldActive;
    private boolean isChampTargetted;
    private boolean isAbilityTargetted;
    private boolean isPassiveTargetted;
    public static final double ORIGINALOPACITY = 0.7;
    public static final double CLICKEDOPACITY = 0.3;
    public static final String FIELDURL = "images/squareItems/bricktile.png";
    FadeTransition ft;

    public GameSquare() {
        affectType = "none";
        isChampActive = false;
        isFieldActive = false;
        isChampTargetted = false;
        isAbilityTargetted=false;
        isPassiveTargetted=false;
        specName = "field";
        getStyleClass().add("gameSquare");
        prefWidthProperty().bind(gameGrid.widthProperty().multiply(0.2));
        prefHeightProperty().bind(gameGrid.heightProperty().multiply(0.2));
        setElement(FIELDURL);
    }

    public void setElement(String url) {
        getChildren().remove(getIVsquareImage());
        setUrlName(url);
        ImageView ivSquare = new ImageView(new Image(url));
        ivSquare.fitHeightProperty().bind(this.heightProperty());
        ivSquare.fitWidthProperty().bind(this.widthProperty());
        ivSquare.setOpacity(ORIGINALOPACITY);
        setIVsquareImage(ivSquare);
        getChildren().add(ivSquare);
    }

    public void applySquareActiveEffect() {
        ft = new FadeTransition(Duration.millis(500), this.getIVsquareImage());
        ft.setFromValue(ORIGINALOPACITY);
        ft.setToValue(CLICKEDOPACITY);
        ft.setAutoReverse(false);
        ft.play();
    }

    public void applyIsDoneEffect() {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        this.setEffect(lighting);
    }

    public void applyChampClickedEffect() {
        this.setEffect(new Glow(0.75));
    }
    
    public void applyChampTargettedEffect() {
        SepiaTone sepiatone = new SepiaTone();
        sepiatone.setLevel(0.8);
        Glow glow = new Glow(1.5);
        glow.setInput(sepiatone);
        this.setEffect(glow);
        
    }
    

    public void cancelChampClickedEffect() {
        this.setEffect(null);
    }

    

    public void cancelSquareActiveEffect() {
        ft = new FadeTransition(Duration.millis(500), this.getIVsquareImage());
        ft.setFromValue(CLICKEDOPACITY);
        ft.setToValue(ORIGINALOPACITY);
        ft.setAutoReverse(false);
        ft.play();
    }

    public void applyDropShadow(Color color) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(5.0);
        ds.setOffsetX(5.0);
        ds.setColor(color);
        this.setEffect(ds);
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public ImageView getIVsquareImage() {
        return IVsquareImage;
    }

    public void setIVsquareImage(ImageView IVsquareImage) {
        this.IVsquareImage = IVsquareImage;
    }

    public boolean isIsChampActive() {
        return isChampActive;
    }

    public void setIsChampActive(boolean isChampActive) {
        this.isChampActive = isChampActive;
    }

    public boolean isIsFieldActive() {
        return isFieldActive;
    }

    public void setIsFieldActive(boolean isFieldActive) {
        this.isFieldActive = isFieldActive;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public boolean isIsChampTargetted() {
        return isChampTargetted;
    }

    public void setIsChampTargetted(boolean isChampTargetted) {
        this.isChampTargetted = isChampTargetted;
    }

    public boolean isIsAbilityTargetted() {
        return isAbilityTargetted;
    }

    public void setIsAbilityTargetted(boolean isAbilityTargetted) {
        this.isAbilityTargetted = isAbilityTargetted;
    }

    public boolean isIsPassiveTargetted() {
        return isPassiveTargetted;
    }

    public void setIsPassiveTargetted(boolean isPassiveTargetted) {
        this.isPassiveTargetted = isPassiveTargetted;
    }

    public String getAffectType() {
        return affectType;
    }

    public void setAffectType(String affectType) {
        this.affectType = affectType;
    }

    
    

}
