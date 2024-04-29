package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Hoppers game that's been made by
 * The class involves MVC.
 * A game with hopping frogs on pond with lily pads.
 * Can win by only having a Red frog on the pond.
 *
 * @author Ethan Chang
 */

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {

    private HoppersModel model;
    private Stage stage;
    private HoppersConfig currentConfig;
    private String currentFilename;
    private int row;
    private int col;
    private char board[][];
    private Label text;
    private GridPane gameBoard;
    private Button gridButtons[][];
    private BorderPane wholeBoard;
    private static final char GREEN = 'G';
    private static final char RED = 'R';
    private static final char LILYPAD = '.';
    private static final char WATER = '*';

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
        this.model = new HoppersModel(currentFilename);
        this.model.addObserver(this);

    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        currentConfig = new HoppersConfig(currentFilename);

        row = currentConfig.getRows();
        col = currentConfig.getCols();
        board = currentConfig.getGrid();
        gridButtons = new Button[row][col];

        stage.setTitle("Hoppers GUI");
        wholeBoard = new BorderPane();

        FlowPane currentLabel = new FlowPane();

        text = new Label("Loaded: " + currentFilename);

        text.setStyle("-fx-font-weight: bold;");
        currentLabel.getChildren().add(text);
        currentLabel.setAlignment(Pos.CENTER);

        wholeBoard.setTop(currentLabel);

        //Gridpane full of buttons that will be set in the center
        gameBoard = new GridPane();

        gameBoard.setAlignment(Pos.CENTER);

        for (int r = 0; r < row; r++) {

            for (int c = 0; c < col; c++) {

                char val = model.getValue(r, c);

                Button button = new Button();
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                buttonGraphics(button, val);
                gridButtons[r][c] = button;

                gameBoard.add(button, c, r);

                final int rowFinal = r;
                final int colFinal = c;

                button.setOnAction(e -> {
                    model.select(rowFinal, colFinal);
                });

            }
        }

        wholeBoard.setCenter(gameBoard);

        FlowPane buttonRow = new FlowPane();

        Button loadButton = new Button("Load");
        loadButton.setStyle("-fx-font-weight: bold;");

        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-font-weight: bold;");

        Button hintButton = new Button("Hint");
        hintButton.setStyle("-fx-font-weight: bold;");

        buttonRow.getChildren().addAll(loadButton, resetButton, hintButton);

        loadButton.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();

            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            fileChooser.setInitialDirectory(new File(currentPath));

            fileChooser.setTitle("Open Resource File");

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {

                String path = selectedFile.getPath();
                String shortenedFilename = path.substring(path.lastIndexOf(File.separator) + 1);

                try {

                    currentFilename = shortenedFilename;
                    model.load(currentFilename);

                    loadGameBoard();

                } catch (IOException ex) {

                    throw new RuntimeException(ex);
                }

            }

        });

        resetButton.setOnAction(e -> {
            try {

                model.reset();
                updateGameBoard();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        });

        hintButton.setOnAction(e -> {

            model.hint();
            updateGameBoard();

        });

        buttonRow.setAlignment(Pos.CENTER);
        wholeBoard.setBottom(buttonRow);

        Scene scene = new Scene(wholeBoard);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {

        if (text != null) {

            text.setText(msg);

            HoppersConfig currentConfiguration = hoppersModel.getCurrentConfig();

            gameBoard = new GridPane();

            row = currentConfiguration.getRows();
            col = currentConfiguration.getCols();
            board = currentConfiguration.getGrid();
            gridButtons = new Button[row][col];

            gameBoard.setAlignment(Pos.CENTER);

            for (int r = 0; r < row; r++) {

                for (int c = 0; c < col; c++) {

                    char val = model.getValue(r, c);

                    Button button = new Button();
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                    buttonGraphics(button, val);
                    gridButtons[r][c] = button;

                    gameBoard.add(button, c, r);

                    final int rowFinal = r;
                    final int colFinal = c;

                    button.setOnAction(e -> {
                        model.select(rowFinal, colFinal);
                    });

                }
            }

            wholeBoard.setCenter(gameBoard);

            stage.sizeToScene();

        }

    }

    private void loadGameBoard() {

        gameBoard.getChildren().clear();

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {

                Button button = new Button();
                char val = model.getValue(r, c);

                buttonGraphics(button, val);
                gridButtons[r][c] = button;
                gameBoard.add(button, c, r);

                button.setMaxSize(ICON_SIZE, ICON_SIZE);

                final int rowFinal = r;
                final int colFinal = c;

                button.setOnAction(e -> {
                    model.select(rowFinal, colFinal);
                });
            }
        }
    }

    private void updateGameBoard() {

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {

                Button button = gridButtons[r][c];
                char val = model.getValue(r, c);

                buttonGraphics(button, val);
            }
        }

    }

    private void buttonGraphics(Button button, char val) {

        if (val == RED) {

            button.setGraphic(new ImageView(redFrog));

        } else if (val == GREEN) {

            button.setGraphic(new ImageView(greenFrog));

        } else if (val == LILYPAD) {

            button.setGraphic(new ImageView(lilyPad));

        } else if (val == WATER) {

            button.setGraphic(new ImageView(water));

        }

        button.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-width: 0;");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
