package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityInstitutes;
import com.pandasoft.studenthelper.Repositories.RepoInstitutes;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;

import java.util.List;

public class ViewModelInstitutes extends AndroidViewModel {
    private static final String TABLE_NAME = "institutes";
    RepoInstitutes repoInstitutes;
    LiveData<List<EntityInstitutes>> institutesList;


    public ViewModelInstitutes(Application application) {
        super(application);
        repoInstitutes = new RepoInstitutes(application);
        institutesList = repoInstitutes.getAllInstitutes();
        getDownloads();

    }

    private void getDownloads() {
        Context lContext = getApplication();
        // check internet connection
        if (!MyToolsCls.isNetworkConnected(lContext)) return;
        String user_token = MyToolsCls.getUserToken(lContext);
        long timestamp = MyToolsCls.getLong(lContext, TABLE_NAME);

        // get list of download from internet
        new UploadCls.DownloadEntityTask<EntityInstitutes>(lContext, TABLE_NAME, null, data -> {
            EntityInstitutes entity = data.getValue(EntityInstitutes.class);
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

    public LiveData<List<EntityInstitutes>> getAllData() {
        return this.institutesList;
    }


    public void insert(EntityInstitutes institutes, MyToolsCls.OnCompleteListener listener) {
        institutes.setUpdate_type(0);
        repoInstitutes.insert(institutes);
        listener.onComplete(null);
    }

    public void update(EntityInstitutes institutes, MyToolsCls.OnCompleteListener listener) {
        institutes.setUpdate_type(1);
        repoInstitutes.update(institutes);
        listener.onComplete(null);

    }

    public void setDeleted(EntityInstitutes institutes, MyToolsCls.OnCompleteListener listener) {
        institutes.setUpdate_type(2);// set to be deleted to the internet
        institutes.setIs_uploaded(false);
        repoInstitutes.update(institutes);
        listener.onComplete(null);

    }

    public void delete(EntityInstitutes institutes, MyToolsCls.OnCompleteListener listener) {
        repoInstitutes.delete(institutes);
        listener.onComplete(null);
    }


    public LiveData<List<EntityInstitutes>> getUploads() {
        return repoInstitutes.getUploads();
    }
}
