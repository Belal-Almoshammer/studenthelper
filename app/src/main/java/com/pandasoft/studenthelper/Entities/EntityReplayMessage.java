package com.pandasoft.studenthelper.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reply_message")
public class EntityReplayMessage extends BaseEntity {

    @NonNull
    @PrimaryKey
    private String id;
    private long id_chat;
    private String id_student;
    private String reply;

    public EntityReplayMessage(String id, long id_chat, String id_student, String reply) {
        this.id = id;
        this.id_chat = id_chat;
        this.id_student = id_student;
        this.reply = reply;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getId_chat() {
        return id_chat;
    }

    public void setId_chat(long id_chat) {
        this.id_chat = id_chat;
    }

    public String getId_student() {
        return id_student;
    }

    public void setId_student(String id_student) {
        this.id_student = id_student;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

}
