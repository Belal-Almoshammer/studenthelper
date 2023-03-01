package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityInstitutes;

import java.util.List;

@Dao
public interface DaoInstitutes {
    @Insert
    void insert(EntityInstitutes institutes);

    @Update
    void update(EntityInstitutes institutes);

    @Delete
    void delete(EntityInstitutes institutes);

    @Query("SELECT * FROM institutes WHERE update_type!=2")
    LiveData<List<EntityInstitutes>> getAllInstitutes();

    @Query("SELECT * FROM institutes WHERE id=:id")
    EntityInstitutes getInstitute(long id);

    @Query("SELECT * FROM institutes WHERE is_uploaded=0 AND update_type=2")
    LiveData<List<EntityInstitutes>> getUploads();
}
