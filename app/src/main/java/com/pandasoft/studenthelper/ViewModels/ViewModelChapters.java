package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntitySubjectChapters;
import com.pandasoft.studenthelper.Repositories.RepoSubjectChapters;

;

import java.util.List;

public class ViewModelChapters extends AndroidViewModel {
    private final RepoSubjectChapters repoSubjectChapters;

    public ViewModelChapters( Application application) {
        super(application);
        repoSubjectChapters = new RepoSubjectChapters(application);
    }

    public LiveData<List<EntitySubjectChapters>> getAllData(String id_subject) {
        return repoSubjectChapters.getAllSubjectChapters(id_subject);
    }
}
