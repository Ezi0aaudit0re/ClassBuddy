package com.example.amannagpal.classbuddyv2.RecordingLogic;

import android.content.Context;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import androidx.annotation.NonNull;
import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;
import omrecorder.WriteAction;

public class OmRecorderV1 {

    private Recorder recorder = null;
    private String filepath = null;
    private Context mContext = null;





    public OmRecorderV1(String file, Context context){
        filepath = file;
        mContext = context;
        setUpRecorder();

    }



    public void setUpRecorder(){
        recorder = OmRecorder.wav(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override public void onAudioChunkPulled(AudioChunk audioChunk) {
                        animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                    }
                }), file());

        // FOR SKIP SILENCE
//        recorder = OmRecorder.wav(
//                new PullTransport.Noise(mic(),
//                        new PullTransport.OnAudioChunkPulledListener() {
//                            @Override public void onAudioChunkPulled(AudioChunk audioChunk) {
//                                animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
//                            }
//                        },
//                        new WriteAction.Default(),
//                        new Recorder.OnSilenceListener() {
//                            @Override public void onSilence(long silenceTime) {
//                                Log.d("program", "Sillence deetected for: " +  String.valueOf(silenceTime));
//                            }
//                        }, 200
//                ), file()
//        );
    }


    private void animateVoice(final float maxPeak) {
        return;
    }

    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );
    }

    @NonNull
    private File file() {
        try{
            File file = new File(filepath);
            file.createNewFile();
            return new File(filepath);
        }
        catch (Exception e){
            Log.d("program", "Cannot create file  " +  filepath);
            return new File(filepath);
        }
    }


    public Recorder getRecorder(){
        return recorder;
    }



}
