package com.codepath.apps.twitterEnhanced.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.adapters.TweetsPagerAdapter;
import com.codepath.apps.twitterEnhanced.clients.TwitterClient;
import com.codepath.apps.twitterEnhanced.fragments.TimelineFragment;
import com.codepath.apps.twitterEnhanced.models.Tweet;
import com.codepath.apps.twitterEnhanced.models.User;
import com.codepath.apps.twitterEnhanced.services.TweetOfflineService;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codepath.apps.twitterEnhanced.properties.properties.OFFLINE_PROFILE_URL;
import static com.codepath.apps.twitterEnhanced.properties.properties.OFFLINE_SCREEN_NAME;
import static com.codepath.apps.twitterEnhanced.properties.properties.OFFLINE__NAME;


public class TimelineActivity extends AppCompatActivity {

    TweetOfflineService tweetofflineservice;
    Tweet composeTweet;
    private TimelineFragment timelinefragment;
    @BindView(R.id.vpTwitter)
    ViewPager vpTwitter;
    @BindView(R.id.tlTwitter)
    TabLayout tlTwitter;
    private TwitterClient client;
    Toolbar toolbar;
    MenuItem miActionProgressItem;


    public TimelineActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        toolbar = (Toolbar) findViewById(R.id.tbBottomTwitter);

        toolbar.inflateMenu(R.menu.menu_twitter);//changed
        //toolbar2 menu items CallBack listener
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if (arg0.getItemId() == R.id.ivToolbarExplore) {

                }
                return false;
            }
        });


        client = new TwitterClient(this);
        ButterKnife.bind(this);
        vpTwitter.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), TimelineActivity.this));
        tlTwitter.setupWithViewPager(vpTwitter);
        Toolbar tbTwitter = (Toolbar) findViewById(R.id.tbTwitter);
        setSupportActionBar(tbTwitter);
        bindChromeIntenttoAdapter();


    }

    private void bindChromeIntenttoAdapter() {
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {

                // Make sure to check whether returned data will be null.
                String titleOfPage = intent.getStringExtra(Intent.EXTRA_SUBJECT);
                String urlOfPage = intent.getStringExtra(Intent.EXTRA_TEXT);
                Uri imageUriOfPage = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                String intentSring = "Title: " + titleOfPage + "\r\n" + "URL: " + urlOfPage;
                if (intent != null) {
                    Tweet t = new Tweet();
                    t.setText(intentSring);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("TWEET_OBJ", t);
                    //  onComposeTweet(bundle);
                }
            }
        }
    }


    public void onUserExploreView(View v) {
        Intent i = new Intent(this, UserExploreActivity.class);
        startActivity(i);
    }

    public void onUserMessageView(View v) {
        Intent i = new Intent(this, DirectMessageActivity.class);
        startActivity(i);
    }

    public void onUserProfileView(View v) {
        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    public Tweet constructOfflineTweetUser() {
        Tweet tweetOffline = new Tweet();
        tweetOffline.setRetweetCount(0l);
        tweetOffline.setFavoriteCount(0l);
        User u = new User();
        u.setScreenName(OFFLINE_SCREEN_NAME);
        u.setName(OFFLINE__NAME);
        u.setProfileImageUrl(OFFLINE_PROFILE_URL);
        u.setVerified(false);
        tweetOffline.setUser(u);
        return tweetOffline;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress

        return super.onPrepareOptionsMenu(menu);

    }


    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

}