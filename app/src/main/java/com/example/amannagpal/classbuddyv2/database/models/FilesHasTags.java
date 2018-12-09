package com.example.amannagpal.classbuddyv2.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * This class represents the table that acts as the bridge between filees and tags table
 */
@Entity(tableName = "files_has_tags",
        foreignKeys = {@ForeignKey(entity = Files.class, parentColumns = "files_id", childColumns = "files_id"),
                       @ForeignKey(entity = Tags.class, parentColumns = "tags_id", childColumns = "tags_id")},
        indices = {@Index("files_id"), @Index("tags_id")}
        )
public class FilesHasTags {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "files_id")
    private int filesID;
    @ColumnInfo(name = "tags_id")
    private int tagsID;

    public void setId(int id) {
        this.id = id;
    }

    public void setFilesID(int filesID) {
        this.filesID = filesID;
    }

    public void setTagsID(int tagsID) {
        this.tagsID = tagsID;
    }

    public FilesHasTags(final int filesID, final int tagsID){
        this.id = id;
        this.filesID = filesID;
        this.tagsID = tagsID;
    }




    /**
     * Getters andd setters
     */

    /**
     * GETTERS
     */
    public int getId() {
        return id;
    }

    public int getFilesID() {
        return filesID;
    }

    public int getTagsID() {
        return tagsID;
    }
}
