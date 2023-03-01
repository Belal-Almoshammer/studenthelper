package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoTeacher;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityTeacher;

import java.util.List;

public class RepoTeacher {
    LiveData<List<EntityTeacher>> allTeachers;
    DaoTeacher daoTeacher;

    public RepoTeacher(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoTeacher = myRoomDatabase.daoTeacher();
        this.allTeachers = this.daoTeacher.getAllTeachers();
    }

    public void insert(EntityTeacher teacher) {
        new RepoTeacher.InsertAsyncTask(this.daoTeacher).execute(teacher);

    }

    public void update(EntityTeacher teacher) {
        new RepoTeacher.UpdateAsyncTask(this.daoTeacher).execute(teacher);
    }

    public void delete(EntityTeacher teacher) {
        new RepoTeacher.DeleteAsyncTask(this.daoTeacher).execute(teacher);
    }

    public LiveData<List<EntityTeacher>> getAllTeachers() {
        return this.allTeachers;
    }

    private static class InsertAsyncTask extends AsyncTask<EntityTeacher, Void, Void> {

        DaoTeacher daoTeacher;

        public InsertAsyncTask(DaoTeacher dao) {
            this.daoTeacher = dao;
        }

        @Override
        protected Void doInBackground(EntityTeacher... entityTeachers) {
            this.daoTeacher.insert(entityTeachers[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityTeacher, Void, Void> {

        DaoTeacher daoTeacher;

        public UpdateAsyncTask(DaoTeacher dao) {
            this.daoTeacher = dao;
        }

        @Override
        protected Void doInBackground(EntityTeacher... entityTeachers) {
            if (daoTeacher.getTeacher(entityTeachers[0].getId()) != null)
                this.daoTeacher.update(entityTeachers[0]);
            else
                this.daoTeacher.insert(entityTeachers[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityTeacher, Void, Void> {

        DaoTeacher DaoTeacher;

        public DeleteAsyncTask(DaoTeacher dao) {
            this.DaoTeacher = dao;
        }

        @Override
        protected Void doInBackground(EntityTeacher... entityTeachers) {
            this.DaoTeacher.delete(entityTeachers[0]);
            return null;
        }
    }
}

