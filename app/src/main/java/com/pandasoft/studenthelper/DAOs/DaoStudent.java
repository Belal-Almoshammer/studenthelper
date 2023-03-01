package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityStudent;

import java.util.List;

@Dao
public interface DaoStudent {
    @Insert
    void insert(EntityStudent student);

    @Update
    void update(EntityStudent student);

    @Delete
    void delete(EntityStudent student);

    @Query("SELECT * FROM students")
    LiveData<List<EntityStudent>> getAllStudents();

    @Query("SELECT * FROM students WHERE id=:id")
    EntityStudent getStudent(long id);
}
