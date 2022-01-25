package com.gol.GameOfLife;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.util.HashSet;

public class FxWindow extends Application {

    //public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final String title = "Game of Life";
    public boolean showGrid = false;
    public int tileGap = 2;
    public Dimension tileOffset = new Dimension(0, 0);

    GOLcore core;
    TilePane editPaneTiles;

    @Override
    public void start(Stage stage) throws Exception {
        core = new GOLcore();
        stage.setTitle(title);

        //Ui elements
        Button mainEditButton = new Button("Edit");
        Button mainConfirmButton = new Button("Confirm");

        Button mainNextGenButton = new Button("Next Generation");
        Button mainRunButton = new Button("Run");

        Slider mainSimSpeedSlider = new Slider(0, 100, core.simSpeed);

        Button editSaveButton = new Button("Save");
        Button editEnterButton = new Button("Enter");
        Button editClearButton = new Button("Clear");

        Slider tileWidthSlider = new Slider(0, core.tileSize.width, core.tileSize.width);
        Slider tileHeightSlider = new Slider(0, core.tileSize.height, core.tileSize.height);

        TextField mainTileGapField = new TextField(Integer.toString(tileGap));

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

        VBox mainRightUI = new VBox();
        mainRightUI.getChildren().addAll(mainNextGenButton, mainRunButton, mainSimSpeedSlider);

        HBox mainBottomUI = new HBox();
        mainBottomUI.setPadding(new Insets(20, 20, 20, 20));
        mainBottomUI.setSpacing(10);
        mainBottomUI.setAlignment(Pos.CENTER);
        mainBottomUI.getChildren().addAll(mainEditButton, tileWidthSlider, tileHeightSlider, mainTileGapField, mainConfirmButton);

        HBox editBottomUI = new HBox();
        editBottomUI.setPadding(new Insets(20, 20, 20, 20));
        editBottomUI.setSpacing(10);
        editBottomUI.setAlignment(Pos.CENTER);
        editBottomUI.getChildren().addAll(editSaveButton, widthBox, heightBox, editEnterButton, editClearButton);

        //Def main panel
        BorderPane mainBorder = new BorderPane();
        Canvas canvas = new Canvas(core.size.width * core.tileSize.width, core.size.height * core.tileSize.height);
        StackPane background = new StackPane(canvas);
        background.setStyle("-fx-background-color: BLACK");
        background.setPadding(new Insets(20, 20, 20, 20));
        mainBorder.setCenter(background);
        mainBorder.setBottom(mainBottomUI);
        mainBorder.setRight(mainRightUI);

        refreshMainTiles(canvas);

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

            refreshMainTiles(canvas);
            stage.setScene(main);
        });

        mainEditButton.setOnAction(e -> { //Edit button in main panel, sets scene to edit panel
            refreshEditTiles();
            stage.setScene(edit);
        });

        mainConfirmButton.setOnAction(e -> { //Applies current slider and tile gap inputs
            core.tileSize = new Dimension((int) tileWidthSlider.getValue(), (int) tileHeightSlider.getValue());
            tileGap = parseInt(mainTileGapField);
            refreshMainTiles(canvas);
        });

        mainNextGenButton.setOnAction(e -> {
            core.state = core.nextGeneration(core.state);
            refreshMainTiles(canvas);
        });

        editClearButton.setOnAction(e -> { //TEMP only works bcs refreshEditTiles is brok, separate func needed
            refreshEditTiles();
        });

        mainRunButton.setOnAction(e -> {
            core.running = true;
            mainRightUI.getChildren().get(0).setVisible(false);
        });

        //Slider actions
    }

    public int parseInt(TextField input){ //Convenience bundle; checks and returns integer from a text field
        try {
            return Integer.parseInt(input.getText());
        } catch(NumberFormatException e) {
            throw new NumberFormatException("suck dick");
        }
    }

    public void refreshEditTiles() { //Refreshes edit tiles according to current core attributes, will overwrite previous data
        //FIXME no it fucking doesnt, just clears all data, cbf to fix it rn
        if (editPaneTiles == null) {
            editPaneTiles = new TilePane();
        }

        HashSet<CheckBox> checkBoxes = new HashSet<>();
        for (int i = 0; i < core.size.height; i++) {
            for (int j = 0; j < core.size.width; j++) {
                CheckBox checkBox = new CheckBox();
                //checkBox.setSelected(core.state[i][j]);
                checkBoxes.add(checkBox);
            }
        }

        this.editPaneTiles.getChildren().clear();
        this.editPaneTiles.getChildren().addAll(checkBoxes);
    }

    public void refreshMainTiles(Canvas canvas) { //Draws tiles to main panel according to core state
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setFill(Color.WHITE);
        graphics.setStroke(Color.DARKGRAY);

        canvas.setWidth(core.size.width * core.tileSize.width);
        canvas.setHeight(core.size.height * core.tileSize.height);

        if (showGrid) {
            for (int i = 0; i <= core.size.height; i++) {
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
                            (i * core.tileSize.width) + tileGap + tileOffset.width,
                            (j * core.tileSize.height) + tileGap + tileOffset.height,
                            core.tileSize.width - tileGap * 2, core.tileSize.height - tileGap * 2);
                }
            }
        }
    }

}
