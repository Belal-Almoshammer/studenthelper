package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoTeacherCourses;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityTeacherCourses;

import java.util.List;

public class RepoTeacherCourses {
    LiveData<List<EntityTeacherCourses>> allTeacherCourses;
    DaoTeacherCourses daoTeacherCourses;

    public RepoTeacherCourses(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoTeacherCourses = myRoomDatabase.daoTeacherCourses();
        this.allTeacherCourses = this.daoTeacherCourses.getAllTeacherCourses();
    }

    public void insert(EntityTeacherCourses teacher) {
        new RepoTeacherCourses.InsertAsyncTask(this.daoTeacherCourses).execute(teacher);

    }

    public void update(EntityTeacherCourses teacher) {
        new RepoTeacherCourses.UpdateAsyncTask(this.daoTeacherCourses).execute(teacher);
    }

    public void delete(EntityTeacherCourses teacher) {
        new RepoTeacherCourses.DeleteAsyncTask(this.daoTeacherCourses).execute(teacher);
    }

    public LiveData<List<EntityTeacherCourses>> getAllTeacherCourses() {
        return this.allTeacherCourses;
    }

    private static class InsertAsyncTask extends AsyncTask<EntityTeacherCourses, Void, Void> {

        com.pandasoft.studenthelper.DAOs.DaoTeacherCourses DaoTeacherCourses;

        public InsertAsyncTask(DaoTeacherCourses dao) {
            this.DaoTeacherCourses = dao;
        }

        @Override
        protected Void doInBackground(EntityTeacherCourses... entityTeacherCourses) {
            this.DaoTeacherCourses.insert(entityTeacherCourses[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityTeacherCourses, Void, Void> {

        DaoTeacherCourses DaoTeacherCourses;

        public UpdateAsyncTask(DaoTeacherCourses dao) {
            this.DaoTeacherCourses = dao;
        }

        @Override
        protected Void doInBackground(EntityTeacherCourses... entityTeacherCourses) {
            if (DaoTeacherCourses.getCourse(entityTeacherCourses[0].getId()) != null)
                this.DaoTeacherCourses.update(entityTeacherCourses[0]);
            else
                this.DaoTeacherCourses.update(entityTeacherCourses[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityTeacherCourses, Void, Void> {

        DaoTeacherCourses DaoTeacherCourses;

        public DeleteAsyncTask(DaoTeacherCourses dao) {
            this.DaoTeacherCourses = dao;
        }

        @Override
        protected Void doInBackground(EntityTeacherCourses... entityTeacherCourses) {
            this.DaoTeacherCourses.delete(entityTeacherCourses[0]);
            return null;
        }
    }
}
