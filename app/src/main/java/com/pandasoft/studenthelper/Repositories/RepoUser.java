package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoUser;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityUser;

import java.util.List;

public class RepoUser {
    LiveData<List<EntityUser>> allUsers;
    DaoUser daoUser;

    public RepoUser(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoUser = myRoomDatabase.daoUser();
        this.allUsers = this.daoUser.getAllUsers();
    }

    public void insert(EntityUser user) {
        new RepoUser.InsertAsyncTask(this.daoUser).execute(user);

    }

    public void update(EntityUser user) {
        new RepoUser.UpdateAsyncTask(this.daoUser).execute(user);
    }

    public void delete(EntityUser user) {
        new RepoUser.DeleteAsyncTask(this.daoUser).execute(user);
    }

    public LiveData<List<EntityUser>> getAllUsers() {
        return this.allUsers;
    }

    public LiveData<List<EntityUser>> getTeachers() {
        return daoUser.getTeachers();
    }

    private static class InsertAsyncTask extends AsyncTask<EntityUser, Void, Void> {

        com.pandasoft.studenthelper.DAOs.DaoUser DaoUser;

        public InsertAsyncTask(DaoUser dao) {
            this.DaoUser = dao;
        }

        @Override
        protected Void doInBackground(EntityUser... entityUser) {
            this.DaoUser.insert(entityUser[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityUser, Void, Void> {

        DaoUser DaoUser;

        public UpdateAsyncTask(DaoUser dao) {
            this.DaoUser = dao;
        }

        @Override
        protected Void doInBackground(EntityUser... entityUser) {
            this.DaoUser.update(entityUser[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityUser, Void, Void> {

        DaoUser DaoUser;

        public DeleteAsyncTask(DaoUser dao) {
            this.DaoUser = dao;
        }

        @Override
        protected Void doInBackground(EntityUser... entityUser) {
            this.DaoUser.delete(entityUser[0]);
            return null;
        }
    }
}
