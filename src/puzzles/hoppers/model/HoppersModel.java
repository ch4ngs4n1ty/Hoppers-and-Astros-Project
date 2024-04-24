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

        System.out.println("Loaded: " + filename);

        currentConfig = new HoppersConfig(filename);
        this.currentFilename = filename;

        rows = currentConfig.getRows();
        cols = currentConfig.getCols();
        board = currentConfig.getGrid();

        copyBoard = new char[rows][];
        for (int i = 0; i < rows; i++) {
            copyBoard[i] = Arrays.copyOf(board[i], board[i].length);
        }


        System.out.println(toString());

    }

    public void reset() throws IOException {

        currentConfig = new HoppersConfig(this.currentFilename);
        rows = currentConfig.getRows();
        cols = currentConfig.getCols();
        board = currentConfig.getGrid();

//        board = new char[rows][cols];
//
//        for (int i = 0; i < rows; i++) {
//            board[i] = Arrays.copyOf(currentConfig.getGrid()[i], cols);
//        }

        hasCords1 = false;

        System.out.println("Puzzle reset!");
        System.out.println(toString());

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

    public void select(int row, int col) {

        String msg = "";

        if (!hasCords1) {

            this.selectedCord = new Coordinates(row, col);
            valStart = this.board[row][col];

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

                int hopRow = (row1 + row) / 2;
                int hopCol = (col1 + col) / 2;

                if (this.board[hopRow][hopCol] == 'G' || this.board[hopRow][hopCol] == 'R') {

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
