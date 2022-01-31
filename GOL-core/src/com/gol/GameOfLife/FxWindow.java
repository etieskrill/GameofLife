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
import javafx.scene.image.Image;

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

        Button mainTileGapSizePlus = new Button("+");
        Button mainTileGapSizeMinus = new Button("-");

        Label tileGapSizeLabel = new Label(Integer.toString(tileGap));
        tileGapSizeLabel.setAlignment(Pos.CENTER);
        tileGapSizeLabel.setPadding(new Insets(4, 2, 0, 2));

        HBox TileGapBox = new HBox();
        TileGapBox.getChildren().addAll(mainTileGapSizeMinus, tileGapSizeLabel, mainTileGapSizePlus);
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
        mainGridCheckBox.setSelected(showGrid);

        ObservableList<ColorScheme> colSchemes =
                FXCollections.observableArrayList(
                        ColorScheme.LIGHT,
                        ColorScheme.DARK
                );

        ComboBox<ColorScheme> colorSchemeComboBox = new ComboBox<>(colSchemes);
        colorSchemeComboBox.setValue(colorScheme);

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
        stage.getIcons().add(new Image("data:image/jpeg;base64,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"));

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
                    try {
                        core.state[i][j] = ((CheckBox) editPaneTiles.getChildren().get(j * core.size.width + i)).isSelected();
                    } catch (Exception ignored) {}
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

        mainTileGapSizeMinus.setOnAction(e ->{
            tileGap+=1;
            tileGapSizeLabel.setText(Integer.toString(tileGap));
            refreshMainTiles();
        });

        mainTileGapSizePlus.setOnAction(e ->{
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
