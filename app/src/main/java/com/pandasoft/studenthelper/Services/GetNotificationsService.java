package com.pandasoft.studenthelper.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pandasoft.studenthelper.Activities.Notification.ActivityNotification;
import com.pandasoft.studenthelper.Entities.EntityNotifications;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.ViewModels.ViewModelNotifications;

import java.util.Map;

;


// server key:
/*
 * Header
 * Authorization: key = server key
 * Content-Type:application/json
 * Url:https://frm.googleapis.com/fcm/send
 * Data
 * {
 * "to" : "FCM TOKEN",
 * "collapse_key" : "type_a",
 * "priority": 10,
 * "data"    :{
 *       "content" : "Content",
 *       "title":"Notification Title"
 *       }
 * }
 * */


public class GetNotificationsService extends FirebaseMessagingService {
    private static final String TAG = "NotificationsService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        Log.d("TOKEN", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("From", remoteMessage.getFrom());
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        //show notifications
        ViewModelNotifications viewModelNotifications = new ViewModelNotifications(getApplication());
        EntityNotifications entityNotifications = new EntityNotifications();

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");

       entityNotifications.setTitle(title);
       entityNotifications.setDescription(content);

       viewModelNotifications.insert(entityNotifications,msg->{});

        Intent emptyIN = new Intent(getBaseContext(), ActivityNotification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1234, emptyIN, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = "studenthelpter_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID
                    , "NOTIFICATION"
                    , NotificationManager.IMPORTANCE_MAX);
            channel.setDescription("Student Helper Channel test FCM");
            channel.enableLights(true);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            nManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notification2 = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notification2.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo("info")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        nManager.notify(1234, notification2.build());
    }
}
