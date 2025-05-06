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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class UI extends Application{
    static Game newgame;
    static List<Button> buttons = new ArrayList<>();
    static GridPane gridpane = new GridPane();
    int rows = newgame.rows();
	int cols = newgame.cols();

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

    public static void main(String[] args) {
        generateGame(1, 8, 5);
		launch(args);
	}

    public static void generateGame(int bulbs, int rows, int cols){
        newgame = Game.create(Math.max(rows, cols), Math.max(rows, cols));
        newgame.createBulbNode(new Position(1, 5), Side.NORTH);
        newgame.createLinkNode(new Position(2, 5), Side.EAST, Side.SOUTH);
        newgame.createLinkNode(new Position(2, 4), Side.NORTH, Side.SOUTH);
        newgame.createPowerNode(new Position(2, 3), Side.NORTH, Side.WEST, Side.EAST);
    }

    public void printProperties(int r, int c){
		System.out.println("row: " + r + " col: " + c);
	}

    @Override
	public void start(Stage primaryStage) {
		ImageView imgview;

		double windowWidth = 600;
		double windowHeight = 600;

        //System.out.println(windowHeight + " " + windowWidth + " " +max(cols, rows) );
		
		for(int r = 0; r < rows; r++){
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setPercentWidth(100.0 / cols);
			gridpane.getColumnConstraints().add(colConstraints);
		}

		for(int c = 0; c < cols; c++){
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100.0 / rows);
			gridpane.getRowConstraints().add(rowConstraints);
		}

		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
                GameNode node = newgame.node(new Position(r+1, c+1));
                System.out.println(node.bulb + " " + node.link + " " + node.power);
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
                        updateLog("game", finalR, finalC);
                    updateButtons();
				});
                buttons.add(button);
				gridpane.add(button, c, r);
			}
		}
		Scene scene = new Scene(gridpane, windowHeight, windowWidth);

        scene.setOnKeyPressed(event -> {
            switch(event.getCode()){
                case A:
                    stepBack("game");
                    break;
                case D:
                    stepForward("game");
                    break;
                default:
                    break;
            }
        });

        primaryStage.setTitle("Lightbulb game");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
		System.out.println(scene.getWidth() + scene.getHeight());
	}

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

    void removelastnlines(String filename, int lines){
        try{
            System.out.println("removing: " + lines);
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
            System.out.println("line:" + line + "n");
            randomaccess.close();
            return line;
        } catch (IOException e){
            System.out.println(e);
        }
        System.out.println("steping back");
        return "";
    }

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
            if(!node.isEmpty()){
                System.out.println("r: " + r+1 + " c " + c+1 + " light " + node.light());
            }
        }
    }

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
