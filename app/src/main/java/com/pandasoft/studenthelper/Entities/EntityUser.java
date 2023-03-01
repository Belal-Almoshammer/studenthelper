package com.pandasoft.studenthelper.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class EntityUser extends BaseEntity {

    @NonNull
    @PrimaryKey
    private String id;
    private String password;
    private String user_name;
    private String img_user;
    private String phone;
    private int user_type; // 0: teacher, 1: student
    private String email;
    private String note;
    private String id_level;

    private boolean is_admin;

    private String date_register;
    private int id_validity;

    public EntityUser() {
        id = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate_register() {
        return date_register;
    }

    public void setDate_register(String date_register) {
        this.date_register = date_register;
    }

    public int getId_validity() {
        return id_validity;
    }

    public void setId_validity(int id_validity) {
        this.id_validity = id_validity;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getId_level() {
        return id_level;
    }

    public void setId_level(String id_level) {
        this.id_level = id_level;
    }

    public boolean getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }
}
