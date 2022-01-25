package com.gol.GameOfLife;

import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        GOLcore core = new GOLcore();
        FxWindow window = new FxWindow();
        Application.launch(FxWindow.class);
    }

}
