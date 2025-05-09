package com.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

/**
 * Class representing a GUI of the game as well as some additional logic.
 * It uses the JavaFX library to render and process user inputs.
 * 
 * This class contains a Game instance, ArrayList of buttons,
 * 2 GridPanes (1 for the main game window, the other one is for help)
 * 
 * <p>Supported user interactions:</p>
 * <ul>
 *   <li>A: Step back a move</li>
 *   <li>D: Step forward a move</li>
 *   <li>E: Toggle hint view and the standart game view</li>
 *   <li>left or right click: Turn a node by 90 degrees clockwise
 * </ul>
 * 
 */
public class UI extends Application{
    /** Current game instance */
    static Game newgame;
    /** List of buttons of the game board (1 for each GameNode) */
    static List<Button> buttons = new ArrayList<>();
    /** Main gridpane */
    static GridPane gridpane = new GridPane();
    /** Hint gridpane */
    static GridPane hint = new GridPane();
    /** Number of rows of the game board */
    int rows;
    /** Number of columns of the game board */
    int cols;

    /** Number of turns backed */
    int turnsbacked = 0;

    static HashSet<String> currentlyActiveKeys;

    Image bulb = new Image("file:lib/bulb.png");
    Image bulb_powered = new Image("file:lib/bulb_powered.png");
    Image power_1 = new Image("file:lib/power_1.png");
    Image power_I = new Image("file:lib/power_I.png");
    Image power_L = new Image("file:lib/power_L.png");
    Image power_T = new Image("file:lib/power_T.png");
    Image power_X = new Image("file:lib/power_X.png");
    Image link_I = new Image("file:lib/link_I.png");
    Image link_I_powered = new Image("file:lib/link_I_powered.png");
    Image link_L = new Image("file:lib/link_L.png");;
    Image link_L_powered = new Image("file:lib/link_L_powered.png");
    Image link_T = new Image("file:lib/link_T.png");;
    Image link_T_powered = new Image("file:lib/link_T_powered.png");
    Image link_X = new Image("file:lib/link_X.png");;
    Image link_X_powered = new Image("file:lib/link_X_powered.png");
    Image empty = new Image("file:lib/empty.png");;

    /**
     * Main function that launches the application.
     * 
     * @param args command line arguments. First one corresponds to game dimensions (grid is always square)
     */
    public static void main(String[] args) {
		launch(args);
	}

    /**
     * Generates a new game starting state.
     * First it generates a valid solution and than randomly rotates all non-empty nodes.
     * 
     * @param rows number of rows in the grid.
     * @param cols number of columns in the grid.
     */
    public static void generateGame(int rows, int cols){
        newgame = Game.create(Math.max(rows, cols), Math.max(rows, cols));
        int powercol = (int)(Math.random() * cols);
        int powerrow = (int)(Math.random() * rows);
        newgame.createPowerNode(new Position(powerrow+1, powercol+1), Side.NORTH, Side.WEST, Side.EAST);
        double probability = 0;
        boolean[][] filled;
        filled = new boolean[rows][cols];
        filled[powerrow][powercol] = true;
        List<Position> path = new ArrayList<>();
        path.add(new Position(powerrow+1, powercol+1));

        while(Math.random() > probability){
            int direction = (int)(Math.random() * 4);
            
            if(direction == 0 && powerrow != rows-1){
                if(filled[powerrow+1][powercol] == false){
                    powerrow += 1;
                    filled[powerrow][powercol] = true;
                    path.add(new Position(powerrow+1, powercol+1));
                }
            }
            if(direction == 1 && powercol != cols-1){
                if(filled[powerrow][powercol+1] == false){
                    powercol += 1;
                    filled[powerrow][powercol] = true;
                    path.add(new Position(powerrow+1, powercol+1));
                }
            }
            if(direction == 2 && powerrow != 0){
                if(filled[powerrow-1][powercol] == false){
                    powerrow -= 1;
                    filled[powerrow][powercol] = true;
                    path.add(new Position(powerrow+1, powercol+1));
                }
            }
            if(direction == 3 && powercol != 0){
                if(filled[powerrow][powercol-1] == false){
                    powercol -= 1;
                    filled[powerrow][powercol] = true;
                    path.add(new Position(powerrow+1, powercol+1));
                }
            }
            if(path.size() > 1) probability = 0.0005;
        }
        
        int i = 1;
        while(i < path.size() - 1){
            Position previous = path.get(i-1);
            Position current = path.get(i);
            Position next = path.get(i+1);
            Side from = Side.EAST;
            Side to = Side.WEST;
            if(current.col - previous.col == 1) from = Side.WEST;
            if(current.col - previous.col == -1) from = Side.EAST;
            if(current.row - previous.row == 1) from = Side.NORTH;
            if(current.row - previous.row == -1) from = Side.SOUTH;
            if(next.col - current.col == 1) to = Side.EAST;
            if(next.col - current.col == -1) to= Side.WEST;
            if(next.row - current.row == 1) to = Side.SOUTH;
            if(next.row - current.row == -1) to = Side.NORTH;
            newgame.createLinkNode(current, from, to);
            i++;
        }
        Position current = path.get(path.size()-1);
        Position previous = path.get(path.size()-2);
        Side from = Side.NORTH;
        if(current.col - previous.col == 1) from = Side.WEST;
        if(current.col - previous.col == -1) from = Side.EAST;
        if(current.row - previous.row == 1) from = Side.NORTH;
        if(current.row - previous.row == -1) from = Side.SOUTH;
        newgame.createBulbNode(current, from);

        newgame.rotateAll();
    }

    

