package com.Manuskrill;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;

public class GameOfLife {

    private Dimension size;
    private int speed = 100;
    private static int[][] init = new int[1][1];

    public GameOfLife(int[][] init) throws InterruptedException {

        this.size = new Dimension(init[0].length, init.length);

        Application.launch(FxWindowe.class);

        //FxWindow.setScene(new Scene(new Group(), 100, 100, ));

        TimeUnit.SECONDS.sleep(3);

        FxWindowe.setScene(FxWindowe.scene2);

    }

    public static void main(String[] args) throws InterruptedException {

        GameOfLife game = new GameOfLife(init);

        String s = "1000";
        
        int i = Integer.parseInt(Character.toString(s.charAt(0)));

    }

}
