package com.udacity.firebase.compareApp.model;

/**
 * Created by prati on 22-Sep-16.
 */
public class Like {
    public static final String TABLE_NAME = "like";
    public static final String COLUMN_userId = "userId";
    public static final String COLUMN_postId = "postId";
    public static final String COLUMN_IMAGE_ID = "image_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";

    public String userId, postId, image_id, time, date;

    public Like() {
    }

    public Like(String userId, String postId, String image_id, String time, String date) {
        this.userId = userId;
        this.postId = postId;
        this.image_id = image_id;
        this.time = time;
        this.date = date;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getpostId() {
        return postId;
    }

    public void setpostId(String postId) {
        this.postId = postId;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
