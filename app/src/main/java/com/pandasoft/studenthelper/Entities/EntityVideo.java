package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "video")
public class EntityVideo extends BaseEntity {

    @PrimaryKey
    private long id;
    private String id_level;
    private String id_sub;
    private String id_chapter;

    private String user_name;
    private String image;
    private String title;

    private String cloud_url;

    private String size_video;
    private String date_create;
    private String time_create;

    public EntityVideo() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_level() {
        return id_level;
    }

    public void setId_level(String id_level) {
        this.id_level = id_level;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize_video() {
        return size_video;
    }

    public void setSize_video(String size_video) {
        this.size_video = size_video;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getId_sub() {
        return id_sub;
    }

    public void setId_sub(String id_sub) {
        this.id_sub = id_sub;
    }

    public String getId_chapter() {
        return id_chapter;
    }

    public void setId_chapter(String id_chapter) {
        this.id_chapter = id_chapter;
    }

    public String getCloud_url() {
        return cloud_url;
    }

    public void setCloud_url(String cloud_url) {
        this.cloud_url = cloud_url;
    }
}
