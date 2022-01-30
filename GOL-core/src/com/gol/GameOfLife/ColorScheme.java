package com.gol.GameOfLife;

import javafx.scene.paint.Color;

public enum ColorScheme {

    LIGHT("LIGHT", Color.WHITE, Color.LIGHTGRAY, Color.GRAY, Color.LIGHTGRAY),
    //DARCULA,
    DARK("Dark", Color.BLACK, Color.DARKGRAY, Color.LIGHTGRAY, Color.DARKGRAY.darker().darker());

    final Color background;
    final Color menu;
    final Color tiles;
    final Color grid;

    final String name;

    ColorScheme(String name, Color background, Color menu, Color tiles, Color grid) {
        this.name = name;
        this.background = background;
        this.menu = menu;
        this.tiles = tiles;
        this.grid = grid;
    }

}
