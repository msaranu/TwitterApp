package com.codepath.apps.twitterEnhanced.activities;

/**
 * Created by Saranu on 4/1/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.applications.TwitterApplication;
import com.codepath.apps.twitterEnhanced.clients.TwitterClient;
import com.codepath.apps.twitterEnhanced.fragments.UserListFollowersFragment;
import com.codepath.apps.twitterEnhanced.fragments.UserListFollowingFragment;
import com.codepath.apps.twitterEnhanced.models.User;

import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity {
    TwitterClient client;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        client = TwitterApplication.getRestClient();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String screenName = intent.getStringExtra("screen_name");
        String followers = intent.getStringExtra("followers");

        if(followers != null) {
            UserListFollowersFragment userListFollowersFragment = UserListFollowersFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentUserList, userListFollowersFragment);
            ft.commit();
        } else{
            UserListFollowingFragment userListFollowingFragment = UserListFollowingFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentUserList, userListFollowingFragment);
            ft.commit();
        }


    }




}
