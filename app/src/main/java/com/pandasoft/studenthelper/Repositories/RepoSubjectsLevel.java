package com.pandasoft.studenthelper.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoSubjectsLevel;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntitySubjectsLevel;

import java.util.List;

public class RepoSubjectsLevel {
    DaoSubjectsLevel daoSubjectsLevel;
    LiveData<List<EntitySubjectsLevel>> allSubjects;

    public RepoSubjectsLevel(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoSubjectsLevel = myRoomDatabase.daoSubjectsLevel();
    }


    public LiveData<List<EntitySubjectsLevel>> getAllSubjectsLevel(String id_level) {
        return this.daoSubjectsLevel.getAllSubjectsLevel(id_level);

    }
}
