package com.pandasoft.studenthelper.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.Entities.EntityQuestion;
import com.pandasoft.studenthelper.Repositories.RepoQuestion;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;

import java.util.List;

;

public class ViewModelQuestion extends AndroidViewModel {
    RepoQuestion repoQuestion;

    public ViewModelQuestion(Application application) {
        super(application);
        repoQuestion = new RepoQuestion(application);
        getDownloads();
    }


    private void getDownloads() {
        //2#.list of questions
        if (!MyToolsCls.isNetworkConnected(getApplication())) return;
        String user_token = MyToolsCls.getUserToken(getApplication());
        long timestamp = MyToolsCls.getLong(getApplication(), "questions");

        new UploadCls.DownloadEntityTask<EntityQuestion>(getApplication(), "questions", null, data -> {
            //__________
            EntityQuestion entity = data.getValue(EntityQuestion.class);
            assert entity != null;
            MyToolsCls.putLong(getApplication(), "questions", entity.getUpdate_date());
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

    public void insert(EntityQuestion entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(0);
        entity.setId(MyToolsCls.generateId());
        repoQuestion.insert(entity);
        listener.onComplete(null);
    }

    public void update(EntityQuestion entity, MyToolsCls.OnCompleteListener listener) {
        entity.setUpdate_type(1);
        repoQuestion.update(entity);
        listener.onComplete(null);
    }

    public void setDeleted(long quiz_id, MyToolsCls.OnCompleteListener listener) {
        repoQuestion.setDeleted(quiz_id);
        listener.onComplete(null);
    }

    public void delete(EntityQuestion entity, MyToolsCls.OnCompleteListener listener) {
        repoQuestion.update(entity);
        listener.onComplete(null);
    }

    public LiveData<List<EntityQuestion>> getAllData(long quiz_id) {
        return repoQuestion.getAllQuestions(quiz_id);
    }

    public void deleteByQuizId(long id) {
        repoQuestion.deleteByQuizId(id);
    }

    public LiveData<List<EntityQuestion>> getUploads() {
        return repoQuestion.getUploads();
    }

}
