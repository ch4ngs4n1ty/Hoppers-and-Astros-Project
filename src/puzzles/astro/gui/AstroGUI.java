package puzzles.astro.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.astro.model.AstroConfig;
import puzzles.astro.model.AstroModel;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AstroGUI extends Application implements Observer<AstroModel, String> {
    private String fileName;
    private AstroModel model;
    private Label text;
    private Coordinates coord;
    private int rows;
    private int cols;
    private String[][] board;
    private BorderPane wholeBoard;
    private Button[][] gridButtons;
    private GridPane gameBoard;
    private HashMap<String, Image> robotFiles;

    private Stage stage;
    private final static String RESOURCES_DIR = "resources/";
    private Image earth = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"earth.png"));
    private Image astro = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"astro.png"));

    private Image space = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"space.png"));

    private BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("resources/space.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    private Background background = new Background(backgroundImage);

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    private AstroConfig currentConfig;


    /**
     * adds all the robot files to the hashmap of bobot files with maps the String letter from the file to the color of the png file
     */
    public void addRobotFiles(){
        robotFiles = new HashMap<>();
        robotFiles.put("B", new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-blue.png")));
        robotFiles.put("C", new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-green.png")));
        robotFiles.put("D", new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-lightblue.png")));
        robotFiles.put("E", new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-orange.png")));
        robotFiles.put("F", new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-pink.png")));
        robotFiles.put("G", new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-purple.png")));
        robotFiles.put("H", new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-white.png")));
        robotFiles.put("I", new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-yellow.png")));
    }

    /**
     *creates the model and adds ourselves as the observer
     */
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        addRobotFiles();
        this.fileName = filename;
        this.model = new AstroModel(fileName);
        this.model.addObserver(this);

    }
    /**
     * Construct the layout for the game.
     *
     * @param stage container (window) in which to render the GUI
     */
    @Override
    public void start(Stage stage) throws Exception {

        //sets stage to the current stage
        this.stage = stage;

        // generates initial configuration from the provided file and gets values of the row, column, board and buttons for the game window
        AstroConfig currentConfig = new AstroConfig(this.fileName);
        this.rows = currentConfig.getNumRows();
        this.cols = currentConfig.getNumCols();
        this.board = currentConfig.getGrid();
        gridButtons = new Button[rows][cols];

        wholeBoard = new BorderPane(); //borderpane is used for the layout of the board

        // creates gameboard and sets it to the left of the window
        gameBoard = makeBackground(rows, cols);
        wholeBoard.setLeft(gameBoard);

        //sets the message to the top
        FlowPane currentLabel = new FlowPane();

        text = new Label("Loaded: " + fileName);
        currentLabel.getChildren().add(text);
        currentLabel.setAlignment(Pos.CENTER);
        wholeBoard.setTop(currentLabel);


        // creates a flowpane consisting of load, reset and hint buttons and adds to bottom of the borderpane
        FlowPane buttonRow = new FlowPane();
        Button loadButton = new Button("Load");
        Button resetButton = new Button("Reset");
        Button hintButton = new Button("Hint");

        hintButton.setOnAction(event -> { //calls model's hint method for hint button
            model.hint();
        });

        resetButton.setOnAction(event -> { // calls model's reset method for reset button
            try {
                model.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // load button opens up straight to the directory that holds the data files for the astro puzzle
        //changes all the non-static instance variables tp the new config's variables

        loadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
           fileChooser.setInitialDirectory(new File(currentPath));
           File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
               try {
                    String path = selectedFile.getPath();

                    String shortenedFilename = path.substring(path.lastIndexOf(File.separator) + 1);
                    fileName = shortenedFilename;

                    this.currentConfig = new AstroConfig(fileName);

                    text.setText(fileName);

                    gameBoard.getChildren().clear();
                    this.rows = model.getRows();
                    this.cols = model.getCols();

                    gameBoard = makeBackground(rows, cols);
                    wholeBoard.setLeft(gameBoard);

                    model.load(fileName);

                    updateGameboard();

                } catch (IOException ex) {

                    System.out.println(ex);
                }
            }
        });


        buttonRow.getChildren().addAll(loadButton, resetButton, hintButton);
        buttonRow.setAlignment(Pos.CENTER);
        wholeBoard.setBottom(buttonRow);

        //creates the n,s, w, e buttons for the controls and adds the center of borderpane
        GridPane controls = makeControls();
        controls.setAlignment(Pos.CENTER);
        wholeBoard.setCenter(controls);

        // sets the scene to the borderpane containing the entire window
        //sets title, stage and makes stake resizable
        Scene scene = new Scene(wholeBoard);
        stage.setScene(scene);
        stage.setTitle("AstroGUI");
        stage.sizeToScene();

        stage.setResizable(true);
        stage.show();
    }

    /**
     * creates a gridpane of the game board with specified dimensions and adds imaged for each coordinate
     * @param rows of gameboard
     * @param cols of gameboard
     * @return the gridpane for the background of gameboard
     */
    public GridPane makeBackground(int rows, int cols){
        GridPane background = new GridPane();

        background.getChildren().clear(); //new
        this.gridButtons = new Button[rows][cols];

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                StackPane cell = new StackPane();

                // Set the background of the StackPane
                cell.setBackground(this.background);
                cell.setMinSize(ICON_SIZE, ICON_SIZE);
                cell.setMaxSize(ICON_SIZE, ICON_SIZE);

                background.add(cell, col, row);

                Button button = new Button();
                String val = this.model.getVal(row,col);

                buttonImage(button, val);

                int finalRow = row;
                int finalCol = col;
                button.setOnAction(event -> {
                    this.model.select(finalRow, finalCol);
                });
                // Add the button to the StackPane
                cell.getChildren().add(button);
                gridButtons[row][col] = button;
            }
        }
        return background;
    }

    /**
     * sets the image to each button coordinate
     * @param button to set image  on
     * @param val the string equaivalent of explorer or space or earth
     */
    public void buttonImage(Button button, String val){

        if(val.equals(".")){
            button.setGraphic(new ImageView(space));
            button.setMaxSize(ICON_SIZE, ICON_SIZE);
        } else if (val.equals("*")) {
            button.setGraphic(new ImageView(earth));
            button.setMaxSize(ICON_SIZE, ICON_SIZE);
        } else if (val.equals("A")) {
            button.setGraphic(new ImageView(astro));
            button.setMaxSize(ICON_SIZE, ICON_SIZE);
        } else{

            Image robotImage = robotFiles.get(val);
            ImageView robotImageView = new ImageView(robotImage);

            button.setGraphic(robotImageView);

            button.setMaxSize(ICON_SIZE, ICON_SIZE);
        }
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-width: 0;");
    }


    public GridPane makeControls(){
        GridPane controls = new GridPane();

        Button up = new Button("N");
        controls.add(up, 1, 0);
        up.setOnAction(event -> {
            this.model.move("n");
        });

        Button down = new Button("S");
        controls.add(down, 1, 2);
        down.setOnAction(event -> {
            this.model.move("s");
        });

        Button rightcontrol = new Button("E");
        controls.add(rightcontrol, 2,1);
        rightcontrol.setOnAction(event -> {
            this.model.move("e");
        });

        Button left = new Button("W");
        controls.add(left, 0, 1);
        left.setOnAction(event -> {
            this.model.move("w");
        });
        return controls;
    }

    @Override
    public void update(AstroModel astroModel, String msg) {
        if (text != null) {
            text.setText(msg);

            AstroConfig currentAstroConfig = astroModel.getCurrentConfig();

            gameBoard = new GridPane();

            this.rows = currentAstroConfig.getNumRows();
            this.cols = currentAstroConfig.getNumCols();
            this.board = currentAstroConfig.getGrid();
            gridButtons = new Button[rows][cols];

            gameBoard.setAlignment(Pos.CENTER);

            GridPane background = new GridPane();

            //background.getChildren().clear(); //new
            this.gridButtons = new Button[rows][cols];

            for(int row = 0; row < rows; row++){
                for(int col = 0; col < cols; col++){
                    StackPane cell = new StackPane();

                    // Set the background of the StackPane
                    cell.setBackground(this.background);
                    cell.setMinSize(ICON_SIZE, ICON_SIZE);
                    cell.setMaxSize(ICON_SIZE, ICON_SIZE);

                    gameBoard.add(cell, col, row);

                    Button button = new Button();
                    String val = this.model.getVal(row,col);

                    buttonImage(button, val);

                    int finalRow = row;
                    int finalCol = col;
                    button.setOnAction(event -> {
                        this.model.select(finalRow, finalCol);
                    });

                    // Add the button to the StackPane
                    cell.getChildren().add(button);
                    gridButtons[row][col] = button;
                }
            }
            wholeBoard.setLeft(gameBoard);
            stage.sizeToScene();
        }
    }

    public void updateGameboard(){

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                Button button = gridButtons[r][c];
                String val = model.getVal(r, c);
                buttonImage(button, val);
            }
        }
    }

    /**
     * main function to launch the game
     * @param args the file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AstroGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
