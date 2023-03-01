package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz")
public class EntityQuiz extends BaseEntity {
    @PrimaryKey
    private long id;
    private String name;
    private String description;
    private int points;
    private int questions_count;
    private String id_sub;


    public EntityQuiz() {

    }

    @Ignore
    public EntityQuiz(String name, String description, int points, String id_sub) {
        this.name = name;
        this.description = description;
        this.points = points;
        this.id_sub = id_sub;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getQuestions_count() {
        return questions_count;
    }

    public void setQuestions_count(int questions_count) {
        this.questions_count = questions_count;
    }

    public String getId_sub() {
        return id_sub;
    }

    public void setId_sub(String id_sub) {
        this.id_sub = id_sub;
    }
}
