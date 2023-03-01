package com.pandasoft.studenthelper.Tools;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pandasoft.studenthelper.Entities.BaseEntity;

import java.io.File;

public class UploadCls {

    public static void DownloadFromPath(Context mContext, String url) {

        if (!URLUtil.isValidUrl(url)) {
            Toast.makeText(mContext, "رابط غير صالح", Toast.LENGTH_SHORT).show();
            return;
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String title = URLUtil.guessFileName(url, null, null);
        request.setTitle(title);
        request.setDescription("جاري تنزيل الكتاب");

        String cookie = CookieManager.getInstance().getCookie(url);

        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(mContext, "جاري التزيل", Toast.LENGTH_SHORT).show();
    }

    public interface OnFailureListener {
        void onFailure();
    }

    public interface OnLoadedListener {
        void onLoaded(DataSnapshot data);
    }

    public interface OnUploadOrDownloadFileListener {
        void onProgress(long value);

        void onFailure(String msg);

        void onSuccess(Uri uri);
    }

    public static class UploadEntityTask<T extends BaseEntity> extends AsyncTask<Void, Void, Void> {

        @SuppressLint("StaticFieldLeak")
        private final Context context;
        private final String table_name;
        private final T entity;
        private final MyToolsCls.OnUpload listener;

        public UploadEntityTask(Context context, String table_name, T entity, MyToolsCls.OnUpload listener) {
            this.context = context;
            this.table_name = table_name;
            this.entity = entity;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MyToolsCls.getTimeStamp(table_name, time -> {
                // get firebase instance
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(table_name);
                //Get user token
                String user_token = MyToolsCls.getUserToken(context);
                entity.setUser_token(user_token);
                entity.setUpdate_date(time);
                entity.setUser_update_id(MyToolsCls.getCurrentUserId(context));
                entity.setIs_uploaded(true);

                if (entity.getUpdate_type() == 0)  // if is insert mode then generate upload key
                    entity.setUpload_id(reference.push().getKey());

                if (entity.getUpload_id() == null) {
                    listener.onFailure(null);
                } else reference.child(entity.getUpload_id())
                        .setValue(entity, (error, ref) -> {
                            if (error == null) {
                                if(listener != null)
                                listener.onSuccess();
                            } else {
                                if(listener!= null)
                                listener.onFailure(error.getMessage());
                            }
                        });
            });
            return null;
        }
    }

    public static class DownloadEntityTask<T extends BaseEntity> extends AsyncTask<Void, Void, Void> {

        @SuppressLint("StaticFieldLeak")
        private final Context mContext;
        private final String table_name;
        private final OnLoadedListener mListener;
        private OnFailureListener failureListener;
        private Query query;

        public DownloadEntityTask(Context context, String t_name, Query query, OnLoadedListener listener) {
            mContext = context;
            this.table_name = t_name;
            mListener = listener;
            this.query = query;
        }

        public void setOnFailure(OnFailureListener listener) {
            failureListener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //begin download

            long stamp = MyToolsCls.getLong(mContext, table_name);

            if (query == null) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(table_name);
                query = reference.orderByChild("update_date").startAfter(stamp + 1, "update_date");
            }
            Log.e("TIMESTAMP", "TimeStamp of " + table_name + ": " + stamp);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        mListener.onLoaded(data);
                    }
                    if (failureListener != null) failureListener.onFailure();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (failureListener != null) failureListener.onFailure();
                }
            });
            //end function
            return null;
        }
    }

    public static class UploadFileTask extends AsyncTask<Void, Void, Void> {

        private final OnUploadOrDownloadFileListener mListener;
        private final String table_name;
        private final Uri uri;


        /**
         * @param t_name table name
         * @param uri Uri path of file
         */
        public UploadFileTask(String t_name, Uri uri, OnUploadOrDownloadFileListener listener) {
            table_name = t_name;
            this.uri = uri;
            mListener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String file_name = URLUtil.guessFileName(uri.toString(), null, null);
            mListener.onProgress(0);
            final StorageReference reference = FirebaseStorage
                    .getInstance()
                    .getReference().child(table_name)
                    .child(file_name);

            UploadTask uploadTask = reference.putFile(uri);

            uploadTask.addOnProgressListener(snapshot -> {
                long total = snapshot.getTotalByteCount();
                long progress = snapshot.getBytesTransferred();

                if (total > 0 && progress > 0) {
                    mListener.onProgress(100 * (progress / total));
                }
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (task.isSuccessful())
                return reference.getDownloadUrl();
                else Toast.makeText(null, "خطأ في رفع الملف!", Toast.LENGTH_SHORT).show();
                return null;
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    mListener.onSuccess(downloadUri);
                } else {
                    mListener.onFailure(task.getException().getMessage());
                }
            });
            //end doInBackground
            return null;
        }
    }

    public static class DownloadFileTask extends AsyncTask<Void, Void, Void> {
        OnUploadOrDownloadFileListener mListener;
        Uri uri;
        String table_name;

        public DownloadFileTask(String t_name, Uri uri, OnUploadOrDownloadFileListener listener) {
            this.mListener = listener;
            this.uri = uri;
            this.table_name = t_name;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String file_name = URLUtil.guessFileName(uri.toString(), null, null);

            // download path
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(table_name).child(file_name);

            File rootPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "StudentHelper");

            if (!rootPath.exists()) {
                rootPath.mkdirs();
            }

            final File localFile = new File(rootPath, file_name);

            reference.getFile(localFile).addOnSuccessListener(bytes -> {
                Log.i("Downloader", "File downloaded successfully; " + bytes.toString());
                Uri uri = Uri.fromFile(localFile);
                mListener.onSuccess(uri);
            }).addOnFailureListener(e -> {
                Log.e("Downloader", "File not downloaded; " + e.getMessage());
                mListener.onFailure(e.getMessage());
            }).addOnProgressListener(snap -> {
                long total = snap.getTotalByteCount();
                long progress = snap.getBytesTransferred();

                if (total > 0 && progress > 0) {
                    mListener.onProgress(100 * (progress / total));
                }
            });
            return null;
        }
    }
}