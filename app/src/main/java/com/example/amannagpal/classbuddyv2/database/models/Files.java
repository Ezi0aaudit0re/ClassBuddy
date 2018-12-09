package com.example.amannagpal.classbuddyv2.database.models;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "files")
public class Files {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "files_id")
    private int fileID;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "lecture")
    private String lecture;
    @ColumnInfo(name = "transcript_loc")
    private String transcript_loc;
//    @ColumnInfo(name = "created_at")
//    public String created_at;
//    @ColumnInfo(name = "updated_at")
//    public String updated_at;


    public Files(String name, String lecture, String transcript_loc){
        this.name = name;
        this.lecture = lecture;
        this.transcript_loc = transcript_loc;
//        this.created_at = created_at;
//        this.updated_at = updated_at;

    }



    /**
     *  Getters and setters
     */


    /**
     * GETTERS
     */
    public int getFileID() {
        return fileID;
    }

    public String getName() {
        return name;
    }

    public String getLecture() {
        return lecture;
    }


    /**
     * SETTERS
     *
     */

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public void setTranscript_loc(String transcript_loc) {
        this.transcript_loc = transcript_loc;
    }

    public String getTranscript_loc() {

        return transcript_loc;
    }


    public String toString(){
        return("ID: " + this.getFileID() + " Name: " + this.getName());

    }
}
