package com.example.amannagpal.classbuddyv2;

import com.example.amannagpal.classbuddyv2.Analysis.Analyze;
import com.example.amannagpal.classbuddyv2.RecordingLogic.OmRecorderV1;
import com.example.amannagpal.classbuddyv2.RecordingLogic.Recorder;
import com.example.amannagpal.classbuddyv2.RecordingLogic.RecorderV2;
import com.example.amannagpal.classbuddyv2.RecordingLogic.WaveRecorder;
import com.example.amannagpal.classbuddyv2.SpeechToText.Transcript;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.amannagpal.classbuddyv2.database.AppDatabase;
import com.haozhang.lib.AnimatedRecordingView;

import java.io.File;

import androidx.room.Room;

// template source - https://android.jlelse.eu/tablayout-and-viewpager-in-your-android-app-738b8840c38a
public class MainActivity extends AppCompatActivity implements RecorderFragment.Listner {

    public static final int PERMISSION_NOT_GRANTED = 0;
    public static final int PERMISSION_GRANTED = 1;
    public static final String TRANSCRIPT_DIR = "transcript_dir";
    private static final String TEMP_FILENAME = "temp_transcript.html";
    private int[] tabs_icons = {
            R.drawable.files,
            R.drawable.recorder
    };
    private StringBuilder pathSave = new StringBuilder();
    private TabLayout tabLayout;
    public static AppDatabase db;
    private int recordingPermissionCode = PERMISSION_NOT_GRANTED; // 0 means no permission has been granted yet
    String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private omrecorder.Recorder recorder = null;
    private Context mContext;
    int recordingClickCount = 0;
    String dir_path = null;
    private String tempWavFilepath = null;
    private Analyze analyze;
//    private Handler keyWordhandler = null;
    private Handler handler = null;
    private String transcriptKeyKeywords =  null;
    private final int notificationId = 1;
    private NotificationManagerCompat notificationManager = null;
    private Chronometer chronometer = null;
    private boolean isChronometerRunning = false;
    private long pausedTime;
    private AnimatedRecordingView mAnimatedRecordingView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();


