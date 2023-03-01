package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityBook;

import java.util.List;

@Dao
public interface DaoBook {

    @Insert
    void insert(EntityBook book);

    @Update
    void update(EntityBook book);

    @Delete
    void delete(EntityBook book);

    @Query("SELECT * FROM book WHERE id=:book_id")
    EntityBook getBook(long book_id);

    @Query("SELECT * FROM BOOK WHERE id_sub=:id_sub and update_type!=2")
    LiveData<List<EntityBook>> getBooks(String id_sub);

    @Query("SELECT * FROM book WHERE is_uploaded=0 AND update_type=2")
    LiveData<List<EntityBook>> getUploads();
}
