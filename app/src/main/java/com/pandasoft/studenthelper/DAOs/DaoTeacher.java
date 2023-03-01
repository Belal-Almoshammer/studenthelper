package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityTeacher;

import java.util.List;

@Dao
public interface DaoTeacher {
    @Insert
    void insert(EntityTeacher teacher);

    @Update
    void update(EntityTeacher teacher);

    @Delete
    void delete(EntityTeacher teacher);

    @Query("SELECT * FROM teacher")
    LiveData<List<EntityTeacher>> getAllTeachers();

    @Query("SELECT * FROM teacher WHERE id=:id")
    EntityTeacher getTeacher(long id);
}