        Intent intent = getIntent();
        if(intent.hasExtra("message")){
            Toast.makeText(MainActivity.this, intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
        }



        // handler too get dataa from speech to text  api
        handler = new Handler(getMainLooper());

        // setup notification manager
        notificationManager = NotificationManagerCompat.from(getApplicationContext());


        tempWavFilepath =  pathSave.append(getApplicationContext().getFilesDir().toString()).append("/tempV5").append("_" + Integer.toString(0)).append(".wav").toString();
        recorder = new OmRecorderV1(pathSave.toString(), this).getRecorder();
        dir_path = mContext.getApplicationContext().getFilesDir().toString() +
                File.separator + TRANSCRIPT_DIR;

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        TabsFragmentPagerAdapter adapter = new TabsFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabs_icons();


        try{
            // ROOM android code to connect to database
            if(getApplicationContext() == null){
                throw new Exception("Value of getContext is null");
            }
            db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "classbuddy.db")
                    .allowMainThreadQueries()
                    .build();
        }
        catch (Exception e){
            Log.d("program", e.toString());

        }





    }


    public void setTabs_icons(){
        tabLayout.getTabAt(0).setIcon(tabs_icons[1]);
        tabLayout.getTabAt(1).setIcon(tabs_icons[0]);
    }





    /**
     * RECORDING LOGIC
     */


    public void startRecording(View v){




        //  handle permissions
        if(!getRecordingPermission()){
            Toast.makeText(this, "ClassBuddy  would like to access your microphone", Toast.LENGTH_LONG).show();
        }
        else{



            if(recordingClickCount == 1){
                Log.d("program", "Stopping recording");
                // the user wants to stop the recording
                stopRecording();
                recordingClickCount = 0;
                return;
            }


            new Thread(new Runnable() {
                @Override
                public void run() {

                    recorder.startRecording();
                }
            }).start();

            if(chronometer == null){
                chronometer = v.findViewById(R.id.chronometer);
            }

            //  start the chronometer
            startChronometer();

            // start the animation
            mAnimatedRecordingView.start();

            // set the mic volume
            float vol = 50;
            mAnimatedRecordingView.setVolume(vol);


            Toast.makeText(this, "Recording...", Toast.LENGTH_LONG).show();

            recordingClickCount = (recordingClickCount + 1) % 2;
        }




    }


    public void stopRecording(){
        try{
            // TODO add this  to a differennt thread
            recorder.stopRecording();

            // stop  the chronometer
            stopChronometer();

            // stop the animation
            mAnimatedRecordingView.stop();


            Toast.makeText(this, "Recording stopped", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Log.d("program", "Cannot stop recording: " + e.toString());
        }

   }



    public boolean getRecordingPermission(){

        // check if the permission is granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            recordingPermissionCode = PERMISSION_GRANTED;
        }
        else{
            // if not request for permissionn
            ActivityCompat.requestPermissions(this, permissions, recordingPermissionCode);
        }


        return true;

    }





    public void saveTranscript(){


        final NotificationCompat.Builder mBuilder = setUpmBuild();


        // check if recorder instance is set otherwise throw exception
        try{
            if(recorder == null){
                throw new Exception("Some problem recorder is null");
            }
        }
        catch (Exception e){
            // make the recorder null
            stopRecording();
            Log.d("program", e.toString());
            return;
        }

        Runnable runnable = new Runnable() {


            @Override
            public void run() {
                try{

                    Transcript transcriptInstance = new Transcript(pathSave.toString(), mContext);
                    String transcript = transcriptInstance.getSpeechText();
                    Log.d("Program", "Transcript recieved successfully: " + transcript);

                    // save a transcript to a temporary file

                    if(Helper.createDirectoryIfNotExists(dir_path)){

                        // create a temp_file
                        String temp_file_path = dir_path + "/" + TEMP_FILENAME;

                        // Do the analysis on the file
                        analyze = new Analyze(transcript, true);
                        transcriptKeyKeywords = analyze.makeRequest();

                        // append the important  key words to the transcript

                        transcript = transcriptKeyKeywords + " " +  transcript;




                        // write transcript to the temp_file
                        Helper.writeToFile(transcript, temp_file_path);


                        // print the transcript
                        Helper.readFromFile(temp_file_path);

                        handler.post(SaveTranscriptActivity.runnable);

                        // notify about  completion
                        notificationManager.notify(notificationId,  mBuilder.build());

                    }
                }
                catch(Exception e){
                    Log.d("program", "Error in background thread: " +  e.toString());
                    handler.post(SaveTranscriptActivity.errorRunnable);
                    notificationManager.notify(notificationId, mBuilder.setContentTitle("Error...").setContentText("Error saving the file to the database: " + e.toString()).build());
                }


            }






        };

        // this will notify  the user and start the thread
        new Thread(runnable).start();




        // send the filepath to saveTranscriptActivity
        // this activity will delete the temp file and create a new file to save the data
        Intent intent  = new Intent(this, SaveTranscriptActivity.class);
        intent.putExtra("transcript_path", dir_path + "/" + TEMP_FILENAME);
        intent.putExtra("wav_file_path", tempWavFilepath);
        startActivity(intent);
    }


    /**
     * THis method calls the start conversion with a notification bar that displays  how much task is completed
     */

    public NotificationCompat.Builder setUpmBuild(){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "channel1");
        mBuilder.setContentTitle("Analyzing...")
                .setContentText("Audio analysis Completed")
                .setSmallIcon(R.drawable.done)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        return mBuilder;
    }


    public void startChronometer(){

        if(chronometer == null){
            Log.d("program", "Chronometer is null");
            return;
        }

        if(!isChronometerRunning){
            // value is set to false  then only start chronomeeter
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isChronometerRunning = true;


        }

    }

    public void stopChronometer(){
        if(chronometer ==  null){
            Log.d("program", "Chronometer is null");
            return;
        }

        if(isChronometerRunning){


            // chronometer is running
            chronometer.stop();
            isChronometerRunning = false;
        }

    }

    @Override
    public void setChronometer(Chronometer chrono){
        chronometer = chrono;

    }


    public void setRecorderAnimationView(AnimatedRecordingView view){
        mAnimatedRecordingView = view;

        
    }




}
