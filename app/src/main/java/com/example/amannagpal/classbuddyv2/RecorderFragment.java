package com.example.amannagpal.classbuddyv2;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.services.language.v1beta2.model.AnnotateTextRequest;
import com.haozhang.lib.AnimatedRecordingView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecorderFragment extends Fragment {

    private FloatingTextButton button;
    private Listner listner;
    private Chronometer chronometer;
    AnimatedRecordingView mRecordingView = null;

    interface Listner{
        abstract void setChronometer(Chronometer chronometer);
        abstract void saveTranscript();
        abstract void setRecorderAnimationView(AnimatedRecordingView view);
    }




    public RecorderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recorder, container, false);

        try{
            button = view.findViewById(R.id.save_recording);
            chronometer = view.findViewById(R.id.chronometer);
            mRecordingView = view.findViewById(R.id.recording_animation);
            listner = (Listner)view.getContext();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.saveTranscript();

                }
            });

        }
        catch (Exception e){
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(listner == null){
            Log.d("program", "Listner is null");
        }
        listner.setChronometer(chronometer);
        listner.setRecorderAnimationView(mRecordingView);
    }
}
