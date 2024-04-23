package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// TODO: implement your HoppersConfig for the common solver

public class AstroConfig implements Configuration{

    private static int rows;
    private static int cols;

    private String[][] grid;

    private static Coordinates home;
    private Coordinates astonautLoc;

    private HashMap<String, Coordinates> explorers;

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
    private AstroConfig(AstroConfig other, String explorer, Coordinates coordinate){

//        this.explorers = other.explorers;
//
//        Coordinates oldC = explorers.get(explorer);
//        Coordinates newC = coordinate;
//        //this.explorers.put(explorer, coordinate);
//
//
//        this.grid = new String[other.getNumRows()][other.getNumCols()];
//
//        for(int row = 0; row < grid.length; row++){
//            this.grid[row] = other.grid[row].clone();
//        }
//
//        this.grid[newC.row()][newC.col()] = explorer;
//        this.grid[oldC.row()][oldC.col()] = ".";
//
//
//        this.astonautLoc = new Coordinates(coordinate.row(), coordinate.col());

        //this.explorers.put(explorer, coordinate);

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

        this.grid[newC.row()][newC.col()] = explorer;
        this.grid[oldC.row()][oldC.col()] = ".";

        this.astonautLoc = new Coordinates(this.explorers.get("A").row(), this.explorers.get("A").col());

    }

    public int getNumRows(){
        return this.rows;
    }
    public int getNumCols(){
        return this.cols;
    }
    public HashMap<String, Coordinates> getExplorers(AstroConfig astroConfig){
        return this.explorers;
    }

    public ArrayList<Integer> getRowIndexes(HashMap<String, Coordinates> rowNeighbors){
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        for(Map.Entry<String, Coordinates> neighbors: rowNeighbors.entrySet()){
            rowIndexes.add(neighbors.getValue().col());
        }
        Collections.sort(rowIndexes);

        return rowIndexes;
    }

    public ArrayList<Integer> getColIndexes(HashMap<String, Coordinates> colNeighbors){
        ArrayList<Integer> colIndexes = new ArrayList<>();

        for(Map.Entry<String, Coordinates> neighbors: colNeighbors.entrySet()){
            colIndexes.add(neighbors.getValue().row());
        }
        Collections.sort(colIndexes);

        return colIndexes;
    }

    public String[] getColVals(int cols){
        String[] colVals = new String[getNumCols()];
        for(int i = 0; i < grid.length; i ++){
            colVals[i] = grid[i][cols];
        }
        return colVals;
    }
    public String getVal(int row, int col) {
        return grid[row][col];
    }

