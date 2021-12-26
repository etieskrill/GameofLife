
package com.Manuskrill;

        import javafx.application.Application;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.geometry.Pos;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.layout.StackPane;
        import javafx.stage.Modality;
        import javafx.stage.Stage;
        import javafx.scene.control.Label;
        import javafx.scene.layout.VBox;
public class ConfirmButton {
        static boolean answer;

    public static boolean display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); //makes so this window has to be taken care of first
        window.setTitle(title);
        window.setWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("yes");
        Button noButton = new Button("no");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });


        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,yesButton,noButton);
        label.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); //shows and wait until its closed

        return answer;
    }
}
