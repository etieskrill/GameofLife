package com.gol.GameOfLife;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.util.HashSet;

public class FxWindow extends Application {

    /**
     * This class creates a GOLcore instance, and using several parameters defined below which only concern graphic
     * display it visualises the current tile state. Features include an edit pane (partially borked atm), color scheme
     * selector, general UI among other features which allow for more simple human interaction and QOL-features.
     *
     * As of now, it also includes the node listeners. Whether they preload all functions into some buffer or the
     * functions are called each time an event is detected will determine if a separate class will be created for them
     * or not.
     *
     * This class currently acts as a core builder. This functionality will subside eventually when it is clear how the
     * JavaFx application framework functions, and how to properly interface with it from outside.
     * why i be writin this nobdy gon reed it a/w
     */

    //Attributes only concerning graphic interface
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public String title = "Game of Life";
    public static boolean showGrid = true;
    public static int tileGap = 1;
    public static Dimension tileSize = new Dimension(17, 17);
    public static Dimension tileOffset = new Dimension(0, 0);
    public static ColorScheme colorScheme = ColorScheme.LIGHT;

    public static GOLcore core = new GOLcore(true);

    public TilePane editPaneTiles;
    public static Canvas canvas = new Canvas(core.size.width * tileSize.width, core.size.height * tileSize.height);

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(title);

        //Ui elements
        Button mainEditButton = new Button("Edit");

        Button mainNextGenButton = new Button("Next Generation");
        Button mainRunButton = new Button("Run");
        mainRunButton.managedProperty().bind(mainRunButton.visibleProperty());
        Button mainStopButton = new Button("Stop");
        mainStopButton.managedProperty().bind(mainRunButton.visibleProperty());
        mainStopButton.setDisable(true);

        Button tileGapSizePlus = new Button("+");
        Button tileGapSizeMinus = new Button("-");

        Label tileGapSizeLabel = new Label(Integer.toString(tileGap));
        tileGapSizeLabel.setAlignment(Pos.CENTER);

        HBox TileGapBox = new HBox();
        TileGapBox.getChildren().addAll(tileGapSizeMinus, tileGapSizeLabel, tileGapSizePlus);
        TileGapBox.setSpacing(5);


        HBox mainRunBox = new HBox();
        mainRunBox.getChildren().addAll(mainRunButton, mainStopButton);

        Slider mainSimSpeedSlider = new Slider(core.minSimSpeed, core.maxSimSpeed, core.simSpeed);
        mainSimSpeedSlider.setShowTickLabels(true);
        mainSimSpeedSlider.setShowTickMarks(true);
        mainSimSpeedSlider.setMajorTickUnit((double) core.simSpeed / 2);
        mainSimSpeedSlider.setMinorTickCount(core.simSpeed / 20);
        mainSimSpeedSlider.setBlockIncrement((double) core.simSpeed / 10);

        Button editSaveButton = new Button("Save");
        Button editEnterButton = new Button("Enter");
        Button editClearButton = new Button("Clear");

        Slider tileSizeSlider = new Slider(0,tileSize.width, tileSize.width);

        TextField mainTileGapField = new TextField(Integer.toString(tileGap));
        mainTileGapField.setPromptText("Must be integer");

        CheckBox mainGridCheckBox = new CheckBox("Show grid");

        ObservableList<ColorScheme> colSchemes =
                FXCollections.observableArrayList(
                        ColorScheme.LIGHT,
                        ColorScheme.DARK
                );

        ComboBox<ColorScheme> colorSchemeComboBox = new ComboBox<>(colSchemes);
        colorSchemeComboBox.setValue(ColorScheme.DARK);

        Label widthLabel = new Label("width:");
        widthLabel.setAlignment(Pos.CENTER);
        TextField widthField = new TextField(Integer.toString(core.size.width));
        widthField.setAlignment(Pos.CENTER);
        widthField.setMaxWidth(50);
        HBox widthBox = new HBox();
        widthBox.getChildren().addAll(widthLabel, widthField);
        widthBox.setSpacing(5);
        widthBox.setAlignment(Pos.CENTER);

        Label heightLabel = new Label("height:");
        widthLabel.setAlignment(Pos.CENTER);
        TextField heightField = new TextField(Integer.toString(core.size.height));
        heightField.setAlignment(Pos.CENTER);
        heightField.setMaxWidth(50);
        HBox heightBox = new HBox();
        heightBox.getChildren().addAll(heightLabel, heightField);
        heightBox.setSpacing(5);
        heightBox.setAlignment(Pos.CENTER);

