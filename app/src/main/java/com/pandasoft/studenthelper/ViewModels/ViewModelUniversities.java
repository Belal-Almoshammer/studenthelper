package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityUniversities;
import com.pandasoft.studenthelper.Repositories.RepoUniversities;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;

import java.util.List;

public class ViewModelUniversities extends AndroidViewModel {

    private static final String TABLE_NAME = "universities";
    private final LiveData<List<EntityUniversities>> mList;
    RepoUniversities repoUniversities;


    public ViewModelUniversities(Application application) {
        super(application);
        repoUniversities = new RepoUniversities(application);
        mList = repoUniversities.getAllUniversities();
        getDownloads();
    }

    private void getDownloads() {
        Context lContext = getApplication();
        // check internet connection
        if (!MyToolsCls.isNetworkConnected(getApplication())) return;
        String user_token = MyToolsCls.getUserToken(lContext);
        long timestamp = MyToolsCls.getLong(getApplication(), TABLE_NAME);

        // get list of download from internet
        new UploadCls.DownloadEntityTask<EntityUniversities>(lContext, TABLE_NAME, null, data -> {
            EntityUniversities entity = data.getValue(EntityUniversities.class);
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

    public void insert(EntityUniversities entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(0);// set to be inserted
        repoUniversities.insert(entity);
        listener.onComplete(null);
    }

    public void update(EntityUniversities entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(1);// set to be updated

        repoUniversities.update(entity);
        listener.onComplete(null);

    }

    public void setDeleted(EntityUniversities entity, MyToolsCls.OnCompleteListener listener) {

        SharedPreferences preferences = getApplication().getSharedPreferences("main_pref", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "00");

        if (entity.getUser_update_id() != null && entity.getUser_update_id().equals(user_id)) {
            entity.setUpdate_type(2);// set to be deleted to the internet
            entity.setIs_uploaded(false);
            repoUniversities.update(entity);
        } else repoUniversities.delete(entity);
        entity.setUpdate_type(2);// set to be deleted.

        listener.onComplete(null);
    }

    public void delete(EntityUniversities entity, MyToolsCls.OnCompleteListener listener) {
        repoUniversities.delete(entity);
        listener.onComplete(null);
    }

    public LiveData<List<EntityUniversities>> getAllData() {
        return this.mList;
    }


    public LiveData<List<EntityUniversities>> getUploads() {
        return repoUniversities.getUploads();
    }
}
