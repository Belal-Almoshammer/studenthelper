package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityQuestion;

import java.util.List;

@Dao
public interface DaoQuestion {
    @Insert
    void insert(EntityQuestion question);

    @Update
    void update(EntityQuestion question);

    @Delete
    void delete(EntityQuestion question);

    @Query("SELECT * from questions WHERE quiz_id=:quiz_id AND update_type!=2")
    LiveData<List<EntityQuestion>> getAllQuestions(long quiz_id);

    @Query("SELECT * FROM questions WHERE id=:id")
    EntityQuestion getRow(long id);

    @Query("DELETE FROM questions WHERE quiz_id=:id")
    void deleteByQuizId(long id);

    @Query("SELECT * FROM questions WHERE is_uploaded=0")
    LiveData<List<EntityQuestion>> getUploads();

    @Query("DELETE FROM questions WHERE quiz_id=:quiz_id")
    void setDeleted(long quiz_id);
}