        VBox mainRightUI = new VBox(); //TODO presets
        mainRightUI.setPadding(new Insets(10, 10, 10, 10));
        mainRightUI.setSpacing(10);
        mainRightUI.getChildren().addAll(mainNextGenButton, mainSimSpeedSlider, mainRunBox, mainGridCheckBox, colorSchemeComboBox);

        HBox mainBottomUI = new HBox();
        mainBottomUI.setPadding(new Insets(20, 20, 20, 20));
        mainBottomUI.setSpacing(10);
        mainBottomUI.setAlignment(Pos.CENTER);
        mainBottomUI.getChildren().addAll(mainEditButton, tileSizeSlider, TileGapBox);

        HBox editBottomUI = new HBox();
        editBottomUI.setPadding(new Insets(20, 20, 20, 20));
        editBottomUI.setSpacing(10);
        editBottomUI.setAlignment(Pos.CENTER);
        editBottomUI.getChildren().addAll(editSaveButton, widthBox, heightBox, editEnterButton, editClearButton);

        //Def main panel
        BorderPane mainBorder = new BorderPane();
        StackPane background = new StackPane(canvas);
        background.setStyle("-fx-background-color: " + ColorScheme.getColorName(colorScheme.background)); //FIXME why it not work? no idea
        background.setPadding(new Insets(20, 20, 20, 20));
        mainBorder.setCenter(background);
        mainBorder.setBottom(mainBottomUI);
        mainBorder.setRight(mainRightUI);

        refreshMainTiles();

        Scene main = new Scene(mainBorder);

        //Def edit panel
        refreshEditTiles();
        editPaneTiles.setAlignment(Pos.CENTER);
        editPaneTiles.setPrefColumns(core.size.width);
        editPaneTiles.setPrefRows(core.size.height);
        editPaneTiles.setMaxWidth(Region.USE_PREF_SIZE);
        editPaneTiles.setPadding(new Insets(20, 20, 0, 20));

        BorderPane editBorder = new BorderPane();
        editBorder.setCenter(editPaneTiles);
        editBorder.setBottom(editBottomUI);

        Scene edit = new Scene(editBorder);
        edit.getStylesheets().add(String.valueOf(getClass().getResource("/style.css")));

        //Choose scene
        stage.setScene(main);
        stage.setResizable(false);
        stage.show();

        //Button actions
        editEnterButton.setOnAction(e -> { //Enter button in edit panel, confirms changes to grid size, deletes current state
            //TODO instead of recreating the entire tilepane everytime, perhaps just change existing one and then show, but dat conversion shit nefarious
            int prevWidth = core.size.width;
            int prevHeight = core.size.height;

            core.size.width = parseInt(widthField);
            core.size.height = parseInt(heightField);

            editPaneTiles.setPrefColumns(core.size.width);
            editPaneTiles.setPrefRows(core.size.height);

            core.state = new boolean[core.size.height][core.size.width];
            for (int i = 0; i < core.size.height; i++) {
                for (int j = 0; j < core.size.width; j++) {
                    core.state[i][j] = false;
                }
            }

            refreshEditTiles();
            widthField.setText(Integer.toString(core.size.width));
            heightField.setText(Integer.toString(core.size.height));

            stage.sizeToScene();

            if (stage.getHeight() > screenSize.height - 2 || stage.getWidth() > screenSize.width - 2) {
                Alert alert = new Alert(
                        Alert.AlertType.WARNING,
                        "Window size exceeds screen size. Keep changes?",
                        ButtonType.APPLY,
                        ButtonType.CANCEL
                );

                alert.showAndWait();

                if (alert.getResult() == ButtonType.CANCEL) {
                    core.size.width = prevWidth;
                    core.size.height = prevHeight;

                    core.state = new boolean[core.size.height][core.size.width];
                    for (int i = 0; i < core.size.height; i++) {
                        for (int j = 0; j < core.size.width; j++) {
                            core.state[i][j] = false;
                        }
                    }

                    refreshEditTiles();
                    widthField.setText(Integer.toString(core.size.width));
                    heightField.setText(Integer.toString(core.size.height));

                    editPaneTiles.setPrefColumns(core.size.width);
                    editPaneTiles.setPrefRows(core.size.height);

                    stage.sizeToScene();
                }
            }

        });

