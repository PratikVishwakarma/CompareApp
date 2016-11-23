package com.udacity.firebase.compareApp.model;

/**
 * Created by prati on 05-Sep-16.
 */
public class Post {
    public static final String TABLE_NAME = "Post";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_POST_ID = "postid";
    public static final String COLUMN_TOTAL_IMAGE = "totalimage";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LIKE = "like";

    public String userId;
    public String postId;
    public String content;
    public String time;
    public String date;
    public int totalImage, like;
    public Post() {
    }

    public Post(String userId, String postId, String content, String time, String date, int totalImage, int like) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.time = time;
        this.date = date;
        this.totalImage = totalImage;
        this.like = like;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getTotalImage() {
        return totalImage;
    }

    public int getLike() {
        return like;
    }
}
