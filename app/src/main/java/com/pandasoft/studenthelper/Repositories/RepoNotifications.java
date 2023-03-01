package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoNotifications;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityNotifications;

import java.util.List;

public class RepoNotifications {
    private final DaoNotifications daoNotifications;
    private final LiveData<List<EntityNotifications>> allNotifications; // Auto refresh data from database

    public RepoNotifications(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getInstance(app);
        daoNotifications = db.daoNotifications();
        allNotifications = daoNotifications.getAllNotifications();
    }

    // Operations
    // insert
    public void insert(EntityNotifications notification) {
        new RepoNotifications.InsertAsyncTask(daoNotifications).execute(notification);
    }

    // update
    public void update(EntityNotifications notification) {
        new RepoNotifications.UpdateAsyncTask(daoNotifications).execute(notification);
    }

    // delete
    public void delete(EntityNotifications notification) {
        new RepoNotifications.DeleteAsyncTask(daoNotifications).execute(notification);
    }

    // getAllNotifications
    public LiveData<List<EntityNotifications>> getAllNotifications() {
        return allNotifications;
    }

    private static class InsertAsyncTask extends AsyncTask<EntityNotifications, Void, Void> {
        public DaoNotifications daoNotifications;

        public InsertAsyncTask(DaoNotifications daoNotifications) {
            this.daoNotifications = daoNotifications;
        }

        @Override
        protected Void doInBackground(EntityNotifications... entityNotifications) {
            this.daoNotifications.insert(entityNotifications[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityNotifications, Void, Void> {
        public DaoNotifications daoNotifications;

        public UpdateAsyncTask(DaoNotifications daoNotifications) {
            this.daoNotifications = daoNotifications;
        }

        @Override
        protected Void doInBackground(EntityNotifications... entityNotifications) {
            if (daoNotifications.getNotification(entityNotifications[0].getId()) != null)
                this.daoNotifications.update(entityNotifications[0]);
            else
                this.daoNotifications.insert(entityNotifications[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityNotifications, Void, Void> {
        public DaoNotifications daoNotifications;

        public DeleteAsyncTask(DaoNotifications daoNotifications) {
            this.daoNotifications = daoNotifications;
        }

        @Override
        protected Void doInBackground(EntityNotifications... entityNotifications) {
            this.daoNotifications.delete(entityNotifications[0]);
            return null;
        }
    }
}
