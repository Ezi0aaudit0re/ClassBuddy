package com.example.amannagpal.classbuddyv2.RecordingLogic;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.example.amannagpal.classbuddyv2.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * This class handnles all the recording requests
 */
public class Recorder {

    // constants
    public static final String TEMP_FILENAME = "TempV1";
    public static final String FILE_FORMAT = ".pcm";
    private static final String CONVERTED_FILE_FORMAT = ".flac";




    // variables
    MediaRecorder mediaRecorder  = null;
    MediaPlayer mediaPlayer  = null;
//    ByteArrayOutputStream byteArrayOutputStream;
//    ParcelFileDescriptor[] descriptors;
//    ParcelFileDescriptor parcelRead, parcelWrite;


    File file;
    FileOutputStream fileOutputStream;
    int counter = 0; // each recording will be 20 mins long and counter is used to keep track of it





    private StringBuilder pathSave= new StringBuilder(); // this is where the recorded audio will be saved
    private StringBuilder pathWav = new StringBuilder();
    Context applicationContext = null;


    public Recorder(int record, Context context){
        // set up media recorder if user wants to rercord
        if(record == 1){
            applicationContext = context;
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);


            // WE still have to set the directory of where the recording will be saved and set the audio recorder
        }

    }




    /**
     * This method is used to create the path string where the file will be saved
     * @return True/False depending on success
     */
    public boolean createUpdatePathSave(){

        try{
            // create the string
            // update the pathSave variable with the string
            pathSave.append(applicationContext.getFilesDir().toString()).append("/" + TEMP_FILENAME).append("_" + Integer.toString(counter)).append(FILE_FORMAT);
            pathWav.append(applicationContext.getFilesDir().toString()).append("/" + TEMP_FILENAME).append(("_") + Integer.toString(counter)).append(CONVERTED_FILE_FORMAT);


            // update media recorder output source
            mediaRecorder.setOutputFile(pathSave.toString());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);



            return true;

        }
        catch(Exception e){
            Log.d("program", "Problem creating the pathSave String: " + e.toString());
            return false;
        }






    }





    // starrt the rrecording
    public boolean startRecording(){
        try{
            if(createUpdatePathSave()){
                mediaRecorder.prepare();
                mediaRecorder.start();
            }



        }
        catch (Exception e){
            Log.d("program", "Cannot start recording: " + e.toString());
            mediaRecorder.release();
            mediaRecorder.reset();
            return false;
        }

        return true;


    }


    // stop recording
    public boolean stopRecording(){
        try{
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            return true;
        }
        catch (Exception e){
            Log.d("program", "Cannot stop recording: " + e.toString());
            return false;
        }

    }


    // play the recording
    public void playRecording(){
        this.printFiles();
        try{
            mediaPlayer = new MediaPlayer();
            Log.d("program", "File path is: " + pathSave.toString());
            mediaPlayer.setDataSource(pathSave.toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e){
            Log.d("program", "Error in playing recording: " + e.toString());
        }

    }


    public void playRecording(String audioFilepath){
        Log.d("program", "Files after conversionn are: ");
        this.printFiles();
        try{
            mediaPlayer = new MediaPlayer();
            Log.d("program", "File path is: " + pathSave.toString());
            mediaPlayer.setDataSource(audioFilepath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e){
            Log.d("program", "Error in playing recording: " + e.toString());
        }

    }



    // get the filepath
    public String getFilepath(){
        return this.pathSave.toString();
    }


    // get the pathWav


    public String getPathWav() {
        return pathWav.toString();
    }



    // get all the files stored for the application
    public void printFiles(){
        String dir = applicationContext.getFilesDir().toString();

        File files = new File(dir);

        for (String file: files.list()){
            Log.d("program", "Filennamee is: " + file);

        }
    }
}
