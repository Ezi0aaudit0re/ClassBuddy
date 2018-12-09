package com.example.amannagpal.classbuddyv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.amannagpal.classbuddyv2.database.AppDatabase;
import com.example.amannagpal.classbuddyv2.database.models.Files;
import com.example.amannagpal.classbuddyv2.database.models.Tags;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class TranscriptActivity extends AppCompatActivity {

    private Toolbar mTopToolbar;
    public RecyclerView tagsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TagsAdapter tagsAdapter;
    private AppDatabase db = MainActivity.db;
    private WebView webView = null;
    private TextView nameTextView = null;
    private LinearLayout linearLayout;

    BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcript);

//        linearLayout = findViewById(R.id.bottol_sheet_linear_layout);
//        sheetBehavior = BottomSheetBehavior.from(linearLayout);

        Intent intent = getIntent();

        int fileID  =  intent.getIntExtra("FileID", -1);


        // get the information of a file based on a particular id
        // check if something failed and we got a wrong default vaalue
        if(fileID == -1){
            Log.d("program", "Something went wrong cannot get fileID");
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }
        else{
            Files file = db.filesDao().findFileByID(fileID);

            nameTextView = findViewById(R.id.file_name_text_view);

            nameTextView.append(file.getName());

            webView = findViewById(R.id.transcript_web_view);

            String transcript_loc = file.getTranscript_loc();

            // check if file exists
            if(Helper.checkFileExists(transcript_loc)){
                String data = Helper.readDataFromFile(transcript_loc);
                webView.loadData(data, "text/html", "UTF-8");
            }
            else{
                Log.d("program", "File doesnot exist: " + file.getTranscript_loc());
            }

            // populate the tags field
            Tags[] tags = db.tagsDao().findTagsByFileID(fileID);

            tagsAdapter = new TagsAdapter(tags);
        }





        // fill the recucler view with tags
        tagsRecyclerView = (RecyclerView) findViewById(R.id.tags_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        tagsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true);
        tagsRecyclerView.setLayoutManager(mLayoutManager);

        tagsRecyclerView.setAdapter(tagsAdapter);





    }


}
