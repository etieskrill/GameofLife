package com.Manuskrill;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class FxWindow extends Application {

    private final int width = 100;
    private final int height = 100;
    private final String title = "an interesting tit";
    public static Stage stage;
    public static Scene scene1;
    public static Scene scene2;

    public void start(Stage stage) throws InterruptedException{

        Group root1 = new Group();
        Group root2 = new Group();
        Scene scene1 = new Scene(root1, width, height);
        Scene scene2 = new Scene(root1, width, height);

        Text text = new Text(10, 90, "JavaFX Scene");
        text.setFill(Color.DARKRED);
        text.setFont(new Font(20));

        root1.getChildren().add(text);

        Text text1 = new Text(10, 90, "hehehehehe");
        text.setFill(Color.LIME);
        text.setFont(new Font(40));

        root2.getChildren().add(text1);

        stage.setScene(scene1);
        stage.show();

        TimeUnit.SECONDS.sleep(6);

        stage.setScene(scene2);

    }

    public static void setScene(Scene scene) {
        stage.setScene(scene);
    }

}
