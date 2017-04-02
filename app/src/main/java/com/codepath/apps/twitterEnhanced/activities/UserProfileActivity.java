package com.codepath.apps.twitterEnhanced.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.adapters.UserProfilePagerAdapter;
import com.codepath.apps.twitterEnhanced.applications.TwitterApplication;
import com.codepath.apps.twitterEnhanced.clients.TwitterClient;
import com.codepath.apps.twitterEnhanced.fragments.UserHeaderFragment;
import com.codepath.apps.twitterEnhanced.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    @BindView(R.id.vpTwitter)
    ViewPager vpTwitter;
    @BindView(R.id.tlTwitter)
    TabLayout tlTwitter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String screenName = getIntent().getStringExtra("screen_name");

        if(savedInstanceState == null) {
            UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentHeader, userHeaderFragment);
            ft.commit();
        }
        setContentView(R.layout.activity_user_profile);
        client = TwitterApplication.getRestClient();
        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.tbBottomTwitter);

        toolbar.inflateMenu(R.menu.menu_twitter);//changed
        //toolbar2 menu items CallBack listener
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if(arg0.getItemId() == R.id.ivToolbarUserProfile){

                }
                return false;
            }
        });


        vpTwitter.setAdapter(new UserProfilePagerAdapter(getSupportFragmentManager(), UserProfileActivity.this, screenName));
        tlTwitter.setupWithViewPager(vpTwitter);



    }


}
