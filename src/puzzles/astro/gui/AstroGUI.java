package puzzles.astro.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import puzzles.astro.model.AstroConfig;
import puzzles.astro.model.AstroModel;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class AstroGUI extends Application implements Observer<AstroModel, String> {
    /** The resources directory is located directly underneath the gui package */

    private String fileName;
    private AstroModel model;
    private Label text;
    private Coordinates coord;
    private int rows;
    private int cols;
    private Button gridButtons[][];

    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image robot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-blue.png"));
    private Image earth = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"earth.png"));
    private Image astro = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"astro.png"));

    private Image space = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"space.png"));

    private BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("resources/space.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    private Background background = new Background(backgroundImage);

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;

    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.fileName = filename;
        this.model = new AstroModel(fileName);
        this.model.addObserver(this);

    }

    @Override
    public void start(Stage stage) throws Exception {

        AstroConfig currentConfig = new AstroConfig(this.fileName);
        this.rows = currentConfig.getNumRows();
        this.cols = currentConfig.getNumCols();
        gridButtons = new Button[rows][cols];

        BorderPane wholeBoard = new BorderPane();

        // creates gameboard and sets it to the left of the window
        GridPane gameBoard = makeBackground(rows, cols);
        wholeBoard.setLeft(gameBoard);

        //sets the message to the top
        FlowPane currentLabel = new FlowPane();

        text = new Label("Loaded: " + fileName);
        currentLabel.getChildren().add(text);
        currentLabel.setAlignment(Pos.CENTER);
        wholeBoard.setTop(currentLabel);


        // creates a flowpane consisting of load, reset and hint buttons
        // adds the flowpane to the bottom of the window
        FlowPane buttonRow = new FlowPane();

        Button loadButton = new Button("Load");
        Button resetButton = new Button("Reset");
        Button hintButton = new Button("Hint");

        hintButton.setOnAction(event -> {
            model.hint();
        });

        resetButton.setOnAction(event -> {
            try {
                model.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        buttonRow.getChildren().addAll(loadButton, resetButton, hintButton);
        buttonRow.setAlignment(Pos.CENTER);
        wholeBoard.setBottom(buttonRow);


        GridPane controls = makeControls();

        controls.setAlignment(Pos.CENTER);
        wholeBoard.setCenter(controls);

//        Button button = new Button();
//        button.setGraphic(new ImageView(robot));
//        button.setBackground(background);
//        button.setMinSize(ICON_SIZE, ICON_SIZE);
//        button.setMaxSize(ICON_SIZE, ICON_SIZE);
//        Scene scene = new Scene(button);
//        //Scene scene = new Scene(borderPane);

        //stage.setScene(scene);
        Scene scene = new Scene(wholeBoard);
        stage.setScene(scene);
        stage.setTitle("AstroGUI");
        stage.show();
    }

//    public GridPane makeBackground(int rows, int cols){
//        GridPane background = new GridPane();
//        for(int row = 0; row< rows; row++){
//            for(int col = 0; col < cols; col++){
//
//                StackPane cell = new StackPane();
//
//
//                cell.setBackground(this.background);
//
//                cell.setMinSize(ICON_SIZE, ICON_SIZE);
//                cell.setMaxSize(ICON_SIZE, ICON_SIZE);
//
//                // Add the cell to the GridPane at the specified row and column
//                background.add(cell, col, row);
//
//                Button button = new Button();
//                String val = this.model.getVal(row,col);
//
//
//                if(val.equals(".")){
//                    button.setGraphic(new ImageView(space));
//                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
//
//                } else if (val.equals("*")) {
//                    button.setGraphic(new ImageView(earth));
//                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
//
//                } else if (val.equals("A")) {
//                    button.setGraphic(new ImageView(astro));
//                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
//
//
//                } else{
//                    button.setGraphic(new ImageView(robot));
//                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
//                }
//                int finalRow = row;
//                int finalCol = col;
//                button.setOnAction(event -> {
//                    this.model.select(finalRow, finalCol);
//                });
//                button.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-width: 0;");
//                background.add(button, col, row);
//            }
//        }
//        return background;
//    }

    public GridPane makeBackground(int rows, int cols){
        GridPane background = new GridPane();
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

//                if(val.equals(".")){
//                    button.setGraphic(new ImageView(space));
//                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
//                } else if (val.equals("*")) {
//                    button.setGraphic(new ImageView(earth));
//                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
//                } else if (val.equals("A")) {
//                    button.setGraphic(new ImageView(astro));
//                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
//                } else{
//                    button.setGraphic(new ImageView(robot));
//                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
//                }

                int finalRow = row;
                int finalCol = col;
                button.setOnAction(event -> {
                    this.model.select(finalRow, finalCol);
                });
                //button.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-width: 0;");

                // Add the button to the StackPane
                cell.getChildren().add(button);
                gridButtons[row][col] = button;
            }
        }
        return background;
    }
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
            button.setGraphic(new ImageView(robot));
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

        this.model = astroModel;
        if (text != null) {
            text.setText(msg);
            updateGameboard();
        }
    }

    public void updateGameboard(){

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                Button button = gridButtons[r][c];
                String val = model.getVal(r, c);
                buttonImage(button, val);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AstroGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
