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

                    //this.grid[r][c] = lines[c].charAt(0);

                    //HashSet<Coordinates> cordSet = new HashSet<>();

                    char currentChar = lines[c].charAt(0);
                    Coordinates coordinates = new Coordinates(r, c);


                    if (character.containsKey(String.valueOf(currentChar))) {
                        character.get(String.valueOf(currentChar)).add(coordinates);

                    } else {

                        List<Coordinates> coordinatesList = new ArrayList<>();
                        coordinatesList.add(coordinates);
                        character.put(String.valueOf(currentChar), coordinatesList);
                    }

//                    if (lines[c].charAt(0) == 'G') {
//
//                        Coordinates coordinates = new Coordinates(r, c);
//                        character.put(GREEN_FROG, coordinates);
//
//                    } else if (lines[c].charAt(0) == 'R') {
//
//                        Coordinates coordinates = new Coordinates(r,c);
//                        character.put(RED_FROG, coordinates);
//
//                    } else if (lines[c].charAt(0) == '*') {
//
//                        Coordinates coordinates = new Coordinates(r, c);
//                        character.put(EMPTY, coordinates);
//
//                    } else if (lines[c].charAt(0) == '.') {
//
//                        Coordinates coordinates = new Coordinates(r, c);
//                        character.put(INVALID, coordinates);
//                    }

                }

            }

            System.out.println(character);

        }
    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
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

                Coordinates currentCoordinates = new Coordinates(row, col);

                for (Map.Entry<String, List<Coordinates>> entry : character.entrySet()) {

                    if (entry.getValue().contains(currentCoordinates)) {
                        result.append(entry.getKey());

                    }
                }

                if (col < getCols() - 1) {
                    result.append(" ");
                }
            }

            result.append(System.lineSeparator());

        }


        return result.toString();
    }
}
