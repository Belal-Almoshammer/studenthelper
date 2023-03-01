package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntitySubjectChapters;

import java.util.List;

@Dao
public interface DaoSubjectChapters {
    @Insert
    void insert(EntitySubjectChapters subjectChapters);

    @Update
    void update(EntitySubjectChapters subjectChapters);

    @Delete
    void delete(EntitySubjectChapters subjectChapters);

    @Query("SELECT c.*FROM subject_chapters c  where c.id_sub=:id_subject")
    LiveData<List<EntitySubjectChapters>> getAllSubjectChapters(String id_subject);


    @Query("SELECT * FROM subject_chapters")
    EntitySubjectChapters[] getList();

    @Query("SELECT * FROM subject_chapters WHERE id_sub=:id_subject")
    EntitySubjectChapters[] getList(String id_subject);
}
