package com.example.amannagpal.classbuddyv2.database.DAO;

import com.example.amannagpal.classbuddyv2.database.models.Tags;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TagsDao {

    @Query("SELECT * FROM tags")
    List<Tags> getAllTags();


    /**
     * This method gets all the tags that are assocaited with a file and does the necessary joins
     * @param fileID the id of the file to get associated tags with
     */
    @Query("SELECT * FROM tags INNER JOIN files_has_tags ON " +
            "tags.tags_id=files_has_tags.tags_id WHERE " +
            "files_id=:fileID")
    Tags[] findTagsByFileID(final int fileID);

    @Insert
    void insert(Tags tag);

    /**
     *  Insert multiple tags
     * @param tags a LIST of tags
     */
    @Insert
    long[] insertAll(Tags... tags);

}
