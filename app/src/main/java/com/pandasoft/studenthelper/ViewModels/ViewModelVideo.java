package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityVideo;
import com.pandasoft.studenthelper.Repositories.RepoVideo;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;

import java.util.List;

;

public class ViewModelVideo extends AndroidViewModel {
    private static final String TABLE_NAME = "video";
    RepoVideo repoVideo;


    public ViewModelVideo(Application application) {
        super(application);
        repoVideo = new RepoVideo(application);
        getDownloads();
    }

    private void getDownloads() {
        Context lContext = getApplication();
        // check internet connection
        if (!MyToolsCls.isNetworkConnected(lContext)) return;
        String user_token = MyToolsCls.getUserToken(lContext);
        long timestamp = MyToolsCls.getLong(getApplication(), "questions");

        // get list of download from internet
        new UploadCls.DownloadEntityTask<EntityVideo>(lContext, TABLE_NAME, null, data -> {
            EntityVideo entity = data.getValue(EntityVideo.class);

            assert entity != null;
            if (!entity.getUser_token().equals(user_token) && entity.getUpdate_date() > timestamp) {
                MyToolsCls.putLong(lContext, TABLE_NAME, entity.getUpdate_date());
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

    public void insert(EntityVideo entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(0);// set to be inserted
        repoVideo.insert(entity);
        listener.onComplete(null);
    }

    public void update(EntityVideo entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(1);// set to be updated

        repoVideo.update(entity);
        listener.onComplete(null);
    }

    public void setDeleted(EntityVideo entity, MyToolsCls.OnCompleteListener listener) {
        SharedPreferences preferences = getApplication().getSharedPreferences("main_pref", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "00");

        if (entity.getUser_update_id() != null && entity.getUser_update_id().equals(user_id)) {
            entity.setUpdate_type(2);// set to be deleted to the internet
            entity.setIs_uploaded(false);
            repoVideo.update(entity);
        } else repoVideo.delete(entity);
        entity.setUpdate_type(2);// set to be deleted.

        listener.onComplete(null);
    }

    public void delete(EntityVideo entity, MyToolsCls.OnCompleteListener listener) {
        repoVideo.delete(entity);
        listener.onComplete(null);
    }

    public LiveData<List<EntityVideo>> getVideos(String id_sub) {
        return repoVideo.getVideos(id_sub);
    }

    public LiveData<List<EntityVideo>> getUploads() {
        return repoVideo.getUploads();
    }
}
