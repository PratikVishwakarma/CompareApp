package com.udacity.firebase.compareApp.utils;

import com.pratik.compareApp.BuildConfig;

/**
 * Constants class store most important strings and paths of the app
 */
public final class Constants {

    /**
     * Constants related to locations in Firebase, such as the name of the node
     * where active lists are stored (ie "activeLists")
     */

    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_LOCATION_POST_LISTS = "Post";
    public static final String FIREBASE_LOCATION_HOME = "home";
    public static final String FIREBASE_LOCATION_STORAGE_POSTIMAGE = "postImage";
    public static final String FIREBASE_LOCATION_STORAGE_DISPLAYPICTURE = "displayPicture";

    /**
     * Constants for Firebase object properties
     */
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "timestampLastChanged";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    public static final String FIREBASE_PROPERTY_ITEM_NAME = "itemName";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";
    public static final String FIREBASE_PROPERTY_USER_HAS_LOGGED_IN_WITH_PASSWORD = "hasLoggedInWithPassword";
    public static final String FIREBASE_PROPERTY_USER_HAS_LOGGED_IN_FIRST_TIME = "hasLoggedInFirstTime";
    public static final String FIREBASE_PROPERTY_USER_HAS_PROVIDED_EXTRA_IMFORMATION = "hasProvidedExtraInformation";
    public static final String FIREBASE_PROPERTY_USER_SET_DISPLAY_PICTURE = "hasSetDisplayPicture";
    public static final String FIREBASE_PROPERTY_USER_GENDER = "gender";
    public static final String FIREBASE_PROPERTY_USER_MOBILE_NO = "mobileno";


    /*
    * Constants for formats like time and date
    * */
    public static final String FORMATE_ADD_POST_DATE = "d-MMM-yyyy";
    public static final String FORMATE_ADD_POST_TIME = "HH:mm";


    public static final String STRING_POSTID_DIFFERENTIATOR = "_de%f@f46e8n8^ia_t3or";



    public static final String TYPE_POST_TYPE_POST_BY_YOU = "byYou";
    public static final String TYPE_POST_TYPE_POST_BY_FRIEND = "byYourFriend";
    public static final String TYPE_POST_TYPE_SHARED_BY_FRIEND = "sharedByYourFriend";
    public static final String TYPE_POST_TYPE_TAGGED_BY_FRIEND = "taggedByYourFriend";

    /**
     * Constants related to SharedPreferences to store the current logedIn username
     */
    public static final String KEY_ENCODED_USERNAME = "encoded_username";
    public static final String KEY_ENCODED_EMAIL = "encoded_useremail";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "useremail";
    public static final String KEY_USER_LOGEDIN_STATUS = "user_logedin_status";



    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_POSTS_LIST = FIREBASE_URL + "/" + FIREBASE_LOCATION_POST_LISTS;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;



    /**
     * Constants for bundles, extras and shared preferences keys
     */


}
