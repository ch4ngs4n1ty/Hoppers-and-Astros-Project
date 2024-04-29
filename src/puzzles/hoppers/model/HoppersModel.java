package puzzles.hoppers.model;

import puzzles.astro.model.AstroConfig;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Contains the methods that can be used for PTUI and GUI Hoppers
 * Represents the functionality of the hoppers game
 *
 * @author Ethan Chang
 */

public class HoppersModel {

    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    public HoppersConfig currentConfig;
    /** the current file name */
    private String currentFilename;
    /** number of rows  */
    private static int rows;
    /** number of cols  */
    private static int cols;
    /** 2d array of char board  */
    private char board[][];
    /** cord that's been selected  */
    private Coordinates selectedCord;
    /** first val that's selected  */
    private char valStart;
    /** num of row that's been selected */
    private int row1;
    /** num of col that's been selected */
    private int col1;
    /** checks to see if the first cord has been selected */
    private boolean hasCords1 = false;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {

        this.observers.add(observer);

    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void notifyObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * HoppersModel constructor
     * @param filename: Filename of hoppers txt file
     * @throws IOException: If filename is wrong or can't be found
     */
    public HoppersModel(String filename) throws IOException {

        //Loads the current file name
        load(filename);

    }

    /**
     * Loads the current filename and the board
     * @param filename: Current filename that's been loaded
     * @throws IOException: If filename is wrong or can't be found
     */
    public void load(String filename) throws IOException {

        //Creates a new current config from HoppersConfig
        currentConfig = new HoppersConfig(filename);
        this.currentFilename = filename;

        rows = currentConfig.getRows();
        cols = currentConfig.getCols();
        board = currentConfig.getGrid();

        //Once the file is loaded, it will notify observers
        notifyObservers("Loaded: " + currentFilename);

    }

    /**
     * Resets an entire board to the start of the current board
     * @throws IOException: If filename is wrong or can't be found
     */
    public void reset() throws IOException {

        //New currentConfig will be updated
        currentConfig = new HoppersConfig(this.currentFilename);

        //Loads the new current file
        load(currentFilename);

        rows = currentConfig.getRows();
        cols = currentConfig.getCols();
        board = currentConfig.getGrid();

        hasCords1 = false;

        //Notifies observer that the puzzle has been reset
        notifyObservers("Puzzle reset!");

    }

    /**
     * Gives the hint to the user
     */
    public void hint() {

        //Creates a new solver for current config
        Solver solveHopperPuzzle = new Solver(currentConfig);

        //Gets the shortest path for the current config
        LinkedList<Configuration> resultingPath = (LinkedList<Configuration>) solveHopperPuzzle.solve(currentConfig);

        //If the path is null, then there's no possible solution
        if (resultingPath == null) {

            notifyObservers("No solution!");

        } else if (resultingPath.size() <= 1) {

            //It's already solved if the path size is less than or equal to 1
            notifyObservers("Already solved!");

        } else {

            //Picks up the index 1 of the current config in the shortest path
            this.currentConfig = (HoppersConfig) resultingPath.get(1);

            this.board = currentConfig.getGrid();
            notifyObservers("Next step!");

        }
    }

    /**
     * Select method that picks two cords to move
     * @param row: number of row that user selects
     * @param col: number of col that user selects
     */
    public void select(int row, int col) {

        String msg = "";

        //If the user didn't select first cords
        if (!hasCords1) {


            this.selectedCord = new Coordinates(row, col);

            //Value that the user selects
            valStart = this.board[row][col];

            //If the starting value is nto a frog, then it returns no frog at the specific cord message
            if (valStart == '.' || valStart == '*' || row >= rows || col >= cols) {

                msg = "No frog at " + selectedCord;

            } else {

                msg =  "Selected " + selectedCord;

                //Sets row1 as the first row cord
                this.row1 = row;

                //Sets col1 as the first col cord
                this.col1 = col;

                //Once it's been selected, it will be set to true
                hasCords1 = true;

            }

        } else {

            //Represents the second selected coordinates
            Coordinates endCord = new Coordinates(row, col);

            //Value that the frog will hop to, it must always be empty
            char valFinish = this.board[row][col];

            if (valFinish == '.') {

                //Gets the value that a selected frog will hop over
                int hopRow = (row1 + row) / 2;
                int hopCol = (col1 + col) / 2;

                char hopVal = this.board[hopRow][hopCol];

                //The value that a selected frog hops over must be a green frog
                if (hopVal == 'G') {

                    //Checks to see if the frog hops over diagonal, vertical, or horizontal way
                    if (diagonalCheck(row1, col1, row, col) || (straightCheck(row1, col1, row, col))) {

                        //Will set the value to where the frog hops to
                        this.board[row][col] = valStart;

                        //Will set the value the frog hops over to as empty
                        this.board[hopRow][hopCol] = '.';

                        //Sets the value to where the frog left to empty
                        this.board[row1][col1] = valFinish;

                        msg = "Jumped from " + selectedCord + "  to " + endCord;

                        //Since the frog hops over, it will allow user to select a new frog
                        hasCords1 = false;

                    }

                } else {

                    //If frog can't hop over, then the user has chance to select a new frog
                    hasCords1 = false;
                    msg = "Can't jump from " + selectedCord + " to " + endCord;

                }

            } else {

                //If frog can't hop over, then the user has chance to select a new frog
                hasCords1 = false;
                msg = "Can't jump from " + selectedCord + " to " + endCord;

            }

        }

        notifyObservers(msg);

    }

    /**
     * Checks to see if the frog hops over diagonal
     * @param row1: num of row for first selected cord
     * @param col1: num of col for first selected cord
     * @param row2: num of row for second selected cord
     * @param col2: num of col for second selected cord
     * @return: True if the frog hops over diagonal, false if it doesn't
     */
    private boolean diagonalCheck(int row1, int col1, int row2, int col2) {

        int rowCheck = row2 - row1;
        int colCheck = col2 - col1;

        if (rowCheck == -2 && colCheck == 2) {

            return true;

        } else if (rowCheck == 2 && colCheck == -2) {

            return true;

        } else if (rowCheck == 2 && colCheck == 2) {

            return true;

        } else if (rowCheck == -2 && colCheck == -2) {

            return true;

        }

        //If frog doesn't hop over diagonal, it will be false
        return false;
    }

    /**
     * Checks to see if frog hops over vertical or horizontal
     * @param row1: num of row for first selected cord
     * @param col1: num of col for first selected cord
     * @param row2: num of row for second selected cord
     * @param col2: num of col for second selected cord
     * @return: True if frog hops over vertical or horizontal, false if it doesn't
     */
    private boolean straightCheck(int row1, int col1, int row2, int col2) {

        int rowCheck = row2 - row1;
        int colCheck = col2 - col1;


        if (rowCheck == -4 && colCheck == 0) {

            return true;

        } else if (rowCheck == 0 && colCheck == -4) {

            return true;

        } else if (rowCheck == 4 && colCheck == 0) {

            return true;

        } else if (rowCheck == 0 && colCheck == 4) {

            return true;

        }

        //If frog doesn't hop over vertical or horizontal, then it will be false
        return false;
    }

    /**
     * Gets the value from the game board
     * @param row: number of row
     * @param col: number of col
     * @return: value with current row and col
     */
    public char getValue(int row, int col) {

        return this.board[row][col];

    }

    /**
     * Gets current HoppersConfig
     * @return: current HoppersConfig
     */
    public HoppersConfig getCurrentConfig() {

        return this.currentConfig;

    }

    /**
     * Builds the game board
     * @return: The entire game board of hoppers
     */
    public String toString() {

        StringBuilder result = new StringBuilder("   ");

        for (int col = 0; col < cols; col++) {

            result.append(col);

            if (col < cols - 1) {
                result.append(" ");
            }

        }

        result.append("\n");

        result.append("  ");

        int numDash = cols * 2;

        for (int col = 0; col < numDash; col++) {

            result.append("-");
        }

        result.append("\n");

        for (int row = 0; row < rows; row++) {

            result.append(row + "| ");

            for (int col = 0; col < cols; col++) {

                result.append(board[row][col]);

                if (col < cols - 1) {
                    result.append(" ");
                }

            }

            result.append("\n");
        }

        return result.toString();

    }

}
