package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoEducationalLevel;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityEducationalLevel;

import java.util.List;

public class RepoEductionLevel {
    private final DaoEducationalLevel daoEducationalLevel;
    private final LiveData<List<EntityEducationalLevel>> allEductionLevels; // LiveData To Auto refresh data from database


    public RepoEductionLevel(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getInstance(app);
        daoEducationalLevel = db.daoEducationalLevel();
        allEductionLevels = daoEducationalLevel.getAllEducationalLevel();
    }

    // Operations
    // insert
    public void insert(EntityEducationalLevel entity) {
        new RepoEductionLevel.InsertAsyncTask(daoEducationalLevel).execute(entity);
    }

    // update
    public void update(EntityEducationalLevel entity) {
        new RepoEductionLevel.UpdateAsyncTask(daoEducationalLevel).execute(entity);
    }

    // delete
    public void delete(EntityEducationalLevel entity) {
        new RepoEductionLevel.DeleteAsyncTask(daoEducationalLevel).execute(entity);
    }

    // getAllEductionLevels
    public LiveData<List<EntityEducationalLevel>> getAllEductionLevels() {
        return allEductionLevels;
    }

    public GetLevelsAsyncTask listAsyncTask() {
        return new GetLevelsAsyncTask(daoEducationalLevel);
    }


    public int getLevelNameById(String name) {
        return daoEducationalLevel.getLevelIdByName(name);
    }


    private static class InsertAsyncTask extends AsyncTask<EntityEducationalLevel, Void, Void> {
        public DaoEducationalLevel daoEducationalLevel;

        public InsertAsyncTask(DaoEducationalLevel ed) {
            this.daoEducationalLevel = ed;
        }

        @Override
        protected Void doInBackground(EntityEducationalLevel... educationalLevels) {
            this.daoEducationalLevel.insert(educationalLevels[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityEducationalLevel, Void, Void> {
        public DaoEducationalLevel daoEducationalLevel;

        public UpdateAsyncTask(DaoEducationalLevel daoEd) {
            this.daoEducationalLevel = daoEd;
        }

        @Override
        protected Void doInBackground(EntityEducationalLevel... educationalLevels) {
            if (daoEducationalLevel.getRow(educationalLevels[0].getId_level()) != null)
                this.daoEducationalLevel.update(educationalLevels[0]);
            else
                this.daoEducationalLevel.insert(educationalLevels[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityEducationalLevel, Void, Void> {
        public DaoEducationalLevel daoEducationalLevel;

        public DeleteAsyncTask(DaoEducationalLevel daoEducationalLevel) {
            this.daoEducationalLevel = daoEducationalLevel;
        }

        @Override
        protected Void doInBackground(EntityEducationalLevel... educationalLevels) {
            this.daoEducationalLevel.delete(educationalLevels[0]);
            return null;
        }
    }

    public static class GetLevelsAsyncTask extends AsyncTask<Void, Void, EntityEducationalLevel[]> {
        private final DaoEducationalLevel daoEducationalLevel;
        private OnLoadListData mListener;

        public GetLevelsAsyncTask(DaoEducationalLevel dao) {
            daoEducationalLevel = dao;
        }

        public GetLevelsAsyncTask setOnLoadList(OnLoadListData listener) {
            mListener = listener;
            return this;
        }

        @Override
        protected EntityEducationalLevel[] doInBackground(Void... voids) {
            return daoEducationalLevel.getLevelNames();
        }

        @Override
        protected void onPostExecute(EntityEducationalLevel[] list) {
            if (mListener != null) {
                mListener.onLoadList(list);
            }
        }

        public interface OnLoadListData {
            void onLoadList(EntityEducationalLevel[] list);
        }
    }

}
