package puzzles.astro.solver;

import puzzles.astro.model.AstroConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Astro {
    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Usage: java Astro filename");
        } else{
            try{
                String filename = args[0];

                AstroConfig startConfig = new AstroConfig(filename);

                System.out.println(startConfig);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
