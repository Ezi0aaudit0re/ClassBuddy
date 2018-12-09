package com.example.amannagpal.classbuddyv2.database.DAO;

import com.example.amannagpal.classbuddyv2.database.models.FilesHasTags;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FilesHasTagsDao {

    @Query("SELECT * FROM files_has_tags")
    List<FilesHasTags> getAllFileHasTags();

    @Insert
    long[] insertAll(FilesHasTags... filesHasTags);
}
