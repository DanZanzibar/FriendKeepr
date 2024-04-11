//******************************************************************************
//  InternalFileStorageManager.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This class is responsible for managing access to a single internal file.
//  It implements a file 'lock' to prevent concurrent access to the file. Only
//  one instance of this class should be instantiated per managed file.
//******************************************************************************
package com.example.friendkeepr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InternalFileStorageManager {

    private final Object lock = new Object();
    private final File file;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public InternalFileStorageManager(File file) {
        this.file = file;
    }

    //--------------------------------------------------------------------------
    //  This method writes a 'String' to the managed file.
    //--------------------------------------------------------------------------
    public void writeToFile(String data) {
        synchronized (lock) {
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(data + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //--------------------------------------------------------------------------
    //  This method returns a 'String' from the managed file.
    //--------------------------------------------------------------------------
    public String readFromFile() throws FileNotFoundException {
        synchronized (lock) {
            StringBuilder data = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    data.append(line).append("\n");
                }
            } catch (IOException e){
                e.printStackTrace();
            }

            return data.toString();
        }
    }

    //--------------------------------------------------------------------------
    //  This method clears the file of any stored 'String'.
    //--------------------------------------------------------------------------
    public void clearFile() {
        writeToFile("");
    }
}
