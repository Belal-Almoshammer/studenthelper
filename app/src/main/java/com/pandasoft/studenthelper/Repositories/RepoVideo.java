package com.pandasoft.studenthelper.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pandasoft.studenthelper.DAOs.DaoVideo;
import com.pandasoft.studenthelper.Database.MyRoomDatabase;
import com.pandasoft.studenthelper.Entities.EntityVideo;

import java.util.List;

public class RepoVideo {
    DaoVideo daoVideo;

    public RepoVideo(Application app) {
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getInstance(app);

        this.daoVideo = myRoomDatabase.daoVideo();
    }
    public void insert(EntityVideo video) {
        new RepoVideo.InsertAsyncTask(this.daoVideo).execute(video);

    }

    public void update(EntityVideo video) {
        new RepoVideo.UpdateAsyncTask(this.daoVideo).execute(video);
    }

    public void delete(EntityVideo video) {
        new RepoVideo.DeleteAsyncTask(this.daoVideo).execute(video);
    }

    public LiveData<List<EntityVideo>> getVideos(String id_sub) {
        return this.daoVideo.getVideos(id_sub);
    }

    public LiveData<List<EntityVideo>> getUploads() {
        return daoVideo.getUploads();
    }

    private static class InsertAsyncTask extends AsyncTask<EntityVideo, Void, Void> {

        com.pandasoft.studenthelper.DAOs.DaoVideo DaoVideo;

        public InsertAsyncTask(DaoVideo dao) {
            this.DaoVideo = dao;
        }

        @Override
        protected Void doInBackground(EntityVideo... entityVideo) {
            this.DaoVideo.insert(entityVideo[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityVideo, Void, Void> {

        DaoVideo DaoVideo;

        public UpdateAsyncTask(DaoVideo dao) {
            this.DaoVideo = dao;
        }

        @Override
        protected Void doInBackground(EntityVideo... entityVideo) {
            if (this.DaoVideo.getVideo(entityVideo[0].getId()) != null)
                this.DaoVideo.update(entityVideo[0]);
            else
                this.DaoVideo.insert(entityVideo[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityVideo, Void, Void> {

        DaoVideo DaoVideo;

        public DeleteAsyncTask(DaoVideo dao) {
            this.DaoVideo = dao;
        }

        @Override
        protected Void doInBackground(EntityVideo... entityVideo) {
            this.DaoVideo.delete(entityVideo[0]);
            return null;
        }
    }
}
