package controller;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

//Withing Package Imports
import model.Champion;
import model.GameObject.AttackableGameObject;
import model.GameSquare;
import static model.GameSquare.FIELDURL;
import model.Mark;
import model.GameObject.Nexus;
import model.Passive;
import model.champions.Selna;
import model.champions.Nywa;
import model.champions.Chedipe;
import model.champions.Dun;
import model.champions.Alery;
import model.champions.Houyi;
import model.champions.Nagamasa;
import model.champions.Pyrubo;
import model.champions.Rathmore;
import model.champions.Wukong;
import view.ChampionInfoPane;
import static view.GameView.gameStage;
import static view.GameView.centerPane;
import static view.GameView.gameGrid;

/**
 * **
 * IMPORTANT NOTES: 1) ALWAYS USE setValue for properties, otherwise the binding
 * to the ChampionPane will not work 2) Never set usedAbility back to false
 * because ability can only be used once
 *
 * @author chenshandi
 */
public class GameController implements GameControllerInterface {

    public static ArrayList<Champion> enemyChampList = new ArrayList<Champion>();
    public static ArrayList<Champion> allyChampList = new ArrayList<Champion>();
    private ArrayList<Nexus> nexusList = new ArrayList<>();
    private ArrayList<Passive> movementCheckerPassiveList = new ArrayList<Passive>();
    private ArrayList<Passive> rangeCheckerPassiveList = new ArrayList<Passive>();
    private ArrayList<Passive> autoAttackCheckerPassiveList = new ArrayList<Passive>();
    private ArrayList<Passive> damageReceiveCheckerPassiveList = new ArrayList<Passive>();
    private ArrayList<Passive> uponDeathCheckerPassiveList = new ArrayList<Passive>();
    private ArrayList<Passive> endTurnCheckerPassiveList = new ArrayList<Passive>();
    public final GridLogicController GRIDCONTROLLER = new GridLogicController();
    public final String blueNexusUrl = "images/gameIcons/Red_Nexus_Sand_Tile.png";
    public final String greenNexusUrl = "images/gameIcons/Blue_Nexus_Sand_Tile.png";
    public static int numOfActiveChampSquare = 0;
    public static int numOfActiveFieldSquare = 0;
    public static int numOfAllyAbilityNotCasted = 5;
    public static int numOfEnemyAbilityNotCasted = 5;
    public static int numOfNotDoneChamps = 5;
    public static IntegerProperty turnNumber = new SimpleIntegerProperty(0);
    public static boolean isFieldEnabled = false;
    public static boolean allyTurn = true;
    public static Stack<Champion> championStack = new Stack<Champion>();
    public static Stack<GameSquare[]> movementStack = new Stack<GameSquare[]>();
    private static HashMap<Champion, ArrayList<ImageView>> postMarkCollector = new HashMap<Champion, ArrayList<ImageView>>();
    public static Queue<Integer> turnCounterQueue = new LinkedList<Integer>();
    private MediaPlayer gameSoundMediaPlayer;
    private Media gameSoundMedia;
    private File gameSoundFile;
    private Media helperSoundMedia;
    private MediaPlayer helperSoundMediaPlayer;
    private File helperSoundFile;
    private static GameController instance = null;
//Constructor

    private GameController() {

    }

    public static synchronized GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }
    
    public void clear(){
        instance=null;
    }
//-------------------------------
// VIEW INITIALIZATION FUNCTIONS

    public void populateGameBoard() {
        gameGrid.getChildren().clear();
        for (int i = 0; i < BOARDNUMOFCOLS; i++) {
            for (int j = 0; j < BOARDNUMOFROWS; j++) {
                GameSquare square = new GameSquare();
                gameGrid.add(square, i, j);
            }
        }
    }

    public void loadNexus() {
        nexusList.clear();
        Nexus blueNexus = new Nexus("ENEMY");
        blueNexus.setGameSquare((GameSquare) getNodeByRowColumnIndex(0, BOARDNUMOFCOLS - 1, gameGrid));
        blueNexus.getGameSquare().setElement(blueNexus.getImgUrl().getValue());
        Nexus redNexus = new Nexus("ALLY");
        redNexus.setGameSquare((GameSquare) getNodeByRowColumnIndex(BOARDNUMOFROWS - 1, 0, gameGrid));
        redNexus.getGameSquare().setElement(blueNexus.getImgUrl().getValue());
        //hp bar and hover
        blueNexus.getHpBar().getStyleClass().add("hpbarenemy");
        centerPane.getChildren().addAll(blueNexus.getHpBar(), blueNexus.getTxtHp(), blueNexus.getPane());
        redNexus.getHpBar().getStyleClass().add("hpbarally");
        centerPane.getChildren().addAll(redNexus.getHpBar(), redNexus.getTxtHp(), redNexus.getPane());
        nexusList.add(blueNexus);
        nexusList.add(redNexus);
    }

