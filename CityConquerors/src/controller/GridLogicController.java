package controller;

import static controller.GameControllerInterface.BOARDNUMOFCOLS;
import static controller.GameControllerInterface.BOARDNUMOFROWS;
import java.util.ArrayList;

/*
Logic controller that contains all functions for return an ArrayList of indices
for the gameGrid that are requested for different needs
 */
public class GridLogicController {

    public GridLogicController() {

    }

    /**
     * returns an ArrayList of all adjacentSquares of gameGrid
     *
     * @param row
     * @param col
     * @return
     */
    public ArrayList<Integer[]> getAllAdjacentSquares(int row, int col) {
        ArrayList<Integer[]> results = new ArrayList<Integer[]>();
        int lowerIterRow = -1;
        int upperIterRow = 1;
        int lowerIterCol = -1;
        int upperIterCol = 1;
        //setting correct boundary for iterations
        if (row == 0) {
            lowerIterRow = 0;
        }
        if (row == BOARDNUMOFROWS-1) {
            upperIterRow = 0;
        }
        if (col == 0) {
            lowerIterCol = 0;
        }
        if (col == BOARDNUMOFCOLS-1) {
            upperIterCol = 0;
        }

        for (int i = lowerIterRow; i <= upperIterRow; i++) {
            for (int j = lowerIterCol; j <= upperIterCol; j++) {
                if (i + j == 1 || i + j == -1) {
                    results.add(new Integer[]{row + i, col + j});
                }
            }
        }
        return results;
    }

    /**
     * get all reachable enemy coordinates within a specified distance
     *
     * @param row
     * @param col
     * @param range
     * @return
     */
    public ArrayList<Integer[]> getAllreachableCoordsWithinDistance(int row, int col, int range) {
        ArrayList<Integer[]> results = new ArrayList<>();
        for (int i = max((row - range), 0);
                i <= min((row + range), BOARDNUMOFROWS - 1); i++) {
            for (int j = max((col - range), 0);
                    j <= min((col + range), BOARDNUMOFCOLS - 1); j++) {
                if (absDiff(j, col) + absDiff(i, row) <= range
                        && (row != i || col != j)) {
                    results.add(new Integer[]{i, j});
                }
            }
        }
        return results;
    }

    /**
     * returns a list of all attackable coordinates when the champion gains
     * extra horizontal and vertical range
     *
     * @param rangeAug range augmentation
     * @param row the row index of the attacker
     * @param col the col index of the attacker
     * @return
     */
    public ArrayList<Integer[]> getAllReachableWithExtraRange(int range, int rangeAug, int row, int col) {
        //search left
        ArrayList<Integer[]> results = new ArrayList<>();
        int leftBound = max(0, col - range - rangeAug);
        results.add(new Integer[]{row, leftBound});

        //search right
        int rightBound = min(BOARDNUMOFCOLS-1, col + range + rangeAug);
        results.add(new Integer[]{row, rightBound});

        //search top
        int topBound = max(0, row - range - rangeAug);
        results.add(new Integer[]{topBound, col});
        //search bottom
        int bottomBound = min(BOARDNUMOFROWS-1, row + range + rangeAug);
        results.add(new Integer[]{bottomBound, col});
        return results;
    }

    public ArrayList<Integer[]> getAllReachableEnemyCoordsAtRange(int row, int col, int range) {
        ArrayList<Integer[]> results = new ArrayList<>();
        for (int i = max((row - range), 0);
                i <= min((row + range), BOARDNUMOFROWS - 1); i++) {
            for (int j = max((col - range), 0);
                    j <= min((col + range), BOARDNUMOFCOLS - 1); j++) {
                if (absDiff(j, col) + absDiff(i, row) == range) {
                    results.add(new Integer[]{i, j});
                }
            }
        }
        return results;
    }
    
    /** 
     * returns an array list of all coordinates of the squares within the range of a square
     * @param row row coordinate of the square
     * @param col column coordinate of the square
     * @param range specified range for the calculations
     * @return 
     */
    
    public ArrayList<Integer[]> getAllReachableSquaresWithinDistance(int row, int col, int range){
        ArrayList<Integer[]> results = new ArrayList<>();
        for (int i = max((row - range), 0);
                i <= min((row + range), BOARDNUMOFROWS - 1); i++) {
            for (int j = max((col - range), 0);
                    j <= min((col + range), BOARDNUMOFCOLS - 1); j++) {
                if (absDiff(j, col) + absDiff(i, row) <= range&&absDiff(j, col) + absDiff(i, row)!=0) {
                    results.add(new Integer[]{i, j});
                }
            }
        }
        return results;
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

}
