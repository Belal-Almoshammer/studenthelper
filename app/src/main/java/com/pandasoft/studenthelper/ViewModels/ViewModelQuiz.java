package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityQuiz;
import com.pandasoft.studenthelper.Repositories.RepoQuiz;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;

import java.util.List;

;

public class ViewModelQuiz extends AndroidViewModel {
    private static final String TABLE_NAME = "quiz";
    RepoQuiz repoQuiz;

    public ViewModelQuiz(Application application) {
        super(application);
        repoQuiz = new RepoQuiz(application);
        getDownloads();
    }

    private void getDownloads() {
        Context lContext = getApplication();
        // check internet connection
        if (!MyToolsCls.isNetworkConnected(lContext)) return;
        String user_token = MyToolsCls.getUserToken(lContext);
        long timestamp = MyToolsCls.getLong(getApplication(), "questions");

        // get list of download from internet

        //1#.list of quiz
        new UploadCls.DownloadEntityTask<EntityQuiz>(lContext, TABLE_NAME, null, data -> {
            //__________
            EntityQuiz entity = data.getValue(EntityQuiz.class);

            // long stamp = MyToolsCls.getLong(lContext, TABLE_NAME);
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

    public void insert(EntityQuiz entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(0);
        repoQuiz.insert(entity);
        listener.onComplete(null);
    }

    public void update(EntityQuiz entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(1);
        repoQuiz.update(entity);
        listener.onComplete(null);
    }

    public void setDeleted(EntityQuiz entity, MyToolsCls.OnCompleteListener listener) {
        SharedPreferences preferences = getApplication().getSharedPreferences("main_pref", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "00");

        if (entity.getUser_update_id() != null && entity.getUser_update_id().equals(user_id)) {
            entity.setUpdate_type(2);// set to be deleted to the internet
            entity.setIs_uploaded(false);
            repoQuiz.update(entity);
        } else repoQuiz.delete(entity);
        entity.setUpdate_type(2);// set to be deleted.


        listener.onComplete(null);
    }

    public void delete(EntityQuiz entity, MyToolsCls.OnCompleteListener listener) {
        repoQuiz.delete(entity);
        listener.onComplete(null);

    }

    public LiveData<List<EntityQuiz>> getList(String id_sub) {
        return repoQuiz.getList(id_sub);
    }

    public LiveData<EntityQuiz> getQuiz(int quiz_id) {
        return repoQuiz.getQuiz(quiz_id);
    }

    public LiveData<Integer> getMaxId() {
        return repoQuiz.getMaxId();
    }

    public LiveData<List<EntityQuiz>> getUploads() {
        return repoQuiz.getUploads();
    }


}
