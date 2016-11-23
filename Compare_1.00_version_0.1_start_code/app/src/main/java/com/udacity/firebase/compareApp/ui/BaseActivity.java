package com.udacity.firebase.compareApp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.pratik.compareApp.R;
import com.udacity.firebase.compareApp.ui.login.CreateAccountActivity;
import com.udacity.firebase.compareApp.ui.login.LoginActivity;

/**
 * BaseActivity class is used as a base class for all activities in the app
 * It implements GoogleApiClient callbacks to enable "Logout" in all activities
 * and defines variables that are being shared across all activities
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = BaseActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;

    private static SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
//            sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            /* Get mEncodedEmail and mProvider from SharedPreferences, use null as default value */
//            mEncodedEmail = sp.getString(Constants.KEY_ENCODED_EMAIL, null);
//            mProvider = sp.getString(Constants.KEY_PROVIDER, null);
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();


        if (!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            //[START auth_state_listener]


            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        /* Move user to LoginActivity, and remove the backstack */
//                        Intent intent = new Intent(BaseActivity.this, User.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
                        Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
//                        SharedPreferences.Editor spe = sp.edit();
//                        spe.putString(Constants.KEY_ENCODED_EMAIL, null);
//                        spe.putString(Constants.KEY_PROVIDER, null);
                        //takeUserToLoginScreenOnUnAuth();
                        // User is signed out
                        Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                    }
                }
            };
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        } if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initializeBackground(LinearLayout linearLayout) {

        /**
         * Set different background image for landscape and portrait layouts
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen_land);
        } else {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen);
        }
    }
    protected void logout() {

        /* Logout if mProvider is not null */
        mAuth.signOut();
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
//        SharedPreferences.Editor spe = sp.edit();
//        spe.putString(Constants.KEY_SIGNUP_EMAIL, null).apply();
        takeUserToLoginScreenOnUnAuth();
    }
    private void takeUserToLoginScreenOnUnAuth() {
        /* Move user to LoginActivity, and remove the backstack */
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
