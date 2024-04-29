package puzzles.hoppers.ptui;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hoppers game that's been made by PTUI and HoppersModel.
 * The class involves MVC.
 * A game with hopping frogs on pond with lily pads.
 * Can win by only having a Red frog on the pond.
 *
 * @author Ethan Chang
 */

public class HoppersPTUI implements Observer<HoppersModel, String> {

    /** current model of hoppers*/
    private HoppersModel model;
    /** current filename of hoppers*/
    private String currentFilename;

    /**
     * Creates the PTUI
     * @param filename: filename of hoppers txt files
     * @throws IOException: if filename is invalid
     */
    public void init(String filename) throws IOException {

        //Creates new model of HoppersModel
        this.model = new HoppersModel(filename);

        this.model.addObserver(this);

        System.out.println("Loaded: " + filename);
        System.out.println(this.model.toString());

        displayHelp();

    }

    /**
     * Update method for the PTUI
     * @param model: the model
     * @param data: message from the model
     *
     */
    @Override
    public void update(HoppersModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    /**
     * Displays the list of helpful commands the user can use
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * Run loops every time the user inputs something and makes call into the Model
     * @throws IOException: if file name is invalid or missing, it will catch it
     */
    public void run() throws IOException {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {

            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );

            if (words.length > 0) {

                if (words[0].startsWith("h")) {
                    this.model.hint();

                } else if (words[0].startsWith("l")) {

                    this.currentFilename = words[1];
                    this.model.load(currentFilename);

                } else if (words[0].startsWith("s") && words.length == 3) {

                    int row = Integer.parseInt(words[1]);
                    int col = Integer.parseInt(words[2]);

                    this.model.select(row, col);

                } else if (words[0].startsWith("q")) {
                    break;

                } else if (words[0].startsWith("r")) {

                    this.model.reset();


                } else {

                    init(this.currentFilename);

                }
            }
        }
    }

    /**
     * Helper method to get current file name
     * @param filename: Filename from the command args
     */
    public void saveFileName(String filename) {

        this.currentFilename = filename;

    }

    /**
     * The main program
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.saveFileName(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
