package com.Manuskrill;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;

public class Fenster extends Application /*implements EventHandler<ActionEvent>*/{
    Stage window; //creates stage
    Scene scene1, scene2; //creates scenes for this class

    public static void main(String[] args) {
        launch(args); //launches the javafx window
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage; //just easier terminology and sets primary stage to our stage called window
        window.setOnCloseRequest(e-> {
            e.consume(); //consumes setOnCloseRequest so it wont close anyways
            closeProgram();
        }); //if user uses x on topright to close window  this method is done

        Button buttonA = new Button("File");  //some buttons to have content
        Button buttonB = new Button("Edit");   //H stands for horizontal
        Button buttonC = new Button("View");
        Button buttonD = new Button("close"); //close button, closeProgram function on bottom of file
        buttonD.setOnAction(e->closeProgram());

        HBox topMenu = new HBox();              //just a layout style (do not use HBox for top menus, this an exampe)
        topMenu.getChildren().addAll(buttonA,buttonB,buttonC, buttonD); //adds all the Buttons to the topMenulayout

        VBox leftMenu = new VBox(); //adds vertical layout for sidemenu
        Button button1 = new Button("Go to other scene");   //random Buttons as examples
        button1.setOnAction(e-> window.setScene(scene2));      //changes scene for this window

        Button button2 = new Button("Alert Button");    //simple alert button
        button2.setOnAction(e-> AlertBox.display("ACHTUNG", "this an alert!")); //AlertBox is the class .diplay opens the class as window (stage)

        Button button3 = new Button("Confirm Button"); //simple confirm button which returns true/false
        button3.setOnAction(e-> {
            boolean result = ConfirmButton.display("confirm?", "do you want to confirm this?"); //the .display function again
            System.out.println(result); //prints result in JDE console
        });
        leftMenu.getChildren().addAll(button1,button2,button3); //adds all these random buttons to the left menu

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(10);
        grid.setVgap(8); //This a test

        Label Username = new Label("Username:");
        GridPane.setConstraints(Username, 0,0);
        TextField UsernameInput = new TextField("Usernamehere");
        GridPane.setConstraints(UsernameInput, 1, 0);

        Label age = new Label("Age:");
        GridPane.setConstraints(age, 0,2);
        TextField ageinput = new TextField("Age here");
        GridPane.setConstraints(ageinput, 1, 2);

        Label password = new Label("password:");
        GridPane.setConstraints(password, 0,1);
        TextField passwordInput = new TextField();
        GridPane.setConstraints(passwordInput, 1,1);
        passwordInput.setPromptText("password");

        Button LoginButton = new Button("log in");
        LoginButton.setOnAction(e->{
            if(IsInt(ageinput,ageinput.getText())){
                System.out.println("nice age bratan");
            }

            System.out.println( UsernameInput.getText() + passwordInput.getText());
        });
        GridPane.setConstraints(LoginButton, 1, 3);
        CheckBox box1 = new CheckBox("Option1");
        CheckBox box2 = new CheckBox("Option2");
        CheckBox box3 = new CheckBox("Option3");
        GridPane.setConstraints(box1, 2, 0);
        GridPane.setConstraints(box2, 2, 1);
        GridPane.setConstraints(box3, 2, 2);

        grid.getChildren().addAll(Username, UsernameInput,
                passwordInput, password,age, ageinput, box1,
                box2,box3, LoginButton);


        BorderPane borderPane = new BorderPane(); //BorderPane is a given possible layout -> use google if more questione
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftMenu);
        borderPane.setCenter(grid);

        Label label1 = new Label( "Some dummy text");


        scene1 = new Scene(borderPane, 600, 400);

        Label label2 = new Label("Some more dummy text");
        Button buttonx = new Button("Go to other scene again");
        buttonx.setOnAction(e->window.setScene(scene1));

        StackPane layout2 = new StackPane();
        layout2.getChildren().add(buttonx);
        scene2 = new Scene(layout2, 200, 300);




        window.setScene(scene1);
        window.show();
        window.setTitle("Game of Life");
    }
    private void closeProgram(){
        boolean answer = ConfirmButton.display("Exit", "are you sure you want to exit?");
        if(answer){
            window.close();
            System.out.println("File was saved");
        }
    }
    private static boolean IsInt(TextField input, String message){ //checks in input is integer
        try{
            int age = Integer.parseInt(input.getText());
            return true;
        }catch(NumberFormatException e){
            System.out.println("Error: " +  message + " is not a Number");
            return false;
        }
    }
    private void installEventHandler(final Node keyNode) {
        // handler for enter key press / release events, other keys are
        // handled by the parent (keyboard) node handler
        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.ENTER) {
                            setPressed(keyEvent.getEventType()
                                    == KeyEvent.KEY_PRESSED);

                            keyEvent.consume();
                        }
                    }
                };

        keyNode.setOnKeyPressed(keyEventHandler);
        keyNode.setOnKeyReleased(keyEventHandler);
    }
}
