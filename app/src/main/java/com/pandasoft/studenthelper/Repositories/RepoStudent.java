package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoStudent;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityStudent;

import java.util.List;

public class RepoStudent {
    DaoStudent daoStudent;
    LiveData<List<EntityStudent>> allStudents;

    public RepoStudent(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);
        this.daoStudent = myRoomDatabase.daoStudent();
        this.allStudents = this.daoStudent.getAllStudents();
    }

    public void insert(EntityStudent student) {
        new InsertAsyncTask(this.daoStudent).execute(student);
    }

    public void update(EntityStudent student) {
        new UpdateAsyncTask(this.daoStudent).execute(student);
    }

    public void delete(EntityStudent student) {
        new DeleteAsyncTask(daoStudent).execute(student);
    }

    public LiveData<List<EntityStudent>> getAllStudents() {
        return this.allStudents;
    }

    private static class InsertAsyncTask extends AsyncTask<EntityStudent, Void, Void> {
        DaoStudent daoStudent;

        public InsertAsyncTask(DaoStudent dao) {
            this.daoStudent = dao;
        }

        @Override
        protected Void doInBackground(EntityStudent... entityStudents) {
            this.daoStudent.insert(entityStudents[0]);
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<EntityStudent, Void, Void> {

        DaoStudent daoStudent;

        public UpdateAsyncTask(DaoStudent dao) {
            this.daoStudent = dao;
        }

        @Override
        protected Void doInBackground(EntityStudent... entityStudents) {
            if (daoStudent.getStudent(entityStudents[0].getId()) != null)
                daoStudent.update(entityStudents[0]);
            else
                daoStudent.insert(entityStudents[0]);
            return null;
        }
    }

    public static class DeleteAsyncTask extends AsyncTask<EntityStudent, Void, Void> {
        DaoStudent daoStudent;

        public DeleteAsyncTask(DaoStudent dao) {
            this.daoStudent = dao;
        }

        @Override
        public Void doInBackground(EntityStudent... entityStudents) {
            this.daoStudent.delete(entityStudents[0]);
            return null;
        }
    }
}