        editSaveButton.setOnAction(e -> { //Save button in edit panel, confirms entered tile config and sets scene to main panel
            for (int j = 0; j < core.size.width; j++) {
                for (int i = 0; i < core.size.height; i++) {
                    core.state[i][j] = ((CheckBox) editPaneTiles.getChildren().get(j * core.size.width + i)).isSelected();
                } //TODO wtf it dond work for non-squares
            }

            refreshMainTiles();
            stage.setScene(main);
        });

        mainEditButton.setOnAction(e -> { //Edit button in main panel, sets scene to edit panel
            refreshEditTiles();
            if (core.running) {
                core.running = false;
                mainRunBox.getChildren().get(0).setDisable(false);
                mainRunBox.getChildren().get(1).setDisable(true);
            }
            stage.setScene(edit);
        });

        tileSizeSlider.valueProperty().addListener((observableValue, number, t1) -> {
            tileSize = new Dimension((int) tileSizeSlider.getValue(), (int) tileSizeSlider.getValue());
            refreshMainTiles();
        });

        mainNextGenButton.setOnAction(e -> {
            core.state = core.nextGeneration(core.state);
            refreshMainTiles();
        });

        editClearButton.setOnAction(e -> { //TEMP only works bcs refreshEditTiles is brok, separate func needed
            refreshEditTiles();
        });

        mainRunButton.setOnAction(e -> {
            core.running = true;
            mainRunBox.getChildren().get(0).setDisable(true);
            mainRunBox.getChildren().get(1).setDisable(false);
        });

        mainStopButton.setOnAction(e -> {
            core.running = false;
            mainRunBox.getChildren().get(0).setDisable(false);
            mainRunBox.getChildren().get(1).setDisable(true);
        });

        tileGapSizeMinus.setOnAction(e ->{
            tileGap+=1;
            tileGapSizeLabel.setText(Integer.toString(tileGap));
            refreshMainTiles();
        });
        tileGapSizePlus.setOnAction(e ->{
            tileGap-=1;
            tileGapSizeLabel.setText(Integer.toString(tileGap));
            refreshMainTiles();
        });

        mainGridCheckBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            showGrid = mainGridCheckBox.isSelected();
            refreshMainTiles();
        });

        mainSimSpeedSlider.valueProperty().addListener((observableValue, number, t1) -> {
            core.simSpeed = (int) mainSimSpeedSlider.getValue();
        });

        colorSchemeComboBox.valueProperty().addListener((observableValue, s, t1) -> {
            colorScheme = colorSchemeComboBox.getValue();
            background.setStyle("-fx-background-color: " + ColorScheme.getColorName(colorScheme.background));
            refreshMainTiles();
        });

        //Override window close request to kill thread if window closed
        stage.setOnCloseRequest(windowEvent -> {
            try {
                core.thread.destroy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.exit();
            System.exit(0);
        });

    }

    public int parseInt(TextField input){ //Convenience bundle; checks and returns integer from a text field
        try {
            return Integer.parseInt(input.getText());
        } catch(NumberFormatException e) {
            throw new NumberFormatException("suck dick");
        }
    }

    public void refreshEditTiles() { //Refreshes edit tiles according to current core attributes
        //FIXME no it fucking doesnt, just clears all data, cbf to fix it rn
        //TODO again, do not create an entirely new panel, reuse old one instead
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

        editPaneTiles.getChildren().clear();
        editPaneTiles.getChildren().addAll(checkBoxes);
    }

    public static void refreshMainTiles() { //Draws tiles to main panel according to core state
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setFill(colorScheme.tiles);
        graphics.setStroke(colorScheme.grid);

        canvas.setWidth(core.size.width * tileSize.width);
        canvas.setHeight(core.size.height * tileSize.height);

        if (showGrid) {
            for (int i = 0; i <= core.size.height; i++) {
                graphics.strokeLine(0, i * tileSize.height, (int) canvas.getWidth(), i * tileSize.height);
            }

            for (int i = 0; i <= core.size.width; i++) {
                graphics.strokeLine(i * tileSize.width, 0, i * tileSize.width, (int) canvas.getHeight());
            }
        }

        for (int i = 0; i < core.size.height; i++) {
            for (int j = 0; j < core.size.width; j++) {
                if (core.state[i][j]) {
                    graphics.fillRect(
                            (i * tileSize.width) + tileGap + tileOffset.width,
                            (j * tileSize.height) + tileGap + tileOffset.height,
                            tileSize.width - tileGap * 2, tileSize.height - tileGap * 2);
                }
            }
        }
    }

}
