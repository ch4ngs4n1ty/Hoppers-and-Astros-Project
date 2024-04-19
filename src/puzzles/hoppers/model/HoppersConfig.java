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
    private LinkedHashMap<String, List<Coordinates>> character;
    private ArrayList<String> frogs;

    String GREEN_FROG = "G";
    String RED_FROG = "R";
    String EMPTY = ".";
    String INVALID = "*";

    public HoppersConfig(String filename) throws IOException {

        character = new LinkedHashMap<>();

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
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {

        ArrayList<Configuration> neighbors = new ArrayList<>();

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c <cols; c++) {

                char val = grid[r][c];

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

            for (int col = 0; col < getCols(); ++col) {

                result.append(grid[row][col]).append("");

//                Coordinates currentCoordinates = new Coordinates(row, col);
//
//                for (Map.Entry<String, List<Coordinates>> entry : character.entrySet()) {
//
//                    if (entry.getValue().contains(currentCoordinates)) {
//                        result.append(entry.getKey());
//
//                    }
//                }

                if (col < getCols() - 1) {
                    result.append(" ");
                }
            }

            result.append(System.lineSeparator());

        }

        return result.toString();

    }
}
