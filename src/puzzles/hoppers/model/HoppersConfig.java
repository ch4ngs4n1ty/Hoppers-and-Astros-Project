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

public class HoppersConfig implements Configuration{

    private int rows;
    private int cols;
    private char grid[][];
    private final int[][] evenMoves = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}, {-4, 0}, {4, 0}, {0, -4}, {0, 4}};
    private final int[][] oddMoves = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
    private char EMPTY = '.';
    private char INVALID = '*';
    private char GREEN = 'G';
    private char RED = 'R';

    public HoppersConfig(String filename) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader(filename))){

            String cords = in.readLine();

            String[] fields = cords.split("\\s+");

            this.rows = Integer.parseInt(fields[0]);
            this.cols = Integer.parseInt(fields[1]);
            this.grid = new char[rows][cols];

            for (int r = 0; r < rows; r++) {

                String line = in.readLine();
                String[] lines = line.split("\\s");

                for (int c = 0; c < cols; c++) {

                    this.grid[r][c] = lines[c].charAt(0);

                }
            }
        }
    }

    @Override
    public boolean isSolution() {

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < cols; c++) {

                if (grid[r][c] == 'G') {
                    return false;
                }
            }
        }
        return true;
    }

    private HoppersConfig(HoppersConfig other) {

        this.rows = other.rows;
        this.cols = other.cols;

        this.grid = new char[rows][cols];

        for (int r = 0; r < this.rows; r++) {

            System.arraycopy(other.grid[r], 0, this.grid[r], 0, cols);

        }
    }

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

                    for (int[] dir : dirConfigs) {
                        int neighborRow = r + dir[0];
                        int neighborCol = c + dir[1];
                        int hopRow = r + dir[0] / 2;
                        int hopCol = c + dir[1] / 2;

                        if (checkNeighborCords(neighborRow, neighborCol)) {

                            if (checkHopCords(hopRow, hopCol)) {

                                if (val == GREEN && grid[hopRow][hopCol] == GREEN) {

                                    HoppersConfig neighborConfig = new HoppersConfig(this);
                                    neighborConfig.grid[r][c] = EMPTY;
                                    neighborConfig.grid[hopRow][hopCol] = EMPTY;
                                    neighborConfig.grid[neighborRow][neighborCol] = GREEN;
                                    neighbors.add(neighborConfig);

                                } else if (val == RED && grid[hopRow][hopCol] == GREEN) {

                                    HoppersConfig neighborConfig = new HoppersConfig(this);
                                    neighborConfig.grid[r][c] = EMPTY;
                                    neighborConfig.grid[hopRow][hopCol] = EMPTY;
                                    neighborConfig.grid[neighborRow][neighborCol] = RED;
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


    public boolean checkNeighborCords(int neighborRow, int neighborCol) {

        if (neighborRow >= 0 && neighborRow < rows) {
            if (neighborCol >= 0 && neighborCol < cols) {
                if (grid[neighborRow][neighborCol] == EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkHopCords(int hopRow, int hopCol) {

        if (hopRow >= 0 && hopRow < rows) {
            if (hopCol >= 0 && hopCol < cols) {
                if (grid[hopRow][hopCol] != INVALID && grid[hopRow][hopCol] != RED) {
                    return true;
                }
            }
        }
        return false;
    }

    public char[][] getGrid() {
        return this.grid;
    }

    public int getRows() {

        return this.rows;

    }

    public int getCols() {

        return this.cols;

    }

    @Override
    public boolean equals(Object other) {

        boolean result = false;

        if (other instanceof HoppersConfig) {
            HoppersConfig otherHoppersConfig = (HoppersConfig) other;

            result = Arrays.deepEquals(this.grid, otherHoppersConfig.grid);
        }

        return result;
    }
    
    @Override
    public int hashCode() {

        return Arrays.deepHashCode(this.grid);

    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        for (int row=0; row < getRows(); ++row) {

            for (int col = 0; col < getCols(); ++col) {

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
