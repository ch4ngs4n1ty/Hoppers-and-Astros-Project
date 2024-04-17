package puzzles.dice;

import puzzles.common.solver.Configuration;

import java.util.*;
/**
 * The representation of a clock configuration
 *
 * @author Ashwin Jagadeesh
 */
public class DiceConfig implements Configuration {
    private String current;
    private static String end;
    private static List<Die> dice;


    /**
     * creates new DiceConfiguration
     * @param current the current string of dice faces
     * @param end the ending string of dice faces
     * @param Dice list of Die
     */
    public DiceConfig(String current, String end, List<Die> Dice){
        this.current = current;
        this.end = end;
        this.dice = Dice;
    }


    /**
     * Copy constructor
     * @param other the other dice configuration to be copied
     * @param current the new current time
     */
    public DiceConfig(DiceConfig other, String current){
        this.current = current;
    }

    /**
     * checks if the config is a solution
     * @return if it's a solution
     */
    @Override
    public boolean isSolution() {
        return Objects.equals(current, end);
    }

    @Override
    public Collection<Configuration> getNeighbors() {

        LinkedHashSet<Configuration> neighbors = new LinkedHashSet();

        for(int i = 0; i < current.length(); i++){
            char currentChar = current.charAt(i);

            Die currentDie = dice.get(i);

            currentDie.getNeighborFaces(currentChar);

            Set<Character> currentCharNeighbors = currentDie.getNeighborFaces(currentChar);
            if(currentCharNeighbors != null){
                for(char neighbor: currentCharNeighbors){
                    StringBuilder sb = new StringBuilder(current);
                    sb.setCharAt(i,neighbor);
                    neighbors.add(new DiceConfig(this, sb.toString()));
                }
            }

        }
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if(other instanceof DiceConfig){
            DiceConfig otherDiceConfig = (DiceConfig) other;
            result = (this.current.equals(otherDiceConfig.current));
        }
        return result;
    }

    @Override
    public int hashCode() {
        return current.hashCode();
    }

    @Override
    public String toString() {
        return current;
    }
}
