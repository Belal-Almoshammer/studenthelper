package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "website")
public class EntityWebsites extends BaseEntity {
    @PrimaryKey
    private long id;
    private String title;
    private String link_web;

    public EntityWebsites() {

    }

    @Ignore
    public EntityWebsites(long id, String title, String link_web) {
        this.id = id;
        this.title = title;
        this.link_web = link_web;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink_web() {
        return link_web;
    }

    public void setLink_web(String link_web) {
        this.link_web = link_web;
    }
}
