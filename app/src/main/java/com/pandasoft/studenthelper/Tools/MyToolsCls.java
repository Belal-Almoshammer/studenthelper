package com.pandasoft.studenthelper.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

;

public class MyToolsCls {

    public static boolean isNetworkConnected(Application application) {
        ConnectivityManager cm = (ConnectivityManager) application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected();
    }

    public static String removeUnvalidCharacters(String s) {
        return s.trim()
                .replaceAll("[\\\\/:*?\"<>|%#&{}()]", "");
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected();
    }

    public static void getTimeStamp(String table_name, OnFinishListener listener) {
        String TIME_TAG = "timestamp/" + table_name.trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("settings");
        reference.child(TIME_TAG)
                .setValue(ServerValue.TIMESTAMP)
                .addOnSuccessListener(task -> {
                    reference.child(TIME_TAG).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            long time;
                            try {
                                time = snapshot.getValue(Long.class);
                            } catch (Exception e) {
                                time = 0;
                            }
                            listener.onFinish(time);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            listener.onFinish(0);
                        }
                    });
                });
    }

    public static void downloadFromPath(Context mContext, String src, String dist) {
        if (!URLUtil.isValidUrl(src)) {
            Toast.makeText(mContext, "رابط غير صالح", Toast.LENGTH_SHORT).show();
            return;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(src));
        String title = URLUtil.guessFileName(src, null, null);
        request.setTitle(title);
        request.setDescription("جاري التنزيل");
        String cookie = CookieManager.getInstance().getCookie(src);

        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(dist, title);

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(mContext, "جاري التزيل", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context Context
     * @param url     String most be pdf file path
     * @return File type
     */
    public static File fileIsDownloaded(Context context, String url) {
        if (url == null) return null;
        int dot_index = url.lastIndexOf('.');
        String ext = url.substring(dot_index);
        if (!ext.toLowerCase().contains("pdf")
                && !ext.toLowerCase().contains("jpg")
                && !ext.toLowerCase().contains("jpeg")
                && !ext.toLowerCase().contains("gif")
                && !ext.toLowerCase().contains("png")
                && !ext.toLowerCase().contains("tif")
                && !ext.toLowerCase().contains("bmp")
                && !ext.toLowerCase().contains("webm")) return null;
        //#1 get path of file from downloads downloads path
        String file_name = URLUtil.guessFileName(url, null, null);
        MimeTypeMap.getFileExtensionFromUrl(url);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/StudentHelper", file_name);
        if (file.exists()) return file;
        // get file from other location
        Uri uri = Uri.parse(url);
        String real_path = FilesCls.RealPathUtil.getRealPath(context, uri);
        if (real_path == null) return null;
        return new File(real_path);
    }

    public static String getYoutubeId(String url) {
        //  if (!isYoutubeUrl(url)) return null;
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=?)([^#&?]*).*$"
                , Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) vId = matcher.group(1);
        return vId;
    }

    public static boolean isYoutubeUrl(String url) {
        return url.contains("?v=");
    }

    public static byte[] generatePdfThumpNail(Uri uri, Context context) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(uri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);


            pdfiumCore.closeDocument(pdfDocument);
            // get bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public static String getCurrentUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("main_pref", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("main_pref", MODE_PRIVATE);
        long value = sharedPreferences.getLong(key, 0);
        Log.i("GET_LONG", "getLong: " + value);
        return value;
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("main_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
        editor.commit();
    }

    public static void sendOTP(Activity activity, String phone, OnFinishOTPListener listener) {
        listener.onPre();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        listener.onPost();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        listener.onFailure(e.getMessage());
                        listener.onPost();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        listener.onSuccess(s);
                        listener.onPost();
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        listener.onFailure(s);
                        listener.onPost();
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public static void setImageSrc(Context context, ImageView image, String file_name) {
        int id = context.getResources().getIdentifier(file_name, "drawable", context.getPackageName());
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable drawable = context.getResources().getDrawable(id);
        image.setImageDrawable(drawable);
    }

    public static boolean checkError(EditText txt, String err_msg) {
        if (txt.getText() == null || txt.getText().length() == 0) {
            txt.setError(err_msg);
            return false;
        }
        txt.setError(null);
        return true;
    }

    public static String generateStr(int length) {
        Random rand = new Random();
        StringBuilder builder = new StringBuilder();
        int rand_length = rand.nextInt(length);
        char tmp;
        for (int i = 0; i < rand_length; i++) {
            tmp = (char) (rand.nextInt(96) + 32);
            builder.append(tmp);
        }
        return removeUnvalidCharacters(builder.toString());
    }

    public static long generateId() {
        //توليد رقم id عشوائي
        Random rand = new Random();
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyMMddhmmssSS");
        String generated_id = format.format(date) + rand.nextInt(999);
        return Long.parseLong(generated_id);
    }

    public static String getUserToken(Context context) {
        return context
                .getSharedPreferences("main_pref", MODE_PRIVATE)
                .getString("user_token", "Non");
    }

    public static void setUserToken(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("main_pref", MODE_PRIVATE).edit();
        editor.putString("user_token", generateStr(32) + generateId());
        editor.apply();
        editor.commit();
    }


    public enum QUERY_TYPE {NONE, INSERT, UPDATE}

    public interface OnFinishListener {
        void onFinish(long time);
    }

    public interface OnFinishOTPListener {
        void onSuccess(String code);

        void onFailure(String msg);

        void onPre();

        void onPost();
    }

    public interface OnUpload {
        void onSuccess();

        void onFailure(String error);
    }

    public interface OnCompleteListener {
        void onComplete(String msg);
    }

}
