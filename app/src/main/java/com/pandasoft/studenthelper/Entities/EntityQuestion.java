package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class EntityQuestion extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String question;
    private String answer;
    private String selection_1;
    private String selection_2;
    private String selection_3;
    private long quiz_id;

    public EntityQuestion() {

    }

    @Ignore
    public EntityQuestion(long id, String question, String answer
            , String selection_1, String selection_2, String selection_3) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.selection_1 = selection_1;
        this.selection_2 = selection_2;
        this.selection_3 = selection_3;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSelection_1() {
        return selection_1;
    }

    public void setSelection_1(String selection_1) {
        this.selection_1 = selection_1;
    }

    public String getSelection_2() {
        return selection_2;
    }

    public void setSelection_2(String selection_2) {
        this.selection_2 = selection_2;
    }

    public String getSelection_3() {
        return selection_3;
    }

    public void setSelection_3(String selection_3) {
        this.selection_3 = selection_3;
    }

    public long getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(long quiz_id) {
        this.quiz_id = quiz_id;
    }
}
