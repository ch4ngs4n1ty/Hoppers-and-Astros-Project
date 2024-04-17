package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.HashSet;

/**
 * The representation of a clock configuration
 *
 * @author Ashwin Jagadeesh
 */

public class ClockConfig implements Configuration {

    private static int hours;
    private static int endTime;

    private int currentTime;

    /**
     * creates new clock configuration
     * @param hours that the clock has
     * @param startTime starting time
     * @param end ending time
     */
    public ClockConfig(int hours, int startTime, int end){
        this.hours = hours;
        this.currentTime = startTime;
        this.endTime = end;
    }

    /**
     * Copy constructor
     * @param other the other clock configuration to be copied
     * @param currentTime the new current time
     */
    public ClockConfig(ClockConfig other, int currentTime){
        this.currentTime = currentTime;
    }


    /**
     * checks if the config is a solution
     * @return if it's a solution
     */
    @Override
    public boolean isSolution() {
        if(currentTime == endTime){
            return true;
        }
        return false;
    }

    /**
     * generates two neighbors of the clock config : one above and one below
     * @return the two neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {

        HashSet<Configuration> neighbors = new HashSet<>();

        int nextHour = currentTime;
        int prevHour = currentTime;

        if(nextHour == hours){
            nextHour = 1;
        } else{
            nextHour += 1;
        }

        if(prevHour == 1){
            prevHour = hours;
        } else{
            prevHour -= 1;
        }

        neighbors.add(new ClockConfig(this, nextHour));
        neighbors.add(new ClockConfig(this, prevHour));
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if(other instanceof ClockConfig){
            ClockConfig otherCloclConfig = (ClockConfig) other;
            result = (this.currentTime==(otherCloclConfig.currentTime));
        }
        return result;
    }

    @Override
    public int hashCode() {
        return this.currentTime;
    }

    @Override
    public String toString() {
        return String.valueOf(this.currentTime);
    }
}
