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

        //Object.equals(this.grid);
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

                if (val == 'G' || val == 'R') {

                    int[][] dirConfigs;

                    //These are the possible neighbors
                    if ((r + c) % 2 == 0) {

                        //Directions that can be used for even number coordinate, total of 8 moves
                        dirConfigs = new int[][]{{-2, -2}, {-2, 2}, {2, -2}, {2, 2}, {-4, 0}, {4, 0}, {0, -4}, {0, 4}};


                    } else {
                        //Directions that can be used for odd number coordinate, total of 4 moves
                        dirConfigs = new int[][]{{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
                    }

                    for (int[] dir : dirConfigs) {
                        int neighborRow = r + dir[0];
                        int neighborCol = c + dir[1];
                        int mr = r + dir[0] / 2;
                        int mc = c + dir[1] / 2;

                        if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols && grid[neighborRow][neighborCol] == '.') {

                            if (mr >= 0 && mr < rows && mc >= 0 && mc < cols && grid[mr][mc] != '*') {

                                if (val == 'G' && grid[mr][mc] == 'G') {

                                    HoppersConfig neighborConfig = new HoppersConfig(this);
                                    neighborConfig.grid[r][c] = '.';
                                    neighborConfig.grid[mr][mc] = '.';
                                    neighborConfig.grid[neighborRow][neighborCol] = val;
                                    neighbors.add(neighborConfig);

                                } else if (val == 'R' && grid[mr][mc] == 'G') {

                                    HoppersConfig neighborConfig = new HoppersConfig(this);
                                    neighborConfig.grid[r][c] = '.';
                                    neighborConfig.grid[mr][mc] = '.';
                                    neighborConfig.grid[neighborRow][neighborCol] = val;
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

    public int getRows() {

        return this.rows;

    }

    public int getCols() {

        return this.cols;

    }

//    public char getVal(int row, int col) {
//
//        return grid[row][col];
//
//    }
    
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

                result.append(grid[row][col]).append("");

                if (col < getCols() - 1) {
                    result.append(" ");
                }
            }

            result.append(System.lineSeparator());

        }

        return result.toString();

    }
}
