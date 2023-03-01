package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoQuiz;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityQuiz;

import java.util.List;

public class RepoQuiz {
    private final DaoQuiz daoQuiz;

    public RepoQuiz(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getInstance(app);
        daoQuiz = db.daoQuiz();
    }

    // Operations
    // insert
    public void insert(EntityQuiz quiz) {
        new InsertAsyncTask(daoQuiz).execute(quiz);
    }

    // update
    public void update(EntityQuiz quiz) {
        new UpdateAsyncTask(daoQuiz).execute(quiz);
    }

    // delete
    public void delete(EntityQuiz quiz) {
        new DeleteAsyncTask(daoQuiz).execute(quiz);
    }

    public LiveData<List<EntityQuiz>> getList(String is_sub) {
        return daoQuiz.getList(is_sub);
    }

    public LiveData<Integer> getMaxId() {
        return daoQuiz.getMaxId();
    }

    public LiveData<EntityQuiz> getQuiz(int quiz_id) {
        return daoQuiz.getQuiz(quiz_id);
    }

    public LiveData<List<EntityQuiz>> getUploads() {
        return daoQuiz.getUploads();
    }

    private static class InsertAsyncTask extends AsyncTask<EntityQuiz, Void, Void> {
        public DaoQuiz daoQuiz;

        public InsertAsyncTask(DaoQuiz daoQuiz) {
            this.daoQuiz = daoQuiz;
        }

        @Override
        protected Void doInBackground(EntityQuiz... entities) {
            this.daoQuiz.insert(entities[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityQuiz, Void, Void> {
        public DaoQuiz daoQuiz;

        public UpdateAsyncTask(DaoQuiz daoQuestion) {
            this.daoQuiz = daoQuestion;
        }

        @Override
        protected Void doInBackground(EntityQuiz... entities) {
            if (this.daoQuiz.getRow(entities[0].getId()) != null)
                this.daoQuiz.update(entities[0]);
            else
                this.daoQuiz.insert(entities[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityQuiz, Void, Void> {
        public DaoQuiz daoQuiz;

        public DeleteAsyncTask(DaoQuiz daoQuiz) {
            this.daoQuiz = daoQuiz;
        }

        @Override
        protected Void doInBackground(EntityQuiz... entities) {
            this.daoQuiz.delete(entities[0]);
            this.daoQuiz.deleteQuestion(entities[0].getId());
            return null;
        }
    }
}
