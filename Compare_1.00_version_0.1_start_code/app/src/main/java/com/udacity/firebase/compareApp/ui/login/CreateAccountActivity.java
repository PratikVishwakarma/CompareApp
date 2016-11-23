package com.udacity.firebase.compareApp.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pratik.compareApp.R;
import com.udacity.firebase.compareApp.model.User;
import com.udacity.firebase.compareApp.ui.BaseActivity;
import com.udacity.firebase.compareApp.utils.Constants;
import com.udacity.firebase.compareApp.utils.Utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

public class CreateAccountActivity extends BaseActivity{

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase mFirebaseDatabase;

    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();
    private ProgressDialog mAuthProgressDialog;
    private EditText mEditTextUsernameCreate, mEditTextNameCreate, mEditTextEmailCreate;
    private String mUserName, mUserEmail, mPassword, mName;

    private boolean userNameAvailableStatus = false, userEmailAvailableStatus = false;

    private SecureRandom mRandom = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        initializeScreen();

    }

    public void initializeScreen() {
        mEditTextUsernameCreate = (EditText) findViewById(R.id.edit_text_username_create);
        mEditTextEmailCreate = (EditText) findViewById(R.id.edit_text_email_create);
        mEditTextNameCreate = (EditText) findViewById(R.id.edit_text_name_create);
        LinearLayout linearLayoutCreateAccountActivity = (LinearLayout) findViewById(R.id.linear_layout_create_account_activity);
        initializeBackground(linearLayoutCreateAccountActivity);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_check_inbox));
        mAuthProgressDialog.setCancelable(false);
    }

    /**
     * Open LoginActivity when user taps on "Sign in" textView
     */
    public void onSignInPressed(View view) {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Create new account using Firebase email/password provider
     */
    public void onCreateAccountPressed(View view) {
        mUserName = mEditTextUsernameCreate.getText().toString();
        mUserEmail = mEditTextEmailCreate.getText().toString().toLowerCase();
        mName= mEditTextNameCreate.getText().toString().toLowerCase();
        mPassword = new BigInteger(130, mRandom).toString(32);



        /**
         * Check that email and user name are okay
         */
        boolean validEmail = isEmailValid(mUserEmail);
        boolean validUserName = isUserNameValid(mUserName);
        boolean validName = isUserNameValid(mName);
        if (!validEmail || !validUserName || !validName) return;

        mAuthProgressDialog.show();
        /**
         * Check that email and username are available
         */
        if(checkEmailAvailability(mUserEmail))
        {
            if(checkUsernameAvailability(mUserName)){
                /**
                 * If everything was valid show the progress dialog to indicate that
                 * account creation has started
                 */

                /**
                 * Create new user with specified email and password
                 */

                mAuth.createUserWithEmailAndPassword(mUserEmail, mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(LOG_TAG, mUserEmail +" , "+mPassword);
                        if(task.isSuccessful()){

                            mAuth.sendPasswordResetEmail(mUserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mAuthProgressDialog.dismiss();
                                    Log.i(LOG_TAG, getString(R.string.log_message_auth_successful));

                                    /**
                                     * Encode user email replacing "." with ","
                                     * to be able to use it as a Firebase db key
                                     */
                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CreateAccountActivity.this);
                                    SharedPreferences.Editor spe = sp.edit();
                                    spe.putString(Constants.KEY_EMAIL, mUserEmail).apply();
                                    spe.putBoolean(Constants.KEY_USER_LOGEDIN_STATUS, Boolean.FALSE).apply();
                                    createUserInFirebaseHelper();
                                    /**
                                     *  Password reset email sent, open app chooser to pick app
                                     *  for handling inbox email intent
                                     */
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                    try {
                                        startActivity(intent);
                                        finish();
                                    } catch (android.content.ActivityNotFoundException ex) {
                                /* User does not have any app to handle email */
                                    }
                                }
                            });
                        } else {
                            mAuthProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),getString(R.string.log_error_occurred)+" on 1",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }    else {
                Toast.makeText(getApplicationContext(), "Username already taken.", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "Email already exist.", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkEmailAvailability(String userEmail){
        mFirebaseDatabase.getReference(Constants.FIREBASE_LOCATION_USERS).child(Utils.encodeEmail(userEmail)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userEmailAvailableStatus = Boolean.FALSE;
                }else{
                    userEmailAvailableStatus = Boolean.TRUE;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userEmailAvailableStatus;
    }
    public boolean checkUsernameAvailability(String userName){
        DatabaseReference usersRef = mFirebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_USERS);
        Query userRefSearchByUsername = usersRef.orderByChild("username").equalTo(userName);
        userRefSearchByUsername.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userNameAvailableStatus = Boolean.FALSE;
                }else{
                    userNameAvailableStatus = Boolean.TRUE;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userNameAvailableStatus;
    }

    /**
     * Creates a new user in Firebase from the Java POJO
     */
    private void createUserInFirebaseHelper() {
        final String encodedEmail = Utils.encodeEmail(mUserEmail);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference(Constants.FIREBASE_LOCATION_USERS);

        final DatabaseReference userLocation = userRef.child(encodedEmail);
        /**
         * See if there is already a user (for example, if they already logged in with an associated
         * email.
         */

        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 /* If there is no user, make one */
                if (dataSnapshot.getValue() == null) {
                 /* Set raw version of date to the ServerValue.TIMESTAMP value and save into dateCreatedMap */

                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                    User newUser = new User(mUserName, mName, mUserEmail, timestampJoined);
                    userLocation.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, getString(R.string.log_error_occurred) );
            }
        });
    }

    private boolean isEmailValid(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEditTextEmailCreate.setError(String.format(getString(R.string.error_invalid_email_not_valid),
                    email));
            return false;
        }
        return isGoodEmail;
    }

    private boolean isUserNameValid(String userName) {
        if (userName.equals("") || userName.length()<3) {
            if(userName.contains(" ")){
                mEditTextUsernameCreate.setError(getResources().getString(R.string.error_cannot_be_empty));
                return false;
            }
        }
        return true;
    }
}
