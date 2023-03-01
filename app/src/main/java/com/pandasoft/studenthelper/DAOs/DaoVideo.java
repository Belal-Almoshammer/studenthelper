package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityVideo;

import java.util.List;

@Dao
public interface DaoVideo {
    @Insert
    void insert(EntityVideo video);

    @Update
    void update(EntityVideo video);

    @Delete
    void delete(EntityVideo video);


    @Query("DELETE FROM video WHERE id=:id")
    void deleteById(long id);

    @Query("SELECT v.* FROM video v WHERE v.id=:id")
    EntityVideo getVideo(long id);

    @Query("SELECT * FROM video WHERE id_sub=:id_sub AND update_type!=2")
    LiveData<List<EntityVideo>> getVideos(String id_sub);

    @Query("SELECT * FROM video WHERE is_uploaded=0")
    LiveData<List<EntityVideo>> getUploads();
}
