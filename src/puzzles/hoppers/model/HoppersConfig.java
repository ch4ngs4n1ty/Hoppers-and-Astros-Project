package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;

// TODO: implement your HoppersConfig for the common solver

/**
 * The representation of Hoppers configuration
 *
 * @author Ethan Chang
 */

public class HoppersConfig implements Configuration{
    /** number of rows */
    private int rows;
    /** number of cols */
    private int cols;
    /** 2d char array of grid */
    private char grid[][];
    /** possible eight moves */
    private final int[][] evenMoves = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}, {-4, 0}, {4, 0}, {0, -4}, {0, 4}};
    /** possible four moves */
    private final int[][] oddMoves = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
    /** empty char */
    private char EMPTY = '.';
    /** invalid char */
    private char INVALID = '*';
    /** green frog char */
    private char GREEN = 'G';
    /** red frog char */
    private char RED = 'R';

    /**
     * HopperConfig constructor that reads the file and then stores each value inside the grid
     * @param filename: Filename from hopper text files
     * @throws IOException: if file name is invalid or empty
     */
    public HoppersConfig(String filename) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader(filename))){

            String cords = in.readLine();

            //Gets list of number of rows and cols
            String[] fields = cords.split("\\s+");

            this.rows = Integer.parseInt(fields[0]);
            this.cols = Integer.parseInt(fields[1]);

            //Creates a new char 2d array grid
            this.grid = new char[rows][cols];

            for (int r = 0; r < rows; r++) {

                String line = in.readLine();
                String[] lines = line.split("\\s");

                for (int c = 0; c < cols; c++) {

                    //Reads each line and stores char in grid
                    this.grid[r][c] = lines[c].charAt(0);

                }
            }
        }
    }

    /**
     * Checks to see if the hopper puzzle reaches the solution
     * @return: True if the solution is found or false if the solution is not found
     *
     */
    @Override
    public boolean isSolution() {

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < cols; c++) {

                char val = grid[r][c];

                if (val == 'G') {

                    return false;

                }

            }

        }

        return true;

    }

    /**
     * Copy constructor for HoppersConfig
     * @param other: the copied config
     */
    private HoppersConfig(HoppersConfig other) {

        this.rows = other.rows;
        this.cols = other.cols;

        this.grid = new char[rows][cols];

        for (int r = 0; r < this.rows; r++) {

            System.arraycopy(other.grid[r], 0, this.grid[r], 0, cols);

        }
    }

    /**
     * Gets every possible configuration for Hoppers
     * @return: List of neighbors for each green frog and a red frog
     */
    @Override
    public Collection<Configuration> getNeighbors() {

        ArrayList<Configuration> neighbors = new ArrayList<>();

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < cols; c++) {

                char val = grid[r][c];

                if (val == GREEN || val == RED) {

                    int[][] dirConfigs;

                    //These are the possible neighbors
                    if ((r + c) % 2 == 0) {

                        //Directions that can be used for even number coordinate, total of 8 moves
                        dirConfigs = this.evenMoves;

                    } else {

                        //Directions that can be used for odd number coordinate, total of 4 moves
                        dirConfigs = this.oddMoves;

                    }

                    //Iterates 2d array of possible moves
                    for (int[] direction : dirConfigs) {

                        //Neighbor values, finds empty spots in the grid
                        int neighborRow = r + direction[0];
                        int neighborCol = c + direction[1];

                        //Hop values, must be green frog to hop over
                        int hopRow = r + direction[0] / 2;
                        int hopCol = c + direction[1] / 2;

                        //Checks to see if neighbor coordinates are valid and that are empty
                        if (checkNeighborCords(neighborRow, neighborCol)) {

                            //Checks to see if hop value coordinates are valid and that are green frogs only
                            if (checkHopCords(hopRow, hopCol)) {

                                char hopVal = grid[hopRow][hopCol];

                                if (val == GREEN && hopVal == GREEN) {

                                    //Creates a copy config
                                    HoppersConfig neighborConfig = new HoppersConfig(this);

                                    //Sets everything else to empty except neighbor val to green frog
                                    neighborConfig.grid[r][c] = EMPTY;
                                    neighborConfig.grid[hopRow][hopCol] = EMPTY;
                                    neighborConfig.grid[neighborRow][neighborCol] = GREEN;

                                    //Adds the possible neighbor to neighbor list
                                    neighbors.add(neighborConfig);

                                } else if (val == RED && hopVal == GREEN) {

                                    //Creates a copy config
                                    HoppersConfig neighborConfig = new HoppersConfig(this);

                                    //Sets everything else to empty except neighbor val to red frog
                                    neighborConfig.grid[r][c] = EMPTY;
                                    neighborConfig.grid[hopRow][hopCol] = EMPTY;
                                    neighborConfig.grid[neighborRow][neighborCol] = RED;

                                    //Adds the possible neighbor to neighbor list
                                    neighbors.add(neighborConfig);

                                }
                            }
                        }
                    }
                }
            }
        }

        return neighbors;

    }

    /**
     * Checks to see if neighbor cords are valid or not
     * @param neighborRow: Row number of neighbor
     * @param neighborCol: Col number of neighbor
     * @return: Returns true if neighbor cords value is empty false if neighbor cord represents something else
     */
    public boolean checkNeighborCords(int neighborRow, int neighborCol) {

        if (neighborRow >= 0 && neighborRow < rows) {

            if (neighborCol >= 0 && neighborCol < cols) {

                char neighborVal = grid[neighborRow][neighborCol];

                if (neighborVal == EMPTY) {

                    return true;

                }

            }

        }

        return false;

    }

    /**
     * Checks to see if hop cords are valid or not
     * @param hopRow: Row number of hop
     * @param hopCol: Col number of col
     * @return: Returns true if hop cords value is green false if hop cords represent something else
     */
    public boolean checkHopCords(int hopRow, int hopCol) {

        if (hopRow >= 0 && hopRow < rows) {

            if (hopCol >= 0 && hopCol < cols) {

                char val = grid[hopRow][hopCol];

                if (val == GREEN) {

                    return true;

                }

            }

        }

        return false;

    }

    /**
     * Gets the current 2d array of grid
     * @return: 2d array char grid
     */
    public char[][] getGrid() {
        return this.grid;
    }

    /**
     * Gets number of rows for current config
     * @return: Number of rows
     */
    public int getRows() {

        return this.rows;

    }

    /**
     * Gets number of cols for current config
     * @return: Number of cols
     */
    public int getCols() {

        return this.cols;

    }

    /**
     * Checks to see if other config matches with this config
     * @param other: Other HoppersConfig
     * @return: True if these two configs equal, false if they don't
     */
    @Override
    public boolean equals(Object other) {

        boolean result = false;

        if (other instanceof HoppersConfig) {

            HoppersConfig otherHoppersConfig = (HoppersConfig) other;

            result = Arrays.deepEquals(this.grid, otherHoppersConfig.grid);
        }

        return result;
    }

    /**
     * Deep hash on the grid
     * @return: int of hashcode for the grid
     */
    @Override
    public int hashCode() {

        return Arrays.deepHashCode(this.grid);

    }

    /**
     * Builds a board for the current config
     * @return: A board of the current config
     */
    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        for (int row = 0; row < getRows(); row++) {

            for (int col = 0; col < getCols(); col++) {

                result.append(grid[row][col]);

                if (col < getCols() - 1) {

                    result.append(" ");

                }

            }

            if (row < getRows() - 1) {

                result.append(System.lineSeparator());

            }

        }

        return result.toString();

    }
}
