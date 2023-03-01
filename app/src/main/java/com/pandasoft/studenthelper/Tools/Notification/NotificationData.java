package com.pandasoft.studenthelper.Tools.Notification;

public class NotificationData {
    private String title;
    private String content;

    public NotificationData(String title, String message) {
        this.title = title;
        this.content = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return content;
    }

    public void setMessage(String content) {
        this.content = content;
    }
}
