package com.udacity.firebase.compareApp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.pratik.compareApp.R;
import com.udacity.firebase.compareApp.ui.postsList.PostListsFragment;
import com.udacity.firebase.compareApp.ui.addPost.AddPostFragment;
import com.udacity.firebase.compareApp.ui.addPost.AddPostFragmentNew;
import com.udacity.firebase.compareApp.ui.searchUser.SearchUserFragment;


public class MainActivity extends BaseActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    SectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();
    }


    /**
     * Override onOptionsItemSelected to use main_menu instead of BaseActivity menu
     *
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Override onOptionsItemSelected to add action_settings only to the MainActivity
     *
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        /**
         * Create SectionPagerAdapter, set it as adapter to viewPager with setOffscreenPageLimit(2)
         **/
        adapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        /**
         * Setup the mTabLayout with view pager
         */
    }

    /**
     * SectionPagerAdapter class that extends FragmentStatePagerAdapter to save fragments state
     */
    public class SectionPagerAdapter extends FragmentStatePagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Use positions (0 and 1) to find and instantiate fragments with newInstance()
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            /**
             * Set fragment to different fragments depending on position in ViewPager
             */
            switch (position) {
                case 0:
                    fragment = AddPostFragment.newInstance();
                    break;
                case 1:
                    fragment = AddPostFragmentNew.newInstance(adapter);
                    break;
                case 2:
                    fragment = PostListsFragment.newInstance();
                    break;
                case 3:
                    fragment = SearchUserFragment.newInstance();
                    break;
                default:
                    fragment = PostListsFragment.newInstance();
                    break;
            }

            return fragment;
        }


        @Override
        public int getCount() {
            return 3;
        }

        /**
         * Set string resources as titles for each fragment by it's position
         *
         * @param position
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.pager_title_add_post);
                case 1:
                    return getString(R.string.pager_title_post_lists);
                case 2:
                    return getString(R.string.pager_title_search_users);
                default:
                    return getString(R.string.pager_title_post_lists);
            }
        }
    }
}
