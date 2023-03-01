package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "universities")
public class EntityUniversities extends BaseEntity {

    @PrimaryKey
    private long id;
    private String name;
    private String text;
    private String address;
    private String email;
    private String phone;
    private String brief;

    private String local_img;
    private String cloud_img;

    public EntityUniversities() {

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getLocal_img() {
        return local_img;
    }

    public void setLocal_img(String local_img) {
        this.local_img = local_img;
    }

    public String getCloud_img() {
        return cloud_img;
    }

    public void setCloud_img(String cloud_img) {
        this.cloud_img = cloud_img;
    }
}
