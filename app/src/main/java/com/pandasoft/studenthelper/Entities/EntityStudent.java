package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class EntityStudent extends BaseEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String password;
    private String img;
    private String id_level;
    private String address;
    private String phone;
    private String email;

    public EntityStudent(long id, String name, String password, String img, String id_level, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.img = img;
        this.id_level = id_level;
        this.address = address;
        this.phone = phone;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId_level() {
        return id_level;
    }

    public void setId_level(String id_level) {
        this.id_level = id_level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
