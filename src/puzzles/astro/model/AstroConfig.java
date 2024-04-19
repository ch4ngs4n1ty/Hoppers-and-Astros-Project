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
            grid[home.col()][home.row()] = "*";

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
        System.out.println("finished!!");
    }


    /**
     * Copy Constructor that updates the grid  & explorer Hashmap with the explorer that was moved
     * @param other the copied config
     * @param explorer the explorer that's moved
     * @param coordinate the explorer's new coordinate
     */
    private AstroConfig(AstroConfig other, String explorer, Coordinates coordinate){

        this.explorers = other.explorers;

        Coordinates oldC = explorers.get(explorer);
        Coordinates newC = coordinate;


        this.grid = new String[other.getNumRows()][other.getNumCols()];

        for(int row = 0; row < grid.length; row++){
            this.grid[row] = other.grid[row].clone();
        }

        this.grid[newC.row()][newC.col()] = explorer;
        this.grid[oldC.row()][oldC.col()] = ".";

        //explorers.put(explorer, coordinate);

    }

    public int getNumRows(){
        return this.rows;
    }
    public int getNumCols(){
        return this.cols;
    }

    public ArrayList<Integer> getRowIndexes(HashMap<String, Coordinates> rowNeighbors){
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        for(Map.Entry<String, Coordinates> neighbors: rowNeighbors.entrySet()){
            rowIndexes.add(neighbors.getValue().col());
        }
        Collections.sort(rowIndexes);
        return rowIndexes;
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
        ArrayList<String> rowNeighbors = new ArrayList<>();

       HashMap<String, Coordinates> rowNeigh = new HashMap<>();

       for(Map.Entry<String, Coordinates> currExplorer: explorers.entrySet()){
           if(currExplorer.getValue().row() == row
           && !(currExplorer.getKey().equals(explorer))) {
               rowNeigh.put(currExplorer.getKey(), currExplorer.getValue());
           }
       }
        return rowNeigh;
    }

    public ArrayList<String> getColNeighbors(String explorer, int col){
        ArrayList<String> colNeighbors = new ArrayList<>();

        for(int i = 0; i< rows; i++){
            String currentValue = grid[i][col];

            if(!currentValue.equals(".") && !currentValue.equals(explorer) && !currentValue.equals("*")){
                colNeighbors.add(currentValue);
            }
        }
        return colNeighbors;
    }

    @Override
    public boolean isSolution() {
        return astonautLoc.equals(home);
    }

    @Override
    public Collection<Configuration> getNeighbors() {

        LinkedHashSet<Configuration> neighbors= new LinkedHashSet<>();

        //traverses through each explorer and creates configuration for each possible move
        for(Map.Entry<String, Coordinates> explorer: explorers.entrySet()){

            Coordinates explorerCoord = explorer.getValue(); // explorer's current coordinate

            //current explorer's coordinates
            HashMap<String, Coordinates> rowNeighbors = getRowNeighbors(explorer.getKey(), explorerCoord.row());

            // moves current explorer if possible
            if(!(rowNeighbors.isEmpty())){

                //creates temporary coordinates for the possible left and right movements
                Coordinates rightMost = null;
                Coordinates leftMost = null;

                //traverses th
                for(Map.Entry<String, Coordinates> neighbor: rowNeighbors.entrySet()){
                    Coordinates currNeighCoord = neighbor.getValue();

                    ArrayList<Integer> rowNeighIndexes = getRowIndexes(rowNeighbors);
                    if(rowNeighIndexes.getFirst() < explorerCoord.col()
                            && rowNeighIndexes.getLast() > explorerCoord.col()){
                        if((explorerCoord.col() - rowNeighIndexes.getFirst() > 1)){
                            leftMost = new Coordinates(explorerCoord.row(), rowNeighIndexes.getFirst() + 1);
                        }

                        if(rowNeighIndexes.getLast()- explorerCoord.col() > 1){
                            rightMost = new Coordinates(explorerCoord.row(), rowNeighIndexes.getLast() - 1 );
                        }
                    }

                    else{
                        if((explorerCoord.col() - rowNeighIndexes.getLast() > 1)){
                            leftMost = new Coordinates(explorerCoord.row(), rowNeighIndexes.getLast() + 1);
                        }

                        if(rowNeighIndexes.getFirst()- explorerCoord.col() > 1){
                            rightMost = new Coordinates(explorerCoord.row(), rowNeighIndexes.getFirst() - 1 );
                        }
                    }


                }
                if(leftMost != null){
                    neighbors.add(new AstroConfig(this, explorer.getKey(), leftMost));
                    //break;
                }

                if(rightMost != null) {
                    neighbors.add(new AstroConfig(this, explorer.getKey(), rightMost));
                }
            }


//            Coordinates currentExplorerCoord = explorer.getValue();
//            String[] currenRow = getRowVals(currentExplorerCoord.row());
//
//            //checks currentExplorers rows to see if there's any neighbors
//            ArrayList<String> rowVals = getRowNeighbors(explorer.getKey(), currentExplorerCoord.row());
//
//            //checking to see what's the left most neighbor
//            int leftmost = rows-1;
//            while (leftmost >= 0 && !currenRow[leftmost].equals(".")) {
//                leftmost--;
//            }
//            Coordinates rightmost = new Coordinates(currentExplorerCoord.row(), leftmost);
//            neighbors.add(new AstroConfig(this, explorer.getKey(), rightmost));


        }
        System.out.println("TESTING>>>>>>");
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() { return 0; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row=0; row<getNumRows(); ++row) {
            for (int col=0; col<getNumCols(); ++col) {
                result.append(getVal(row, col));
                if (col<getNumCols()-1) {
                    result.append(" ");
                }
            }
            result.append(System.lineSeparator());
        }

        return result.toString();
        //return null;
    }
}
