package com.udacity.firebase.compareApp.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pratik.compareApp.R;
import com.udacity.firebase.compareApp.model.Post;
import com.udacity.firebase.compareApp.model.PostImage;
import com.udacity.firebase.compareApp.ui.addPost.AddPostFragmentNew;
import com.udacity.firebase.compareApp.utils.Constants;
import com.udacity.firebase.compareApp.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SetDisplayPictureActivity extends AppCompatActivity {

    public String userEmail, encodedUserEmail;

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference usersRef;
    public FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    ImageButton imageButtonaddDisplayPicture;
    ImageView imageViewShowDisplayPicture;
    Button buttonUploadDisplayPic;
    public ProgressDialog progressDialog;

    public int screen_width = 0, screen_height = 0;
    public static final int GALLERY_INTENT_1 = 1;

    Bitmap bitmap1 = null;
    public static Uri uri1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_display_picture);

        initializeScreen();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();


        if(currentUser != null){
            userEmail = currentUser.getEmail();
            encodedUserEmail = Utils.encodeEmail(userEmail);
        }

        imageButtonaddDisplayPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("return-data", true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_1);
            }
        });

        buttonUploadDisplayPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUploadDisplayPic.setVisibility(View.INVISIBLE);
                upload_images();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT_1 && resultCode == RESULT_OK && data != null){
            uri1 = data.getData();
            imageViewShowDisplayPicture.setImageURI(null);
            imageViewShowDisplayPicture.setImageURI(uri1);
            bitmap1 = ((BitmapDrawable)imageViewShowDisplayPicture.getDrawable()).getBitmap();
        }
    }

    public void upload_images() {
        final String imageName = encodedUserEmail + "_img_DP" + ".jpg";
        StorageReference photoRef = mStorageRef.child(Constants.FIREBASE_LOCATION_STORAGE_DISPLAYPICTURE).child(encodedUserEmail).child(imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                firebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_USERS).
                        child(encodedUserEmail).child(Constants.FIREBASE_PROPERTY_USER_SET_DISPLAY_PICTURE).setValue(Boolean.TRUE);
                Intent intent = new Intent(getApplication(), AddPostFragmentNew.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


    public void initializeScreen(){

//        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
//        screen_height = (int) (displayMetrics.heightPixels / displayMetrics.density);
//        screen_width = (int) (displayMetrics.widthPixels / displayMetrics.density);

        imageButtonaddDisplayPicture = (ImageButton) findViewById(R.id.image_button_set_display_pic);

        imageViewShowDisplayPicture= (ImageView) findViewById(R.id.image_view_show_display_pic);

        buttonUploadDisplayPic = (Button) findViewById(R.id.button_done_with_display_picture);

        progressDialog = new ProgressDialog(getApplicationContext());

    }

}
