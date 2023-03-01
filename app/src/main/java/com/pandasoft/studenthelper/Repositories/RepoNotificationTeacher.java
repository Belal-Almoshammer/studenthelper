package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoNotificationsTeacher;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityNotificationsTeacher;

import java.util.List;

public class RepoNotificationTeacher {
    private final LiveData<List<EntityNotificationsTeacher>> allNotificationTeacher; // Auto refresh data from database
    private final DaoNotificationsTeacher daoNotificationsTeacher;

    public RepoNotificationTeacher(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getInstance(app);
        daoNotificationsTeacher = db.daoNotificationsTeacher();
        allNotificationTeacher = daoNotificationsTeacher.getAllNotificationsTeacher();
    }

    // Operations
    // insert
    public void insert(EntityNotificationsTeacher notification) {
        new RepoNotificationTeacher.InsertAsyncTask(daoNotificationsTeacher).execute(notification);
    }

    // update
    public void update(EntityNotificationsTeacher notification) {
        new RepoNotificationTeacher.UpdateAsyncTask(daoNotificationsTeacher).execute(notification);
    }

    // delete
    public void delete(EntityNotificationsTeacher notification) {
        new RepoNotificationTeacher.DeleteAsyncTask(daoNotificationsTeacher).execute(notification);
    }

    // getAllNotificationsTeacher
    public LiveData<List<EntityNotificationsTeacher>> getAllNotificationsTeacher() {
        return allNotificationTeacher;
    }

    private static class InsertAsyncTask extends AsyncTask<EntityNotificationsTeacher, Void, Void> {
        public DaoNotificationsTeacher daoNotificationsTeacher;

        public InsertAsyncTask(DaoNotificationsTeacher dao) {
            this.daoNotificationsTeacher = dao;
        }

        @Override
        protected Void doInBackground(EntityNotificationsTeacher... entityNotifications) {
            this.daoNotificationsTeacher.insert(entityNotifications[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityNotificationsTeacher, Void, Void> {
        public DaoNotificationsTeacher daoNotificationsTeacher;

        public UpdateAsyncTask(DaoNotificationsTeacher DaoNotificationsTeacher) {
            this.daoNotificationsTeacher = DaoNotificationsTeacher;
        }

        @Override
        protected Void doInBackground(EntityNotificationsTeacher... entityNotifications) {
            if (daoNotificationsTeacher.getRow(entityNotifications[0].getId()) != null)
                this.daoNotificationsTeacher.update(entityNotifications[0]);
            else
                this.daoNotificationsTeacher.insert(entityNotifications[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityNotificationsTeacher, Void, Void> {
        public DaoNotificationsTeacher DaoNotificationsTeacher;

        public DeleteAsyncTask(DaoNotificationsTeacher daoNotificationsTeacher) {
            this.DaoNotificationsTeacher = daoNotificationsTeacher;
        }

        @Override
        protected Void doInBackground(EntityNotificationsTeacher... entityNotifications) {
            this.DaoNotificationsTeacher.delete(entityNotifications[0]);
            return null;
        }
    }
}
