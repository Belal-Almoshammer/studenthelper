package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoBook;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityBook;

import java.util.List;

public class RepoBook {
    private final DaoBook daoBook;

    public RepoBook(Application app) {

        MyRoomDatabase db = MyRoomDatabase.getInstance(app);
        this.daoBook = db.daoBook();
    }


    public LiveData<List<EntityBook>> getUploads() {
        return daoBook.getUploads();
    }

    // Operations
    // insert
    public void insert(EntityBook book) {
        new InsertAsyncTask(daoBook).execute(book);
    }

    // update
    public void update(EntityBook book) {
        new UpdateAsyncTask(daoBook).execute(book);
    }

    // delete
    public void delete(EntityBook book) {
        new DeleteAsyncTask(daoBook).execute(book);
    }

    public EntityBook getBook(int book_id) {
        return daoBook.getBook(book_id);
    }

    public LiveData<List<EntityBook>> getBooks(String id_sub) {
        return daoBook.getBooks(id_sub);
    }

    private static class InsertAsyncTask extends AsyncTask<EntityBook, Void, Void> {
        public DaoBook daoBook;

        public InsertAsyncTask(DaoBook daoBook) {
            this.daoBook = daoBook;
        }

        @Override
        protected Void doInBackground(EntityBook... entityBooks) {
            this.daoBook.insert(entityBooks[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityBook, Void, Void> {
        public DaoBook daoBook;

        public UpdateAsyncTask(DaoBook daoBook) {
            this.daoBook = daoBook;
        }

        @Override
        protected Void doInBackground(EntityBook... entityBooks) {
            if (daoBook.getBook(entityBooks[0].getId()) != null)
                this.daoBook.update(entityBooks[0]);
            else this.daoBook.insert(entityBooks[0]);
            return null;
        }
    }


    private static class DeleteAsyncTask extends AsyncTask<EntityBook, Void, Void> {
        public DaoBook daoBook;

        public DeleteAsyncTask(DaoBook daoBook) {
            this.daoBook = daoBook;
        }

        @Override
        protected Void doInBackground(EntityBook... entityBooks) {
            this.daoBook.delete(entityBooks[0]);
            return null;
        }
    }
}
