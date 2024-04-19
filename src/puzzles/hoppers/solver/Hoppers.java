package puzzles.hoppers.solver;

import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;

public class Hoppers {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        } else {

            String filename = args[0];

            try {

                System.out.println("File: data/hoppers/" + filename);
                System.out.println(" ");

                HoppersConfig startConfig = new HoppersConfig(filename);
                System.out.println(startConfig);


            } catch (IOException e) {
                System.err.println(e);
            }

        }
    }
}
