package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityWebsites;
import com.pandasoft.studenthelper.Repositories.RepoWebsites;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;

import java.util.List;

public class ViewModelWebsite extends AndroidViewModel {
    private final String TABLE_NAME = "website";
    private final LiveData<List<EntityWebsites>> mList;
    private final RepoWebsites repoWebsite;

    public ViewModelWebsite(Application application) {
        super(application);
        repoWebsite = new RepoWebsites(application);
        mList = repoWebsite.getAllWebsites();
        getDownloads();
    }

    private void getDownloads() {
        Context lContext = getApplication();
        // check internet connection
        if (!MyToolsCls.isNetworkConnected(lContext)) return;
        String user_token = MyToolsCls.getUserToken(lContext);
        long timestamp = MyToolsCls.getLong(getApplication(), "questions");
        // get list of download from internet
        new UploadCls.DownloadEntityTask<EntityWebsites>(lContext, TABLE_NAME, null, data -> {
            EntityWebsites entity = data.getValue(EntityWebsites.class);
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


    public void insert(EntityWebsites entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(0);
        repoWebsite.insert(entity);
        listener.onComplete(null);
    }

    public void update(EntityWebsites entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(1);

        repoWebsite.update(entity);
        listener.onComplete(null);
    }

    public void setDeleted(EntityWebsites entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(2);
        entity.setIs_uploaded(false);

        repoWebsite.update(entity);
        listener.onComplete(null);

    }

    public void delete(EntityWebsites entity, MyToolsCls.OnCompleteListener listener) {
        repoWebsite.delete(entity);
        listener.onComplete(null);
    }

    public LiveData<List<EntityWebsites>> getAllData() {
        return mList;
    }

    public LiveData<List<EntityWebsites>> getUploads() {
        return repoWebsite.getUploads();
    }
}
