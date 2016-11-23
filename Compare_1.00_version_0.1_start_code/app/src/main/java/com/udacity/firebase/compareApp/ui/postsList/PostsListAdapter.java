package com.udacity.firebase.compareApp.ui.postsList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pratik.compareApp.R;
import com.udacity.firebase.compareApp.model.Post;
import com.udacity.firebase.compareApp.model.User;
import com.udacity.firebase.compareApp.utils.Constants;

/**
 * Created by prati on 14-Nov-16.
 */

public class PostsListAdapter extends FirebaseListAdapter<Post>{
    private String userEmail, encodedEmail;
    private final Class<Post> mModelClass;
    protected int mLayout;
    private Post postlist;
    protected Activity mActivity;

    public PostsListAdapter(Activity activity, Class<Post> modelClass, int modelLayout,
                            Query ref, String encodedEmail) {
        super(activity,modelClass,modelLayout,ref);

        this.encodedEmail =encodedEmail;
        mModelClass = modelClass;
        mLayout = modelLayout;
        mActivity = activity;
    }

    /**
     * Public method that is used to pass shoppingList object when it is loaded in ValueEventListener
     */
    public void setPostsList(Post post) {
        this.postlist = post;
        this.notifyDataSetChanged();
    }

    @Override
    protected void populateView(View view, Post post, int i) {
        final TextView textViewUserName = (TextView) view.findViewById(R.id.item_post_text_view_userName);
        final TextView textViewContent = (TextView) view.findViewById(R.id.item_post_text_view_content);

        textViewContent.setText(post.getContent());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersLoc = firebaseDatabase.getReference().child(Constants.FIREBASE_LOCATION_USERS).child(encodedEmail);
        usersLoc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    textViewUserName.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
