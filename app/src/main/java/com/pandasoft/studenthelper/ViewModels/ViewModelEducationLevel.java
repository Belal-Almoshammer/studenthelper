package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityEducationalLevel;
import com.pandasoft.studenthelper.Repositories.RepoEductionLevel;

;

import java.util.List;

public class ViewModelEducationLevel extends AndroidViewModel {
    private final RepoEductionLevel repoEductionLevel;

    public ViewModelEducationLevel( Application application) {
        super(application);
        repoEductionLevel = new RepoEductionLevel(application);
    }

    public LiveData<List<EntityEducationalLevel>> getEducationLevels() {
        return repoEductionLevel.getAllEductionLevels();
    }
}
