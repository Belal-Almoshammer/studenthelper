package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityNotifications;
import com.pandasoft.studenthelper.Repositories.RepoNotifications;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

import java.util.List;

;

public class ViewModelNotifications extends AndroidViewModel {

    RepoNotifications repoNotifications;

    public ViewModelNotifications(Application application) {
        super(application);
        repoNotifications = new RepoNotifications(application);
    }

    public void insert(EntityNotifications entity, MyToolsCls.OnCompleteListener listener) {
        repoNotifications.insert(entity);
        entity.setId(MyToolsCls.generateId());
        listener.onComplete(null);
    }

    public void update(EntityNotifications entity, MyToolsCls.OnCompleteListener listener) {

        entity.setUpdate_type(1);
        repoNotifications.update(entity);
        listener.onComplete(null);
    }

    public void delete(EntityNotifications entity, MyToolsCls.OnCompleteListener listener) {
        repoNotifications.delete(entity);
        listener.onComplete(null);
    }

    public LiveData<List<EntityNotifications>> getList() {
        return repoNotifications.getAllNotifications();
    }


}
