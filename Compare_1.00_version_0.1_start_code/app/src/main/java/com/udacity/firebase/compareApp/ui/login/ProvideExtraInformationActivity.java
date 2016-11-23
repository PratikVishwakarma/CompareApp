package com.udacity.firebase.compareApp.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.api.BooleanResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pratik.compareApp.R;
import com.udacity.firebase.compareApp.utils.Constants;
import com.udacity.firebase.compareApp.utils.Utils;

public class ProvideExtraInformationActivity extends AppCompatActivity {

    public String userEmail, userGender = "Female";
    public String userMobileNo;

    public RadioGroup radioGroupGender;
    public RadioButton radioButtonMale, radioButtonFemale;
    public Button doneButton;
    public EditText mobileNo;

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference usersRef;
    public FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_extra_information);
        initializeScreen();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            userEmail = currentUser.getEmail();
        }
    }

    public void onDonePressed(View view){
        usersRef = firebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_USERS);
        usersRef.child(Utils.encodeEmail(userEmail)).child(Constants.FIREBASE_PROPERTY_USER_GENDER).setValue(userGender);
        userMobileNo = mobileNo.getText().toString();
        if(userMobileNo != null || userMobileNo != ""){
            usersRef.child(Utils.encodeEmail(userEmail)).child(Constants.FIREBASE_PROPERTY_USER_MOBILE_NO).setValue(userMobileNo);
        }
        usersRef.child(Utils.encodeEmail(userEmail)).child(Constants.FIREBASE_PROPERTY_USER_HAS_PROVIDED_EXTRA_IMFORMATION).setValue(Boolean.TRUE);
        Intent intent = new Intent(getApplicationContext(), SetDisplayPictureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void initializeScreen(){

        radioGroupGender = (RadioGroup) findViewById(R.id.radio_group_gender);
        radioButtonFemale = (RadioButton) findViewById(R.id.radio_btn_female);
        radioButtonMale= (RadioButton) findViewById(R.id.radio_btn_male);
        mobileNo = (EditText) findViewById(R.id.edit_text_mobile_no);
        doneButton = (Button) findViewById(R.id.button_done_with_extra_information);
        doneButton.setEnabled(Boolean.FALSE);
    }

    public void onRadioGroupClicked(View view) {
        int viewId = view.getId();
        switch(viewId) {
            case R.id.radio_btn_female:
                radioButtonMale.setChecked(false);
                radioButtonFemale.setChecked(true);
                doneButton.setEnabled(Boolean.TRUE);
                userGender = "Female";
                break;
            case R.id.radio_btn_male:
                radioButtonFemale.setChecked(false);
                radioButtonMale.setChecked(true);
                doneButton.setEnabled(Boolean.TRUE);
                userGender = "Male";
                break;
        }
        Log.e("details ", "Username gender is "+userGender);
    }
}
