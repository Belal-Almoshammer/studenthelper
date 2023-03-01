package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityTipsAndAdvice;

import java.util.List;

@Dao
public interface DaoTipsAndAdvice {
    @Insert
    void insert(EntityTipsAndAdvice tipsAndAdvice);

    @Update
    void update(EntityTipsAndAdvice tipsAndAdvice);

    @Delete
    void delete(EntityTipsAndAdvice tipsAndAdvice);

    @Query("SELECT * FROM tips_and_advice WHERE update_type!=2")
    LiveData<List<EntityTipsAndAdvice>> getAllTipsAndAdvice();

    @Query("SELECT * FROM tips_and_advice WHERE id=:id")
    EntityTipsAndAdvice getTip(long id);

    @Query("SELECT * FROM tips_and_advice WHERE is_uploaded=0")
    LiveData<List<EntityTipsAndAdvice>> getUploads();
}
