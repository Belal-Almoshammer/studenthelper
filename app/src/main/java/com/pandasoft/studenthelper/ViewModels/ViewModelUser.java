package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityUser;
import com.pandasoft.studenthelper.Repositories.RepoUser;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

import java.util.List;

public class ViewModelUser extends AndroidViewModel {

    private final LiveData<List<EntityUser>> mList;
    RepoUser repoUser;


    public ViewModelUser(Application application) {
        super(application);
        repoUser = new RepoUser(application);
        mList = repoUser.getAllUsers();
    }

    public void insert(EntityUser entity, MyToolsCls.OnCompleteListener listener) {
        repoUser.insert(entity);
        listener.onComplete(null);
    }

    public void update(EntityUser entity, MyToolsCls.OnCompleteListener listener) {

        entity.setUpdate_type(1);
        repoUser.update(entity);
        listener.onComplete(null);
    }

    public void delete(EntityUser entity, MyToolsCls.OnCompleteListener listener) {
        repoUser.delete(entity);
        listener.onComplete(null);
    }

    public LiveData<List<EntityUser>> getAllData() {
        return this.mList;
    }
}