    /**
     * Prints the properties of a certain node.
     * @param r row
     * @param c column
     */
    public void printProperties(int r, int c){
        GameNode node = newgame.node(new Position(r, c));
        System.out.println(node.position.col + " " + node.position.row + " " + node.isBulb() + " " + node.isLink() + " " + node.isPower());
        System.out.println(node.east_connector + " " + node.south_connector + " " + node.west_connector + " " + node.north_connector);
    }

    /**
     * JavaFX start method.
     * Sets up the primary stage as well as both gridpanes.
     * Generates a new game and adds all the needed buttons and labels.
     * Adds logic for button clicks and keyboard presses.
     */
    @Override
	public void start(Stage primaryStage) {
        Parameters params = getParameters();
        List<String> args = params.getRaw();

        int size = Integer.valueOf(args.get(0));

        generateGame(size, size);

        rows = newgame.rows();
	    cols = newgame.cols();

		ImageView imgview;

		double windowWidth = 1080;
		double windowHeight = 1080;

        Label[][] labels = new Label[rows][cols];
		
		for(int r = 0; r < rows; r++){
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setPercentWidth(100.0 / cols);
			gridpane.getColumnConstraints().add(colConstraints);
            hint.getColumnConstraints().add(colConstraints);
		}

		for(int c = 0; c < cols; c++){
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100.0 / rows);
			gridpane.getRowConstraints().add(rowConstraints);
            hint.getRowConstraints().add(rowConstraints);
		}

		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
                GameNode node = newgame.node(new Position(r+1, c+1));
                imgview = getCorrectImageView(node);
				
				imgview.fitWidthProperty().bind(gridpane.widthProperty().divide(cols));
			    imgview.fitHeightProperty().bind(gridpane.heightProperty().divide(rows));
				final int finalR = r+1;
				final int finalC = c+1;

