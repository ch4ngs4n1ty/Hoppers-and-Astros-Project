package puzzles.hoppers.gui;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {


    private HoppersModel model;
    private HoppersConfig currentConfig;
    private String currentFilename;
    private char board[][];
    private int row;
    private int col;
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));

    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;

    public void init() throws IOException {

        String filename = getParameters().getRaw().get(0);
        currentFilename = filename;
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        //this.model.load(filename);

    }

    @Override
    public void start(Stage stage) throws Exception {

        currentConfig = new HoppersConfig(currentFilename);
        row = currentConfig.getRows();
        col = currentConfig.getCols();

        board = currentConfig.getGrid();

        stage.setTitle("Hoppers GUI");
        BorderPane wholeBoard = new BorderPane();


        GridPane gameBoard = new GridPane();

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {

                Button button = new Button();
                button.setGraphic(new ImageView(redFrog));
                button.setMaxSize(ICON_SIZE, ICON_SIZE);

                gameBoard.add(button, r, c);
            }
        }

        wholeBoard.setCenter(gameBoard);

        Scene scene = new Scene(wholeBoard);
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
