package com.gol.GameOfLife;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashSet;

public class Window extends Application {

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public final String title = "Game of Life";

    @Override
    public void start(Stage stage) throws Exception {
        GOLcore core = new GOLcore();

        stage.setTitle(title);

        //Universal ui elements
        Button ok = new Button("Ok boomer");
        Button succ = new Button("succ");
        Button enter = new Button("enter");

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

        HBox bottomUI = new HBox();
        bottomUI.setPadding(new Insets(20, 20, 20, 20));
        bottomUI.setSpacing(10);
        bottomUI.setAlignment(Pos.CENTER)   ;
        bottomUI.getChildren().addAll(ok, succ, widthBox, heightBox, enter);

        //Button actions
        enter.setOnAction(e-> {
            widthBox.getText
        });

        //Def edit panel
        HashSet<CheckBox> checkBoxes = new HashSet<CheckBox>();
        for (int i = 0; i < core.state.length * core.state[0].length; i++) {
            checkBoxes.add(new CheckBox());
        }

        TilePane tiles = new TilePane();
        tiles.getChildren().addAll(checkBoxes);
        tiles.setAlignment(Pos.CENTER);
        tiles.setPrefColumns(50);
        tiles.setPrefRows(50);
        tiles.setMaxWidth(Region.USE_PREF_SIZE);
        tiles.setPadding(new Insets(20, 20, 0, 20));

        BorderPane editBorder = new BorderPane();
        editBorder.setCenter(tiles);
        editBorder.setBottom(bottomUI);

        Scene edit = new Scene(editBorder);
        edit.getStylesheets().add(String.valueOf(this.getClass().getResource("/style.css")));
        stage.setScene(edit);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println(screenSize);
        Application.launch(args);
    }

}
