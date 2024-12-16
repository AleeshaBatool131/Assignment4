package com.example.hspsm;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class ViewMap {

    public static Scene getMainScene(Stage primaryStage) {
        MainScene mainScene = new MainScene(primaryStage);
        return mainScene.getScene();
    }
}

class MainScene {
    private final Scene scene;

    public MainScene(Stage primaryStage) {
        GridPane blockPane = new GridPane();
        blockPane.setHgap(15);
        blockPane.setVgap(15);
        blockPane.setPadding(new Insets(20, 20, 20, 20));

        for (int i = 1; i <= 5; i++) {
            Button block = new Button("Welcome User \nMap View\nBlock " + i);
            block.setPrefSize(180, 180);
            block.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                    "-fx-background-color: linear-gradient(to bottom, #4facfe, #00f2fe); " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 20px; " +
                    "-fx-border-radius: 20px; " +
                    "-fx-padding: 10px;");

            block.setOnAction(e -> {
                PlotScene plotScene = new PlotScene(primaryStage);
                primaryStage.setScene(plotScene.getScene());
            });

            block.setOnMouseEntered(e -> block.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                    "-fx-background-color: linear-gradient(to bottom, #00f2fe, #4facfe); " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 20px; " +
                    "-fx-border-radius: 20px; " +
                    "-fx-padding: 10px;"));
            block.setOnMouseExited(e -> block.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                    "-fx-background-color: linear-gradient(to bottom, #4facfe, #00f2fe); " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 20px; " +
                    "-fx-border-radius: 20px; " +
                    "-fx-padding: 10px;"));

            blockPane.add(block, (i - 1) % 3, (i - 1) / 3);
        }

        this.scene = new Scene(blockPane, 600, 500);
    }

    public Scene getScene() {
        return scene;
    }
}

class PlotScene {
    private final Scene scene;

    public PlotScene(Stage primaryStage) {
        Pane plotPane = new Pane();
        int plotSize = 150;
        int rows = 4;
        int cols = 6;
        int roadWidth = 25;

        Random random = new Random();
        Color[] colors = {Color.LIGHTGREEN, Color.LIGHTBLUE, Color.LIGHTCORAL, Color.LIGHTYELLOW, Color.LIGHTPINK};

        plotPane.setStyle("-fx-background-color: #F0F0F0;");

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int plotNumber = row * cols + col + 1;

                double x = col * plotSize;
                double y = row * (plotSize + roadWidth);

                Rectangle plot = new Rectangle(x, y, plotSize, plotSize);
                plot.setFill(colors[random.nextInt(colors.length)]);
                plot.setStroke(Color.BLACK);
                plot.setArcHeight(10);
                plot.setArcWidth(10);

                String dimensionsText = "Plot " + plotNumber + "\nW: " + plotSize + " H: " + plotSize;
                Text plotText = new Text(x + plotSize / 4, y + plotSize / 2 + 10, dimensionsText);
                plotText.setStyle("-fx-font-family: Arial; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #333333;");

                plotPane.getChildren().addAll(plot, plotText);
            }

            if (row < rows - 1) {
                Rectangle hRoad = new Rectangle(0, (row + 1) * (plotSize + roadWidth) - roadWidth, cols * plotSize, roadWidth);
                hRoad.setFill(Color.GRAY);
                plotPane.getChildren().add(hRoad);
            }
        }

        this.scene = new Scene(plotPane, cols * plotSize, rows * (plotSize + roadWidth));
    }

    public Scene getScene() {
        return scene;
    }
}
