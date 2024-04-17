package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

// TODO: implement your HoppersConfig for the common solver

public class AstroConfig implements Configuration{

    private static int rows;
    private static int cols;

    private String[][] grid;

    private static Coordinates home;
    private Coordinates astonautLoc;

    private HashMap<String, Coordinates> robots;

    public AstroConfig(String filename) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String line = in.readLine();

            String[] coordinates = line.split("\\s");

            this.rows = Integer.parseInt(coordinates[0]);
            this.cols = Integer.parseInt(coordinates[1]);

            System.out.println(rows);
            System.out.println(cols);

            this.grid = new String[rows][cols];

            //creates grid with the row and col dimensions
            for (int row = 0; row < this.rows; row++) {
                for (int col = 0; col < this.cols; col++) {
                    grid[row][col] = String.valueOf(0);
                }
            }

            String[] goalLocation = in.readLine().split("\\s+");
            this.home = new Coordinates(String.valueOf(goalLocation[1].charAt(0)), String.valueOf(goalLocation[1].charAt(2)));
            System.out.println(home);


            String[] astronaut = in.readLine().split("\\s+");
            this.astonautLoc = new Coordinates(String.valueOf(astronaut[1].charAt(0)), String.valueOf(astronaut[1].charAt(2)));
            System.out.println(astonautLoc);


            // create a hashmap of robot name to their coordinates
            int numRobots = Integer.parseInt(in.readLine());

            robots = new HashMap<>();
            for(int i = 0; i < numRobots; i++){
                String[] robotFields = in.readLine().split(" ");
                Coordinates currentRobot = new Coordinates(String.valueOf(robotFields[1]));
                robots.put(robotFields[0], currentRobot);
            }
            System.out.println(robots);



        }

    }

    public int getRows(){
        return this.rows;
    }
    public int getCols(){
        return this.cols;
    }
    public String getVal(int row, int col) {
        return grid[row][col];
    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
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
        for (int row=0; row<getRows(); ++row) {
            for (int col=0; col<getCols(); ++col) {
                result.append(getVal(row, col));
                if (col<getCols()-1) {
                    result.append(" ");
                }
            }
            result.append(System.lineSeparator());
        }

        return result.toString();
        //return null;
    }
}
