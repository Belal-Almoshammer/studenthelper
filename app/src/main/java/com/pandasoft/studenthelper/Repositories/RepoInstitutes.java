package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoInstitutes;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityInstitutes;

import java.util.List;

public class RepoInstitutes {
    private final DaoInstitutes daoInstitutes;
    private final LiveData<List<EntityInstitutes>> allInstitutes; // Auto refresh data from database

    public RepoInstitutes(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getInstance(app);
        daoInstitutes = db.daoInstitutes();
        allInstitutes = daoInstitutes.getAllInstitutes();
    }

    // Operations
    // insert
    public void insert(EntityInstitutes institute) {
        new RepoInstitutes.InsertAsyncTask(daoInstitutes).execute(institute);
    }

    // update
    public void update(EntityInstitutes institute) {
        new RepoInstitutes.UpdateAsyncTask(daoInstitutes).execute(institute);
    }

    // delete
    public void delete(EntityInstitutes institute) {
        new RepoInstitutes.DeleteAsyncTask(daoInstitutes).execute(institute);
    }

    // getAllInstitutes
    public LiveData<List<EntityInstitutes>> getAllInstitutes() {
        return allInstitutes;
    }

    public LiveData<List<EntityInstitutes>> getUploads() {
        return daoInstitutes.getUploads();
    }

    private static class InsertAsyncTask extends AsyncTask<EntityInstitutes, Void, Void> {
        public DaoInstitutes daoInstitutes;

        public InsertAsyncTask(DaoInstitutes daoInstitutes) {
            this.daoInstitutes = daoInstitutes;
        }

        @Override
        protected Void doInBackground(EntityInstitutes... entityInstitutes) {
            if (entityInstitutes != null && entityInstitutes.length > 0)
                this.daoInstitutes.insert(entityInstitutes[0]);
            return null;
        }
    }


    private static class UpdateAsyncTask extends AsyncTask<EntityInstitutes, Void, Void> {
        public DaoInstitutes daoInstitutes;

        public UpdateAsyncTask(DaoInstitutes daoInstitutes) {
            this.daoInstitutes = daoInstitutes;
        }

        @Override
        protected Void doInBackground(EntityInstitutes... entityInstitutes) {
            if (daoInstitutes.getInstitute(entityInstitutes[0].getId()) != null)
                this.daoInstitutes.update(entityInstitutes[0]);
            else
                this.daoInstitutes.insert(entityInstitutes[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityInstitutes, Void, Void> {
        public DaoInstitutes daoInstitutes;

        public DeleteAsyncTask(DaoInstitutes dao) {
            this.daoInstitutes = dao;
        }

        @Override
        protected Void doInBackground(EntityInstitutes... entityInstitutes) {
            if (entityInstitutes != null && entityInstitutes.length > 0)
                this.daoInstitutes.delete(entityInstitutes[0]);
            return null;
        }
    }

}