//----------------------------------------------------------
/*
    GAME LOGIC SMALL HELPER FUNCTIONS   
     */
    public int getCol(GameSquare gameSquare) {
        return GridPane.getColumnIndex(gameSquare);
    }

    public int getRow(GameSquare gameSquare) {
        return GridPane.getRowIndex(gameSquare);
    }

    //API function to retrieve a Node of a particular row and column index
    public static Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    //API for replacing the node at a particular index
    public void replaceNodeByColRowIndex(GameSquare gameSquare, int row, int column, GridPane grid) {
        grid.getChildren().remove(getNodeByRowColumnIndex(row, column, grid));
        grid.add(gameSquare, column, row);
    }

    public int absDiff(int i, int j) {
        if (i > j) {
            return i - j;
        } else if (i < j) {
            return j - i;
        } else {
            return 0;
        }
    }

    public int min(int i, int j) {
        if (i < j) {
            return i;
        } else if (i > j) {
            return j;
        } else {
            return i;
        }
    }

    public int max(int i, int j) {
        if (i < j) {
            return j;
        } else if (i > j) {
            return i;
        } else {
            return i;
        }
    }

    boolean isAlly(Champion champion) {
        return allyChampList.contains(champion);
    }

    public boolean isAllySquare(GameSquare square) {
        boolean isAllyS = false;
        for (Champion ch : allyChampList) {
            if (square.getUrlName().equals(ch.getGameSquare().getUrlName())) {
                isAllyS = true;
            }
        }
        return isAllyS;
    }

    public boolean isEnemySquare(GameSquare square) {
        boolean isEnemyS = false;
        for (Champion ch : enemyChampList) {

            if (square.getUrlName().equals(ch.getGameSquare().getUrlName())) {
                isEnemyS = true;
            }
        }
        return isEnemyS;
    }

    public Champion getEnemyFromSquare(GameSquare square) {
        for (Champion ch : enemyChampList) {
            if (square.getUrlName().equals(ch.getGameSquare().getUrlName())) {
                return ch;
            }
        }
        return null;
    }

    public Champion getAllyFromSquare(GameSquare square) {
        for (Champion ch : allyChampList) {
            if (square.getUrlName().equals(ch.getGameSquare().getUrlName())) {
                return ch;
            }
        }
        return null;
    }

    //-----------------------------------------------------------------------------
    // IN GAME LOGICAL INITIALIZATION FUNCTIONS
    /**
     * load champions to indicated positions TODO: needs to be modified based on
     * user selection later on
     */
    public void loadChampions() {
        //ally champs
        allyChampList.get(0).setGameSquare((GameSquare) getNodeByRowColumnIndex(BOARDNUMOFROWS - 2, 0, gameGrid));
        allyChampList.get(0).getGameSquare().setElement(allyChampList.get(0).getImgUrl().get());
        wireUpChampSquare(allyChampList.get(0).getGameSquare(), allyChampList.get(0));

        allyChampList.get(1).setGameSquare((GameSquare) getNodeByRowColumnIndex(BOARDNUMOFROWS - 1, 1, gameGrid));
        allyChampList.get(1).getGameSquare().setElement(allyChampList.get(1).getImgUrl().get());
        wireUpChampSquare(allyChampList.get(1).getGameSquare(), allyChampList.get(1));

        allyChampList.get(2).setGameSquare((GameSquare) getNodeByRowColumnIndex(BOARDNUMOFROWS - 2, 1, gameGrid));
        allyChampList.get(2).getGameSquare().setElement(allyChampList.get(2).getImgUrl().get());
        wireUpChampSquare(allyChampList.get(2).getGameSquare(), allyChampList.get(2));

        allyChampList.get(3).setGameSquare((GameSquare) getNodeByRowColumnIndex(BOARDNUMOFROWS - 2, 2, gameGrid));
        allyChampList.get(3).getGameSquare().setElement(allyChampList.get(3).getImgUrl().get());
        wireUpChampSquare(allyChampList.get(3).getGameSquare(), allyChampList.get(3));

        allyChampList.get(4).setGameSquare((GameSquare) getNodeByRowColumnIndex(BOARDNUMOFROWS - 3, 1, gameGrid));
        allyChampList.get(4).getGameSquare().setElement(allyChampList.get(4).getImgUrl().get());
        wireUpChampSquare(allyChampList.get(4).getGameSquare(), allyChampList.get(4));

        //------------------------Enemy champs
        enemyChampList.get(0).setGameSquare((GameSquare) getNodeByRowColumnIndex(0, BOARDNUMOFROWS - 2, gameGrid));
        enemyChampList.get(0).getGameSquare().setElement(enemyChampList.get(0).getImgUrl().get());
        wireUpChampSquare(enemyChampList.get(0).getGameSquare(), enemyChampList.get(0));

        enemyChampList.get(1).setGameSquare((GameSquare) getNodeByRowColumnIndex(0, BOARDNUMOFROWS - 3, gameGrid));
        enemyChampList.get(1).getGameSquare().setElement(enemyChampList.get(1).getImgUrl().get());
        wireUpChampSquare(enemyChampList.get(1).getGameSquare(), enemyChampList.get(1));

        enemyChampList.get(2).setGameSquare((GameSquare) getNodeByRowColumnIndex(1, BOARDNUMOFROWS - 2, gameGrid));
        enemyChampList.get(2).getGameSquare().setElement(enemyChampList.get(2).getImgUrl().get());
        wireUpChampSquare(enemyChampList.get(2).getGameSquare(), enemyChampList.get(2));

        enemyChampList.get(3).setGameSquare((GameSquare) getNodeByRowColumnIndex(1, BOARDNUMOFROWS - 3, gameGrid));
        enemyChampList.get(3).getGameSquare().setElement(enemyChampList.get(3).getImgUrl().get());
        wireUpChampSquare(enemyChampList.get(3).getGameSquare(), enemyChampList.get(3));

        enemyChampList.get(4).setGameSquare((GameSquare) getNodeByRowColumnIndex(2, BOARDNUMOFROWS - 2, gameGrid));
        enemyChampList.get(4).getGameSquare().setElement(enemyChampList.get(4).getImgUrl().get());
        wireUpChampSquare(enemyChampList.get(4).getGameSquare(), enemyChampList.get(4));
    }

    public void populateEnemyChampsList() {
        enemyChampList.clear();
        enemyChampList.add(Wukong.getInstance());
        enemyChampList.add(Alery.getInstance());
        enemyChampList.add(Nywa.getInstance());
        enemyChampList.add(Rathmore.getInstance());
        enemyChampList.add(Houyi.getInstance());
    }

    public void populateAllyChampsList() {
        allyChampList.clear();
        allyChampList.add(Nagamasa.getInstance());
        allyChampList.add(Pyrubo.getInstance());
        allyChampList.add(Selna.getInstance());
        allyChampList.add(Chedipe.getInstance());
        allyChampList.add(Dun.getInstance());
    }

    /**
     * load all passives accordingly
     */
    public void loadPassives() {
        for (Champion ch : allyChampList) {
            switch (ch.getPassive().getType()) {
                case "MOVEMENT":
                    movementCheckerPassiveList.add(ch.getPassive());
                    break;
                case "RANGE":
                    rangeCheckerPassiveList.add(ch.getPassive());
                    break;
                case "AUTOATTACK":
                    autoAttackCheckerPassiveList.add(ch.getPassive());
                    break;
                case "DAMAGERECEIVE":
                    damageReceiveCheckerPassiveList.add(ch.getPassive());
                    break;
                case "DEATH":
                    uponDeathCheckerPassiveList.add(ch.getPassive());
                    break;
                case "ENDTURN":
                    endTurnCheckerPassiveList.add(ch.getPassive());
                    break;

                default:
                    break;
            }
        }

        for (Champion ch : enemyChampList) {
            switch (ch.getPassive().getType()) {
                case "MOVEMENT":
                    movementCheckerPassiveList.add(ch.getPassive());
                    break;
                case "RANGE":
                    rangeCheckerPassiveList.add(ch.getPassive());
                    break;
                case "AUTOATTACK":
                    autoAttackCheckerPassiveList.add(ch.getPassive());
                    break;
                case "DAMAGERECEIVE":
                    damageReceiveCheckerPassiveList.add(ch.getPassive());
                    break;
                case "DEATH":
                    uponDeathCheckerPassiveList.add(ch.getPassive());
                    break;
                case "ENDTURN":
                    endTurnCheckerPassiveList.add(ch.getPassive());
                    break;
                default:
                    break;
            }
        }
    }

    //----------------------------------------------------------
    // IN GAME ENABLER FUNCTIONS
    public void wireUpChampHoverAndHpBar() {
        for (Champion ch : allyChampList) {
            ch.getHpBar().getStyleClass().add("hpbarally");
            centerPane.getChildren().addAll(ch.getHpBar(), ch.getTxtHp(), ch.getMarkBox());
        }

        for (Champion ch : enemyChampList) {
            ch.getHpBar().getStyleClass().add("hpbarenemy");
            centerPane.getChildren().addAll(ch.getHpBar(), ch.getTxtHp(), ch.getMarkBox());
        }

        for (Champion ch : allyChampList) {
            centerPane.getChildren().add(ch.getChampionPane());
        }

        for (Champion ch : enemyChampList) {
            centerPane.getChildren().add(ch.getChampionPane());
        }

    }

    /**
     * activate the champ square for user action and increment the champ active
     * square count by 1
     *
     * @param square
     */
    public void activateChamp(GameSquare square) {
        square.applyChampClickedEffect();
        numOfActiveChampSquare = 1;
    }

    /**
     * inactivate the champ square to enable other champ square activations and
     * decrease the champ active square count by 1
     *
     * @param square
     */
    public void inactivateChamp(GameSquare square) {
        square.cancelChampClickedEffect();
        numOfActiveChampSquare = 0;
    }

    /**
     * wire up the champion square click behavior
     *
     * @param champSquare
     * @param champion
     */
    public void wireUpChampSquare(GameSquare champSquare, Champion champion) {
        champSquare.setOnMousePressed(e -> {
            showChampInfoPane(champion);
            enableInTurnChampionsActions(champSquare, champion, allyTurn);
            if (!champion.getCanMove().getValue() && (allyTurn == isAlly(champion))
                    && !champion.getIsDone().getValue()) {
                champion.getGameContextMenu().show(champSquare, e.getScreenX(), e.getScreenY());
                wireUpAttackMenuItem(champion.getGameContextMenu().getATTACKITEM(), champSquare, champion);
                wireUpCastMenuItem(champion.getGameContextMenu().getCASTABILITYITEM(), champSquare, champion);
            }
        });
    }

    /**
     * enable users interactions with champ clicks and moves
     *
     * @param champSquare
     * @param champion
     * @param isAlly
     */
    public void enableInTurnChampionsActions(GameSquare champSquare, Champion champion, boolean isAlly) {
        //when you first click on the champ, activate it and light up all reachable squares
        if (!champSquare.isIsChampActive() && (isAlly == isAlly(champion)) && champion.getCanMove().getValue() && numOfActiveChampSquare == 0) {
            activateChamp(champSquare);
            champSquare.setIsChampActive(true);
            lightenUpAllReachableSquares(champSquare, champion);
            champion.getMovementContextMenu().show(champSquare, Side.RIGHT, 0, 40);
            wireUpDontMoveItem(champion.getMovementContextMenu().getDONTMOVEITEM(), champSquare, champion);

            // when all reachable squares are lighted, reclick on the champ will disable all effects
        } else if (champSquare.isIsChampActive() && (isAlly == isAlly(champion)) && champion.getCanMove().getValue()) {
            inactivateChamp(champSquare);
            champSquare.setIsChampActive(false);
            unlightenAllActiveFieldSquares();
            champion.getMovementContextMenu().hide();
            //when the champ has finished movement, and is active to attack
        } else if (champSquare.isIsChampActive() && (isAlly == isAlly(champion)) && !champion.getCanMove().getValue()
                && !champion.getAutoAttacked().getValue()) {
            activateChamp(champSquare);
            champSquare.setIsChampActive(true);

            //when user cancel the attack intention and chooses to change target or something
        } else if (!champSquare.isIsChampActive() && (isAlly == isAlly(champion)) && !champion.getCanMove().getValue()
                && !champion.getAutoAttacked().getValue()) {
            inactivateChamp(champSquare);
            champSquare.setIsChampActive(false);
            unlightenAllActiveFieldSquares();
        }

    }

    public void wireUpDontMoveItem(MenuItem menuItem, GameSquare square, Champion champion) {
        menuItem.setOnAction(e -> {
            champion.getMovementContextMenu().hide();
            champion.getCanMove().setValue(false);
            inactivateChamp(square);
            square.setIsChampActive(false);
            unlightenAllActiveFieldSquares();
            championStack.push(champion);
            movementStack.push(new GameSquare[]{champion.getGameSquare(), square});
        });
    }

    /**
     * wire up champion info and show them on champion info pane
     *
     * @param champion
     */
    public void showChampInfoPane(Champion champion) {
        Platform.runLater(() -> {
            ChampionInfoPane.getInstance().loadSpecificChampionPane(champion);
        });
    }

    /**
     * Automatically end the turn for the user if conditions satisfy
     */
    public void AutoEndTurnChecker() {
        if (numOfNotDoneChamps == 0) {
            endTurn();
        }
    }

    //-------------------------------------------------
    //IN GAME LOGICAL FUNCTIONS
    /**
     * returns true if ch1 and ch2 are from the same team, false otherwise
     *
     * @param ch1
     * @param ch2
     * @return
     */
    public boolean fromSameTeam(Champion ch1, Champion ch2) {
        boolean b1 = allyChampList.contains(ch1);
        boolean b2 = allyChampList.contains(ch2);
        return b1 == b2;
    }

    /**
     * Auto effect clearing function for champions
     */
    public void allChampionsEffectClear() {
        for (Champion ch : allyChampList) {
            ch.getGameSquare().setEffect(null);
        }
        for (Champion ch : enemyChampList) {
            ch.getGameSquare().setEffect(null);
        }
    }

    public void endTurn() {
        turnNumber.setValue(turnNumber.getValue() + 1);
        numOfActiveChampSquare = 0;
        numOfNotDoneChamps = 5;
        //cleanse leftover effects
        if (allyTurn) {
            for (Champion champ : allyChampList) {
                champ.getGameSquare().setEffect(null);
            }
            allyTurn = false;
            for (Champion champ : enemyChampList) {
                champ.getIsDone().setValue(false);
                champ.getCanMove().setValue(true);
                champ.getAutoAttacked().setValue(false);
                champ.getGameSquare().setIsChampActive(false);
                wireUpChampSquare(champ.getGameSquare(), champ);
                markTakeEffectChecker(champ);
            }

        } else {
            //cleanse leftover effects
            for (Champion champ : enemyChampList) {
                champ.getGameSquare().setEffect(null);
            }
            allyTurn = true;
            for (Champion champ : allyChampList) {
                champ.getIsDone().setValue(false);
                champ.getGameSquare().setIsChampActive(false);
                champ.getCanMove().setValue(true);
                champ.getAutoAttacked().setValue(false);
                wireUpChampSquare(champ.getGameSquare(), champ);
                markTakeEffectChecker(champ);
            }
        }
        //clear all post type marks
        if (!turnCounterQueue.isEmpty()) {
            if (turnCounterQueue.peek() + 2 == turnNumber.getValue()) {
                // Getting an iterator 
                Iterator postMarkIterator = postMarkCollector.entrySet().iterator();
                while (postMarkIterator.hasNext()) {
                    Map.Entry mapElement = (Map.Entry) postMarkIterator.next();
                    ArrayList<ImageView> markIVlist = (ArrayList) (mapElement.getValue());
                    for (ImageView iv : markIVlist) {
                        if (((Champion) mapElement.getKey()).getMarkBox().getChildren().contains(iv)) {
                            ((Champion) mapElement.getKey()).getMarkBox().getChildren().remove(iv);
                        }
                    }
                    postMarkIterator.remove();
                }
                turnCounterQueue.remove();
            }
        }

        for (Passive passive : endTurnCheckerPassiveList) {
            passive.unwind();
        }
        unlightenAllActiveFieldSquares();
    }

    /**
     * traverse the pre mark list of the specific champion and let all marks
     * take effect
     *
     * @param champ
     */
    public void markTakeEffectChecker(Champion champ) {
        for (Iterator<Mark> iterator = champ.getMarkList().iterator(); iterator.hasNext();) {
            Mark mark = iterator.next();
            if (mark.getNumOfTurn().getValue() == 1) {
                mark.takeEffect(champ);
                if (mark.getType().equals("POST")) {
                    if (!postMarkCollector.containsKey(champ)) {
                        postMarkCollector.put(champ, new ArrayList<>());
                        postMarkCollector.get(champ).add(mark.getIvMark());
                    } else {
                        postMarkCollector.get(champ).add(mark.getIvMark());
                    }
                } else if (mark.getType().equals("PRE")) {
                    champ.getMarkBox().getChildren().remove(mark.getIvMark());
                }
                iterator.remove();
            } else {
                mark.takeEffect(champ);
            }
        }
    }

    /**
     * lights up all possible squares for the particular champion to move from
     * the square he is standing on
     *
     * @param champSquare
     * @param champion
     */
    public void lightenUpAllReachableSquares(GameSquare champSquare, Champion champion) {
        int row = getRow(champSquare);
        int col = getCol(champSquare);
        int movingDistance = champion.getMovingDistance().getValue();
        for (Integer[] ints : movementChecker(row, col, movingDistance, champion)) {
            ((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))).setIsFieldActive(true);
            ((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))).applySquareActiveEffect();
            enableFieldSquareClickForChampionToMove(((GameSquare) (getNodeByRowColumnIndex(ints[0], ints[1], gameGrid))), champion, row, col);
        }

    }

    /**
     * return all possible reachable squares based on different unit collision
     * conditions
     *
     * @param row
     * @param col
     * @param movingDist
     * @return
     */
    public HashSet<Integer[]> movementChecker(int row, int col, int movingDist, Champion champion) {
        HashSet<Integer[]> coords = new HashSet<Integer[]>();
        //unwind all movement related passives
        for (Passive passive : movementCheckerPassiveList) {
            passive.unwind();
        }
        //With all units different than FIELDURL as collisions
        if (champion.getUnitCollisionType().getValue().equals("")) {
            coords = getAllReachableCoordsWithUnitCollision(row, col, movingDist, new HashSet<Integer[]>());
        } //without absolutely no collisions
        else if (champion.getUnitCollisionType().getValue().equals("ALL")) {
            coords = new HashSet<Integer[]>();
            for (int i = max((row - movingDist), 0);
                    i <= min((row + movingDist), BOARDNUMOFROWS - 1); i++) {
                for (int j = max((col - movingDist), 0);
                        j <= min((col + movingDist), BOARDNUMOFCOLS - 1); j++) {
                    if (((GameSquare) (getNodeByRowColumnIndex(i, j, gameGrid))).getUrlName().equals(FIELDURL)
                            && absDiff(j, col) + absDiff(i, row) <= movingDist
                            && (row != i || col != j)) {
                        coords.add(new Integer[]{i, j});
                    }
                }
            }
        }

        return coords;
    }

    public HashSet<Integer[]> getAllReachableCoordsWithUnitCollision(int row, int col, int movingDist, HashSet<Integer[]> coords) {
        //With all units as collisions       
        if (movingDist == 0) {
            //stop search
        } else {
            if (row + 1 < BOARDNUMOFROWS && ((GameSquare) (getNodeByRowColumnIndex(row + 1, col, gameGrid))).getUrlName().equals(FIELDURL)) {
                coords.add(new Integer[]{row + 1, col});
                getAllReachableCoordsWithUnitCollision(row + 1, col, movingDist - 1, coords);
            }
            if (row - 1 >= 0 && ((GameSquare) (getNodeByRowColumnIndex(row - 1, col, gameGrid))).getUrlName().equals(FIELDURL)) {
                coords.add(new Integer[]{row - 1, col});
                getAllReachableCoordsWithUnitCollision(row - 1, col, movingDist - 1, coords);
            }
            if (col + 1 < BOARDNUMOFCOLS && ((GameSquare) (getNodeByRowColumnIndex(row, col + 1, gameGrid))).getUrlName().equals(FIELDURL)) {
                coords.add(new Integer[]{row, col + 1});
                getAllReachableCoordsWithUnitCollision(row, col + 1, movingDist - 1, coords);
            }
            if (col - 1 >= 0 && ((GameSquare) (getNodeByRowColumnIndex(row, col - 1, gameGrid))).getUrlName().equals(FIELDURL)) {
                coords.add(new Integer[]{row, col - 1});
                getAllReachableCoordsWithUnitCollision(row, col - 1, movingDist - 1, coords);
            }
        }
        return coords;
    }

    public void unlightenAllActiveFieldSquares() {
        for (int i = 0; i < BOARDNUMOFROWS; i++) {
            for (int j = 0; j < BOARDNUMOFCOLS; j++) {
                if (((GameSquare) (getNodeByRowColumnIndex(i, j, gameGrid))).isIsFieldActive()
                        && ((GameSquare) (getNodeByRowColumnIndex(i, j, gameGrid))).getUrlName().equals(FIELDURL)) {
                    ((GameSquare) (getNodeByRowColumnIndex(i, j, gameGrid))).cancelSquareActiveEffect();
                    ((GameSquare) (getNodeByRowColumnIndex(i, j, gameGrid))).setIsFieldActive(false);
                }
            }
        }
    }

    public void enableFieldSquareClickForChampionToMove(GameSquare square, Champion champion, int row, int col) {
        square.setOnMouseClicked(e -> {
            if (square.isIsFieldActive()) {
                championStack.push(champion);
                movementStack.push(new GameSquare[]{champion.getGameSquare(), square});
                //swap
                replaceNodeByColRowIndex(new GameSquare(), row, col, gameGrid);
                square.setElement(champion.getImgUrl().getValue());
                champion.setGameSquare(square);
                wireUpChampSquare(square, champion);
                champion.getCanMove().setValue(false);
                unlightenAllActiveFieldSquares();
                inactivateChamp(champion.getGameSquare());

            }
        });
    }

    //enable undo movements functionality
    public void undoPreviousMovement(GameSquare previousChampSquare, GameSquare champSquare, Champion champion) {
        if (!champion.getCanMove().getValue() && !champion.getAutoAttacked().getValue() && !champion.getUsedAbility().getValue()
                && !champion.getIsDone().getValue()) {
            int newRow = GridPane.getRowIndex(champSquare);
            int newCol = GridPane.getColumnIndex(champSquare);
            int prevRow = GridPane.getRowIndex(previousChampSquare);
            int prevCol = GridPane.getColumnIndex(previousChampSquare);
            if (newRow == prevRow && newCol == prevCol) {
                champion.getCanMove().setValue(true);
            } else {
                replaceNodeByColRowIndex(new GameSquare(), newRow, newCol, gameGrid);
                previousChampSquare.setElement(champion.getImgUrl().getValue());
                previousChampSquare.setIsChampActive(false);
                numOfActiveChampSquare = 0;
                champion.setGameSquare(previousChampSquare);
                replaceNodeByColRowIndex(previousChampSquare, prevRow, prevCol, gameGrid);
                wireUpChampSquare(previousChampSquare, champion);
                champion.getCanMove().setValue(true);
            }
            allChampionsEffectClear();
        }
    }

    /**
     * champion's stats after each action
     *
     * @param attacker
     * @param target
     */
    public void updateStatsAfterAutoAttack(Champion attacker, Champion target) {
        attacker.getAutoAttacked().setValue(true);
        updateHpStatsWithDeathChecker(target, attacker.getAd().getValue());
        allChampionsEffectClear();
        numOfActiveChampSquare = 0;
    }

    /**
     * function for hp update taking death into account
     *
     * @param recipient is the champion who received the damage
     * @param dmgReceived is the damage received
     */
    public void updateHpStatsWithDeathChecker(Champion recipient, double dmgReceived) {

        if (recipient.getHp().getValue() > 0) {
            for (Passive passive : damageReceiveCheckerPassiveList) {
                passive.unwind();
            }

            if (recipient.getHp().getValue() <= dmgReceived) {
                recipient.getHp().setValue(0);
                recipient.getIsAlive().setValue(false);
                for (Passive passive : uponDeathCheckerPassiveList) {
                    passive.unwind();
                }
                if (!recipient.getIsAlive().getValue()) {
                    if (allyChampList.contains(recipient)) {
                        allyChampList.remove(recipient);
                    } else if (enemyChampList.contains(recipient)) {
                        enemyChampList.remove(recipient);
                    }
                    removePassiveFromGame(recipient);
                    Platform.runLater(() -> {
                        runDeathAnimation(recipient);
                    });

                }

                recipient.getGameSquare().setEffect(null);

            } // normal update
            else {
                recipient.getHp().setValue(recipient.getHp().getValue() - dmgReceived);
            }
            recipient.getGameSquare().setIsChampTargetted(false);
        }

    }

    private void removePassiveFromGame(Champion ch) {
        if(movementCheckerPassiveList.contains(ch.getPassive())){
            System.out.println("passive: "+ch.getPassive().getName()+" removed");
            movementCheckerPassiveList.remove(ch.getPassive());
        }
        if(rangeCheckerPassiveList.contains(ch.getPassive())){
            System.out.println("passive: "+ch.getPassive().getName()+" removed");
            rangeCheckerPassiveList.remove(ch.getPassive());
        }
        if(autoAttackCheckerPassiveList.contains(ch.getPassive())){
            System.out.println("passive: "+ch.getPassive().getName()+" removed");
            autoAttackCheckerPassiveList.remove(ch.getPassive());
        }
        if(damageReceiveCheckerPassiveList.contains(ch.getPassive())){
            System.out.println("passive: "+ch.getPassive().getName()+" removed");
            damageReceiveCheckerPassiveList.remove(ch.getPassive());
        }
        if(uponDeathCheckerPassiveList.contains(ch.getPassive())){
            System.out.println("passive: "+ch.getPassive().getName()+" removed");
            uponDeathCheckerPassiveList.remove(ch.getPassive());
        }
        if(endTurnCheckerPassiveList.contains(ch.getPassive())){
            System.out.println("passive: "+ch.getPassive().getName()+" removed");
            endTurnCheckerPassiveList.remove(ch.getPassive());
        }
    }

    private void runDeathAnimation(Champion recipient) {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), recipient.getGameSquare());
        ft.setFromValue(
                0.2);
        ft.setToValue(
                1);
        ft.setAutoReverse(
                true);
        ft.setCycleCount(
                4);
        ft.play();
        ft.setOnFinished(e -> {
            GameSquare newSquare = new GameSquare();
            newSquare.setElement(FIELDURL);
            //clean up square of the dead champ by replacement
            replaceNodeByColRowIndex(newSquare, GridPane.getRowIndex(recipient.getGameSquare()),
                    GridPane.getColumnIndex(recipient.getGameSquare()), gameGrid);
            recipient.getMarkList().clear();
            recipient.getMarkBox().getChildren().clear();
            centerPane.getChildren().removeAll(recipient.getTxtHp(), recipient.getHpBar(), recipient.getChampionPane());
        });
    }

    public void enableAllAttackableChampSquares(GameSquare square, Champion champion) {
        for (Passive passive : rangeCheckerPassiveList) {
            passive.unwind();
        }
        ArrayList<GameSquare> tempTargettedSquares = new ArrayList<>();
        int row = GridPane.getRowIndex(square);
        int col = GridPane.getColumnIndex(square);
        int distance = champion.getRange().getValue();
        for (int i = max((row - distance), 0);
                i <= min((row + distance), BOARDNUMOFROWS - 1); i++) {
            for (int j = max((col - distance), 0);
                    j <= min((col + distance), BOARDNUMOFCOLS - 1); j++) {
                if (absDiff(j, col) + absDiff(i, row) == distance && !champion.getAutoAttacked().getValue()) {
                    if ((allyTurn && isEnemySquare((GameSquare) getNodeByRowColumnIndex(i, j, gameGrid)))
                            || (!allyTurn && isAllySquare((GameSquare) getNodeByRowColumnIndex(i, j, gameGrid)))) {
                        tempTargettedSquares.add((GameSquare) getNodeByRowColumnIndex(i, j, gameGrid));
                        wireUpEffectForOneSquare((GameSquare) getNodeByRowColumnIndex(i, j, gameGrid), champion);
                    } else if (((GameSquare) getNodeByRowColumnIndex(i, j, gameGrid)).getAffectType().equals("auto")) {
                        tempTargettedSquares.add((GameSquare) getNodeByRowColumnIndex(i, j, gameGrid));
                        wireUpEffectForPassiveSquare((GameSquare) getNodeByRowColumnIndex(i, j, gameGrid), champion);
                    }
                }
            }
        }

        champion.getGameContextMenu().getCANCELITEM().setOnAction(e -> {
            for (GameSquare gs : tempTargettedSquares) {
                inactivateChamp(gs);
            }
        });
    }

    /**
     * wires up the attack, hp changing mechanics
     *
     * @param square
     * @param champion
     */
    public void wireUpEffectForOneSquare(GameSquare square, Champion champion) {
        if (!champion.getAutoAttacked().getValue()) {
            square.applyChampTargettedEffect();
            square.setOnMouseClicked(ev -> {
                if (!champion.getAutoAttacked().getValue()) {
                    if (allyTurn) {
                        getEnemyFromSquare(square).getGameSquare().setIsChampTargetted(true);
                        for (Passive passive : autoAttackCheckerPassiveList) {
                            passive.unwind();
                        }
                        updateStatsAfterAutoAttack(champion, getEnemyFromSquare(square));

                    } else {
                        getAllyFromSquare(square).getGameSquare().setIsChampTargetted(true);
                        for (Passive passive : autoAttackCheckerPassiveList) {
                            passive.unwind();
                        }
                        updateStatsAfterAutoAttack(champion, getAllyFromSquare(square));
                    }
                }
            });
        }
    }

    public AttackableGameObject getAttackableGameObjectFromSquare(GameSquare square) {
        for (Nexus n : nexusList) {
            if (n.getGameSquare() == square) {
                return n;
            }
        }
        return null;
    }

    public void wireUpEffectForPassiveSquare(GameSquare square, Champion champion) {
        if (!champion.getAutoAttacked().getValue()) {
            square.applyChampTargettedEffect();
            square.setOnMouseClicked(ev -> {
                if (!champion.getAutoAttacked().getValue()
                        && !allyTurn == getAttackableGameObjectFromSquare(square).getSide().equals("ALLY")) {
                    updateObjectHpfromAuto(champion, getAttackableGameObjectFromSquare(square));
                }
            });
        }
    }

    public void updateObjectHpfromAuto(Champion attacker, AttackableGameObject object) {
        if (object.getHp().getValue() <= attacker.getAd().getValue()) {
            object.getHp().setValue(0);
            object.getIsDestroyed().setValue(true);
            if (nexusList.contains(object) && !object.getIsDestroyed().getValue()) {
                nexusList.remove(object);
            } else if (nexusList.contains(object) && !object.getIsDestroyed().getValue()) {
                nexusList.remove(object);
            }
            object.getGameSquare().setEffect(null);

        } // normal update
        else {
            object.getHp().setValue(object.getHp().getValue() - attacker.getAd().getValue());

        }
        //visually remove everything from the square and replace the square with
        // a field square
        if (object.getIsDestroyed().getValue()) {
            GameSquare newSquare = new GameSquare();
            newSquare.setElement(FIELDURL);
            //clean up square of the dead champ by replacement
            replaceNodeByColRowIndex(newSquare, GridPane.getRowIndex(object.getGameSquare()),
                    GridPane.getColumnIndex(object.getGameSquare()), gameGrid);
            centerPane.getChildren().removeAll(object.getTxtHp(), object.getHpBar(), object.getPane());
        }
        object.getGameSquare().setIsChampTargetted(false);
    }

    /**
     * wire up event action for auto attack item if the champ has not auto
     * attacked, lights up all reachable attack champ squares for him.
     *
     * @param menuItem
     * @param square
     * @param champion
     */
    public void wireUpAttackMenuItem(MenuItem menuItem, GameSquare square, Champion champion) {
        menuItem.setOnAction(e -> {
            if (!champion.getAutoAttacked().getValue()) {
                champion.getGameSquare().cancelChampClickedEffect();
                enableAllAttackableChampSquares(square, champion);
            } else {
                warningPaneAnimation(square, AUTOUSEDWARNINGMESSAGE);
            }
        });

    }

    /**
     * enable cast ability menuitem functionalities
     *
     * @param menuItem
     * @param square
     * @param champion
     */
    public void wireUpCastMenuItem(MenuItem menuItem, GameSquare square, Champion champion) {
        menuItem.setOnAction(e -> {
            if (!champion.getUsedAbility().getValue()) {
                champion.castAbility(gameGrid);

            } else {
                warningPaneAnimation(square, ABILITYUSEDMESSAGE);
            }
        });
    }

    /**
     * Undo button wire up with stacks
     *
     * @param btUndo
     */
    public void wireUpUndoButton(Button btUndo) {
        btUndo.setOnMouseClicked(e -> {
            if (!movementStack.isEmpty() && !championStack.isEmpty()) {
                GameSquare[] squaresToSwap = movementStack.pop();
                undoPreviousMovement(squaresToSwap[0], squaresToSwap[1], championStack.pop());
            }
        });

    }

    public void endGameConditionCheck() {
        String winner = "NONE";
        if (nexusList.size() != 2) {
            winner = nexusList.get(0).getSide();
            endGame(winner);
        } else if (allyChampList.isEmpty()) {
            endGame("ENEMY");
        } else if (enemyChampList.isEmpty()) {
            endGame("ALLY");
        }

    }

    public void endGame(String winner) {
        //run end game animation and collect statss
        System.out.println("The Game has ended and the winner is " + winner + " team!!!");
    }

    //----------------------------------------------------------------------------------------------
    //ANIMATION AND TRANSITIONS
    public void warningPaneAnimation(GameSquare square, String msg) {
        StackPane warningBox = new StackPane();
        warningBox.getStyleClass().add("warningPane");
        Text txt = new Text(msg);
        txt.getStyleClass().add("warningText");
        warningBox.getChildren().add(txt);
        Scene sc = new Scene(warningBox, 200, 30);
        Stage stage = new Stage();
        stage.setScene(sc);
        sc.getStylesheets().add("css/game.css");
        stage.initOwner(gameStage);
        stage.initStyle(StageStyle.TRANSPARENT);
        try {
            loadGameSound("ingame/warning.mp4", 0);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GameController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        stage.show();
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(2000),
                new KeyValue(stage.getScene().getRoot().opacityProperty(), 0.3));
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished((ae) -> stage.close());
        timeline.play();
    }

    //---------------------------------------------------------------------------------------------
    //GAME SOUND AND BACKGROUND MUSIC
    public void loadGameSound(String gameSoundString, double startTime) throws MalformedURLException {
        /*stop the previous selected song*/
        if (gameSoundFile != null) {
            String path = gameSoundFile.getAbsolutePath();
            if (gameSoundMediaPlayer != null) {
                gameSoundMediaPlayer.stop();
            }
        }
        //load newly selected song
        gameSoundFile = new File("src/audio/" + gameSoundString);
        gameSoundMedia = new Media(gameSoundFile.toURI().toURL().toExternalForm());
        gameSoundMediaPlayer = new MediaPlayer(gameSoundMedia);
        gameSoundMediaPlayer.setStartTime(Duration.seconds(startTime));
        gameSoundMediaPlayer.setVolume(70);
        gameSoundMediaPlayer.setAutoPlay(true);
        //mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
        gameSoundMediaPlayer.play();
    }

    public void loadHelperSound(String gameSoundString, double startTime) throws MalformedURLException {
        /*stop the previous selected song*/
        if (helperSoundFile != null) {
            String path = helperSoundFile.getAbsolutePath();
            if (helperSoundMediaPlayer != null) {
                helperSoundMediaPlayer.stop();
            }
        }
        //load newly selected song
        helperSoundFile = new File("src/audio/" + gameSoundString);
        helperSoundMedia = new Media(helperSoundFile.toURI().toURL().toExternalForm());
        helperSoundMediaPlayer = new MediaPlayer(helperSoundMedia);
        helperSoundMediaPlayer.setStartTime(Duration.seconds(startTime));
        helperSoundMediaPlayer.setVolume(70);
        helperSoundMediaPlayer.setAutoPlay(true);
        //mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
        helperSoundMediaPlayer.play();
    }

}
