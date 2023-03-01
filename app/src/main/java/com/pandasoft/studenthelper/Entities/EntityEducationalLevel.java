package com.pandasoft.studenthelper.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "educational_level")
public class EntityEducationalLevel {

    @NonNull
    @PrimaryKey
    private String id_level;
    private String name;

    public EntityEducationalLevel() {
        id_level = "-1";
    }

    @Ignore
    public EntityEducationalLevel( String id_level, String name) {
        this.id_level = id_level;
        this.name = name;
    }


    public String getId_level() {
        return id_level;
    }

    public void setId_level( String id_level) {
        this.id_level = id_level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
