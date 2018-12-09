package com.example.amannagpal.classbuddyv2.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * This is the model class for tags
 */
@Entity(tableName = "tags")
public class Tags {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tags_id")
    private int tagsID;
    @ColumnInfo(name = "tag_name")
    private String tagName;
//    private String created_at;
//    private String updated_at;


    public Tags(String tagName){
        this.tagsID = tagsID;
        this.tagName = tagName;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
    }




    /**
     * Getters and Setters
     */

    /**
     * GETTERS
     */
    public int getTagsID(){
        return this.tagsID;
    }

    public String getTagName(){
        return this.tagName;
    }


    public void setTagsID(int tagsID) {
        this.tagsID = tagsID;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
