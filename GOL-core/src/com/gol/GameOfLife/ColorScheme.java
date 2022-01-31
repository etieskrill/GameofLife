package com.gol.GameOfLife;

import javafx.scene.paint.Color;

public enum ColorScheme {

    LIGHT("Light", Color.WHITE, Color.LIGHTGRAY, Color.BLACK, Color.LIGHTGRAY),
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

    public static String getColorName(Color color) { //FIXME debug this
        if (Color.WHITE.equals(color)) {
            return "WHITE";
        } else if (Color.LIGHTGRAY.equals(color)) {
            return "LIGHTGRAY";
        } else if (Color.DARKGRAY.equals(color)) {
            return "DARKGRAY";
        } else if (Color.BLACK.equals(color)) {
            return "BLACK";
        } else throw new RuntimeException("yalls colours wack");
    }

}
