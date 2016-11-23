package com.udacity.firebase.compareApp.ui.addPost;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pratik.compareApp.R;
import com.squareup.picasso.Picasso;
import com.udacity.firebase.compareApp.model.Home;
import com.udacity.firebase.compareApp.model.Post;
import com.udacity.firebase.compareApp.model.PostImage;
import com.udacity.firebase.compareApp.model.User;
import com.udacity.firebase.compareApp.ui.MainActivity;
import com.udacity.firebase.compareApp.ui.postsList.PostListsFragment;
import com.udacity.firebase.compareApp.utils.Constants;
import com.udacity.firebase.compareApp.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddPostFragmentNew extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // Write a message to the database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;

    public static MainActivity.SectionPagerAdapter adapterViewPager;


    private static String userEmail,encodedUserEmail, userName, postid = null, content_post = " ";

    private User user;

    public View rootView;
    RelativeLayout relativeLayout_image_show;
    ImageView showImageView1, showImageView2, showImageView3, showImageView4;
    ImageButton addImagebtn1, addImagebtn2, addImagebtn3, addImagebtn4;
    Button button_done_post;
    EditText edit_text_post_content;
    ViewPager vp;


    public ProgressDialog progressDialog;

    public int screen_width = 0, screen_height = 0, i=0, totalNoOfImage=0;
    public static final int GALLERY_INTENT_1 = 1,GALLERY_INTENT_2 = 2, GALLERY_INTENT_3 = 3,GALLERY_INTENT_4 = 4;

    Bitmap bitmap1 = null, bitmap2 = null, bitmap3 = null, bitmap4 = null;
    ArrayList<String> imageNameList = new ArrayList<String>();
    public static Uri uri1 = null, uri2 = null, uri3 = null, uri4 = null;

    public boolean uploadAllImagesStatus = Boolean.FALSE;

    public AddPostFragmentNew() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddPostFragmentNew newInstance(MainActivity.SectionPagerAdapter adapter) {
        AddPostFragmentNew fragment = new AddPostFragmentNew();
        Bundle args = new Bundle();
        adapterViewPager = adapter;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_add_post_fragment_new, container, false);
        relativeLayout_image_show = (RelativeLayout) rootView.findViewById(R.id.add_post_add_view_show_image);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getReferenceFromUrl("gs://compare-28d74.appspot.com");

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            userEmail = currentUser.getEmail();
            encodedUserEmail = Utils.encodeEmail(userEmail);
        }

        initializeScreen(rootView);
        addImagebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("return-data", true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_1);
            }
        });
        addImagebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("return-data", true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_2);
            }
        });
        addImagebtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("return-data", true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_3);
            }
        });
        addImagebtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("return-data", true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_4);
            }
        });

        button_done_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_done_post.setVisibility(View.INVISIBLE);
                content_post = edit_text_post_content.getText().toString();
                createPostId();
            }
        });
        if (getArguments() != null) {
        }
        return rootView;
    }

    public void createPostId(){
        final Query query = firebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_USERS).child(Utils.encodeEmail(userEmail));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    long time= System.currentTimeMillis();
                    user = dataSnapshot.getValue(User.class);
                    Log.e("Inside getUser found", user.getEmail()+Constants.STRING_POSTID_DIFFERENTIATOR+time);
                    postid = Utils.encodeEmail(user.getEmail())+Constants.STRING_POSTID_DIFFERENTIATOR+time;
                    String imagename = Utils.encodeUsername(user.getUsername())+time;
                    upload_images(postid, imagename);
                }else{
                    user = null;
                    Log.e("not found", user.getEmail());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT_1 && resultCode == RESULT_OK && data != null){
            uri1 = data.getData();
            showImageView1.setImageURI(null);
            showImageView1.setImageURI(uri1);
//            Picasso.with(getContext())
//                    .load(uri1)
//                    .resize(200, 200)
//                    .into(showImageView1);
            addImagebtn1.setImageURI(null);
            addImagebtn1.setImageURI(uri1);
            bitmap1 = ((BitmapDrawable)showImageView1.getDrawable()).getBitmap();
        } else if(requestCode == GALLERY_INTENT_2 && resultCode == RESULT_OK && data != null){
            uri2 = data.getData();
            showImageView2.setImageURI(null);
            showImageView2.setImageURI(uri2);
//            Picasso.with(getContext())
//                    .load(uri2)
//                    .resize(200,200)
//                    .into(showImageView2);
            addImagebtn2.setImageURI(null);
            addImagebtn2.setImageURI(uri2);
            bitmap2 = ((BitmapDrawable)showImageView2.getDrawable()).getBitmap();
        } else if(requestCode == GALLERY_INTENT_3 && resultCode == RESULT_OK && data != null){
            uri3 = data.getData();
            showImageView3.setImageURI(null);
            showImageView3.setImageURI(uri3);
//            Picasso.with(getContext())
//                    .load(uri3)
//                    .resize(200,200)
//                    .into(showImageView3);

            addImagebtn3.setImageURI(null);
            addImagebtn3.setImageURI(uri3);
            bitmap3 = ((BitmapDrawable)showImageView3.getDrawable()).getBitmap();
//            try {
//                bitmap3 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri3);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } else if(requestCode == GALLERY_INTENT_4 && resultCode == RESULT_OK && data != null){
            uri4 = data.getData();
            showImageView4.setImageURI(null);
            showImageView4.setImageURI(uri4);
//            Picasso.with(getContext())
//                    .load(uri4)
//                    .resize(200,200)
//                    .into(showImageView4);
            addImagebtn4.setImageURI(null);
            addImagebtn4.setImageURI(uri4);
            bitmap4 = ((BitmapDrawable)showImageView4.getDrawable()).getBitmap();
        }
    }

    public void upload_images(String getpostid, String imageNameAd) {
        postid = getpostid;
        final Uri[] auri = {uri1, uri2, uri3, uri4};
        final Bitmap[] abitmaps = {bitmap1, bitmap2, bitmap3, bitmap4};
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        for (i = 0; i < 4; i++) {
            if (auri[i] != null) {
                final String imageName = imageNameAd + "_img_" + i + ".jpg";
                final String postImageId = imageNameAd + "_img_" + i;
                StorageReference photoRef = mStorageRef.child(Constants.FIREBASE_LOCATION_STORAGE_POSTIMAGE).child(encodedUserEmail).child(imageName);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                abitmaps[i].compress(Bitmap.CompressFormat.JPEG, 55, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = photoRef.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(i == 4) uploadAllImagesStatus = Boolean.TRUE;
                        imageNameList.add(imageName);
                        totalNoOfImage++;
                        PostImage postImage = new PostImage(encodedUserEmail, postid, imageName, 0);
                        firebaseDatabase.getReference().child(PostImage.TABLE_NAME).child(Utils.encodeEmail(userEmail)).child(postid).child(postImageId).setValue(postImage);
                        Toast.makeText(getContext(), "Upload complete....", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
            if(uploadAllImagesStatus = Boolean.TRUE){
                Log.e("Total no of Images upload ", totalNoOfImage+"  "+ imageNameList.size()+" ");
                Toast.makeText(getContext(), "Update the Post....", Toast.LENGTH_SHORT).show();
                DateFormat datef = new SimpleDateFormat(Constants.FORMATE_ADD_POST_DATE);
                String currentdate = datef.format(Calendar.getInstance().getTime());
                DateFormat timef = new SimpleDateFormat(Constants.FORMATE_ADD_POST_TIME);
                String currenttime = timef.format(Calendar.getInstance().getTime());
                Post newPost = new Post(encodedUserEmail, postid, content_post, currentdate, currenttime, totalNoOfImage, 0);
                Task<Void> voidTask = firebaseDatabase.getReference().child(Post.TABLE_NAME).child(encodedUserEmail).child(postid).setValue(newPost);
                if(voidTask.isSuccessful()){
                    Log.e("Total no of Images upload ", "Task of post successful");
                }
                Home home = new Home(postid, userEmail, Constants.TYPE_POST_TYPE_POST_BY_YOU, currenttime, currentdate);
                Task<Void> voidTaskHome = firebaseDatabase.getReference().child(Home.TABLE_NAME).child(encodedUserEmail).setValue(home);
//                Intent intent = new Intent(getContext(), PostListsFragment.class);
//                startActivity(intent);
//                ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
//                viewPager.setCurrentItem(2);

//                vp.setOffscreenPageLimit(3);
//                vp.setAdapter(adapterViewPager);
//                vp.setCurrentItem(2);

//                FragmentActivity fragmentActivity = new FragmentActivity();
//                Intent intent = new Intent(getContext(), PostListsFragment.class);
//                fragmentActivity.startActivity(intent);
            }
        }

        progressDialog.dismiss();
    }



    public void initializeScreen(View rootView){

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        screen_height = (int) (displayMetrics.heightPixels / displayMetrics.density);
        screen_width = (int) (displayMetrics.widthPixels / displayMetrics.density);

        addImagebtn1 = (ImageButton) rootView.findViewById(R.id.add_post_add_image1);
        addImagebtn2 = (ImageButton) rootView.findViewById(R.id.add_post_add_image2);
        addImagebtn3 = (ImageButton) rootView.findViewById(R.id.add_post_add_image3);
        addImagebtn4 = (ImageButton) rootView.findViewById(R.id.add_post_add_image4);

        showImageView1 = (ImageView) rootView.findViewById(R.id.add_post_image1_show);
        showImageView2 = (ImageView) rootView.findViewById(R.id.add_post_image2_show);
        showImageView3 = (ImageView) rootView.findViewById(R.id.add_post_image3_show);
        showImageView4 = (ImageView) rootView.findViewById(R.id.add_post_image4_show);

        button_done_post = (Button) rootView.findViewById(R.id.add_post_done);

        edit_text_post_content = (EditText) rootView.findViewById(R.id.edit_text_post_content);

        vp = (ViewPager) rootView.findViewById(R.id.pager);

        progressDialog = new ProgressDialog(getContext());

    }

    // TODO: Rename method, update argument and hook method into UI event
}

