package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoUniversities;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityUniversities;

import java.util.List;

public class RepoUniversities {
    LiveData<List<EntityUniversities>> allUniversities;
    DaoUniversities daoUniversities;

    public RepoUniversities(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoUniversities = myRoomDatabase.daoUniversities();
        this.allUniversities = this.daoUniversities.getAllUniversities();
    }

    public void insert(EntityUniversities university) {
        new RepoUniversities.InsertAsyncTask(this.daoUniversities).execute(university);

    }

    public void update(EntityUniversities university) {
        new RepoUniversities.UpdateAsyncTask(this.daoUniversities).execute(university);
    }

    public void delete(EntityUniversities university) {
        new RepoUniversities.DeleteAsyncTask(this.daoUniversities).execute(university);
    }

    public LiveData<List<EntityUniversities>> getAllUniversities() {
        return this.allUniversities;
    }

    public LiveData<List<EntityUniversities>> getUploads() {
        return daoUniversities.getUploads();
    }

    private static class InsertAsyncTask extends AsyncTask<EntityUniversities, Void, Void> {

        com.pandasoft.studenthelper.DAOs.DaoUniversities DaoUniversities;

        public InsertAsyncTask(DaoUniversities dao) {
            this.DaoUniversities = dao;
        }

        @Override
        protected Void doInBackground(EntityUniversities... entityUniversities) {
            this.DaoUniversities.insert(entityUniversities[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityUniversities, Void, Void> {

        DaoUniversities DaoUniversities;

        public UpdateAsyncTask(DaoUniversities dao) {
            this.DaoUniversities = dao;
        }

        @Override
        protected Void doInBackground(EntityUniversities... entityUniversities) {
            if (this.DaoUniversities.getUniversity(entityUniversities[0].getId()) != null)
                this.DaoUniversities.update(entityUniversities[0]);
            else
                this.DaoUniversities.insert(entityUniversities[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityUniversities, Void, Void> {

        DaoUniversities DaoUniversities;

        public DeleteAsyncTask(DaoUniversities dao) {
            this.DaoUniversities = dao;
        }

        @Override
        protected Void doInBackground(EntityUniversities... entityUniversities) {
            this.DaoUniversities.delete(entityUniversities[0]);
            return null;
        }
    }
}
