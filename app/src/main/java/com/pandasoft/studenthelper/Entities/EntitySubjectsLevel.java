package com.pandasoft.studenthelper.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subjects_level")
public class EntitySubjectsLevel {

    @NonNull
    @PrimaryKey
    private String id;
    private String id_level;
    private String id_sub;

    private String level_name;
    private String subject_name;

    private String img_name;
    private String color_code;

    public EntitySubjectsLevel(String id, String id_level, String id_sub) {
        this.id = id;
        this.id_level = id_level;
        this.id_sub = id_sub;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getId_level() {
        return id_level;
    }

    public void setId_level(String id_level) {
        this.id_level = id_level;
    }

    public String getId_sub() {
        return id_sub;
    }

    public void setId_sub(String id_sub) {
        this.id_sub = id_sub;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }
}
