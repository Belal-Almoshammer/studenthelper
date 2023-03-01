package com.pandasoft.studenthelper.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

;

@Entity(tableName = "subjects")
public class EntitySubject {


    @NonNull
    @PrimaryKey
    private String id;
    private String name;
    private String color_code;
    private String img_name;

    public EntitySubject( String id, String name, String color_code, String img_name) {
        this.id = id;
        this.name = name;
        this.color_code = color_code;
        this.img_name = img_name;
    }


    public String getId() {
        return id;
    }

    public void setId( String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }
}
