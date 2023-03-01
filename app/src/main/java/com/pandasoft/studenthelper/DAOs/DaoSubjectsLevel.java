package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntitySubjectsLevel;

import java.util.List;

@Dao
public interface DaoSubjectsLevel {
    @Insert
    void insert(EntitySubjectsLevel subjectsLevel);

    @Update
    void update(EntitySubjectsLevel subjectsLevel);

    @Delete
    void delete(EntitySubjectsLevel subjectsLevel);

    @Query("SELECT sl.id,l.id_level,l.name as level_name,s.id as id_sub,s.name as subject_name,s.img_name,s.color_code FROM subjects_level sl join subjects s on sl.id_sub = s.id join educational_level l on sl.id_level = l.id_level WHERE sl.id_level=:id_level")
    LiveData<List<EntitySubjectsLevel>> getAllSubjectsLevel(String id_level);


    @Query("SELECT sl.id,l.id_level,l.name as level_name,s.id as id_sub,s.name as subject_name,s.img_name,s.color_code FROM subjects_level sl join subjects s on sl.id_sub = s.id join educational_level l on sl.id_level = l.id_level")
    EntitySubjectsLevel getSubjectLevel();//String level_id
}
