package puzzles.astro.solver;

import puzzles.astro.model.AstroConfig;
import puzzles.common.solver.Configuration;

import java.io.IOException;
import java.util.Collection;

public class Astro {
    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Usage: java Astro filename");
        } else{
            try{
                String filename = args[0];

                AstroConfig startConfig = new AstroConfig(filename);

                System.out.println(startConfig);

                //System.out.println(startConfig.getRowNeighbors("A", 4));
                //System.out.println(startConfig.getColNeighbors("A", 4));

                Collection<Configuration> neighbors = startConfig.getNeighbors();

                for(Configuration config: neighbors){
                    System.out.println(config);
                }

                //System.out.println(startConfig.getNeighbors());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
