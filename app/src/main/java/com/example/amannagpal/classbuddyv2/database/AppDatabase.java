package com.example.amannagpal.classbuddyv2.database;

import com.example.amannagpal.classbuddyv2.database.DAO.FilesDao;
import com.example.amannagpal.classbuddyv2.database.DAO.FilesHasTagsDao;
import com.example.amannagpal.classbuddyv2.database.DAO.TagsDao;
import com.example.amannagpal.classbuddyv2.database.models.Files;
import com.example.amannagpal.classbuddyv2.database.models.FilesHasTags;
import com.example.amannagpal.classbuddyv2.database.models.Tags;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Files.class, FilesHasTags.class, Tags.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FilesDao filesDao();
    public abstract TagsDao tagsDao();
    public abstract FilesHasTagsDao filesHasTags();

}
