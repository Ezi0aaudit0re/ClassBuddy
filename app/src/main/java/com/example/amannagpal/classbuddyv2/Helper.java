package com.example.amannagpal.classbuddyv2;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public final class Helper {



    public static final boolean createDirectoryIfNotExists(String dir){
        boolean success = true;
        try{
            File folder = new File(dir);
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                Log.d("program", "Folder created successfully");
            } else {
                throw new Exception();
            }

        }
        catch(Exception e){
            Log.d("program", "Cannot create dir: " + dir);
        }

        return success;

    }



    public static final boolean writeToFile(String string, String filepath){

        try{

            PrintWriter writer = new PrintWriter(filepath, "UTF-8");
            writer.println(string);
            writer.close();
            return true;
        }
        catch(Exception e){
            Log.d("program", "Cannot write to file: " + filepath + " , error message: " + e.toString());
            return false;
        }

    }


    public static final void readFromFile(String filepath){
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            Log.d("program", "Cannot read from file: " + filepath + ", error: " + e.toString());
            return;
        }
        String content = contentBuilder.toString();
        Log.d("program", "String from file is: " + content);
    }


    /**
     * This method prints all the files in the specified directory
     * @param dir -> the context to print in
     */
    public final static void printFiles(String dir){

        Log.d("program", "Printing all the files");

        File files = new File(dir);

        for (String file: files.list()){
            Log.d("program", "Filennamee is: " + dir + "/" + file);

        }
    }


    /**
     * check if file exists
     */
    public final static boolean checkFileExists(String filepath){
        File file = new File(filepath);

        if (file.exists()){
            Log.d("program", "File exists");
            return true;
        }
        else{
            Log.d("program", "File: " + filepath + ", Does not exist" );
            return false;

        }
    }


    /**
     * Read the data from the specified file path
     */
    public final static String readDataFromFile(String filepath){
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            Log.d("program", "Cannot read from file: " + filepath + ", error: " + e.toString());
        }
        String content = contentBuilder.toString();
        return content;
    }


    /**
     * THis  method  deletes all the files  in  the given dirrectory
     */
    public final static void deleteAllFiles(String dir){

        Log.d("program", "Delete all files");


        File files = new File(dir);

        for (String file: files.list()){
            Log.d("program", "Deleting file: " + file );
            File temp_file = new File(dir + File.separator + file);
            temp_file.delete();

        }



    }
}
