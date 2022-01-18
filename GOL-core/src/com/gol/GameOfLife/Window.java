package com.gol.GameOfLife;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;

public class Window extends Application {

    public static final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

    public final String title = "Game of Life";

    @Override
    public void start(Stage stage) throws Exception {
        GOLcore core = new GOLcore();

        stage.setTitle(title);

        HashSet<CheckBox> checkBoxes = new HashSet<CheckBox>();
        for (int i = 0; i < core.state.length * core.state[0].length; i++) {
            CheckBox checkBox = new CheckBox();
            checkBoxes.add(checkBox);
        }

        TilePane tiles = new TilePane();
        tiles.getChildren().addAll(checkBoxes);
        tiles.setAlignment(Pos.CENTER_LEFT);

        Scene edit = new Scene(tiles, size.getWidth() / 2, size.getHeight() / 2);
        edit.getStylesheets().add(String.valueOf(this.getClass().getResource("/style.css"))); //"file:///C://Users//Etienne//IdeaProjects//GameofLife//GOL-core//resources//style.css"
        stage.setScene(edit);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println(size);
        Application.launch(args);
    }

}
