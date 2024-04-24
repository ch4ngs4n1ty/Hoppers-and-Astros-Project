package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AstroModel {
    /** the collection of observers of this model */
    private final List<Observer<AstroModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private AstroConfig currentConfig;
    private static int rows;
    private static int cols;
    private String board[][];

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<AstroModel, String> observer) {
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


    public AstroModel(String filename) throws IOException {
        currentConfig = new AstroConfig(filename);
        System.out.println("Loaded: " + filename);

        rows = currentConfig.getNumRows();
        cols = currentConfig.getNumCols();
        board = currentConfig.getGrid();

        System.out.println(toString());
    }

    public void select(int row, int col){
        Coordinates selectedCoord = new Coordinates(row, col);

        String msg = "";
        if(this.board[row][col].equals(".")){
            msg = "No piece at " + selectedCoord;
        }
        else{
            msg =  "Selected: " + selectedCoord;
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
