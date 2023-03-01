package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityEducationalLevel;

import java.util.List;

@Dao
public interface DaoEducationalLevel {


    @Insert
    void insert(EntityEducationalLevel educationalLevel);

    @Update
    void update(EntityEducationalLevel educationalLevel);

    @Delete
    void delete(EntityEducationalLevel educationalLevel);

    @Query("SELECT * FROM educational_level")
    LiveData<List<EntityEducationalLevel>> getAllEducationalLevel();

    @Query("SELECT * FROM educational_level")
    EntityEducationalLevel[] getLevelNames();

    @Query("SELECT id_level from educational_level where name=:name")
    int getLevelIdByName(String name);

    @Query("SELECT * FROM educational_level WHERE id_level=:id_level")
    EntityEducationalLevel getRow(String id_level);

}