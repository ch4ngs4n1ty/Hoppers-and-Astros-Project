package puzzles.hoppers.model;

import puzzles.astro.model.AstroConfig;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    private String currentFilename;
    private static int rows;
    private static int cols;
    private char board[][];
    private char copyBoard[][];
    private Coordinates selectedCord;
    private char valStart;
    private int row1;
    private int col1;
    private int row2;
    private int col2;
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

    public HoppersModel(String filename) throws IOException {

        //Loads the current file name
        load(filename);

    }


    public void load(String filename) throws IOException {

        currentConfig = new HoppersConfig(filename);
        this.currentFilename = filename;

        rows = currentConfig.getRows();
        cols = currentConfig.getCols();
        board = currentConfig.getGrid();

        //notifyObservers("Loaded: ");

        System.out.println("Loaded: " + currentFilename);
        System.out.println(toString());


    }

    /**
     * Resets an entire board to the start of the current board
     * @throws IOException
     */
    public void reset() throws IOException {

        currentConfig = new HoppersConfig(this.currentFilename);

        load(currentFilename);

        rows = currentConfig.getRows();
        cols = currentConfig.getCols();
        board = currentConfig.getGrid();

        hasCords1 = false;

        System.out.println("Puzzle reset!");

    }

    public void hint(){
        Solver solveHopperPuzzle = new Solver(currentConfig);
        LinkedList<Configuration> resultingPath = (LinkedList<Configuration>) solveHopperPuzzle.solve(currentConfig);
        if(resultingPath == null){
            notifyObservers("No solution");
        }
        else{

            this.currentConfig = (HoppersConfig) resultingPath.get(1);
            this.board = currentConfig.getGrid();
            notifyObservers("Next step!");

        }
    }


    public void select(int row, int col) {

        String msg = "";

        if (!hasCords1) {

            this.selectedCord = new Coordinates(row, col);
            valStart = this.board[row][col];

            //If the starting value is nto a frog, then it returns no frog at the specific cord message
            if (valStart == '.' || valStart == '*' || row > rows-1 || col > cols-1) {

                msg = "No frog at " + selectedCord;

            } else {

                msg =  "Selected " + selectedCord;
                this.row1 = row;
                this.col1 = col;
                hasCords1 = true;

            }

        } else {

            Coordinates endCord = new Coordinates(row, col);
            char valFinish = this.board[row][col];

            if (valFinish == '.') {

                //Gets the value that a selected frog will hop over
                int hopRow = (row1 + row) / 2;
                int hopCol = (col1 + col) / 2;

                //The value that a selected frog hops over must be a green frog
                if (this.board[hopRow][hopCol] == 'G') {

                    this.board[row][col] = valStart;
                    this.board[hopRow][hopCol] = '.';
                    this.board[row1][col1] = valFinish;

                    msg = "Jumped from " + selectedCord + "  to " + endCord;

                    hasCords1 = false;

                } else {

                    msg = "Can't jump from " + selectedCord + " to " + endCord;

                }

            } else {

                msg = "Can't jump from " + selectedCord + " to " + endCord;

            }

        }

        notifyObservers(msg);

    }

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
