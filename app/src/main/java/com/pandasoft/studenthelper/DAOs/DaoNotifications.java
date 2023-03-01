package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityNotifications;

import java.util.List;

@Dao
public interface DaoNotifications {
    @Insert
    void insert(EntityNotifications notifications);

    @Update
    void update(EntityNotifications notifications);

    @Delete
    void delete(EntityNotifications notifications);

    @Query("SELECT * FROM notifications WHERE update_type!=2")
    LiveData<List<EntityNotifications>> getAllNotifications();

    @Query("SELECT * FROM notifications WHERE id=:id")
    EntityNotifications getNotification(long id);

}
