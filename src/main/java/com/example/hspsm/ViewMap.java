package com.example.hspsm;

import javafx.geometry.Pos;
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
        blockPane.setHgap(30);
        blockPane.setVgap(30);
        blockPane.setAlignment(Pos.CENTER);

        for (int i = 1; i <= 5; i++) {
            Button block = new Button("Welcome User \nMap View\nBlock " + i);
            block.setPrefSize(250, 250); // Increased block size
            block.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; " +
                    "-fx-background-color: linear-gradient(to bottom, #4facfe, #00f2fe); " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 20px; " +
                    "-fx-border-radius: 20px; " +
                    "-fx-padding: 10px;");

            block.setOnAction(e -> {
                PlotScene plotScene = new PlotScene(primaryStage);
                primaryStage.setScene(plotScene.getScene());
            });

            blockPane.add(block, (i - 1) % 3, (i - 1) / 3);
        }

        this.scene = new Scene(blockPane, 1300, 800);
    }

    public Scene getScene() {
        return scene;
    }
}
class PlotScene {

    private final Scene scene;

    public PlotScene(Stage primaryStage) {
        Pane plotPane = new Pane();
        int plotSize = 170;
        int rows = 3;
        int cols = 6;
        int serviceRoadWidth = 20;
        int mainRoadWidth = 50;

        Color[] colors = {Color.LIGHTGREEN, Color.LIGHTBLUE, Color.LIGHTCORAL, Color.LIGHTYELLOW, Color.LIGHTPINK};

        plotPane.setStyle("-fx-background-color: linear-gradient(to bottom, #add8e6, #f0e68c);");

        double totalWidth = cols * plotSize;
        double totalHeight = rows * (plotSize + serviceRoadWidth);
        double offsetX = (1300 - totalWidth) / 2;
        double offsetY = (800 - totalHeight - mainRoadWidth) / 2;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int plotNumber = row * cols + col + 1;

                double x = offsetX + col * plotSize;
                double y = offsetY + row * (plotSize + serviceRoadWidth);

                String plotType = (row == 2) ? "Commercial" : "Residential";

                Color randomColor = getRandomColor(colors);

                Button plotButton = new Button(" Plot Number " + plotNumber);
                plotButton.setLayoutX(x);
                plotButton.setLayoutY(y);
                plotButton.setMinWidth(plotSize);
                plotButton.setMinHeight(plotSize);
                String style = String.format("-fx-background-color: rgba(%d,%d,%d,%.2f); " +
                                "-fx-border-color: black; -fx-font-weight: bold;",
                        (int) (randomColor.getRed() * 255),
                        (int) (randomColor.getGreen() * 255),
                        (int) (randomColor.getBlue() * 255),
                        randomColor.getOpacity());
                plotButton.setStyle(style);

                int areaSize = getRandomAreaSize();
                plotButton.setUserData(new PlotData(areaSize, plotType));

                plotButton.setOnAction(e -> togglePlotDetails(plotButton, plotNumber));

                plotPane.getChildren().add(plotButton);
            }

            if (row < rows - 1) {
                Rectangle hRoad = new Rectangle(offsetX, offsetY + (row + 1) * (plotSize + serviceRoadWidth) - serviceRoadWidth, cols * plotSize, mainRoadWidth);
                hRoad.setFill(Color.GRAY);
                plotPane.getChildren().add(hRoad);
            }
        }

        Rectangle mainRoad = new Rectangle(offsetX, offsetY + (rows * (plotSize + serviceRoadWidth)), cols * plotSize, mainRoadWidth);
        mainRoad.setFill(Color.DARKGRAY);
        plotPane.getChildren().add(mainRoad);

        Text mainRoadText = new Text("Main Road");
        mainRoadText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: white;");
        mainRoadText.setX(1300 / 2 - mainRoadText.getLayoutBounds().getWidth() / 2);  // Center the text horizontally
        mainRoadText.setY(offsetY + rows * (plotSize + serviceRoadWidth) + mainRoadWidth / 2 + 6);  // Center vertically in the service road
        plotPane.getChildren().add(mainRoadText);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #ff6347; -fx-text-fill: white; " +
                "-fx-border-radius: 10px; -fx-padding: 10px;");
        backButton.setLayoutX(20);
        backButton.setLayoutY(20);
        backButton.setOnAction(e -> primaryStage.setScene(new MainScene(primaryStage).getScene()));

        plotPane.getChildren().add(backButton);

        this.scene = new Scene(plotPane, 1300, 800);
    }

    public Scene getScene() {
        return scene;
    }

    private void togglePlotDetails(Button plotButton, int plotNumber) {
        PlotData plotData = (PlotData) plotButton.getUserData();

        if (plotButton.getText().contains("Plot")) {
            String details = " Type: " + plotData.getPlotType() +
                    "\nArea: " + plotData.getAreaSize() + " Marla";

            plotButton.setText(details);

            plotButton.setStyle("-fx-background-color: lightgray; -fx-border-color: black; " +
                    "-fx-font-weight: bold; -fx-font-size: 14px;");
        } else {
            String originalText =   " Plot Number " + plotNumber;
            plotButton.setText(originalText);

            plotButton.setStyle("-fx-background-color: " + getRandomColor(new Color[]{Color.LIGHTGREEN, Color.LIGHTBLUE, Color.LIGHTCORAL, Color.LIGHTYELLOW, Color.LIGHTPINK}) + "; " +
                    "-fx-border-color: black; -fx-font-weight: bold;");
        }
    }

    private int getRandomAreaSize() {
        Random random = new Random();
        int[] possibleSizes = {5, 7, 10};
        return possibleSizes[random.nextInt(possibleSizes.length)];
    }

    private Color getRandomColor(Color[] colors) {
        Random random = new Random();
        return colors[random.nextInt(colors.length)];
    }

}
class PlotData {
    private final int areaSize;
    private final String plotType;

    public PlotData(int areaSize, String plotType) {
        this.areaSize = areaSize;
        this.plotType = plotType;
    }

    public int getAreaSize() {
        return areaSize;
    }

    public String getPlotType() {
        return plotType;
    }
}