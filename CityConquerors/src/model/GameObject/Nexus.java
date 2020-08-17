package model.GameObject;

import controller.GameControllerInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import model.GameSquare;
import view.HpBar;
import view.NexusPane;

public class Nexus extends AttackableGameObject {

    private String side;
    private final String name = "NEXUS";
    private DoubleProperty maxHp;
    private BooleanProperty isDestroyed;
    private ImageView imageView;
    private StringProperty imgUrl;
    private GameSquare gameSquare = new GameSquare();
    private HpBar hpBar = new HpBar();
    private String description;
    private Text txtHp = new Text("");
    private NexusPane nexusPane = new NexusPane();

    public Nexus(String side) {
        this.side = side;     
        maxHp = new SimpleDoubleProperty(150);
        this.getHp().setValue(150);
        isDestroyed = new SimpleBooleanProperty(false);
        if (side.equals("ALLY")) {
            imageView = new ImageView(new Image("images/gameIcons/Blue_Nexus_Sand_Tile.png"));
            imgUrl = new SimpleStringProperty("images/gameIcons/Blue_Nexus_Sand_Tile.png");
        }
        
        else if(side.equals("ENEMY")){
            imageView = new ImageView(new Image("images/gameIcons/Red_Nexus_Sand_Tile.png"));
            imgUrl = new SimpleStringProperty("images/gameIcons/Red_Nexus_Sand_Tile.png");
        }
        nexusPane.setVisible(false);
        hpBar.setManaged(false);
        txtHp.setManaged(false);
        txtHp.textProperty().bindBidirectional(getHp(), new NumberStringConverter());
             
    }
    

    @Override
    public String getSide() {
        return side;
    }

    @Override
    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public DoubleProperty getMaxHp() {
        return maxHp;
    }


    @Override
    public BooleanProperty getIsDestroyed() {
        return isDestroyed;
    }

    @Override
    public void setIsDestroyed(BooleanProperty isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    @Override
    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public StringProperty getImgUrl() {
        return imgUrl;
    }

    @Override
    public void setImgUrl(StringProperty imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public GameSquare getGameSquare() {
        return gameSquare;
    }

    @Override
    public void setGameSquare(GameSquare gameSquare) {
        this.gameSquare = gameSquare;
        gameSquare.setAffectType("auto");
        gameSquare.setSpecName("nexus");
        gameSquare.setElement(imgUrl.getValue());
        gameSquare.hoverProperty().addListener((observable, oldValue, show) -> {
            nexusPane.setManaged(false);
            if (show) {
                double x = gameSquare.getLayoutX();
                double y = gameSquare.getLayoutY();
                if (GridPane.getRowIndex(gameSquare) == GameControllerInterface.BOARDNUMOFROWS - 1) {
                    nexusPane.resizeRelocate(x + gameSquare.getWidth(), y-30, 150, 100);
                } else {
                    nexusPane.resizeRelocate(x + gameSquare.getWidth(), y, 150, 100);
                }
                nexusPane.setVisible(true);
            } else {
                nexusPane.setVisible(false);
            }
        });
        hpBar.setManaged(false);
        hpBar.layoutXProperty().bind(gameSquare.layoutXProperty());
        hpBar.layoutYProperty().bind(gameSquare.layoutYProperty());
        hpBar.progressProperty().bind(getHp().divide(getMaxHp()));
        txtHp.setManaged(false);
        txtHp.layoutXProperty().bind(hpBar.layoutXProperty().add(hpBar.getWidth() / 2 - 5));
        txtHp.layoutYProperty().bind(hpBar.layoutYProperty().add(hpBar.getHeight() - 2));
        txtHp.getStyleClass().add("hpText"); 
    }

    @Override
    public HpBar getHpBar() {
        return hpBar;
    }

    public void setHpBar(HpBar hpBar) {
        this.hpBar = hpBar;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Text getTxtHp() {
        return txtHp;
    }

    @Override
    public void setTxtHp(Text txtHp) {
        this.txtHp = txtHp;
    }

    @Override
    public Pane getPane() {
        return nexusPane;
    }

    public void setNexusPane(NexusPane nexusPane) {
        this.nexusPane = nexusPane;
    }  
    
}
