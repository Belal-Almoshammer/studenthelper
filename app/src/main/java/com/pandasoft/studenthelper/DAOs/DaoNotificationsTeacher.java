package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityNotificationsTeacher;

import java.util.List;

@Dao
public interface DaoNotificationsTeacher {
    @Insert
    void insert(EntityNotificationsTeacher notificationsTeacher);

    @Update
    void update(EntityNotificationsTeacher notificationsTeacher);

    @Delete
    void delete(EntityNotificationsTeacher notificationsTeacher);

    @Query("SELECT * FROM notifications_teacher")
    LiveData<List<EntityNotificationsTeacher>> getAllNotificationsTeacher();

    @Query("SELECT * FROM notifications_teacher WHERE id=:id")
    EntityNotificationsTeacher getRow(long id);

}
