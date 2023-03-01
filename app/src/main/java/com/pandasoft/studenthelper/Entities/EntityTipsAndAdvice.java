package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tips_and_advice")
public class EntityTipsAndAdvice extends BaseEntity {

    @PrimaryKey
    private long id;
    private String title;
    private String note;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
