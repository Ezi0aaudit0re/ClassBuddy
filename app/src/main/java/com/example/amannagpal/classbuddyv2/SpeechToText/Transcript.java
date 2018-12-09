/**
 * This class connects with google Cloud API
 * We also call the analyze class in this to get the analysis of the transcirpt
 * We then append the analysis on the top of the transcript
 */
package com.example.amannagpal.classbuddyv2.SpeechToText;

import android.content.Context;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.util.Base64;
import android.util.Log;

import com.example.amannagpal.classbuddyv2.Constants;
import com.example.amannagpal.classbuddyv2.R;
import com.example.amannagpal.classbuddyv2.RecordingLogic.Recorder;
import com.example.amannagpal.classbuddyv2.RecordingLogic.RecorderV2;
import com.example.amannagpal.classbuddyv2.RecordingLogic.WaveRecorder;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.speech.v1beta1.Speech;
import com.google.api.services.speech.v1beta1.SpeechRequestInitializer;
import com.google.api.services.speech.v1beta1.model.RecognitionAudio;
import com.google.api.services.speech.v1beta1.model.RecognitionConfig;
import com.google.api.services.speech.v1beta1.model.SpeechRecognitionResult;
import com.google.api.services.speech.v1beta1.model.SyncRecognizeRequest;
import com.google.api.services.speech.v1beta1.model.SyncRecognizeResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Transcript{

    private final String CLOUD_API_KEY = Constants.CLOUD_API_KEY;
    private String filepath;
    private String filepathWav;
    private Context mContext;
    byte[] audioFileBytes = null;
    Speech speechSerivce = null;
    String base64EncodedData = null;
    RecognitionConfig recognitionConfig = null;
    RecognitionAudio recognitionAudio = null;
    private Path path = null;

    public Transcript(String filepathWav, Context context){
        this.mContext = context;
        filepath = filepathWav;
        // check if filepath exists
        if(this.checkFileExists()){
            convertToBase4();
        }
        else{
           // throw an exception
        }

        setUpApi();

    }

    /**
     * This meethod seets up the API
     */
    public void setUpApi(){

        try{
            // check if we have a string
            if(base64EncodedData == null){
                throw new Exception("Audio file not converted to base64 becuase the audio filepath is null");
            }
            else{

                // setup speech builder
                speechSerivce = new Speech.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),
                        null
                ).setSpeechRequestInitializer(
                        new SpeechRequestInitializer(CLOUD_API_KEY))
                        .build();




                // set up configurations such as language
                recognitionConfig = new RecognitionConfig();
                recognitionConfig.setLanguageCode("en-US");


                // set the audio source in base64 format
                recognitionAudio = new RecognitionAudio();
                recognitionAudio.setContent(base64EncodedData);
//                recognitionAudio.setContent(R.raw.test_audio);
            }


        }
        catch(Exception e){
            Log.d("program", e.toString());
            return;
        }

    }


    /**
     * This method converts audio to base4
     */
    public void convertToBase4(){
        try{

            final Uri soundUri = Uri.fromFile(new File(filepath));
            InputStream stream = mContext.getContentResolver().openInputStream(soundUri);
            byte[] audioData = IOUtils.toByteArray(stream);
            stream.close();

            audioFileBytes = audioData;


            base64EncodedData = Base64.encodeToString(audioData, Base64.NO_WRAP);
            Log.d("program", "Base64 string is: " + base64EncodedData);
        }
        catch (Exception e){
            Log.d("program", "Problem converting to base64: " + e.toString());
            return;
        }
    }


    /**
     * This method checks if file exists
     */
    public boolean checkFileExists(){
        File f = new File(filepath);

        if(f.exists())
            return true;
        else
            return false;
    }





    /**
     * This method gets the conversion from speech to text
     */
    public String getSpeechText(){

        String transcript = null;
        try{


            // Create Request
            SyncRecognizeRequest request = new SyncRecognizeRequest();
            request.setConfig(recognitionConfig);
            request.setAudio(recognitionAudio);


            // Generate response
            SyncRecognizeResponse response = speechSerivce.speech().syncrecognize(request).execute();


            // Extract Transcript
            SpeechRecognitionResult result = response.getResults().get(0);
            transcript = result.getAlternatives().get(0).getTranscript();

            return transcript;


        }
        catch(Exception e){
            Log.d("program", e.toString());
        }
        return transcript;


    }


    public String getBase64EncodedData(){
        return base64EncodedData;
    }









}
