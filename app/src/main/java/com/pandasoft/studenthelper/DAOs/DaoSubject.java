package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntitySubject;

import java.util.List;

@Dao
public interface DaoSubject {
    @Insert
    void insert(EntitySubject subjects);

    @Update
    void update(EntitySubject subjects);

    @Delete
    void delete(EntitySubject subjects);

    @Query("SELECT * FROM subjects")
    LiveData<List<EntitySubject>> getAllSubjects();


    @Query("SELECT * FROM subjects")
    EntitySubject[] getList();


    @Query("SELECT * FROM subjects")
    EntitySubject getSubject();
}
