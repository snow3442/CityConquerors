package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsView {
    static Scene settingsScene; 
    private Pane settings = new Pane();
    //set Scene
    public SettingsView() {
        buildSettingsPane();
        buildScene();
             
    }
    
    private void buildScene(){
        Stage settingsStage = new Stage();
        settingsScene = new Scene(settings);
        settingsStage.setScene(settingsScene);
        settingsScene.getStylesheets().add("css/game.css");
        settingsStage.initStyle(StageStyle.TRANSPARENT);
        settingsStage.show();
    }
    
    private void buildSettingsPane(){
        settings.setPrefSize(600, 600);      
        settings.getStyleClass().add("settingsPane");
    }
    
    
    
    
}
