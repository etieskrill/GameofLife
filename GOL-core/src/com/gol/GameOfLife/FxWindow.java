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
    public boolean showGrid = true;
    public final int tileGap = 2;

    GOLcore core;
    TilePane editPaneTiles;

    @Override
    public void start(Stage stage) throws Exception {
        core = new GOLcore();
        stage.setTitle(title);

        //Ui elements
        Button mainEditButton = new Button("Edit");

        Button editSaveButton = new Button("Save");
        Button editEnterButton = new Button("Enter");

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

        HBox mainBottomUI = new HBox();
        mainBottomUI.setPadding(new Insets(20, 20, 20, 20));
        mainBottomUI.setSpacing(10);
        mainBottomUI.setAlignment(Pos.CENTER);
        mainBottomUI.getChildren().addAll(mainEditButton);

        HBox editBottomUI = new HBox();
        editBottomUI.setPadding(new Insets(20, 20, 20, 20));
        editBottomUI.setSpacing(10);
        editBottomUI.setAlignment(Pos.CENTER);
        editBottomUI.getChildren().addAll(editSaveButton, widthBox, heightBox, editEnterButton);

        //Def main panel
        BorderPane mainBorder = new BorderPane();
        Canvas canvas = new Canvas(core.size.width * core.tileSize.width, core.size.height * core.tileSize.height);
        StackPane background = new StackPane(canvas);
        background.setStyle("-fx-background-color: BLACK");
        background.setPadding(new Insets(20, 20, 0, 20));
        mainBorder.setCenter(background);
        mainBorder.setBottom(mainBottomUI);
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        refreshMainTiles(graphics, canvas);

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
        editBorder.setBottom(editBottomUI);

        Scene edit = new Scene(editBorder);
        edit.getStylesheets().add(String.valueOf(this.getClass().getResource("/style.css")));

        //Choose scene
        stage.setScene(main);
        stage.show();

        //Button actions
        editEnterButton.setOnAction(e -> { //Enter button in edit panel, confirms changes to grid size
            this.core.size.width = parseInt(widthField);
            this.core.size.height = parseInt(heightField);

            editPaneTiles.setPrefColumns(this.core.size.width);
            editPaneTiles.setPrefRows(this.core.size.height);

            refreshEditTiles();
            widthField.setText(Integer.toString(this.core.size.width));
            heightField.setText(Integer.toString(this.core.size.height));
        });

        editSaveButton.setOnAction(e -> { //Save button in edit panel, confirms entered tile config and sets scene to main panel
            for (int i = 0; i < core.size.height; i++) {
                for (int j = 0; j < core.size.width; j++) {
                    core.state[j][i] = ((CheckBox) editPaneTiles.getChildren().get(i * core.size.height + j)).isSelected();
                }
            }

            //System.out.println(Arrays.deepToString(core.state)); //Filthy debög

            refreshMainTiles(graphics, canvas);
            stage.setScene(main);
        });

        mainEditButton.setOnAction(e -> { //Edit button in main panel, sets scene to edit panel
            stage.setScene(edit);
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

    public void refreshMainTiles(GraphicsContext graphics, Canvas canvas) { //Draws tiles to main panel according to core state
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setFill(Color.WHITE);
        graphics.setStroke(Color.DARKGRAY);

        if (showGrid) {
            for (int i = 0; i < core.size.height; i++) {
                graphics.strokeLine(0, i * core.tileSize.height, (int) canvas.getWidth(), i * core.tileSize.height);
            }

            for (int i = 0; i <= core.size.width; i++) {
                graphics.strokeLine(i * core.tileSize.width, 0, i * core.tileSize.width, (int) canvas.getHeight());
            }
        }

        for (int i = 0; i < core.size.width; i++) {
            for (int j = 0; j < core.size.height; j++) {
                if (core.state[i][j]) {
                    graphics.fillRect(
                            (i * core.tileSize.width) + tileGap - 1, j * (core.tileSize.height + tileGap - 1),
                            core.tileSize.width - tileGap, core.tileSize.height - tileGap);
                }
            }
        }
    }

}
