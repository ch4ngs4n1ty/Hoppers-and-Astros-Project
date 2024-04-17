package puzzles.common.solver;

import java.util.*;

/**
 * representation of a common Breadth First search algorithm
 * includes method that return the shortest path from starting point to the solution even if not given
 *
 * @author Ashwin Jagadeesh
 */
public class Solver {

    private Map<Configuration, Configuration> predMap;
    private int totalConfigs = 1;

    public Solver(Configuration config){
        this.predMap = new HashMap<>();
        predMap.put(config, null);
    }

    /**
     * generates the shortest path from the start config to the solution indicated by the isSolution  method
     * @param start the starting configuration
     * @return the shortest path if it exists
     */
    public Collection<Configuration> solve(Configuration start){
        Queue<Configuration> toVisit = new LinkedList<>();
        toVisit.offer(start);

        //predMap.put(start, null);
        Configuration solution = null;

        while(!toVisit.isEmpty()) {// && !toVisit.peek().equals(end)){
            Configuration current = toVisit.remove();
            if(current.isSolution()){
                solution = current;
                break;
            }
            for(Configuration config: current.getNeighbors()){
                totalConfigs += 1;
                if(!predMap.containsKey(config) && config != null){
                    predMap.put(config, current);
                    toVisit.offer(config);
                }
            }
        }

        if (solution != null) {
            return constructPath(predMap, solution);
        }else{
            return null;
        }
    }

    /**
     * constructs the shortest path from the start configuration to end configuration
     * @param predecessorMap mapping of the configuration to where they came from
     * @param solution the solution cpnfiguration
     * @return a list that contains the path from the first point of the predecessor map to the solution configuration
     */
    private List<Configuration> constructPath(Map<Configuration, Configuration> predecessorMap, Configuration solution){

        List<Configuration> path = new LinkedList();
        Configuration current = solution;
        while(current != null){
            path.addFirst(current);
            current = predecessorMap.get(current);
        }
        return path;
    }

    /**
     * Get the total number of configs generated during the BFS
     * @return total configurations
     */
    public int getTotalConfigs(){
        return totalConfigs;
    }

    /**
     * get the unique configurations during the BFS
     * @return the unique configs
     */
    public int getUniqueConfigs(){
        return predMap.size();
    }
}
