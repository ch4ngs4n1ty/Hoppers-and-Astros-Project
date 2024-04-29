package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.Collection;

/**
 * Hopper's Solver to find the shortest path for each hopper's txt files
 * Shows step by step how to solve puzzle
 *
 * @author Ethan Chang
 */

public class Hoppers {
    public static void main(String[] args) {

        if (args.length != 1) {

            System.out.println("Usage: java Hoppers filename");

        } else {

            String filename = args[0];

            try {

                System.out.println("File: data/hoppers/" + filename);

                //Gets the start config for HoppersConfig
                HoppersConfig startConfig = new HoppersConfig(filename);

                System.out.println(startConfig);

                //Builds the solver for HoppersConfig
                Solver hoppersPath = new Solver(startConfig);

                //Solves the HoppersConfig and then builds the path
                Collection<Configuration> path = hoppersPath.solve(startConfig);

                System.out.println("Total configs: " + hoppersPath.getTotalConfigs());
                System.out.println("Unique configs: " + hoppersPath.getUniqueConfigs());


                int i = 0;

                if (path == null) {

                    System.out.println("No solution");

                } else {

                    for (Configuration step: path) {

                        System.out.println("Step " + i + ":");
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
