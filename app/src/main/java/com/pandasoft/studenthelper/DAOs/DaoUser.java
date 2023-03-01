package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityUser;

import java.util.List;

@Dao
public interface DaoUser {
    @Insert
    void insert(EntityUser user);

    @Update
    void update(EntityUser user);

    @Delete
    void delete(EntityUser user);

    @Query("SELECT * FROM users")
    LiveData<List<EntityUser>> getAllUsers();

    @Query("SELECT * FROM users WHERE user_type=1")
    LiveData<List<EntityUser>> getTeachers();

    @Query("SELECT * FROM users WHERE user_type=2")
    LiveData<List<EntityUser>> getStudents();
}
