package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration{

    private static int rows;
    private static int cols;
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

        return null;

//        for (int row=0; row<getRows(); ++row) {
//            result.append(row).append(VERT_WALL);
//            // this loop is for the row of cells and the horizontal connections
//            for (int col = 0; col < getCols(); ++col) {
//                // get the cell value to display for this coord
//                result.append(getCell(new Coordinates(row, col)));
//                if (col < getCols() - 1) {
//                    if (isNeighbor(new Coordinates(row, col), new Coordinates(row, col + 1))) {
//                        result.append(EMPTY);
//                    } else {
//                        result.append(VERT_WALL);
//                    }
//                }
//            }
    }
}