    /**
     * retreives all the horizontal neighbors of given explorer
     * @param explorer to check
     * @param row the row of the explorer
     * @return Hashmap of neighboring explorer and their coordinates
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

    // Method to find the left neighbor(s) of the current explorer
    private ArrayList<Coordinates> findLeftNeighbors(Coordinates explorerCoord, HashMap<String, Coordinates> rowNeighbors){
        ArrayList<Coordinates> leftNeighbors = new ArrayList<>();
        for (Coordinates neighborCoord : rowNeighbors.values()){
            if(neighborCoord.col() < explorerCoord.col()){
                leftNeighbors.add(neighborCoord);
            }
        }
        return leftNeighbors;
    }

    // Method to find the right neighbor(s) of the current explorer
    private ArrayList<Coordinates> findRightNeighbors(Coordinates explorerCoord, HashMap<String, Coordinates> rowNeighbors){
        ArrayList<Coordinates> rightNeighbors = new ArrayList<>();
        for (Coordinates neighborCoord : rowNeighbors.values()){
            if(neighborCoord.col() > explorerCoord.col()){
                rightNeighbors.add(neighborCoord);
            }
        }
        return rightNeighbors;
    }

    private  ArrayList<Coordinates> findTopNeighbors(Coordinates explorerCoord, HashMap<String, Coordinates> colNeighbors){
        ArrayList<Coordinates> topNeighbors = new ArrayList<>();
        for (Coordinates neighborCoord : colNeighbors.values()){
            if(neighborCoord.row() < explorerCoord.row()){
                topNeighbors.add(neighborCoord);
            }
        }
        return topNeighbors;
    }
    private  ArrayList<Coordinates> findBottomNeighbors(Coordinates explorerCoord, HashMap<String, Coordinates> colNeighbors){
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

                if(!leftNeighbors.isEmpty()){
                    Coordinates leftMost = leftNeighbors.getFirst();
                    for(Coordinates neighbor : leftNeighbors){
                        if(neighbor.col() > leftMost.col()){
                            leftMost = neighbor;
                        }
                    }
                    neighbors.add(new AstroConfig(this, explorer.getKey(), new Coordinates(explorerCoord.row(), leftMost.col()+ 1)));
                }

                if(!rightNeighbors.isEmpty()){
                    Coordinates rightMost = rightNeighbors.get(0);
                    for(Coordinates neighbor: rightNeighbors){
                        if(neighbor.col() < rightMost.col()){
                            rightMost = neighbor;
                        }
                    }
                    neighbors.add(new AstroConfig(this, explorer.getKey(), new Coordinates(explorerCoord.row(), rightMost.col()-1)));
                }
                /**

                //creates temporary coordinates for the possible left and right movements
                Coordinates rightMost = null;
                Coordinates leftMost = null;

                //traverses the neighbors in the same row
                for(Map.Entry<String, Coordinates> neighbor: rowNeighbors.entrySet()){
                    Coordinates currNeighCoord = neighbor.getValue();

                    ArrayList<Integer> rowNeighIndexes = getRowIndexes(rowNeighbors); // gets the indexes of all row neighbors

                    // true if the current explorer is in between two neighbors
                    if(rowNeighIndexes.getFirst() < explorerCoord.col()
                            && rowNeighIndexes.getLast() > explorerCoord.col()){
                        if((explorerCoord.col() - rowNeighIndexes.getFirst() > 1)){
                            leftMost = new Coordinates(explorerCoord.row(), rowNeighIndexes.getFirst() + 1);
                        }

                        if(rowNeighIndexes.getLast()- explorerCoord.col() > 1){
                            rightMost = new Coordinates(explorerCoord.row(), rowNeighIndexes.getLast() - 1 );
                        }
                    }

                    //true if current explorer has only 1 neighbor
                    else{
                        if((explorerCoord.col() - rowNeighIndexes.getLast() > 1)){
                            leftMost = new Coordinates(explorerCoord.row(), rowNeighIndexes.getLast() + 1);
                        }

                        if(rowNeighIndexes.getFirst()- explorerCoord.col() > 1){
                            rightMost = new Coordinates(explorerCoord.row(), rowNeighIndexes.getFirst() - 1 );
                        }
                    }
                }
                // adds possible configurations to list of configs
                if(leftMost != null){
                    neighbors.add(new AstroConfig(this, explorer.getKey(), leftMost));
                    //break;
                }

                if(rightMost != null) {
                    neighbors.add(new AstroConfig(this, explorer.getKey(), rightMost));
                }
            }
*
            // VERTICAL MOVEMENTS

            HashMap<String, Coordinates> colNeighbors = getColNeighbors(explorer.getKey(), explorerCoord.col());

            // moves current explorer if possible
            if(!(colNeighbors.isEmpty())) {

                //creates temporary coordinates for the possible left and right movements
                Coordinates topMost = null;
                Coordinates bottomMost = null;

                //traverses the neighbors in the same column
                for (Map.Entry<String, Coordinates> neighbor : colNeighbors.entrySet()) {
                    Coordinates currNeighCoord = neighbor.getValue();

                    ArrayList<Integer> colNeighIndexes = getColIndexes(colNeighbors); // gets the indexes of all column neighbors

                    // true if the current explorer is in between two neighbors
                    if (colNeighIndexes.getFirst() < explorerCoord.row()
                            && colNeighIndexes.getLast() > explorerCoord.row()) {

                        if ((explorerCoord.row() - colNeighIndexes.getFirst() > 1)) {
                            //topMost = new Coordinates(explorerCoord.row(), colNeighIndexes.getFirst() + 1);
                            topMost = new Coordinates(colNeighIndexes.getFirst() + 1, explorerCoord.col());
                        }

                        if (colNeighIndexes.getLast() - explorerCoord.row() > 1) {
                            //bottomMost = new Coordinates(explorerCoord.row(), colNeighIndexes.getLast() - 1 );
                            bottomMost = new Coordinates(colNeighIndexes.getLast() - 1, explorerCoord.col());
                        }
                    } else { // true if current explorer has only 1 vertical neighbor
                        if (explorerCoord.row() - colNeighIndexes.getFirst() > 1) { //
                            //if((colNeighIndexes.getLast() - explorerCoord.row() > 1)){
                            topMost = new Coordinates(colNeighIndexes.getFirst() + 1, explorerCoord.col());//explorerCoord.row(), colNeighIndexes.getLast() + 1);
                            break;
                        }
                        if ((colNeighIndexes.getFirst() - explorerCoord.row() > 1)) {
                            bottomMost = new Coordinates(colNeighIndexes.getFirst() - 1, explorerCoord.col());
                        } else if ((colNeighIndexes.getLast() - explorerCoord.row() > 1) && colNeighIndexes.size() == 1) {
                            //if(explorerCoord.row()- colNeighIndexes.getLast() > 1){
                            bottomMost = new Coordinates(colNeighIndexes.getLast() - 1, explorerCoord.col());
                        }
//                        else{
//                            bottomMost = new Coordinates(colNeighIndexes.getFirst()-1, explorerCoord.col());
//                        }
                    }
                }
                if (topMost != null) {
                    neighbors.add(new AstroConfig(this, explorer.getKey(), topMost));
                    //break;
                }

                if (bottomMost != null) {
                    neighbors.add(new AstroConfig(this, explorer.getKey(), bottomMost));
                }
            }
            }
        }*/
            }
            HashMap<String, Coordinates> colNeighbors = getColNeighbors(explorer.getKey(), explorerCoord.col());
            if(!colNeighbors.isEmpty()){
                ArrayList<Coordinates> topNeighbors = findTopNeighbors(explorerCoord, colNeighbors);
                ArrayList<Coordinates> bottomNeighbors = findBottomNeighbors(explorerCoord, colNeighbors);

                if(!topNeighbors.isEmpty()){
                    Coordinates topMost = topNeighbors.getFirst();
                    for(Coordinates neighbor : topNeighbors){
                        if(neighbor.row() > topMost.row()){
                            topMost = neighbor;
                        }
                    }
                    neighbors.add(new AstroConfig(this, explorer.getKey(), new Coordinates(topMost.row() + 1, explorerCoord.col())));
                }
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

//    public Coordinates getAstonautLoc(AstroConfig astroConfig){
//        return this.astonautLoc;
//    }

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
        //return null;
    }
}
