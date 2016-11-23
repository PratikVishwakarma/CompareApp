package com.udacity.firebase.compareApp.model;

import java.util.HashMap;

/**
 * Defines the data structure for User objects.
 */
public class User {
    private String name;
    private String email;
    private String username;
    private String gender;
    private String mobileno;
    private HashMap<String, Object> timestampJoined;
    private boolean hasLoggedInWithPassword;
    private boolean hasLoggedInFirstTime;
    private boolean hasSetDisplayPicture;
    private boolean hasProvidedExtraInformation;



    /**
     * Required public constructor
     */
    public User() {
    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email and timestampJoined as params
     *
     * @param name
     * @param email
     * @param timestampJoined
     */
    public User(String username, String name, String email, HashMap<String, Object> timestampJoined) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.gender = "female";
        this.mobileno = "no_number_provided";
        this.timestampJoined = timestampJoined;
        this.hasLoggedInWithPassword = Boolean.FALSE;
        this.hasProvidedExtraInformation = Boolean.FALSE;
        this.hasLoggedInFirstTime = Boolean.TRUE;
        this.hasSetDisplayPicture = Boolean.FALSE;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getMobileno() {
        return mobileno;
    }

    public boolean isHasProvidedExtraInformation() {
        return hasProvidedExtraInformation;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }

    public boolean isHasSetDisplayPicture() {
        return hasSetDisplayPicture;
    }

    public boolean isHasLoggedInFirstTime() {
        return hasLoggedInFirstTime;
    }
}
