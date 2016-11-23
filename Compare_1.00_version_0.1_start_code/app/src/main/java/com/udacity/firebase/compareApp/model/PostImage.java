package com.udacity.firebase.compareApp.model;

/**
 * Created by prati on 11-Sep-16.
 */
public class PostImage {
    //Firebase postLinkRef = new Firebase("https://facebooklogindemo-8f640.firebaseio.com/postImage");
    public static final String TABLE_NAME = "postImage";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_POST_ID = "postId";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_LIKE = "like";

    private String user_id, post_id, image;
    private int like;

    public PostImage() {}

    public PostImage(String user_id, String post_id, String image, int like) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.image = image;
        this.like = like;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
