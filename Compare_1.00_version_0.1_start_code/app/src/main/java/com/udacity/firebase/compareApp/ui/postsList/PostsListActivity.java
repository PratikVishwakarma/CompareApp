package com.udacity.firebase.compareApp.ui.postsList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pratik.compareApp.R;
import com.udacity.firebase.compareApp.model.Post;
import com.udacity.firebase.compareApp.ui.BaseActivity;
import com.udacity.firebase.compareApp.utils.Constants;
import com.udacity.firebase.compareApp.utils.Utils;

public class PostsListActivity extends BaseActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef, postsRef, userPostLocationRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;

    private String userEmail, encodedUserEmail;

    private Post posts;
    private PostsListAdapter postsListAdapter;

    private ListView listViewPostsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null){
            userEmail = currentUser.getEmail();
            encodedUserEmail = Utils.encodeEmail(userEmail);
        }

        usersRef = firebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_USERS);
        postsRef = firebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_POST_LISTS);
        userPostLocationRef = postsRef.child(encodedUserEmail);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        /**
         * Setup the adapter
         */
        postsListAdapter = new PostsListAdapter(PostsListActivity.this, Post.class,
                R.layout.single_post_item, userPostLocationRef, encodedUserEmail);
        /* Create ActiveListItemAdapter and set to listView */
        listViewPostsList.setAdapter(postsListAdapter);


        userPostLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Post currentPost = postSnapshot.getValue(Post.class);
                        posts = currentPost;
                        postsListAdapter.setPostsList(currentPost);
                    }

                }else{
        Toast.makeText(getApplicationContext(),"POsts not exists ",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initializeScreen() {
        listViewPostsList = (ListView) findViewById(R.id.list_view_posts_list);
        Toast.makeText(getApplicationContext(),"POstsListActivity called.. ",Toast.LENGTH_SHORT).show();
    }
}
