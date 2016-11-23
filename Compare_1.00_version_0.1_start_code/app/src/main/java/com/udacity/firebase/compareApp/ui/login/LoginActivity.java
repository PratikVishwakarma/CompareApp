package com.udacity.firebase.compareApp.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pratik.compareApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.udacity.firebase.compareApp.model.User;
import com.udacity.firebase.compareApp.ui.BaseActivity;
import com.udacity.firebase.compareApp.ui.MainActivity;
import com.udacity.firebase.compareApp.ui.postsList.PostsListActivity;
import com.udacity.firebase.compareApp.utils.Constants;
import com.udacity.firebase.compareApp.utils.Utils;
import com.udacity.firebase.compareApp.model.User;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userEmail, encodedUserEmail;

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    private static SharedPreferences sp = null;

    public static String mUsername = null, mEmail = null;
    public static boolean mlogedInStatus = false;

    private EditText mEditTextEmailInput, mEditTextPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        initializeScreen();
        if(currentUser != null){

            userEmail = currentUser.getEmail();
            encodedUserEmail = Utils.encodeEmail(userEmail);
            Toast.makeText(getApplicationContext(), currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            /* Get mEncodedEmail and mlogedInStatus from SharedPreferences, use null as default value */
//            mEmail = sp.getString(Constants.KEY_EMAIL, null);
//            mlogedInStatus  = sp.getBoolean(Constants.KEY_USER_LOGEDIN_STATUS, Boolean.TRUE);
            checkLoggedInWithPassword(userEmail);
        }
    }

    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Sign in with Password provider when user clicks sign in button
     */
    public void onSignInPressed(View view) {
        signInPassword();
    }

    public void checkLoggedInWithPassword(String userEmail){
        String encodedUserEmail = Utils.encodeEmail(userEmail);
        DatabaseReference usersRef = mFirebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_USERS);
        usersRef.child(encodedUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                boolean hasLoggedInWithPassword = user.isHasLoggedInWithPassword();
                boolean hasProvidedExtraInformation = user.isHasProvidedExtraInformation();
                if(!hasLoggedInWithPassword){
                    Toast.makeText(getApplicationContext(), "Verify your email first", Toast.LENGTH_SHORT).show();
                } else {
                    if(hasProvidedExtraInformation){
                        Intent intent = new Intent(getApplicationContext(), PostsListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else{
                        Intent intent = new Intent(getApplicationContext(), ProvideExtraInformationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Sign in with Password provider (used when user taps "Done" action on keyboard)
     */
    public void signInPassword() {
        final String email = mEditTextEmailInput.getText().toString();
        final String password = mEditTextPasswordInput.getText().toString();

        /**
         * If email and password are not empty show progress dialog and try to authenticate
         */
        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                /* Go to main activity */
                if(task.isSuccessful()){
                    setAuthenticatedUserPasswordProvider(email);
                    /* Save provider name and encodedEmail for later use and start MainActivity */

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putBoolean(Constants.KEY_USER_LOGEDIN_STATUS, Boolean.TRUE).apply();
                    spe.putString(Constants.KEY_ENCODED_EMAIL, email).apply();


                    checkLoggedInWithPassword(email);
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
                } else{
                    mAuthProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"auth_failed ",
                            Toast.LENGTH_SHORT).show();
                    mEditTextEmailInput.setError(getString(R.string.error_message_email_issue));
                }

            }
        });
    }

    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);
        LinearLayout linearLayoutLoginActivity = (LinearLayout) findViewById(R.id.linear_layout_login_activity);
        initializeBackground(linearLayoutLoginActivity);
        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
        /* Setup Google Sign In */
    }

    /**
     * Helper method that makes sure a user is created if the user
     * logs in with Firebase's email/password provider.
     */
    private void setAuthenticatedUserPasswordProvider(String emailId) {

        String encodedUserEmail = Utils.encodeEmail(emailId);
        DatabaseReference usersRef = mFirebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_USERS);
        Log.e(LOG_TAG, "Linnk to reset is "+usersRef.child(encodedUserEmail).child(Constants.FIREBASE_PROPERTY_USER_HAS_LOGGED_IN_WITH_PASSWORD).toString());
        usersRef.child(encodedUserEmail).child(Constants.FIREBASE_PROPERTY_USER_HAS_LOGGED_IN_WITH_PASSWORD).setValue(true);
    }

}
