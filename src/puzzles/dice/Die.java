package puzzles.dice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
/**
 * representation of a single die
 * @author Ashwin Jagadeesh
 */

public class Die{
    private String fileName;

    private int numFaces;
    private LinkedHashMap<Character, Set<Character>> neighbors;

    /**
     * Creates new die
     * @param numFaces the numbers of faces of the die
     */

    public Die(int numFaces){
        this.numFaces = numFaces;
        this.fileName = ("die-" + numFaces + ".txt");
        this.neighbors = generateNeighbors(fileName);

    }

    /**
     * generates the neighbors for all the faces of the given die
     * @param fileName filename of the die
     * @return Mapping of face to it's neighbors
     */
    public LinkedHashMap generateNeighbors(String fileName){
        try(BufferedReader in = new BufferedReader(new FileReader(fileName))){
            LinkedHashMap<Character, Set<Character>> neighborMap = new LinkedHashMap<>();
            String line = in.readLine();

            while((line = in.readLine()) != null){

                String[] fields = line.split("\\s+");
                Character face = fields[0].charAt(0);
                LinkedHashSet<Character> neighborsSet = new LinkedHashSet();
                for(int i = 1; i< fields.length; i++){
                    neighborsSet.add(fields[i].charAt(0));
                }
                neighborMap.put(face, neighborsSet);
            }
            return neighborMap;

        }catch (IOException ioe){
            System.out.println(ioe.getMessage());
            return null;
        }
    }

    /**
     * returns neighbors of current face
     * @param ch the face
     * @return Set of all the neighbors
     */
    public Set<Character> getNeighborFaces(char ch){
        Set<Character> neighborfaces = neighbors.get(ch);
        return neighborfaces;
    }

    @Override
    public String toString() {

        StringBuilder returnString = new StringBuilder("File: " + fileName +
                ", Faces: " + numFaces);

        for(Character key : neighbors.keySet()){
            returnString.append("\n\t" +key + "=" + neighbors.get(key));
        }

        return returnString.toString();
    }
}
