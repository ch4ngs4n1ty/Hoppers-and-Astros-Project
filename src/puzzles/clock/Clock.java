package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * representation of a clock with main program to be run
 * @author Ashwin Jagadeesh
 */

public class Clock {

    /**
     * the main method that prints out the shortest path from start to end hour indicated by command line argument
     *
     * @param args the arguements to solve from the starting to ending hour and the total hours
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: java Clock hours start end");
        } else {

            //sets the three arguments to corresponding variables
            int hours = (Integer.parseInt(args[0]));
            int startTime = Integer.parseInt(args[1]);
            int endTime = Integer.parseInt(args[2]);

            // creates a clock configuration with the start time as the current time
            // creates instance of solver using the clock config
            // generates shortest path
            ClockConfig startConfig = new ClockConfig(hours, startTime, endTime);
            Solver solveClockPuzzle = new Solver(startConfig);
            Collection<Configuration> resultingPath = solveClockPuzzle.solve(startConfig);

            System.out.println("Hours: " + hours + ", Start: " + startTime + ", End: " + endTime + "\nTotal configs: "
                    + solveClockPuzzle.getTotalConfigs() + "\nUnique configs: " + solveClockPuzzle.getUniqueConfigs());

            if (resultingPath == null) {
                System.out.println("No solution");
            } else {
                int stepCount = 0;
                for (Configuration hour : resultingPath) {
                    System.out.println("Step " + stepCount + ": " + hour);
                    stepCount++;
                }
            }
        }
    }
}