package com.example.amannagpal.classbuddyv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amannagpal.classbuddyv2.database.AppDatabase;
import com.example.amannagpal.classbuddyv2.database.models.Files;
import com.example.amannagpal.classbuddyv2.database.models.FilesHasTags;
import com.example.amannagpal.classbuddyv2.database.models.Tags;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.amannagpal.classbuddyv2.MainActivity.TRANSCRIPT_DIR;

public class SaveTranscriptActivity extends AppCompatActivity {


    public static final String TRANSCRIPT_EXT = ".html";

    private AppDatabase db = MainActivity.db;
    private TextInputLayout name, lecture, tag;
    private String transcriptPath = null;
    List<String> tagsTemp = new ArrayList<String>();
    long[] saved_file_id;
    long[] saved_tags_id;
//    private Handler mHandler;
    private Button saveInfo = null;
    public static Runnable runnable, errorRunnable = null;
    private boolean dataRecieved = false;
    private String wavFilePath = null;
    private ImageButton playButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_transcript);

        Intent intent = getIntent();
        if(intent.hasExtra("transcript_path")){
            transcriptPath = intent.getExtras().getString("transcript_path");
        }

        if(intent.hasExtra("wav_file_path")){
            wavFilePath = intent.getExtras().getString("wav_file_path");
        }
        else{
            Log.d("program", "FIlepath not sent");
        }


        try{
            name = findViewById(R.id.file_name);
            lecture = findViewById(R.id.file_lecture);
            tag = findViewById(R.id.file_tags);
            saveInfo = findViewById(R.id.save_info);
            playButton = findViewById(R.id.play_button);

        }
        catch (Exception e){
            Log.d("program", e.toString());
        }


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecording();

            }
        });




        runnable = new Runnable() {
            @Override
            public void run() {
                dataRecieved = true;
                Log.d("program", "In the save transcript handler");


            }
        };


        errorRunnable = new Runnable(){
            @Override
            public void run() {
                Log.d("program", "Some error when getting transcript");
                Toast.makeText(getApplicationContext(), "There was some error analyzing audio please. Please click on save transcript again", Toast.LENGTH_LONG).show();
            }
        };


    }



    private boolean validateFileAndLecture(){
        String fileName = name.getEditText().getText().toString().trim();
        String lectureName = lecture.getEditText().getText().toString().trim();

        if(fileName.isEmpty() && lectureName.isEmpty()){
            lecture.setError("Lecture Name cannot be empty");
            name.setError("File Name cannot be empty");
            return false;
        }
        else if(fileName.isEmpty()){
            name.setError("File Name cannot be empty");
            lecture.setError(null);
            return false;
        }
        else if(lectureName.isEmpty()){
            lecture.setError("Lecture Name cannot be empty");
            name.setError(null);
            return false;
        }
        else{
            name.setError(null);
            lecture.setError(null);
            // removee the space for eerror mesasge
            name.setErrorEnabled(false);
            return true;
        }

    }


    /**
     * Save info to the database about the file
     * @param v
     */
    public void saveInfo(View v){

        if(!validateFileAndLecture()){
           return;
        }


        try{


            new Thread(new Runnable() {
                Handler handler = new Handler(getMainLooper());
                @Override
                public void run() {
                    try{
                        // make sure that the transcript has been converted and written to a file
                        while(!dataRecieved){
                            // wait 3 seconds before making the check again
                            TimeUnit.SECONDS.sleep(3);
                        }

                        String filename = getUpdatedName();

                        // insert to the database using the files DAO method
                        saved_file_id = db.filesDao().insertFiles(new Files(name.getEditText().getText().toString().trim(), lecture.getEditText().getText().toString().trim(), filename));

                        // insert the tags
                        if(tagsTemp.isEmpty()){
                            Log.d("program", "user has not entered any tags");
                        }
                        else{
                            Tags[] tagsToInsert = new Tags[tagsTemp.size()];
                            FilesHasTags[] filesHasTags = new FilesHasTags[tagsTemp.size()];


                            for(int i  = 0; i < tagsTemp.size(); i++){
                                tagsToInsert[i] = new Tags(tagsTemp.get(i));
                            }
                            // insert into the database
                            saved_tags_id = db.tagsDao().insertAll(tagsToInsert);


                            // insert in the conneection table
                            // loop through the tags id and insert eatch with the attached file id

                            for(int i = 0; i < saved_tags_id.length; i++){
                                filesHasTags[i] = new FilesHasTags((int) saved_file_id[0], (int) saved_tags_id[i]);
                            }

                            // save to the database
                            db.filesHasTags().insertAll(filesHasTags);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getApplicationContext(), "Transcript stored in database", Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                    }
                    catch (Exception e){
                        Log.d("program", "Error inserting in the database");
                        // post a runnablle
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Error insertig into the database. Please try again", Toast.LENGTH_LONG).show();

                            }
                        });

                    }

                }
            }).start();



            // send an intent to the main activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("message", "Analysis is taking  place, We will notify you when  the analysis  is done.");
            startActivity(intent);
        }
        catch (Exception e){
            Log.d("program", e.toString());
            Toast.makeText(getApplicationContext(), "Data could not be stored ", Toast.LENGTH_LONG).show();

        }

    }


    /**
     * This method validates that value in tag exists
     * Also checks if the user is trying to save the same tag again
     * @return true/false
     */
    public boolean validateTag(String value){


        if(value.isEmpty()){
            tag.setError("Tag cannot be empty");
            return false;
        }
        else if(tagsTemp != null && tagsTemp.contains(value)){
            tag.setError("You seem to bee trying to add the same tag again");
            return false;
        }

        // check if tag already exists in the database
        List<Tags> tagsInDatabase = db.tagsDao().getAllTags();
        for(Tags tagItter : tagsInDatabase){
            if(value.equals(tagItter.getTagName())){
                tag.setError("Tag already stored in database");
                return false;

            }

        }

        return true;

    }


    public void createTags(View v){
        String value = tag.getEditText().getText().toString().trim();

        if(!validateTag(value)){
            return;
        }

        try{
            tagsTemp.add(value);

            // Make a relational database to add tags to the database
            Toast.makeText(this, "Tag successfully created", Toast.LENGTH_LONG).show();

            // empty the container for user to add a new tag
            tag.getEditText().setText("");
            tag.setError(null);

        }
        catch (Exception e){
            Toast.makeText(this, "Tag cannot be created", Toast.LENGTH_LONG).show();
            Log.d("program", e.toString());

        }

    }


    /**
     * This method updates the name of the transcript from the user input name
     * @return String of the new name
     */
    private String getUpdatedName() throws Exception{

        String file_name = name.getEditText().getText().toString().replaceAll("\\s","_");

        String newFilePath = getApplicationContext().getFilesDir().toString() +
                File.separator + TRANSCRIPT_DIR + File.separator + file_name + TRANSCRIPT_EXT;

        // create instance of two file
        File oldFile = new File(transcriptPath);
        File newFile = new File(newFilePath );


        // check if the new file already exists
        if(newFile.exists()){
            Log.d("program", "File with the selected filename already exists");
            Toast.makeText(this, "File with the filename: " + name + ", already exists. Please select a differnt filename", Toast.LENGTH_LONG).show();
            throw new Exception("Cannot save file, file with filename already exists");
        }

        try{
            Log.d("program", "renaming file");
            oldFile.renameTo(newFile);
            // print to check the files
            Helper.printFiles(getApplicationContext().getFilesDir().toString());


            Log.d("program", "files in the directory are: ");
            Helper.printFiles(getApplicationContext().getFilesDir().toString() + File.separator + TRANSCRIPT_DIR);
            return newFilePath;
        }
        catch (Exception e){
            Log.d("program", "Cannot rename: " + e.toString());
            throw new Exception("Cannot rename file");
        }


    }


    public void playRecording(){

        new Thread(new Runnable() {
            @Override
            public void run() {


                Helper.printFiles(getFilesDir().getPath());

                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(wavFilePath));
                mediaPlayer.start();
            }
        }).start();

    }





}
