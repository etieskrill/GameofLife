package com.gol.GameOfLife;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashSet;

public class FxWindow extends Application {

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final String title = "Game of Life";
    public boolean showGrid = false;
    public final int tileGap = 1;

    GOLcore core;
    TilePane editPaneTiles;

    /*public Window(GOLcore core) {
        this.core = core;
    }*/

    @Override
    public void start(Stage stage) throws Exception {
        core = new GOLcore();
        core.state[1][3] = true;
        core.state[1][4] = true;
        core.state[24][13] = true;
        core.state[15][16] = true;
        stage.setTitle(title);

        //Universal ui elements
        Button save = new Button("Save");
        Button succ = new Button("succ");
        Button enter = new Button("Enter");

        Label widthLabel = new Label("width:");
        widthLabel.setAlignment(Pos.CENTER);
        TextField widthField = new TextField(Integer.toString(this.core.size.width));
        widthField.setAlignment(Pos.CENTER);
        widthField.setMaxWidth(50);
        HBox widthBox = new HBox();
        widthBox.getChildren().addAll(widthLabel, widthField);
        widthBox.setSpacing(5);
        widthBox.setAlignment(Pos.CENTER);

        Label heightLabel = new Label("height:");
        widthLabel.setAlignment(Pos.CENTER);
        TextField heightField = new TextField(Integer.toString(this.core.size.height));
        heightField.setAlignment(Pos.CENTER);
        heightField.setMaxWidth(50);
        HBox heightBox = new HBox();
        heightBox.getChildren().addAll(heightLabel, heightField);
        heightBox.setSpacing(5);
        heightBox.setAlignment(Pos.CENTER);

        HBox bottomUI = new HBox();
        bottomUI.setPadding(new Insets(20, 20, 20, 20));
        bottomUI.setSpacing(10);
        bottomUI.setAlignment(Pos.CENTER)   ;
        bottomUI.getChildren().addAll(save, succ, widthBox, heightBox, enter);

        //Def main panel
        BorderPane mainBorder = new BorderPane();
        Canvas canvas = new Canvas(core.size.width * core.tileSize.width, core.size.height * core.tileSize.height);
        StackPane background = new StackPane(canvas);
        background.setStyle("-fx-background-color: BLACK");
        mainBorder.setCenter(background);
        mainBorder.setBottom(bottomUI);
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        graphics.setFill(Color.WHITE);
        graphics.setStroke(Color.DARKGRAY);

        if (showGrid) {
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
                            (i * core.tileSize.width) + tileGap, j * (core.tileSize.height + tileGap),
                            core.tileSize.width - tileGap, core.tileSize.height - tileGap);
                }
            }
        }

        Scene main = new Scene(mainBorder);

        //Def edit panel
        refreshEditTiles();
        editPaneTiles.setAlignment(Pos.CENTER);
        editPaneTiles.setPrefColumns(this.core.size.width);
        editPaneTiles.setPrefRows(this.core.size.height);
        editPaneTiles.setMaxWidth(Region.USE_PREF_SIZE);
        editPaneTiles.setPadding(new Insets(20, 20, 0, 20));

        BorderPane editBorder = new BorderPane();
        editBorder.setCenter(editPaneTiles);
        editBorder.setBottom(bottomUI);

        Scene edit = new Scene(editBorder);
        edit.getStylesheets().add(String.valueOf(this.getClass().getResource("/style.css")));

        //Choose scene
        stage.setScene(edit);
        stage.show();

        //Button actions
        enter.setOnAction(e -> {
            this.core.size.width = parseInt(widthField);
            this.core.size.height = parseInt(heightField);

            editPaneTiles.setPrefColumns(this.core.size.width);
            editPaneTiles.setPrefRows(this.core.size.height);

            /*switch (stage.getScene()) {
                case edit:
            }*/

            refreshEditTiles();
            widthField.setText(Integer.toString(this.core.size.width));
            heightField.setText(Integer.toString(this.core.size.height));
        });

        save.setOnAction(e -> {
            for (int i = 0; i < core.size.height; i++) {
                for (int j = 0; j < core.size.width; j++) {
                    core.state[i][j] = ((CheckBox) editPaneTiles.getChildren().get(i * core.size.height + j)).isSelected();
                }
            }

            //System.out.println(Arrays.deepToString(core.state)); //Debög

            stage.setScene(main);
        });
    }

    public int parseInt(TextField input){ //Convenience bundle; checks and returns integer from a text field
        try {
            return Integer.parseInt(input.getText());
        } catch(NumberFormatException e) {
            throw new NumberFormatException("suck dick");
        }
    }

    public void refreshEditTiles() { //Refreshes edit tiles according to current core attributes, will overwrite previous data
        if (editPaneTiles == null) {
            editPaneTiles = new TilePane();
        }

        HashSet<CheckBox> checkBoxes = new HashSet<CheckBox>();
        for (int i = 0; i < this.core.size.width * this.core.size.height; i++) {
            checkBoxes.add(new CheckBox());
        }

        this.editPaneTiles.getChildren().clear();
        this.editPaneTiles.getChildren().addAll(checkBoxes);
    }

}
