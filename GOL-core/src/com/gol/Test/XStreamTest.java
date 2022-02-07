package com.gol.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class XStreamTest {

    /*Upon construction, the file object often takes in a path, which can be absolute, canonical, or as it is the case
    * below, relative. Canonical is like absolute, but a little formatted, and relative takes not the project folder,
    * but the module as the current root. Relative paths are also written using forward slashes instead of the backward
    * ones. A file object can be made from any file type. After construction, the file is completely independent from
    * the source it may have originated from. A file can also create a new file in a given path, delete it, and get
    * its attributes. A file can consist of a nested folder structure, and so is more or less what one could expect
    * from a file/folder in the explorer.*/

    static File file = new File("GOL-core/resources/presets.xml");

    /*While technically possible, it is advised not to read from or write to a file using the file class, and instead
    * opt for a more specialised wrapper, like the FileReader and FileWriter classes. The Scanner class can also be
    * used to read, and provides much of the same syntax as when using a Scanner to read from the console. I have used
    * a scanner for the first example out of simplicity. */

    static Scanner scanner;

    /*This is something called a static initialization block, it is run once before main loop, there can be multiple,
    * if so they are run from top to bottom, they can also be below the main method. Very handy indeed. Here it uses
    * a try/catch during the initialization without the need to use it inside the constructor, the main method, or some
    * other method. */
    static {
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("First line: " + scanner.nextLine());
        System.out.println("Absolute path: " + file.getAbsolutePath());
        System.out.println("Canonical path(?): " + file.getCanonicalPath());
        System.out.println("Can execute: " + file.canExecute());
        System.out.println("Can read: " + file.canRead());
        System.out.println("Can write: " + file.canWrite());
    }

    public XStreamTest() throws FileNotFoundException {
        System.out.println("smoke vic, sucK dicK");
    }
}
