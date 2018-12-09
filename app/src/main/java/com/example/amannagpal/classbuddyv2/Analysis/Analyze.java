/**
 * This class is used to perform analysis on the transcript
 * We do this with the help of natural language processing functionality provided by google
 */
package com.example.amannagpal.classbuddyv2.Analysis;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;

import com.example.amannagpal.classbuddyv2.Helper;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.language.v1beta2.CloudNaturalLanguage;
import com.google.api.services.language.v1beta2.CloudNaturalLanguageRequestInitializer;
import com.google.api.services.language.v1beta2.model.AnnotateTextRequest;
import com.google.api.services.language.v1beta2.model.AnnotateTextResponse;
import com.google.api.services.language.v1beta2.model.Document;
import com.google.api.services.language.v1beta2.model.Entity;
import com.google.api.services.language.v1beta2.model.Features;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.amannagpal.classbuddyv2.Constants.NLPKEY;

public class Analyze {

    private String transcriptPath = null;
    private Document transcript_document = null;
    private Features features = null;
    private AnnotateTextRequest request = null;
    private List<Entity> entityList;
    private float sentiment;
    private String transcript = null;
    private CloudNaturalLanguage naturalLanguageService = null;
    private Handler mainHandler;


    /**
     * Default constructor
     * @param transcriptOrFilepath -> The transcro
     * @param isTranscript -> True: Called with the transcript, False: Called with the transcript string
     */
    public Analyze(String transcriptOrFilepath, boolean isTranscript){
        if(isTranscript){
            // instance created with the transcript
            transcript = transcriptOrFilepath;
        }
        else{
            // instance created with a filepath
            transcriptPath = transcriptOrFilepath;
            transcript = getTranscriptFromFilepath();
        }


        try{
            setUpAnalyzer();
            transcript_document = get_transcript_document();
            features = set_up_features();
            request = create_request();
        }
        catch(Exception e){
            Log.d("program", "Problem in analysis object: " + e.toString());
        }
    }


    public void setUpAnalyzer(){

         naturalLanguageService =
                new CloudNaturalLanguage.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),
                        null
                ).setCloudNaturalLanguageRequestInitializer(
                        new CloudNaturalLanguageRequestInitializer(NLPKEY)
                ).build();

    }

    public Document get_transcript_document() throws Exception{

        Document document = new Document();
        document.setType("PLAIN_TEXT");
        document.setLanguage("en-US");
        document.setContent(transcript);

        return document;

    }


    public Features set_up_features() throws Exception{

        Features featuresTemp = new Features();
        featuresTemp.setExtractEntities(true);
        featuresTemp.setExtractDocumentSentiment(true);
        return featuresTemp;

    }


    public AnnotateTextRequest create_request() throws Exception{

        AnnotateTextRequest requestTemp = new AnnotateTextRequest();


        requestTemp.setDocument(transcript_document);
        requestTemp.setFeatures(features);
        return requestTemp;

    }


    /**
     * This method makes the request for the natural language processing api
     * This is a netork functionality so runs in a different thread
     */
    public String makeRequest() throws Exception{
        try{
            AnnotateTextResponse response = naturalLanguageService.documents().annotateText(request).execute();
            entityList = response.getEntities();
            sentiment = response.getDocumentSentiment().getScore();

            ArrayList<String> recordingKeyWords = new ArrayList<String>();

            // loop  through the list create an array list of strings of words
            for(Entity entity:entityList){
                recordingKeyWords.add(entity.getName());

            }

            return createKeywordsString(recordingKeyWords);


        }
        catch(Exception e){
            Log.d("program", "Problem in NLP: " + e.toString());
            throw new Exception("Cannot get the NLP analysis");

        }
    }


    /**
     * This method gets the transcript from the filepath
     * @return the string of the transcript stored in the filepath
      */
    public String getTranscriptFromFilepath(){
        transcript = Helper.readDataFromFile(transcriptPath);
        return transcript;
    }


    /**
     * This method creates  a string of the keywords arrrayList
     */
    public String createKeywordsString(ArrayList<String> keywords){


        StringBuilder keywordsStringBuilder = new StringBuilder();
        keywordsStringBuilder.append("<h3>Important keywords for the recording are: </h3>");
        for(String word: keywords){
            keywordsStringBuilder.append(word.toUpperCase()).append("<br/>");
        }
        keywordsStringBuilder.append("<h3> <b> Transcript</b> </h3>");

        return keywordsStringBuilder.toString();

    }
}
