package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.*;

public class AstroModel {
    /** the collection of observers of this model */
    private final List<Observer<AstroModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private AstroConfig currentConfig;
    private String currentFilename;

    private static int rows;
    private static int cols;
    private String board[][];

    private Coordinates selectedCoordinate;
    private Boolean reachedSolution;

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

    /**
     * creates a new game
     * @param filename of the astro puzzle to load
     * @throws IOException if file is not found
     */
    public AstroModel(String filename) throws IOException {
        load(filename);
    }

    /**
     * retrieves the current configuration of the grif
     * @return currentConfig
     */
    public AstroConfig getCurrentConfig(){
        return this.currentConfig;
    }

    /**
     * loads the variables for the new puzzle and notifies observer of the loaded message
     * @param filename of the new puzzle
     * @throws IOException if file is not found
     */
    public void load(String filename) throws IOException {

        currentConfig = new AstroConfig(filename);
        this.currentFilename = filename;

        rows = currentConfig.getNumRows();
        cols = currentConfig.getNumCols();
        board = currentConfig.getGrid();
        this.reachedSolution = false;

        notifyObservers("Loaded: " + currentFilename);
    }

    /**
     * retrives the string value of the specified position given coordinates
     * @param row of rid
     * @param col of grid
     * @return value at the position
     */
    public String getVal(int row, int col){
        return board[row][col];
    }

    /**
     * gets the number of rows
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * gets the number of cols
     * @return the cols
     */
    public int getCols() {
        return cols;
    }


    /**
     * method to select the position on the game board
     * @param row of position
     * @param col of position
     */
    public void select(int row, int col){
        if (!reachedSolution){
            Coordinates selectedCoord = new Coordinates(row, col);

            String msg = "";

            String pos = this.board[row][col];

            //doesn't allow to select a point where there are no explorers
            if (pos.equals(".") || pos.equals("*") || row > rows-1 || col > cols-1){
                msg = "No piece at " + selectedCoord;
            }
            else{
                msg =  "Selected " + selectedCoord;
                selectedCoordinate = selectedCoord;
            }
            notifyObservers(msg);
        } else {
            notifyObservers("Already solved");
        }

    }

    /**
     * checks if the astronaut has reached the home position
     * @return whethere astronaut is at home or nah
     */
    public boolean reachedSolution(){
        return this.reachedSolution;
    }

    /**
     * Resets an entire board to the start of the current board
     * @throws IOException
     */
    public void reset() throws IOException {
        reachedSolution = false;

        currentConfig = new AstroConfig(this.currentFilename);

        load(currentFilename);

        rows = currentConfig.getNumRows();
        cols = currentConfig.getNumCols();
        board = currentConfig.getGrid();
        notifyObservers("Puzzle reset");
    }

    /**
     * moves the selected explorer to the specified cardinal direction
     * @param m is the string value of north south east or west
     */
    public void move(String m) {
        if(reachedSolution){ // doesn't move if the puzzle is already solved
            notifyObservers("Already solved");
            return;
        }
        if (selectedCoordinate == null) { //doesn't move if the piece isn't selected
            notifyObservers("No piece selected");
            return;
        }

        String explorer = board[selectedCoordinate.row()][selectedCoordinate.col()];
        Coordinates newC = null;


        // calls teh corresponding helper method for move
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

        // notifies observer if the postion moved is a new spot and based on the corresponding move direction
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
                notifyObservers("Cant move piece at " + selectedCoordinate + " " + direction);
            }        } else {
            currentConfig = new AstroConfig(this.currentConfig, explorer, newC);
            board = currentConfig.getGrid();
            notifyObservers("Moved from " + selectedCoordinate + "  to " + newC);
            if(currentConfig.isSolution()){
                reachedSolution = true;
            }
        }
        selectedCoordinate = null;
    }

    /**
     * moved the explorer to the south-most position
     * @param explorer to move
     * @return the coordinate of the south-most possible movement
     */
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

    /**
     * moved the explorer to the east-most position
     * @param explorer to move
     * @return the coordinate of the east-most possible movement
     */
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

    /**
     * moved the explorer to the west-most position
     * @param explorer to move
     * @return the coordinate of the west-most possible movement
     */
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

    /**
     * moved the explorer north to the north most position
     * @param explorer to move
     * @return the coordinate of the topmost possible movement
     */
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

    /**
     * sets the config the next config of the shortest path to solution (if a path exists)
     */
    public void hint(){
        if(!reachedSolution){
            Solver solveAstroPuzzle = new Solver(currentConfig);
            LinkedList<Configuration> resultingPath = (LinkedList<Configuration>) solveAstroPuzzle.solve(currentConfig);
            if(resultingPath == null){
                notifyObservers("No solution");
            }
            else{
                this.currentConfig = (AstroConfig) resultingPath.get(1);
                this.board = currentConfig.getGrid();
                notifyObservers("Next step!");
                if(currentConfig.isSolution()){
                    reachedSolution = true;
                }
            }
        }
        else{
            notifyObservers("Already Solved");
        }
    }

    /**
     * tostring of the grid layout and dimensions
     * @return string value of the board layout
     */
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
