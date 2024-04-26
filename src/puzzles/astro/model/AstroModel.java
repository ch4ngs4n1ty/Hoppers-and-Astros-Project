package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.*;

public class AstroModel {
    /** the collection of observers of this model */
    private final List<Observer<AstroModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private AstroConfig currentConfig;
    private static int rows;
    private static int cols;
    private String board[][];

    private Coordinates selectedCoordinate;

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

        String pos = this.board[row][col];
        if (pos.equals(".") || pos.equals("*") || row > rows-1 || col > cols-1){
            msg = "No piece at " + selectedCoord;
        }
        else{
            msg =  "Selected " + selectedCoord;
            selectedCoordinate = selectedCoord;
        }
        notifyObservers(msg);
    }

    public void move(String m) {
        if (selectedCoordinate == null) {
            notifyObservers("No piece selected");
            return;
        }

        String explorer = board[selectedCoordinate.row()][selectedCoordinate.col()];
        Coordinates newC = null;

        if (m.equals("n")) {
            newC = moveNorth(explorer);
        } else if (m.equals("s")) {
            newC = moveSouth(explorer);
        } else if (m.equals("e")) {
            newC = moveEast(explorer);
        } else if (m.equals("w")) {
            newC = moveWest(explorer);
        } else {
            return;
        }

        if (newC == null || newC.equals(selectedCoordinate)) {
            if (newC == null || newC.equals(selectedCoordinate)) {
                String direction;
                if (m.equals("n")) {
                    direction = "NORTH";
                } else if (m.equals("e")) {
                    direction = "EAST";
                } else if (m.equals("w")) {
                    direction = "WEST";
                } else {
                    direction = "SOUTH";
                }
                notifyObservers("Can't move piece at " + selectedCoordinate + " " + direction);
            }        } else {
            currentConfig = new AstroConfig(this.currentConfig, explorer, newC);
            board = currentConfig.getGrid();
            notifyObservers("Moved from " + selectedCoordinate + " to " + newC);
        }
        selectedCoordinate = null;
    }



    public Coordinates moveNorth(String explorer){
        HashMap<String, Coordinates> colNeigh= currentConfig.getColNeighbors(explorer, selectedCoordinate.col());
        if(colNeigh.isEmpty()){
            return selectedCoordinate;
        }
        ArrayList<Coordinates> topNeighbors = currentConfig.findTopNeighbors(selectedCoordinate, colNeigh);
        Coordinates topMost = topNeighbors.getFirst();
        if(!topNeighbors.isEmpty()){
            for(Coordinates neighbor : topNeighbors){
                if(neighbor.row() > topMost.row()){
                    topMost = neighbor;
                }
            }
            topMost = new Coordinates(topMost.row() + 1, topMost.col());
        }
        return topMost;
    }

    public Coordinates moveSouth(String explorer) {
        HashMap<String, Coordinates> colNeigh = currentConfig.getColNeighbors(explorer, selectedCoordinate.col());
        if (colNeigh.isEmpty()) {
            // Return the current position if there are no neighbors
            return selectedCoordinate;
        }

        ArrayList<Coordinates> bottomNeighbors = currentConfig.findBottomNeighbors(selectedCoordinate, colNeigh);
        if (bottomNeighbors.isEmpty()) {
            // If there are column neighbors but no bottom neighbors, return the current position
            return selectedCoordinate;
        }

        // Find the bottommost neighbor
        Coordinates bottomMost = bottomNeighbors.get(0);
        for (Coordinates neighbor : bottomNeighbors) {
            if (neighbor.row() < bottomMost.row()) {
                bottomMost = neighbor;
            }
        }

        // Decrement the row of the bottommost neighbor to move south
        return new Coordinates(bottomMost.row() - 1, bottomMost.col());
    }

    public Coordinates moveEast(String explorer) {
        HashMap<String, Coordinates> rowNeigh = currentConfig.getRowNeighbors(explorer, selectedCoordinate.row());
        if (rowNeigh.isEmpty()) {
            // Return the current position if there are no neighbors
            return selectedCoordinate;
        }

        ArrayList<Coordinates> rightNeighbors = currentConfig.findRightNeighbors(selectedCoordinate, rowNeigh);
        if (rightNeighbors.isEmpty()) {
            // If there are row neighbors but no right neighbors, return the current position
            return selectedCoordinate;
        }

        // Find the rightmost neighbor
        Coordinates rightMost = rightNeighbors.get(0);
        for (Coordinates neighbor : rightNeighbors) {
            if (neighbor.col() < rightMost.col()) {
                rightMost = neighbor;
            }
        }

        // Increment the column of the rightmost neighbor to move east
        return new Coordinates(rightMost.row(), rightMost.col() -1);
    }

    public Coordinates moveWest(String explorer) {
        HashMap<String, Coordinates> rowNeigh = currentConfig.getRowNeighbors(explorer, selectedCoordinate.row());
        if (rowNeigh.isEmpty()) {
            // Return the current position if there are no neighbors
            return selectedCoordinate;
        }

        ArrayList<Coordinates> leftNeighbors = currentConfig.findLeftNeighbors(selectedCoordinate, rowNeigh);
        if (leftNeighbors.isEmpty()) {
            // If there are row neighbors but no left neighbors, return the current position
            return selectedCoordinate;
        }

        // Find the leftmost neighbor
        Coordinates leftMost = leftNeighbors.get(0);
        for (Coordinates neighbor : leftNeighbors) {
            if (neighbor.col() > leftMost.col()) {
                leftMost = neighbor;
            }
        }

        // Decrement the column of the leftmost neighbor to move west
        return new Coordinates(leftMost.row(), leftMost.col() + 1);
    }

    public void hint(){
        Solver solveAstroPuzzle = new Solver(currentConfig);
        LinkedList<Configuration> resultingPath = (LinkedList<Configuration>) solveAstroPuzzle.solve(currentConfig);
        if(resultingPath == null){
            notifyObservers("No solution");
        }
        else{
            this.currentConfig = (AstroConfig) resultingPath.get(1);
            this.board = currentConfig.getGrid();
            notifyObservers("Next step!");
        }
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
