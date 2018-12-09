package com.example.amannagpal.classbuddyv2.RecordingLogic;

import android.content.Context;
import android.media.AudioRecord;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import com.example.amannagpal.classbuddyv2.SpeechToText.AudioConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecorderV2 {

    public static final String TEMP_FILENAME = "TempV2";
    public static final String FILE_FORMAT = ".pcm";
    public static final String WAV_FILE_FORMAT = ".wav";

    public static final int RECORDER_SAMPLERATE = 8000;

    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;

    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_8BIT;

    private AudioRecord recorder = null;
    private Thread writingThread = null;
    private boolean isRecording = false;
    private MediaPlayer mediaPlayer  = null;
    private AudioConverter audioConverter = null;

    Context mContext;

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    StringBuilder pathSave = new StringBuilder();
    StringBuilder pathSaveWav = new StringBuilder();


    public RecorderV2(Context context){
        mContext = context;
//        createUpdatePathSave(0);
//        if(createUpdatePathSave(0) == false) {
//            Log.d("program", "Problem creating the savepath for file");
//        }
    }


//    /**
//     * Start the recording in PCM format
//     */
//    public boolean startRecording() {
//
//        try{
//            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
//                    RECORDER_SAMPLERATE, RECORDER_CHANNELS,
//                    RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);
//
//            recorder.startRecording();
//
//            isRecording = true;
//        }
//        catch (Exception e){
//            Log.d("ProgramError", "Cannot Start recording");
//            Log.d("ProgramError", e.toString() );
//            return false;
//        }
//
//
//        writingThread = new Thread(new Runnable() {
//
//            public void run() {
//
//                writeAudioDataToFile();
//
//            }
//        }, "AudioRecorder Thread to write to file");
//        writingThread.start();
//
//        return true;
//    }
//
//
//    //Conversion of short to byte
//    private byte[] short2byte(short[] sData) {
//        int shortArrsize = sData.length;
//        byte[] bytes = new byte[shortArrsize * 2];
//
//        for (int i = 0; i < shortArrsize; i++) {
//            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
//            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
//            sData[i] = 0;
//        }
//        return bytes;
//    }
//
//
//
//    private void writeAudioDataToFile() {
//        // Write the output audio in byte
//
//        pathSave.append(mContext.getFilesDir().toString()).append("/" + TEMP_FILENAME).append("_" + Integer.toString(0)).append(FILE_FORMAT);
//
//        Log.d("program", "FIle to write is: " + pathSave.toString());
//
//        short sData[] = new short[BufferElements2Rec];
//
//        FileOutputStream os = null;
//        try {
//            os = new FileOutputStream(pathSave.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        while (isRecording) {
//            // gets the voice output from microphone to byte format
//            recorder.read(sData, 0, BufferElements2Rec);
//            Log.d("program","Short wirting to file" + sData.toString());
//            try {
//                // writes the data to file from buffer stores the voice buffer
//                byte bData[] = short2byte(sData);
//
//                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
////    private void writeAudioDataToFileV2() {
////        byte data[] = new byte[bufferSize];
////        String filename = getTempFilename();
////        FileOutputStream os = null;
////
////        try {
////            os = new FileOutputStream(filename);
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////
////        int read = 0;
////        if (null != os) {
////            while (isRecording) {
////                read = recorder.read(data, 0, bufferSize);
////                if (read > 0) {
////                }
////
////                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
////                    try {
////                        os.write(data);
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////
////            try {
////                os.close();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////    }
//
//
//
//
//    // stop the  recording
//    public void stopRecording() {
//        // stops the recording activity
//        if (null != recorder) {
//            isRecording = false;
//
//
//            recorder.stop();
//            recorder.release();
//
//            recorder = null;
//            writingThread = null;
//        }
//    }
//
//
//    /**
//     * This method is used to create the path string where the file will be saved
//     * @return True/False depending on success
//     */
//    public boolean createUpdatePathSave(int counter){
//        try{
//            // create the string
//            // update the pathSave variable with the string
//            pathSave.append(mContext.getFilesDir().toString()).append("/" + TEMP_FILENAME).append("_" + Integer.toString(counter)).append(FILE_FORMAT);
//            pathSaveWav.append(mContext.getFilesDir().toString()).append("/" + TEMP_FILENAME).append("_" + Integer.toString(counter)).append(WAV_FILE_FORMAT);
//            return true;
//
//        }
//        catch(Exception e){
//            Log.d("program", "Problem creating the pathSave String: " + e.toString());
//            return false;
//        }
//    }
//
//
//    /**
//     * Get the path of the file saved
//     * @return pathSave.toString()
//     */
//    public String getPathSave(){
//        return pathSave.toString();
//
//    }
//
//
//
//    // play the recording
//    public void playRecording(){
//        this.printFiles();
//
//        pathSaveWav.append(mContext.getFilesDir().toString()).append("/" + TEMP_FILENAME).append("_" + Integer.toString(0)).append(WAV_FILE_FORMAT);
////        if(pathSave == null){
////
////        }
////        else if(pathSaveWav.equals("")){
////            pathSaveWav.append(mContext.getFilesDir().toString()).append("/" + TEMP_FILENAME).append("_" + Integer.toString(0)).append(WAV_FILE_FORMAT);
////        }
//        // convertiong to Wav format
//        Log.d("program", ".pcm filepath is: " + pathSave.toString() + " , .wav filepath is: " + pathSaveWav.toString());
//        audioConverter = new AudioConverter(mContext, pathSave.toString(), pathSaveWav.toString());
//        try{
//            mediaPlayer = new MediaPlayer();
//            Log.d("program", "File path is: " + pathSaveWav.toString());
//            mediaPlayer.setDataSource(pathSaveWav.toString());
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        }
//        catch (Exception e){
//            Log.d("program", "Error in playing recording: " + e.toString());
//        }
//
//    }
//
//
//
//    // get all the files stored for the application
//    public void printFiles(){
//        String dir = mContext.getFilesDir().toString();
//
//        File files = new File(dir);
//
//        for (String file: files.list()){
//            Log.d("program", "Filennamee is: " + file);
//
//        }
//    }
//
//
//    public String getWavFilepath(){
//        return pathSaveWav.toString();
//
//    }
}
