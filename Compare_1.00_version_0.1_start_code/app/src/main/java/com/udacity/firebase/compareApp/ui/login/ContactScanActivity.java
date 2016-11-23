package com.udacity.firebase.compareApp.ui.login;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.UserRecoverableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pratik.compareApp.R;
import com.udacity.firebase.compareApp.model.User;
import com.udacity.firebase.compareApp.ui.MainActivity;
import com.udacity.firebase.compareApp.utils.Constants;

import java.util.ArrayList;

public class ContactScanActivity extends AppCompatActivity {

    DatabaseReference usersRef;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_scan);

        mFirebaseAuth = FirebaseAuth.getInstance();
        initializeScreen();
        FirebaseUser currentUser =mFirebaseAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.getEmail();
        }

        usersRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS);
        /*
        * Till the contact scan function not working redirect to the mainActivity*/
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        ContentResolver cr = getApplicationContext().getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {
            ArrayList<String> alContacts = new ArrayList<String>();
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Log.e("details ", "name - "+contactName+",no - "+contactNumber);
                        alContacts.add(contactNumber);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
        }

        /*DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS);
        Query userRefSearchByUsername = UsersRef.orderByChild("username").equalTo("Sofiyan");
        userRefSearchByUsername.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User value = dataSnapshot.getValue(User.class);
                } else {
                    Log.e("details ", "Username not exist it's available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    public void initializeScreen(){

    }
}
