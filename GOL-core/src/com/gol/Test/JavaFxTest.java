package com.gol.Test;

import com.gol.GameOfLife.GOLcore;
import com.gol.GameOfLife.FxWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaFxTest extends Application {

    GOLcore core;
    FxWindow fxwindow;

    @Override
    public void start(Stage primaryStage) {
        core = new GOLcore();
        core.state[1][3] = true;
        core.state[1][4] = true;
        core.state[24][13] = true;
        core.state[15][16] = true;

        fxwindow = new FxWindow();

        BorderPane border = new BorderPane();
        Canvas canvas = new Canvas(core.size.width * core.tileSize.width, core.size.height * core.tileSize.height);
        StackPane background = new StackPane(canvas);
        background.setStyle("-fx-background-color: BLACK");
        border.setCenter(background);
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        graphics.setFill(Color.WHITE);
        graphics.setStroke(Color.DARKGRAY);

        if (fxwindow.showGrid) {
            for (int i = 0; i < core.size.height; i++) {
                graphics.strokeLine(0, i * core.tileSize.height, (int) canvas.getWidth(), i * core.tileSize.height);
            }

            for (int i = 0; i < core.size.width; i++) {
                graphics.strokeLine(i * core.tileSize.width, 0, i * core.tileSize.width, (int) canvas.getHeight());
            }
        }

        for (int i = 0; i < core.size.width; i++) {
            for (int j = 0; j < core.size.height; j++) {
                if (core.state[i][j]) {
                    graphics.fillRect(
                            (i * core.tileSize.width) + fxwindow.tileGap, j * (core.tileSize.height + fxwindow.tileGap),
                            core.tileSize.width - fxwindow.tileGap, core.tileSize.height - fxwindow.tileGap);
                }
            }
        }

        Scene main = new Scene(border);
        primaryStage.setScene(main);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
