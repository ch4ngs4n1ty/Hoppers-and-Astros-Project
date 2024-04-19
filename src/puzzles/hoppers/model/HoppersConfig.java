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

    private static int rows;
    private static int cols;
    private char grid[][];

    String GREEN_FROG = "G";
    String RED_FROG = "R";
    String EMPTY = ".";
    String INVALID = "*";

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


                    //HashSet<Coordinates> cordSet = new HashSet<>();

//                    char currentChar = lines[c].charAt(0);
//                    Coordinates coordinates = new Coordinates(r, c);
//
//                    if (character.containsKey(String.valueOf(currentChar))) {
//                        character.get(String.valueOf(currentChar)).add(coordinates);
//
//                    } else {
//
//                        List<Coordinates> coordinatesList = new ArrayList<>();
//                        coordinatesList.add(coordinates);
//                        character.put(String.valueOf(currentChar), coordinatesList);
//                    }
//
//                }
//
//            }
//
//            this.frogs = new ArrayList<>();
//
//            for (Map.Entry<String, List<Coordinates>> entry : character.entrySet()) {
//
//                String key = entry.getKey();
//
//                if ("G".equals(key) || "R".equals(key)) {
//
//                    frogs.add(key);
//
//                }
//
//            }

            //System.out.println(grid);

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

        for (int r = 0; r < rows; r++) {
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

                    if (r == 0 && c == 0) {

                        if (grid[r+2][c+2] == '.') {

                            if (grid[r+1][c+1] == 'G') {

                                HoppersConfig neighborConfig = new HoppersConfig(this);

                                grid[r][c] = '.';
                                grid[r+2][c+2] = 'R';
                                grid[r+1][c+1] = '.';

                                neighbors.add(neighborConfig);

                            }

                        }

                    }

                }

            }

        }

        return neighbors;
    }



    // Helper method to create a copy of the current grid
//        char[][] newGrid = new char[rows][cols];
//        for (int i = 0; i < rows; i++) {
//            System.arraycopy(grid[i], 0, newGrid[i], 0, cols);
//        }
//        return newGrid;


    public int getRows() {

        return this.rows;

    }

    public int getCols() {

        return this.cols;

    }

    public char getVal(int row, int col) {

        return grid[row][col];

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
