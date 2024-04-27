package puzzles.astro.ptui;

import puzzles.common.Observer;
import puzzles.astro.model.AstroModel;

import java.io.IOException;
import java.util.Scanner;

public class AstroPTUI implements Observer<AstroModel, String> {
    private AstroModel model;
    private String currentFileName;


    public void init(String filename) throws IOException {
        this.model = new AstroModel(filename);
        this.model.addObserver(this);
        displayHelp();
    }

    @Override
    public void update(AstroModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    /**
     * Reset the game*/

    public void newGame(String fileName) throws IOException {
        init(fileName);
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "m(ove) n|s|e|w      -- move selected piece in direction" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() throws IOException {
        Scanner in = new Scanner( System.in );

        for ( ; ; ) {
            if(model.reachedSolution()){
                System.out.println("Already solved");
                break;
            }
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );

            if (words.length > 0) {
                if (words[0].startsWith( "l" )){
                    currentFileName = words[1];
                    init(words[1]);
                }
                if (words[0].startsWith( "q" )) {
                    break;
                } else if (words[0].startsWith( "h" )) {
                    model.hint();
                }
                 else if (words[0].startsWith( "s" )) {
                     if (words.length != 3){
                         displayHelp();
                     }
                     int row = Integer.parseInt(words[1]);
                     int col = Integer.parseInt(words[2]);

                     model.select(row, col);

                 } else if (words[0].startsWith("m")) {
                     if(words.length != 2){
                         displayHelp();
                     }
                     else{
                         String dir = words [1];
                         model.move(dir);
                     }

                } else if (words[0].startsWith( "r" )) {
                    //System.out.println("Puzzle reset!");
                    model.reset();
                    //init(currentFileName);

                } else if (words[0].startsWith( "p" )) {
                    System.out.println(model.toString());;
                 }
                 else {
                    displayHelp();
                }
            }
        }
    }

    public void setFileName(String fileName){
        this.currentFileName = fileName;
    }


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
