package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book")
public class EntityBook extends BaseEntity {

    @PrimaryKey
    private long id;
    private String id_level;
    private String id_sub;
    private String name;
    private String local_url;
    private String cloud_url ;
    private String size_book;
    private int type_book;
    private String level_name;

    public EntityBook() {
        this.cloud_url = "";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize_book() {
        return size_book;
    }

    public void setSize_book(String size_book) {
        this.size_book = size_book;
    }

    public int getType_book() {
        return type_book;
    }

    public void setType_book(int type_book) {
        this.type_book = type_book;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getId_sub() {
        return id_sub;
    }

    public void setId_sub(String id_sub) {
        this.id_sub = id_sub;
    }


    public String getLocal_url() {
        return local_url;
    }

    public void setLocal_url(String local_url) {
        this.local_url = local_url;
    }

    public String getCloud_url() {
        return cloud_url;
    }

    public void setCloud_url(String cloud_url) {
        this.cloud_url = cloud_url;
    }
}
