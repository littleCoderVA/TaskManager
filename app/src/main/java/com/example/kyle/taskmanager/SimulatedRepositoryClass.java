package com.example.kyle.taskmanager;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by kyle on 10/10/16.
 * Singleton class for reading and writing from file
 */

class SimulatedRepositoryClass {

    /* Lazy loading for singleton instance, so leaving it null, didn't instantiate it here */
    private static SimulatedRepositoryClass instance = null;
    /* Private method to ensure other classes can't invoke additional instance of this class */
    private SimulatedRepositoryClass(){}
    /* The only method outside classes can call to get the singleton instance of this class */
    static SimulatedRepositoryClass getInstance(){
        if (instance == null) {
            instance = new SimulatedRepositoryClass();
        }
        return instance;
    }

    /**
     * Read results from file and have the content store in an ArrayList with each line as one element
     * @param context context
     * @param fileName storage file name
     */
    ArrayList<String> readFromFileWithLineBreaks(Context context, String fileName){
        FileInputStream fis;
        /**
         * Use buffer reader to be able to read lines from fileInputStream. Otherwise
         * fileInputStream will only have a read() method and read the whole content by byte
         */
        BufferedReader reader;
        ArrayList<String> parsedContent = new ArrayList<>();
        try {
            fis = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(fis));
            String content;
            while ((content = reader.readLine()) != null) {
                parsedContent.add(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsedContent;
    }

    // May need a simpler version of readFromFile()

    /**
     * Save to file use openFileOutput(), get fileOutputStream back. Use getFilesDir to get the path if needed
     * @param history record message
     */
    void writeToFileWithLineBreaks(Context context, String fileName, String history){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            /* This is the way to get line separator */
            fos.write(System.getProperty("line.separator").getBytes());
            fos.write(history.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Save object to file
     * @param context activity
     * @param task record message
     */
    void writeTaskToFile(Context context, TaskInterface task){
        System.out.println("Writing "+task.toString()+" to file");
        writeToFileWithLineBreaks(context, context.getString(R.string.data_file_name), task.toString());
    }

}
