package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.pandasoft.studenthelper.Entities.EntitySubjectsLevel;
import com.pandasoft.studenthelper.Repositories.RepoSubjectsLevel;

import java.util.List;

public class ViewModelSubject extends AndroidViewModel {
    RepoSubjectsLevel repoSubjects;
    MutableLiveData<String> strLevel = new MutableLiveData<>();
    LiveData<List<EntitySubjectsLevel>> mLevels;

    public ViewModelSubject(Application application) {
        super(application);
        repoSubjects = new RepoSubjectsLevel(application);
        mLevels = Transformations.switchMap(strLevel, input -> repoSubjects.getAllSubjectsLevel(input));
    }

    public LiveData<List<EntitySubjectsLevel>> getAllData(String id_level) {
        strLevel.setValue(id_level);
        return mLevels;
    }

    public void setLevelId(String id_level) {
        strLevel.setValue(id_level);
    }

}
