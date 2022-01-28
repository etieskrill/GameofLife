package com.gol.GameOfLife;

import javafx.application.Application;

import java.awt.*;
import java.util.Arrays;

public class GOLcore {

    public Dimension size = new Dimension(20, 20);
    public Dimension tileSize = new Dimension(17, 17);

    public boolean running = false;
    public int simSpeed = 1500;
    public int minSimSpeed = 1000;
    public int maxSimSpeed = 2000;

    public boolean[][] state;

    public GOLcore() {
        state = new boolean[size.height][size.width];
        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                state[i][j] = false;
            }
        }
    }

    public boolean[][] nextGeneration(boolean[][] currentGen) {
        boolean[][] nextGen = new boolean[currentGen.length][currentGen[0].length];

        for (int i = 0; i < currentGen.length; i++) {
            for (int j = 0; j < currentGen[0].length; j++) {
                int nbrAlive;

                if (currentGen[i][j]) {
                    nbrAlive = -1;
                } else {
                    nbrAlive = 0;
                }

                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        try {
                            if (currentGen[i + k][j + l]) {
                                nbrAlive++;
                            }
                        } catch (Exception ignored) {}
                    }
                }

                switch (nbrAlive) {
                    case 2 -> nextGen[i][j] = currentGen[i][j];
                    case 3 -> nextGen[i][j] = true;
                    default -> nextGen[i][j] = false;
                }
            }
        }

        return nextGen;
    }

}
