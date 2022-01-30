package com.gol.GameOfLife;

import java.util.concurrent.TimeUnit;

import static com.gol.GameOfLife.FxWindow.refreshMainTiles;

public class RunningThread extends Thread{

    GOLcore core;

    public RunningThread(GOLcore core) {
        this.core = core;
    }

    @Override
    public void run() {
        while (true) {
            System.out.print("thread access");
            if (core.running) {
                core.state = core.nextGeneration(core.state);
                refreshMainTiles();
                System.out.print(" successful\n");
            } else {
                System.out.print(" denied\n");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10 + core.maxSimSpeed - core.simSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
