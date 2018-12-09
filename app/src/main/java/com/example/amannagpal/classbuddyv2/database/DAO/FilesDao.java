package com.example.amannagpal.classbuddyv2.database.DAO;

import com.example.amannagpal.classbuddyv2.database.models.Files;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FilesDao {

    @Query("SELECT *  FROM files")
    List<Files> getAllFiles();

    @Query("SELECT * FROM files WHERE files_id=:file_id LIMIT 1")
    Files findFileByID(final int file_id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertFiles(Files... files);
}
