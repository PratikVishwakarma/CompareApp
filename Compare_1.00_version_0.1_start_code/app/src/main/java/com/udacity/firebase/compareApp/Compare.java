package com.udacity.firebase.compareApp;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Includes one-time initialization of Firebase related code
 */
public class Compare extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}