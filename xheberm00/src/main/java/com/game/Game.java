package com.game;

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

public class Game extends Application {
	public void printProperties(int r, int c){
		System.out.println("row: " + r + " col: " + c);
	}

	@Override
	public void start(Stage primaryStage) {
		Image img = new Image("file:lib/bulb.png");
		ImageView imgview;

		double windowWidth = 600;
		double windowHeight = 600;
		int rows = 6;
		int cols = 6;

		GridPane gridpane = new GridPane();
		
		for(int r = 0; r < rows; r++){
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setPercentWidth(100.0 / rows);
			gridpane.getColumnConstraints().add(colConstraints);
		}

		for(int c = 0; c < cols; c++){
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100.0 / cols);
			gridpane.getRowConstraints().add(rowConstraints);
		}

		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
				imgview = new ImageView(img);
				
				imgview.fitWidthProperty().bind(primaryStage.widthProperty().divide(cols));
			        imgview.fitHeightProperty().bind(primaryStage.heightProperty().divide(rows));
				final int finalR = r;
				final int finalC = c;

				Button button = new Button("", imgview);
				button.setOnMouseClicked(event -> {
					printProperties(finalR, finalC);
					button.setRotate(button.getRotate() + 90);
				});
				gridpane.add(button, c, r);
			}
		}
		Scene scene = new Scene(gridpane, windowHeight, windowWidth);

        	primaryStage.setTitle("Lightbulb game");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
		System.out.println(scene.getWidth() + scene.getHeight());
	}

	public static void main(String[] args) {
        	launch(args);
	}
}

