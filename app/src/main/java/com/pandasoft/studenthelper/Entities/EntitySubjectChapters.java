package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subject_chapters")
public class EntitySubjectChapters {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String id_sub;
    private String name;
    private String subject_name;

    public EntitySubjectChapters() {

    }

    @Ignore
    public EntitySubjectChapters(long id, String id_sub, String name) {
        this.id = id;
        this.name = name;
        this.id_sub = id_sub;
    }

    public String getId_sub() {
        return id_sub;
    }

    public void setId_sub(String id_sub) {
        this.id_sub = id_sub;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
