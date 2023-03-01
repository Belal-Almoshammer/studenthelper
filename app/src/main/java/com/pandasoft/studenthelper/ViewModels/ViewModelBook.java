package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityBook;
import com.pandasoft.studenthelper.Repositories.RepoBook;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;

import java.util.List;

public class ViewModelBook extends AndroidViewModel {
    private final String TABLE_NAME = "book";
    private final RepoBook repoBook;

    public ViewModelBook(Application app) {
        super(app);
        repoBook = new RepoBook(app);
        getDownloads();
    }

    private void getDownloads() {
        Context lContext = getApplication();
        // check internet connection
        if (!MyToolsCls.isNetworkConnected(lContext)) return;
        String user_token = MyToolsCls.getUserToken(lContext);
        long timestamp = MyToolsCls.getLong(lContext, TABLE_NAME);
        // get list of download from internet
        new UploadCls.DownloadEntityTask<EntityBook>(lContext, TABLE_NAME, null, data -> {
            EntityBook entity = data.getValue(EntityBook.class);
            assert entity != null;
            MyToolsCls.putLong(lContext, TABLE_NAME, entity.getUpdate_date());
            if (!entity.getUser_token().equals(user_token) && entity.getUpdate_date() > timestamp) {
                if (entity.getUpdate_type() == 0) {
                    insert(entity, msg -> {
                    });
                } else if (entity.getUpdate_type() == 1) {
                    update(entity, msg -> {
                    });
                } else if (entity.getUpdate_type() == 2) {
                    delete(entity, msg -> {
                    });
                }
            }
        }).execute();
    }

    public void insert(EntityBook book, MyToolsCls.OnCompleteListener listener) {
        book.setUpdate_type(0);// set to be inserted to internet
        repoBook.insert(book);
        listener.onComplete(null);
    }

    // update
    public void update(EntityBook book, MyToolsCls.OnCompleteListener listener) {
        book.setUpdate_type(1);// set to be updated to internet
        repoBook.update(book);
        listener.onComplete(null);
    }

    public void setDeleted(EntityBook book, MyToolsCls.OnCompleteListener listener) {
        book.setUpdate_type(2);// set to be deleted to the internet
        book.setIs_uploaded(false);
        repoBook.update(book);
        listener.onComplete(null);
    }

    // delete
    public void delete(EntityBook book, MyToolsCls.OnCompleteListener listener) {
        repoBook.delete(book);
        listener.onComplete(null);
    }

    public LiveData<List<EntityBook>> getBooks(String id_sub) {
        return repoBook.getBooks(id_sub);
    }

    public LiveData<List<EntityBook>> getUploads() {
        return repoBook.getUploads();
    }

}



