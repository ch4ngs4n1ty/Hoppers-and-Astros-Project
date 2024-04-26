package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import puzzles.common.Coordinates;
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
    private Label text;
    private Coordinates cords;
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
        stage.sizeToScene();

        FlowPane currentLabel = new FlowPane();

        text = new Label("Loaded: " + currentFilename);

        text.setStyle("-fx-font-weight: bold;");
        currentLabel.getChildren().add(text);
        currentLabel.setAlignment(Pos.CENTER);

        wholeBoard.setTop(currentLabel);

        //Gridpane full of buttons that will be set in the center
        GridPane gameBoard = new GridPane();

        for (int r = 0; r < row; r++) {

            for (int c = 0; c < col; c++) {

                Button button = new Button();
                char val = this.model.getValue(c,r);

                if (val == 'R') {

                    button.setGraphic(new ImageView(redFrog));
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);

                } else if (val == 'G') {

                    button.setGraphic(new ImageView(greenFrog));
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);

                } else if (val == '.') {

                    button.setGraphic(new ImageView(lilyPad));
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);

                } else if (val == '*') {

                    button.setGraphic(new ImageView(water));
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);

                }

                button.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-width: 0;");

                int rowSelect = r;
                int colSelect = c;

                cords = new Coordinates(r , c);
                button.setOnAction(e -> model.select(rowSelect, colSelect));
                gameBoard.add(button, r, c);

            }
        }

        gameBoard.setAlignment(Pos.CENTER);
        wholeBoard.setCenter(gameBoard);

        FlowPane buttonRow = new FlowPane();

        Button loadButton = new Button("Load");
        Button resetButton = new Button("Reset");
        Button hintButton = new Button("Hint");

        buttonRow.getChildren().addAll(loadButton, resetButton, hintButton);
        buttonRow.setAlignment(Pos.CENTER);
        wholeBoard.setBottom(buttonRow);

        Scene scene = new Scene(wholeBoard);
        stage.setScene(scene);

        stage.show();

    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {

        if (text != null) {

            text.setText("selected " + cords);
        }

    }

    //@Override


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
