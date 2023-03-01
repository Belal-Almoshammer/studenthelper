package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityQuiz;

import java.util.List;

@Dao
public interface DaoQuiz {
    @Insert
    void insert(EntityQuiz quiz);

    @Update
    void update(EntityQuiz quiz);

    @Delete
    void delete(EntityQuiz quiz);

    @Query("SELECT * FROM quiz WHERE id=:id")
    EntityQuiz getRow(long id);

    @Query("SELECT q.*,s.questions_count from quiz q left join (SELECT quiz_id,count(id) questions_count from questions group by quiz_id) s on q.id=s.quiz_id where q.id_sub=:id_sub AND q.update_type!=2")
    LiveData<List<EntityQuiz>> getList(String id_sub);

    @Query("SELECT ifnull(max(id),1) FROM quiz")
    LiveData<Integer> getMaxId();

    @Query("SELECT * FROM quiz WHERE id=:quiz_id")
    LiveData<EntityQuiz> getQuiz(int quiz_id);

    @Query("DELETE FROM questions WHERE quiz_id=:quiz_id")
    void deleteQuestion(long quiz_id);

    @Query("SELECT * FROM quiz WHERE is_uploaded=0")
    LiveData<List<EntityQuiz>> getUploads();
}
