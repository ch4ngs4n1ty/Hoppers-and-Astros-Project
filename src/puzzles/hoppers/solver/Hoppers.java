package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.Collection;

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

                Solver hoppersPath = new Solver(startConfig);

                Collection<Configuration> path = hoppersPath.solve(startConfig);

                int i = 0;

                if (path == null) {

                    System.out.println("No solution");

                } else {

                    for (Configuration step: path) {

                        System.out.println("Step " + i);
                        i++;
                        System.out.println(step);
                        System.out.println("");

                    }
                }


            } catch (IOException e) {
                System.err.println(e);
            }

        }
    }
}
