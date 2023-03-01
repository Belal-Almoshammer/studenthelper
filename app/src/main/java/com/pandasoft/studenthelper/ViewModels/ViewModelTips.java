package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityTipsAndAdvice;
import com.pandasoft.studenthelper.Repositories.RepoTipsAndAdvice;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;

import java.util.List;

public class ViewModelTips extends AndroidViewModel {
    private static final String TABLE_NAME = "tips_and_advice";

    RepoTipsAndAdvice repoTipsAndAdvice;
    LiveData<List<EntityTipsAndAdvice>> tipsList;

    public ViewModelTips(Application application) {
        super(application);
        repoTipsAndAdvice = new RepoTipsAndAdvice(application);
        tipsList = repoTipsAndAdvice.getAllTipsAndAdvice();
        getDownloads();
    }

    private void getDownloads() {
        Context lContext = getApplication();
        // check internet connection
        if (!MyToolsCls.isNetworkConnected(lContext)) return;
        String user_token = MyToolsCls.getUserToken(lContext);
        long timestamp = MyToolsCls.getLong(getApplication(), "questions");

        // get list of download from internet
        new UploadCls.DownloadEntityTask<EntityTipsAndAdvice>(lContext, TABLE_NAME, null, data -> {
            //__________
            EntityTipsAndAdvice entity = data.getValue(EntityTipsAndAdvice.class);
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

    public LiveData<List<EntityTipsAndAdvice>> getAllData() {
        return this.tipsList;
    }

    public void insert(EntityTipsAndAdvice entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(0);
        repoTipsAndAdvice.insert(entity);
        listener.onComplete(null);
    }

    public void update(EntityTipsAndAdvice entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(1);

        repoTipsAndAdvice.update(entity);
        listener.onComplete(null);
    }

    public void setDeleted(EntityTipsAndAdvice entity, MyToolsCls.OnCompleteListener listener) {
        SharedPreferences preferences = getApplication().getSharedPreferences("main_pref", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "00");

        if (entity.getUser_update_id() != null && entity.getUser_update_id().equals(user_id)) {
            entity.setUpdate_type(2);// set to be deleted to the internet
            entity.setIs_uploaded(false);
            repoTipsAndAdvice.update(entity);
        } else repoTipsAndAdvice.delete(entity);
        entity.setUpdate_type(2);// set to be deleted.

        listener.onComplete(null);
    }

    public void delete(EntityTipsAndAdvice entity, MyToolsCls.OnCompleteListener listener) {
        repoTipsAndAdvice.delete(entity);
        listener.onComplete(null);
    }

    public LiveData<List<EntityTipsAndAdvice>> getUploads() {
        return repoTipsAndAdvice.getUploads();
    }
}
