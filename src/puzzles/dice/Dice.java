package puzzles.dice;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * representation of a clock with main program to be run
 * @author Ashwin Jagadeesh
 */
public class Dice {

    /**
     * the main method that prints out the shortest path from start to end dice pattern indicated by command line argument
     *
     * @param args expected command line arguement is the start pattern, end patter, dice face numbers in order
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Dice start end die1 die2...");
        } else {
            // sets the command arguements to the variables start path, end path, and the dice types to a list
            String start = args[0];
            String end = args[1];
            List<Die> dice = new LinkedList<>();

            for(int i = 2; i < args.length; i ++){
                dice.add(new Die(Integer.parseInt(args[i])));
            }
            int dieCount = 0;

            //prints tostring of each die
            for(Die d: dice){
                System.out.print("Die #" + dieCount + ": ");
                System.out.println(d);
                dieCount ++;
            }

            //creates starting config, creates instance of solver, calls solve method using dice configuration
            DiceConfig startConfig = new DiceConfig(start, end, dice);
            Solver solveDicePuzzle = new Solver(startConfig);
            Collection<Configuration> resultingPath = solveDicePuzzle.solve(startConfig);

            // prints information of the resulting path
            System.out.println("Start: " + start + ", End: " + end);
            System.out.println("Total configs: " + solveDicePuzzle.getTotalConfigs());
            System.out.println("Unique configs: " + solveDicePuzzle.getUniqueConfigs());

            int stepCount = 0;
            if(resultingPath == null){
                System.out.println("No solution");
            }
            else{
                for(Configuration configuration: resultingPath){
                    System.out.println("Step " + stepCount + ": " + configuration);
                    stepCount ++;
                }
            }
        }
    }
}