package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoSubjectChapters;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntitySubjectChapters;

import java.util.List;

public class RepoSubjectChapters {
    DaoSubjectChapters daoSubjectChapters;
    LiveData<List<EntitySubjectChapters>> allSubjectChapters;

    public RepoSubjectChapters(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoSubjectChapters = myRoomDatabase.daoSubjectChapters();
    }

    public void insert(EntitySubjectChapters subjectChapters) {
        new InsertAsyncTask(this.daoSubjectChapters).execute(subjectChapters);

    }

    public void update(EntitySubjectChapters subjectChapters) {
        new UpdateAsyncTask(this.daoSubjectChapters).execute(subjectChapters);
    }

    public void delete(EntitySubjectChapters subjectChapters) {
        new DeleteAsyncTask(this.daoSubjectChapters).execute(subjectChapters);
    }

    public LiveData<List<EntitySubjectChapters>> getAllSubjectChapters(String id_subject) {
        this.allSubjectChapters = this.daoSubjectChapters.getAllSubjectChapters(id_subject);
        return this.allSubjectChapters;
    }


    public FetchListAsyncTask fetchListAsyncTask() {
        return new FetchListAsyncTask(daoSubjectChapters);
    }

    public interface OnFetchList {
        void onFetch(EntitySubjectChapters[] list);
    }

    private static class InsertAsyncTask extends AsyncTask<EntitySubjectChapters, Void, Void> {

        DaoSubjectChapters daoSubjectChapters;

        public InsertAsyncTask(DaoSubjectChapters dao) {
            this.daoSubjectChapters = dao;
        }

        @Override
        protected Void doInBackground(EntitySubjectChapters... entitySubjectChapters) {
            this.daoSubjectChapters.insert(entitySubjectChapters[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntitySubjectChapters, Void, Void> {

        DaoSubjectChapters daoSubjectChapters;

        public UpdateAsyncTask(DaoSubjectChapters dao) {
            this.daoSubjectChapters = dao;
        }

        @Override
        protected Void doInBackground(EntitySubjectChapters... entitySubjectChapters) {
            this.daoSubjectChapters.update(entitySubjectChapters[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntitySubjectChapters, Void, Void> {

        DaoSubjectChapters daoSubjectChapters;

        public DeleteAsyncTask(DaoSubjectChapters dao) {
            this.daoSubjectChapters = dao;
        }

        @Override
        protected Void doInBackground(EntitySubjectChapters... entitySubjectChapters) {
            this.daoSubjectChapters.delete(entitySubjectChapters[0]);
            return null;
        }
    }

    public class FetchListAsyncTask extends AsyncTask<String, Void, EntitySubjectChapters[]> {
        DaoSubjectChapters daoSubjectChapters;
        OnFetchList mListener;

        public FetchListAsyncTask(DaoSubjectChapters chapters) {
            daoSubjectChapters = chapters;
        }

        public FetchListAsyncTask setOnFetchList(OnFetchList listener) {
            mListener = listener;
            return this;
        }

        @Override
        protected EntitySubjectChapters[] doInBackground(String... strings) {
            if (strings.length > 0)
                return daoSubjectChapters.getList(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(EntitySubjectChapters[] entitySubjectChapters) {
            if (mListener != null)
                mListener.onFetch(entitySubjectChapters);
        }
    }
}
