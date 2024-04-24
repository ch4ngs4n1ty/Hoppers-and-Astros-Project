package puzzles.astro.solver;

import puzzles.astro.model.AstroConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class Astro {
    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Usage: java Astro filename");
        } else{
            try{
                String filename = args[0];

                AstroConfig startConfig = new AstroConfig(filename);

                System.out.println("File: data/astro/" + filename);
                System.out.print(startConfig);


                //solves the puzzle
                Solver solveAstroPuzzle = new Solver(startConfig);
                Collection<Configuration> resultingPath = solveAstroPuzzle.solve(startConfig);

                System.out.println("Total configs: "+ solveAstroPuzzle.getTotalConfigs() + "\nUnique configs: "
                        + solveAstroPuzzle.getUniqueConfigs());

                int stepCount = 0;
                if(resultingPath == null){
                    System.out.println("No solution");
                }
                else{
                    for(Configuration configuration: resultingPath){
                        System.out.println("Step " + stepCount + ": \n" + configuration);
                        stepCount ++;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
