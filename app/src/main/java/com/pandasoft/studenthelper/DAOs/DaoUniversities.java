package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityUniversities;

import java.util.List;

@Dao
public interface DaoUniversities {
    @Insert
    void insert(EntityUniversities universities);

    @Update
    void update(EntityUniversities universities);

    @Delete
    void delete(EntityUniversities universities);

    @Query("SELECT * FROM universities WHERE update_type!=2")
    LiveData<List<EntityUniversities>> getAllUniversities();

    @Query("SELECT * FROM universities WHERE id=:id")
    EntityUniversities getUniversity(long id);

    @Query("SELECT * FROM universities WHERE is_uploaded=0 AND update_type=2")
    LiveData<List<EntityUniversities>> getUploads();
}
