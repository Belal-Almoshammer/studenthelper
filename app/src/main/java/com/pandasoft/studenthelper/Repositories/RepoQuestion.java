package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoQuestion;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityQuestion;

import java.util.List;

public class RepoQuestion {
    private final DaoQuestion daoQuestion;

    public RepoQuestion(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getInstance(app);
        daoQuestion = db.daoQuestion();
    }

    // Operations
    // insert
    public void insert(EntityQuestion question) {
        new RepoQuestion.InsertAsyncTask(daoQuestion).execute(question);
    }

    // update
    public void update(EntityQuestion question) {
        new RepoQuestion.UpdateAsyncTask(daoQuestion).execute(question);
    }

    // delete
    public void delete(EntityQuestion question) {
        new RepoQuestion.DeleteAsyncTask(daoQuestion).execute(question);
    }

    // getAllQuestions
    public LiveData<List<EntityQuestion>> getAllQuestions(long quiz_id) {
        return daoQuestion.getAllQuestions(quiz_id);
    }

    public void deleteByQuizId(long id) {
        new DeleteByQuizId(daoQuestion).execute(id);
    }

    public LiveData<List<EntityQuestion>> getUploads() {
        return daoQuestion.getUploads();
    }

    public void setDeleted(long quiz_id) {
        new DeleteByQuizAsyncTask(daoQuestion, quiz_id).execute();
    }


    private static class DeleteByQuizAsyncTask extends AsyncTask<Void, Void, Void> {

        long quiz_id;
        DaoQuestion daoQuestion;

        public DeleteByQuizAsyncTask(DaoQuestion dao, long quiz_id) {
            this.quiz_id = quiz_id;
            daoQuestion = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            daoQuestion.deleteByQuizId(quiz_id);
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<EntityQuestion, Void, Void> {
        public DaoQuestion daoQuestion;

        public InsertAsyncTask(DaoQuestion daoQuestion) {
            this.daoQuestion = daoQuestion;
        }

        @Override
        protected Void doInBackground(EntityQuestion... entityQuestions) {
            this.daoQuestion.insert(entityQuestions[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityQuestion, Void, Void> {
        public DaoQuestion daoQuestion;

        public UpdateAsyncTask(DaoQuestion daoQuestion) {
            this.daoQuestion = daoQuestion;
        }

        @Override
        protected Void doInBackground(EntityQuestion... entityQuestions) {
            this.daoQuestion.update(entityQuestions[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityQuestion, Void, Void> {
        public DaoQuestion daoQuestion;

        public DeleteAsyncTask(DaoQuestion daoQuestion) {
            this.daoQuestion = daoQuestion;
        }

        @Override
        protected Void doInBackground(EntityQuestion... entityQuestions) {
            this.daoQuestion.delete(entityQuestions[0]);
            return null;
        }
    }

    private class DeleteByQuizId extends AsyncTask<Long, Void, Void> {
        DaoQuestion daoQuestion;

        public DeleteByQuizId(DaoQuestion dao) {
            this.daoQuestion = dao;
        }

        @Override
        protected Void doInBackground(Long... numbers) {
            daoQuestion.deleteByQuizId(numbers[0]);
            return null;
        }
    }
}
