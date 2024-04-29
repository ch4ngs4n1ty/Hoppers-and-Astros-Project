package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The representation of a clock configuration
 *
 * @author Ashwin Jagadeesh
 */
public class AstroConfig implements Configuration{

    private static int rows;
    private static int cols;

    private String[][] grid;

    private static Coordinates home;
    private Coordinates astonautLoc;

    private HashMap<String, Coordinates> explorers;

    /**
     * creates an initial config based on the file provided in the constructor
     * @param filename of the astro puzzle
     * @throws IOException if file is not found
     */
    public AstroConfig(String filename) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {

            //sets rows and column dimensions for the grid
            String line = in.readLine();
            String[] coordinates = line.split("\\s");

            this.rows = Integer.parseInt(coordinates[0]);
            this.cols = Integer.parseInt(coordinates[1]);

            this.grid = new String[rows][cols];

            //creates grid with the row and col dimensions filled with empty spaces
            for (int row = 0; row < this.rows; row++) {
                for (int col = 0; col < this.cols; col++) {
                    grid[row][col] = ".";
                }
            }

            // saves the home coordinates value to instance variable and adds it to grid

            String[] goalLocation = in.readLine().split("\\s+");
            home = new Coordinates(String.valueOf(goalLocation[1].charAt(0)), String.valueOf(goalLocation[1].charAt(2)));
            grid[home.row()][home.col()] = "*";

            // sets astonaut's location and adds it to grid
            explorers = new HashMap<>();

            String[] astronaut = in.readLine().split("\\s+");
            this.astonautLoc = new Coordinates(String.valueOf(astronaut[1].charAt(0)), String.valueOf(astronaut[1].charAt(2)));
            this.grid[this.astonautLoc.row()][this.astonautLoc.col()] = "A";
            explorers.put("A", astonautLoc);


            //reads the remaining lines anf adds each robot to a hashmap of robots mapping their name to coordinate
            int numRobots = Integer.parseInt(in.readLine());

            for(int i = 0; i < numRobots; i++){
                String[] robotFields = in.readLine().split(" ");
                Coordinates currentRobot = new Coordinates(String.valueOf(robotFields[1]));
                explorers.put(robotFields[0], currentRobot);

                //adds robots to grid
                this.grid[currentRobot.row()][currentRobot.col()] = robotFields[0];
            }
        }
    }


    /**
     * Copy Constructor that updates the grid  & explorer Hashmap with the explorer that was moved
     * @param other the copied config
     * @param explorer the explorer that's moved
     * @param coordinate the explorer's new coordinate
     */
    public AstroConfig(AstroConfig other, String explorer, Coordinates coordinate){
        this.explorers = new HashMap<>();
        for(Map.Entry<String, Coordinates> entry : other.explorers.entrySet()){
            this.explorers.put(entry.getKey(), new Coordinates(entry.getValue().row(), entry.getValue().col()));
        }

        Coordinates oldC = this.explorers.get(explorer);
        Coordinates newC = coordinate;

        this.explorers.put(explorer, coordinate);

        this.grid = new String[other.getNumRows()][other.getNumCols()];
        for(int row = 0; row < grid.length; row++){
            this.grid[row] = other.grid[row].clone();
        }

        this.grid[oldC.row()][oldC.col()] = ".";
        this.grid[newC.row()][newC.col()] = explorer;

        this.astonautLoc = new Coordinates(this.explorers.get("A").row(), this.explorers.get("A").col());

    }

    /**
     * getter method for row dimension
     * @return number rows for current config
     */
    public int getNumRows(){
        return this.rows;
    }

    /**
     * getter method for column dimension
     * @return number columns for current config
     */
    public int getNumCols(){
        return this.cols;
    }

    /**
     * get ths value at the coordinates requested
     * @param row
     * @param col
     * @return the string value of the given position on the grid
     */
    public String getVal(int row, int col) {
        return grid[row][col];
    }

    /**
     * retreives all the horizontal neighbors of given explorer
     * @param explorer to check
     * @param row the row of the explorer
     * @return Hashmap of neighboring explorers and their coordinates
     */
    public HashMap<String, Coordinates> getRowNeighbors(String explorer, int row){

        HashMap<String, Coordinates> rowNeigh = new HashMap<>();

       for(Map.Entry<String, Coordinates> currExplorer: explorers.entrySet()){
           if(currExplorer.getValue().row() == row
           && !(currExplorer.getKey().equals(explorer))) {
               rowNeigh.put(currExplorer.getKey(), currExplorer.getValue());
           }
       }
        return rowNeigh;
    }

    /**
     * retreives all the vertical neighbors of given explorer
     * @param explorer to check
     * @param col the column of the explorer
     * @return Hashmap of neighboring explorers and their coordinates
     */
    public HashMap<String, Coordinates> getColNeighbors(String explorer, int col){


        HashMap<String, Coordinates> colNeigh = new HashMap<>();

        for(Map.Entry<String, Coordinates> currExplorer: explorers.entrySet()){
            if(currExplorer.getValue().col() == col
                    && !(currExplorer.getKey().equals(explorer))) {
                colNeigh.put(currExplorer.getKey(), currExplorer.getValue());
            }
        }
        return colNeigh;
    }

    @Override
    public boolean isSolution() {
        Coordinates astroLoc = explorers.get("A");
        return astonautLoc.equals(home);
    }


    /**
     * finds all the neighbors to left of given explorer
     * @param explorerCoord coordinate of explorer we are trying to find left neighbors of
     * @param rowNeighbors hashmap of all the horizontal neighbors of the given explorer
     * @return an arraylist containing coordinates of all left neighbors of given explorer
     */
    public ArrayList<Coordinates> findLeftNeighbors(Coordinates explorerCoord, HashMap<String, Coordinates> rowNeighbors){
        ArrayList<Coordinates> leftNeighbors = new ArrayList<>();
        for (Coordinates neighborCoord : rowNeighbors.values()){
            if(neighborCoord.col() < explorerCoord.col()){
                leftNeighbors.add(neighborCoord);
            }
        }
        return leftNeighbors;
    }

    /**
     * finds all the neighbors to right of given explorer
     * @param explorerCoord coordinate of explorer we are trying to find righ neighbors of
     * @param rowNeighbors hashmap of all the horizontal neighbors of the given explorer
     * @return an arraylist containing coordinates of all right neighbors of given explorer
     */    public ArrayList<Coordinates> findRightNeighbors(Coordinates explorerCoord, HashMap<String, Coordinates> rowNeighbors){
        ArrayList<Coordinates> rightNeighbors = new ArrayList<>();
        for (Coordinates neighborCoord : rowNeighbors.values()){
            if(neighborCoord.col() > explorerCoord.col()){
                rightNeighbors.add(neighborCoord);
            }
        }
        return rightNeighbors;
    }

    /**
     * finds all the neighbors north of given explorer
     * @param explorerCoord coordinate of explorer we are trying to find north neighbors of
     * @param colNeighbors hashmap of all the vertical neighbors of the given explorer
     * @return an arraylist containing coordinates of all north neighbors of given explorer
     */
    public   ArrayList<Coordinates> findTopNeighbors(Coordinates explorerCoord, HashMap<String, Coordinates> colNeighbors){
        ArrayList<Coordinates> topNeighbors = new ArrayList<>();
        for (Coordinates neighborCoord : colNeighbors.values()){
            if(neighborCoord.row() < explorerCoord.row()){
                topNeighbors.add(neighborCoord);
            }
        }
        return topNeighbors;
    }

    /**
     * finds all the neighbors south of given explorer
     * @param explorerCoord coordinate of explorer we are trying to find south neighbors of
     * @param colNeighbors hashmap of all the vertical neighbors of the given explorer
     * @return an arraylist containing coordinates of all south neighbors of given explorer
     */
    public   ArrayList<Coordinates> findBottomNeighbors(Coordinates explorerCoord, HashMap<String, Coordinates> colNeighbors){
        ArrayList<Coordinates> bottomNeighbors = new ArrayList<>();
        for (Coordinates neighborCoord : colNeighbors.values()){
            if(neighborCoord.row() > explorerCoord.row()){
                bottomNeighbors.add(neighborCoord);
            }
        }
        return bottomNeighbors;
    }

    @Override
    public Collection<Configuration> getNeighbors() {

        LinkedHashSet<Configuration> neighbors= new LinkedHashSet<>();

        //traverses through each explorer and creates configuration for each possible move
        for(Map.Entry<String, Coordinates> explorer: explorers.entrySet()){
            Coordinates explorerCoord = explorer.getValue(); // explorer's current coordinate

            // HORIZONTAL MOVEMENTS

            HashMap<String, Coordinates> rowNeighbors = getRowNeighbors(explorer.getKey(), explorerCoord.row());

            // moves current explorer if possible
            if(!(rowNeighbors.isEmpty())){

                ArrayList<Coordinates> rightNeighbors = findRightNeighbors(explorerCoord, rowNeighbors);
                ArrayList<Coordinates> leftNeighbors = findLeftNeighbors(explorerCoord, rowNeighbors);

                // checks the arraylist of left neighbors and adds a config with the left most movement of the explorer
                if(!leftNeighbors.isEmpty()){
                    Coordinates leftMost = leftNeighbors.getFirst();
                    for(Coordinates neighbor : leftNeighbors){
                        if(neighbor.col() > leftMost.col()){
                            leftMost = neighbor;
                        }
                    }
                    neighbors.add(new AstroConfig(this, explorer.getKey(), new Coordinates(explorerCoord.row(), leftMost.col()+ 1)));
                }

                // checks the arraylist of right neighbors and adds a config with the right most movement of the explorer
                if(!rightNeighbors.isEmpty()){
                    Coordinates rightMost = rightNeighbors.get(0);
                    for(Coordinates neighbor: rightNeighbors){
                        if(neighbor.col() < rightMost.col()){
                            rightMost = neighbor;
                        }
                    }
                    neighbors.add(new AstroConfig(this, explorer.getKey(), new Coordinates(explorerCoord.row(), rightMost.col()-1)));
                }
            }

            //VERTICAL MOVEMENTS
            HashMap<String, Coordinates> colNeighbors = getColNeighbors(explorer.getKey(), explorerCoord.col());

            // moves current explorer if possible
            if(!colNeighbors.isEmpty()){

                ArrayList<Coordinates> topNeighbors = findTopNeighbors(explorerCoord, colNeighbors);
                ArrayList<Coordinates> bottomNeighbors = findBottomNeighbors(explorerCoord, colNeighbors);

                // checks the arraylist of top neighbors and adds a config with the top most movement of the explorer
                if(!topNeighbors.isEmpty()){
                    Coordinates topMost = topNeighbors.getFirst();
                    for(Coordinates neighbor : topNeighbors){
                        if(neighbor.row() > topMost.row()){
                            topMost = neighbor;
                        }
                    }
                    neighbors.add(new AstroConfig(this, explorer.getKey(), new Coordinates(topMost.row() + 1, explorerCoord.col())));
                }

                // checks the arraylist of bottom neighbors and adds a config with the bottom most movement of the explorer
                if(!bottomNeighbors.isEmpty()){
                    Coordinates bottomMost = bottomNeighbors.getFirst();
                    for(Coordinates neighbor : bottomNeighbors){
                        if(neighbor.row() < bottomMost.row()){
                            bottomMost = neighbor;
                        }
                    }
                    neighbors.add(new AstroConfig(this, explorer.getKey(), new Coordinates(bottomMost.row() - 1, explorerCoord.col())));
                }
            }
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {

        Boolean result = false;

        if(other instanceof AstroConfig){
            AstroConfig otherAstroConfig = (AstroConfig) other;
            result = Arrays.deepEquals(this.grid, otherAstroConfig.grid);
        }
        return result;
    }

    @Override
    public int hashCode() { return Arrays.deepHashCode(this.grid); }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row=0; row<getNumRows(); ++row) {
            for (int col=0; col<getNumCols(); ++col) {
                result.append(getVal(row, col));
                if (col<= getNumCols()-1) {
                    result.append(" ");
                }
            }
            result.append(System.lineSeparator());
        }

        return result.toString();
    }

    /**
     * getter method for the grid of the board
     * @return the grid of current configuration
     */
    public String[][] getGrid() {
        return this.grid;
    }
}
