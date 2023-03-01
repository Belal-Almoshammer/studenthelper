package com.pandasoft.studenthelper.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "teacher_courses")
public class EntityTeacherCourses extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String teacher_course_id;
    private String id_teacher;
    private String image;
    private String title;
    private String url_video;
    private String size_video;
    private String date_create;
    private String time_create;
    private String id_level;

    public EntityTeacherCourses(long id, String id_teacher, String image, String title, String url_video, String size_video, String date_create, String time_create, String id_level) {
        this.id = id;
        this.id_teacher = id_teacher;
        this.image = image;
        this.title = title;
        this.url_video = url_video;
        this.size_video = size_video;
        this.date_create = date_create;
        this.time_create = time_create;
        this.id_level = id_level;
    }

    public String getTeacher_course_id() {
        return teacher_course_id;
    }

    public void setTeacher_course_id(String teacher_course_id) {
        this.teacher_course_id = teacher_course_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_teacher() {
        return id_teacher;
    }

    public void setId_teacher(String id_teacher) {
        this.id_teacher = id_teacher;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_video() {
        return url_video;
    }

    public void setUrl_video(String url_video) {
        this.url_video = url_video;
    }

    public String getSize_video() {
        return size_video;
    }

    public void setSize_video(String size_video) {
        this.size_video = size_video;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public String getTime_create() {
        return time_create;
    }

    public void setTime_create(String time_create) {
        this.time_create = time_create;
    }

    public String getId_level() {
        return id_level;
    }

    public void setId_level(String id_level) {
        this.id_level = id_level;
    }
}
