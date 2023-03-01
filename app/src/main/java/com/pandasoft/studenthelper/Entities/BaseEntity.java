package com.pandasoft.studenthelper.Entities;

public abstract class BaseEntity {
    public String upload_id;
    public long update_date;
    public int update_type;
    public String user_update_id;
    public boolean is_uploaded = false;
    public String user_token;

    public String getUpload_id() {
        return upload_id;
    }

    public void setUpload_id(String upload_id) {
        this.upload_id = upload_id;
    }

    public long getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(long update_date) {
        this.update_date = update_date;
    }

    public int getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(int update_type) {
        this.update_type = update_type;
    }

    public String getUser_update_id() {
        return user_update_id;
    }

    public void setUser_update_id(String user_update_id) {
        this.user_update_id = user_update_id;
    }

    public boolean getIs_uploaded() {
        return is_uploaded;
    }

    public void setIs_uploaded(boolean is_uploaded) {
        this.is_uploaded = is_uploaded;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }
}
