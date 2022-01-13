package com.Manuskrill;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.util.Arrays;

public class KeyboardInput {

    public KeyboardInput() throws Exception {

        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        Controller keyboard = null;
        boolean running = true;

        for (Controller controller : controllers) {
            if (controller.getType() == Controller.Type.KEYBOARD) {
                keyboard = controller;
            }
        }

        if (keyboard == null) {
            throw new Exception("Could not find suitable Keyboard");
        }

        while (running) {

            keyboard.poll();

            Component[] components = keyboard.getComponents();
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < components.length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                buffer.append(components[i].getName());
                buffer.append(": ");
                if (components[i].isAnalog()) {
                    buffer.append(components[i].getPollData());
                } else buffer.append(components[i].getPollData() == 1f);
            }

            //System.out.println(buffer.toString()); //Shitty debug line

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(ControllerEnvironment.getDefaultEnvironment().getControllers())); //Prints all available devices
        System.out.println("hallo");
        new KeyboardInput();
    }

}
