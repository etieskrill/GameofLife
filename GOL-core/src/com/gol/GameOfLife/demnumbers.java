package com.gol.GameOfLife;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class demnumbers extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            TilePane pane = new TilePane();
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setVgap(5);
            pane.setHgap(5);
            pane.setPrefColumns(13);
            pane.setMaxWidth(Region.USE_PREF_SIZE);
            ObservableList<Node> list = pane.getChildren();

            for (int i = 0;i < 200;i++){
                Label view = new Label();
                view.setText(""+(i+1));
                list.add(view);
            }

            BorderPane border = new BorderPane();
            border.setCenter(pane);

            primaryStage.setScene(new Scene(border));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}