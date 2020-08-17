package model;

import javafx.beans.property.IntegerProperty;
import javafx.scene.image.ImageView;
import javafx.util.converter.NumberStringConverter;
import view.MarkPane;

public abstract class Mark {

    //private ImageView ivMark;
    private IntegerProperty numOfTurns;
    private MarkPane markPane = new MarkPane();

    public Mark() {

    }

    public void buildMarkPane() {
        markPane.getLblMarkName().setText(getName());
        markPane.getLblTurnsLeft().setText(String.valueOf(getNumOfTurn().getValue()));
        markPane.getLblTurnsLeft().textProperty().bindBidirectional(getNumOfTurn(), new NumberStringConverter());
        markPane.getTxtDescription().setText(getDescription());
        markPane.setVisible(false);

    }

    public abstract String getName();

    public abstract ImageView getIvMark();

    public abstract void setIvMark(ImageView ivMark);

    public abstract IntegerProperty getNumOfTurn();

    public abstract void takeEffect(Champion champion);

    public abstract String getType();

    public abstract String getDescription();

    public MarkPane getMarkPane() {
        return markPane;
    }

}
