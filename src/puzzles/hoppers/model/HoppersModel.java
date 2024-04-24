package puzzles.hoppers.model;

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
    private static int rows;
    private static int cols;
    private char board[][];
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

        System.out.println("Loaded: " + filename);
        currentConfig = new HoppersConfig(filename);

        rows = currentConfig.getRows();
        cols = currentConfig.getCols();
        board = currentConfig.getGrid();

        System.out.println(toString());

    }

    public void reset() {

    }

    public void hint() throws IOException {

        Solver hoppersPath = new Solver(currentConfig);
        ArrayList<Configuration> hintsList = new ArrayList<>(hoppersPath.solve(currentConfig));

        if (hintsList == null) {

            System.out.println("No solution");

        } else {

            //this.currentConfig = new HoppersConfig(hintsList.get(1).toString());
            System.out.println(hintsList.get(1).toString());
            //System.out.println(toString());

        }
    }

    public void select(int row, int col) throws IOException {

        String msg = "";

        if (!hasCords1) {

            this.selectedCord = new Coordinates(row, col);
            valStart = this.board[row][col];

            if (valStart == '.' || valStart == '*' || row > rows-1 || col > cols-1) {

                msg = "No piece at " + selectedCord;

            } else {

                msg =  "Selected: " + selectedCord;
                this.row1 = row;
                this.col1 = col;
                hasCords1 = true;

            }

        } else {

            Coordinates endCord = new Coordinates(row, col);
            char valFinish = this.board[row][col];

            msg = "Jumped from " + selectedCord + " to " + endCord;

            this.board[row][col] = valStart;
            this.board[(row1 + row)/2][(col1 + col)/2] = '.';
            this.board[row1][col1] = valFinish;

            hasCords1 = false;

        }

        notifyObservers(msg);

    }


    public String toString() {

        StringBuilder result = new StringBuilder("   ");

        for (int col = 0; col < cols; col++) {

            result.append(col + " ");

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

                result.append(board[row][col] + " ");

            }

            result.append("\n");
        }

        return result.toString();

    }

}
