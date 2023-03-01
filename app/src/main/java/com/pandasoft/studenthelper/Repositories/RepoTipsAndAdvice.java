package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoTipsAndAdvice;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityTipsAndAdvice;

import java.util.List;

public class RepoTipsAndAdvice {
    LiveData<List<EntityTipsAndAdvice>> allTipsAndAdvices;
    DaoTipsAndAdvice daoTipsAndAdvice;

    public RepoTipsAndAdvice(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoTipsAndAdvice = myRoomDatabase.daoTipsAndAdvice();
        this.allTipsAndAdvices = this.daoTipsAndAdvice.getAllTipsAndAdvice();
    }

    public void insert(EntityTipsAndAdvice tipsAndAdvice) {
        new RepoTipsAndAdvice.InsertAsyncTask(this.daoTipsAndAdvice).execute(tipsAndAdvice);

    }

    public void update(EntityTipsAndAdvice tipsAndAdvice) {
        new RepoTipsAndAdvice.UpdateAsyncTask(this.daoTipsAndAdvice).execute(tipsAndAdvice);
    }

    public void delete(EntityTipsAndAdvice tipsAndAdvice) {
        new RepoTipsAndAdvice.DeleteAsyncTask(this.daoTipsAndAdvice).execute(tipsAndAdvice);
    }

    public LiveData<List<EntityTipsAndAdvice>> getAllTipsAndAdvice() {
        return this.allTipsAndAdvices;
    }

    public LiveData<List<EntityTipsAndAdvice>> getUploads() {
        return daoTipsAndAdvice.getUploads();
    }

    private static class InsertAsyncTask extends AsyncTask<EntityTipsAndAdvice, Void, Void> {

        com.pandasoft.studenthelper.DAOs.DaoTipsAndAdvice DaoTipsAndAdvice;

        public InsertAsyncTask(DaoTipsAndAdvice dao) {
            this.DaoTipsAndAdvice = dao;
        }

        @Override
        protected Void doInBackground(EntityTipsAndAdvice... entityTipsAndAdvices) {
            this.DaoTipsAndAdvice.insert(entityTipsAndAdvices[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityTipsAndAdvice, Void, Void> {

        DaoTipsAndAdvice daoTipsAndAdvice;

        public UpdateAsyncTask(DaoTipsAndAdvice dao) {
            this.daoTipsAndAdvice = dao;
        }

        @Override
        protected Void doInBackground(EntityTipsAndAdvice... entityTipsAndAdvices) {
            if (daoTipsAndAdvice.getTip(entityTipsAndAdvices[0].getId()) != null)
                this.daoTipsAndAdvice.update(entityTipsAndAdvices[0]);
            else
                this.daoTipsAndAdvice.insert(entityTipsAndAdvices[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityTipsAndAdvice, Void, Void> {

        DaoTipsAndAdvice DaoTipsAndAdvice;

        public DeleteAsyncTask(DaoTipsAndAdvice dao) {
            this.DaoTipsAndAdvice = dao;
        }

        @Override
        protected Void doInBackground(EntityTipsAndAdvice... entityTipsAndAdvices) {
            this.DaoTipsAndAdvice.delete(entityTipsAndAdvices[0]);
            return null;
        }
    }
}
