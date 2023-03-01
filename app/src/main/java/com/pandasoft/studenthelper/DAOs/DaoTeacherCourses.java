package com.pandasoft.studenthelper.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pandasoft.studenthelper.Entities.EntityTeacherCourses;

import java.util.List;

@Dao
public interface DaoTeacherCourses {
    @Insert
    void insert(EntityTeacherCourses teacherCourses);

    @Update
    void update(EntityTeacherCourses teacherCourses);

    @Delete
    void delete(EntityTeacherCourses teacherCourses);

    @Query("SELECT * FROM teacher_courses WHERE update_type!=2")
    LiveData<List<EntityTeacherCourses>> getAllTeacherCourses();

    @Query("SELECT * FROM teacher_courses WHERE id=:id")
    EntityTeacherCourses getCourse(long id);
}
