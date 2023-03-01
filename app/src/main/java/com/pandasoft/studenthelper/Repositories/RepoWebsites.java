package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoWebsites;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityWebsites;

import java.util.List;

public class RepoWebsites {
    LiveData<List<EntityWebsites>> allWebsites;
    DaoWebsites daoWebsites;

    public RepoWebsites(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoWebsites = myRoomDatabase.daoWebsites();
        this.allWebsites = this.daoWebsites.getWebsites();
    }

    public void insert(EntityWebsites website) {
        new RepoWebsites.InsertAsyncTask(this.daoWebsites).execute(website);

    }

    public void update(EntityWebsites website) {
        new RepoWebsites.UpdateAsyncTask(this.daoWebsites).execute(website);
    }

    public void delete(EntityWebsites website) {
        new RepoWebsites.DeleteAsyncTask(this.daoWebsites).execute(website);
    }

    public LiveData<List<EntityWebsites>> getAllWebsites() {
        return this.allWebsites;
    }

    public LiveData<List<EntityWebsites>> getUploads() {
        return daoWebsites.getUploads();
    }

    private static class InsertAsyncTask extends AsyncTask<EntityWebsites, Void, Void> {

        com.pandasoft.studenthelper.DAOs.DaoWebsites DaoWebsites;

        public InsertAsyncTask(DaoWebsites dao) {
            this.DaoWebsites = dao;
        }

        @Override
        protected Void doInBackground(EntityWebsites... entityWebsites) {
            this.DaoWebsites.insert(entityWebsites[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityWebsites, Void, Void> {

        DaoWebsites DaoWebsites;

        public UpdateAsyncTask(DaoWebsites dao) {
            this.DaoWebsites = dao;
        }

        @Override
        protected Void doInBackground(EntityWebsites... entityWebsites) {
            if (this.DaoWebsites.getWebsite(entityWebsites[0].getId()) != null)
                this.DaoWebsites.update(entityWebsites[0]);
            else this.DaoWebsites.insert(entityWebsites[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityWebsites, Void, Void> {

        DaoWebsites DaoWebsites;

        public DeleteAsyncTask(DaoWebsites dao) {
            this.DaoWebsites = dao;
        }

        @Override
        protected Void doInBackground(EntityWebsites... entityWebsites) {
            this.DaoWebsites.delete(entityWebsites[0]);
            return null;
        }
    }
}