				Button button = new Button("", imgview);
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
				button.setOnMouseClicked(event -> {
					printProperties(finalR, finalC);
                    newgame.node(new Position(finalR, finalC)).turn();
                    if(!newgame.node(new Position(finalR, finalC)).isEmpty())
                        newgame.rotations[finalR-1][finalC-1] = (newgame.rotations[finalR-1][finalC-1]+1)%4;
                    if(!newgame.node(new Position(finalR, finalC)).isEmpty())
                        updateLog("game", finalR, finalC);
                    updateButtons();
				});
                buttons.add(button);
				gridpane.add(button, c, r);
                Label label = new Label("I");
                label.setFont(new Font(20));
                label.setContentDisplay(ContentDisplay.CENTER);
                labels[r][c] = label;
                hint.add(label, c, r);
			}
		}
		Scene scene = new Scene(gridpane, windowHeight, windowWidth);
        Scene scene2 = new Scene(hint, windowHeight, windowWidth);

        scene.setOnKeyPressed(event -> {
            switch(event.getCode()){
                case A:
                    stepBack("game");
                    break;
                case D:
                    stepForward("game");
                    break;
                case E:
                    updateLabels(labels);
                    primaryStage.setScene(scene2);
                    break;
                default:
                    break;
            }
        });

        scene2.setOnKeyPressed(event -> {
            switch(event.getCode()){
                case E:
                    primaryStage.setScene(scene);
                    break;
                default:
                    break;
            }
        });

        primaryStage.setTitle("Lightbulb game");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

    /**
     * Add the most recent node rotation to the game log.
     * 
     * @param filename filename "game" on default
     * @param row row of a node rotated
     * @param col column of a node rotated
     */
    void updateLog(String filename, int row, int col){
        try{
            removelastnlines(filename, turnsbacked);
            turnsbacked = 0;
            FileWriter filewriter = new FileWriter(filename, true);
            filewriter.write(row + " " + col + "\n");
            filewriter.close();
            readnthlinefromend(filename, 2);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    /**
     * Removes last n lines from a file.
     * 
     * @param filename filename "game" on default
     * @param lines number of lines to remove
     */
    void removelastnlines(String filename, int lines){
        try{
            List<String> filelines = Files.readAllLines(Paths.get(filename));
            if (filelines.size() <= lines){
                Files.write(Paths.get(filename), new byte[0]);
            } else{
                List<String> trimmed = filelines.subList(0, filelines.size() - lines);
                Files.write(Paths.get(filename), trimmed);
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }

    /**
     * Returns nth line from the end.
     * 
     * @param filename filename "game" on default
     * @param linestogoback
     * @return String corresponding to nth line from the end.
     */
    String readnthlinefromend(String filename, int linestogoback){
        String line = "";
        if(linestogoback == 0) return "";
        try {
            RandomAccessFile randomaccess = new RandomAccessFile(filename, "r");
            long length = randomaccess.length();
            if (length == 0){
                randomaccess.close();
                return "";
            }
            
            StringBuilder builder = new StringBuilder();

            long ptr = length - 1;
            while(ptr >= 0 && linestogoback >= 0){
                randomaccess.seek(ptr);
                char c = (char) randomaccess.read();
                if(c == '\n'){
                    linestogoback--;
                }
                if(linestogoback == 0){
                    builder.append(c);
                }
                ptr--;
            }
            line = builder.reverse().toString();
            randomaccess.close();
            return line;
        } catch (IOException e){
            System.out.println(e);
        }
        return "";
    }

    /**
     * Reverts last node rotation and adds 1 to turnsbacked variable
     * if possible.
     * 
     * @param filename filename "game" on default
     */
    void stepBack(String filename){
        String line = readnthlinefromend(filename, turnsbacked+1);

        if (line == "") return;

        String[] parts = line.trim().split("\\s+");

        if(parts.length < 2) return;
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        for(int i = 0; i < 3; i++){
            newgame.node(new Position(row, col)).turn();
        }
        updateButtons();

        turnsbacked++;
    }

    /**
     * Re-reverts a node rotation and subtracts 1 from turnsbacked variable
     * if possible.
     * 
     * @param filename filename "game" on default
     */
    void stepForward(String filename){
        String line = readnthlinefromend(filename, turnsbacked);

        if (line == "") return;

        String[] parts = line.trim().split("\\s+");

        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        newgame.node(new Position(row, col)).turn();
        updateButtons();

        turnsbacked--;
    }

    /**
     * Updates all the labels in the hint GridPane
     * corresponding to number of rotation needed to reach the correct
     * solution for each node.
     * 
     * @param labels 2D array of labels to update
     */
    void updateLabels(Label[][] labels){
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                int value = newgame.rotations[r][c];
                value = (4-value)%4;
                String text = String.valueOf(value);
                if(value == 0) text = "";
                labels[r][c].setText(text);
            }
        }
    }

    /**
     * Updates the UI of all buttons in the grid.
     */
    void updateButtons(){
        int i = 0;
        for(Button button : buttons){
            int c = i%cols;
            int r = (i-c)/cols;
            GameNode node = newgame.node(new Position(r+1, c+1));
            ImageView imageview = getCorrectImageView(node);
            imageview.fitWidthProperty().bind(gridpane.widthProperty().divide(cols));
            imageview.fitHeightProperty().bind(gridpane.heightProperty().divide(rows));
            button.setGraphic(imageview);
            i++;
        }
    }

    /**
     * Returns an ImageView for certain gamenode depending
     * on its type, connectors and whether its powered.
     * 
     * @param node GameNode
     * @return ImageView of a certain gamenode
     */
    ImageView getCorrectImageView(GameNode node){
        if(node.isEmpty()){
            return new ImageView(empty);
        }
        if(node.isBulb()){
            ImageView imageview = new ImageView(bulb);
            if(node.light()) imageview = new ImageView(bulb_powered);
            if(node.east()){
                imageview.setRotate(imageview.getRotate() + 90);
            }
            if(node.south()){
                imageview.setRotate(imageview.getRotate() + 180);
            }
            if(node.west()){
                imageview.setRotate(imageview.getRotate() + 270);
            }
            return imageview;
        }
        if(node.isPower()){
            int type = node.connectorsType();
            ImageView imageview = new ImageView(power_1);
            switch (type) {
                case 2:
                    imageview = new ImageView(power_I);
                    if(node.west() && node.east()) imageview.setRotate(imageview.getRotate() + 90);
                    break;
                case 3:
                    imageview = new ImageView(power_L);
                    if(node.east() && node.south()) imageview.setRotate(imageview.getRotate() + 90);
                    if(node.south() && node.west()) imageview.setRotate(imageview.getRotate() + 180);
                    if(node.west() && node.north()) imageview.setRotate(imageview.getRotate() + 270);
                    break;
                case 4:
                    imageview = new ImageView(power_T);
                    if(!node.north()) imageview.setRotate(imageview.getRotate() + 90);
                    if(!node.east())  imageview.setRotate(imageview.getRotate() + 180);
                    if(!node.south()) imageview.setRotate(imageview.getRotate() + 270);
                    break;
                case 5:
                    imageview = new ImageView(power_X);
                    break;
            }
            return imageview;
        }
        if(node.isLink()){
            int type = node.connectorsType();
            ImageView imageview = new ImageView(power_1);
            switch (type) {
                case 2:
                    imageview = new ImageView(link_I);
                    if(node.light()) imageview = new ImageView(link_I_powered);
                    if(node.west() && node.east()) imageview.setRotate(imageview.getRotate() + 90);
                    break;
                case 3:
                    imageview = new ImageView(link_L);
                    if(node.light()) imageview = new ImageView(link_L_powered);
                    if(node.east() && node.south()) imageview.setRotate(imageview.getRotate() + 90);
                    if(node.south() && node.west()) imageview.setRotate(imageview.getRotate() + 180);
                    if(node.west() && node.north()) imageview.setRotate(imageview.getRotate() + 270);
                    break;
                case 4:
                    imageview = new ImageView(link_T);
                    if(node.light()) imageview = new ImageView(link_T_powered);
                    if(!node.north()) imageview.setRotate(imageview.getRotate() + 90);
                    if(!node.east())  imageview.setRotate(imageview.getRotate() + 180);
                    if(!node.south()) imageview.setRotate(imageview.getRotate() + 270);
                    break;
                case 5:
                    imageview = new ImageView(link_X);
                    if(node.light()) imageview = new ImageView(link_X_powered);
                    break;
            }
            return imageview;
        }
        return new ImageView(empty);
    }
}
