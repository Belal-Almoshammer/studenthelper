package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityWebsites;

import java.util.List;

@Dao
public interface DaoWebsites {
    @Insert
    void insert(EntityWebsites websites);

    @Update
    void update(EntityWebsites websites);

    @Delete
    void delete(EntityWebsites websites);

    @Query("SELECT * FROM website WHERE update_type != 2")
    LiveData<List<EntityWebsites>> getWebsites();

    @Query("SELECT * FROM website WHERE id=:id")
    EntityWebsites getWebsite(long id);

    @Query("SELECT * FROM website WHERE is_uploaded=0")
    LiveData<List<EntityWebsites>> getUploads();
}
