package com.pandasoft.studenthelper.Tools.Notification;

import com.google.gson.annotations.SerializedName;

public class PushNotification {
    @SerializedName("data")
    private NotificationData data;
    @SerializedName("to")
    private String to;


    public PushNotification(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
