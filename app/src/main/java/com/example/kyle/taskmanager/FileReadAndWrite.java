package com.example.kyle.taskmanager;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by kyle on 10/10/16.
 */

public class FileReadAndWrite {
    /**
     * Read results from file and have the content store in an ArrayList with each line as one element
     * @param context
     * @param fileName
     */
    public ArrayList<String> readFromFileWithLineBreaks(Context context, String fileName){
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
     * @param history
     */
    public void writeToFileWithLineBreaks(Context context, String fileName, String history){
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

}
