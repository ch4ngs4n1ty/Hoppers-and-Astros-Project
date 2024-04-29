package puzzles.astro.ptui;

import puzzles.common.Observer;
import puzzles.astro.model.AstroModel;

import java.io.IOException;
import java.util.Scanner;

public class AstroPTUI implements Observer<AstroModel, String> {
    private AstroModel model;
    private String currentFileName;

    /**
     * The View initialization adds ourselves as an observer to the Model
     * prints the initial configuration layout with loaded message with the filename
     */
    public void init(String filename) throws IOException {
        this.model = new AstroModel(filename);
        setFileName(filename);
        this.model.addObserver(this);

        System.out.println("Loaded: " + currentFileName);
        System.out.println(this.model.toString());
        displayHelp();
    }

    /**
     * update prints the new configuration along with a message displaying what happened
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param data optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(AstroModel model, String data) {
        System.out.println(data);
        System.out.println(model);
    }


    /**
     * prints the possible commands the user can do
     * this method is called when the user types an invalid command
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "m(ove) n|s|e|w      -- move selected piece in direction" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * The run loop prompts for user input and makes calls into the Model.
     */
    public void run() throws IOException {
        Scanner in = new Scanner( System.in );

        for ( ; ; ) {
            //checks if solution is reacherd and prints message
            if(model.reachedSolution()){
                System.out.println("Already solved");
            }
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );

            if (words.length > 0) {
                if (words[0].startsWith( "l" )){ // calls load with given filename
                    currentFileName = words[1];
                    model.load(currentFileName);
                }
                if (words[0].startsWith( "q" )) { //breaks the loop if user quits
                    break;
                } else if (words[0].startsWith( "h" )) { //displays the next position of the grid if solution exists
                    model.hint();
                }
                 else if (words[0].startsWith( "s" )) { // user selects a coordinate and informs the model
                     if (words.length != 3){
                         displayHelp();
                     }
                     int row = Integer.parseInt(words[1]);
                     int col = Integer.parseInt(words[2]);

                     model.select(row, col);

                 } else if (words[0].startsWith("m")) { // moves the explorer to the specified cardinal location if possible
                     if(words.length != 2){
                         displayHelp();
                     }
                     else{
                         String dir = words [1];
                         model.move(dir);
                     }

                } else if (words[0].startsWith( "r" )) { // resets the board to the initial state of the current file
                    model.reset();

                }
                 else {
                    displayHelp(); // prints the  possible commands if none of the inputs match the possible commands
                }
            }
        }
    }

    /**
     * changes the current file to the given file name in the parameter
     * @param fileName new file name
     */
    public void setFileName(String fileName){
        this.currentFileName = fileName;
    }

    /**
     * The main routine.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AstroPTUI filename");
        } else {
            try {
                AstroPTUI ptui = new AstroPTUI();
                ptui.init(args[0]);

                ptui.setFileName(args[0]);

                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
