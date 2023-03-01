package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications_teacher")
public class EntityNotificationsTeacher extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String id_teacher;
    private String notifications;
    private String description;
    private int is_private;
    private String date_create;
    private String time_create;
    private String name_teacher;

    public EntityNotificationsTeacher() {

    }

    @Ignore
    public EntityNotificationsTeacher(long id, String id_teacher, String notifications, String desc, int is_p, String date_create, String time_create) {
        this.id = id;
        this.id_teacher = id_teacher;
        this.notifications = notifications;
        this.description = desc;
        this.date_create = date_create;
        this.time_create = time_create;
        this.is_private = is_p;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_teacher() {
        return id_teacher;
    }

    public void setId_teacher(String id_teacher) {
        this.id_teacher = id_teacher;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public String getTime_create() {
        return time_create;
    }

    public void setTime_create(String time_create) {
        this.time_create = time_create;
    }

    public String getName_teacher() {
        return name_teacher;
    }

    public void setName_teacher(String name_teacher) {
        this.name_teacher = name_teacher;
    }
}
