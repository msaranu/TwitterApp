package com.codepath.apps.twitterEnhanced.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.adapters.TweetsPagerAdapter;
import com.codepath.apps.twitterEnhanced.clients.TwitterClient;
import com.codepath.apps.twitterEnhanced.fragments.ComposeTweetDialogFragment;
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
    // @BindView(R.id.fabComposeTweet) FloatingActionButton fabComposeTweet;
    Tweet composeTweet;
    private TimelineFragment timelinefragment;
    @BindView(R.id.vpTwitter)
    ViewPager vpTwitter;
    @BindView(R.id.tlTwitter)
    TabLayout tlTwitter;
    private TwitterClient client;
    Toolbar toolbar;

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
                if(arg0.getItemId() == R.id.ivToolbarExplore){
                    int x=0;
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
        setFloatingAction();


   /*     // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(),
                TimelineActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager); */

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
                    onComposeTweet(bundle);
                }
            }
        }
    }



    public void onUserExploreView(View v){
        Intent i = new Intent(this, UserExploreActivity.class);
        startActivity(i);
    }

    public void onUserMessagesView(View v){
        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);
    }
    public void onUserProfileView(View v){
        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);
    }

    private void setFloatingAction() {

    /*    fabComposeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("TWEET_OBJ", new Tweet());
                onComposeTweet(bundle);

            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

    //    LinearLayout menuItemsLayout = (LinearLayout) menu.findItem(R.id.menuItems).getActionView();
      //  ImageView imView1 = (ImageView) menuItemsLayout.findViewById(R.id.ivToolbarUserProfile);
        // MenuItem searchItem = menu.findItem(R.id.action_compose);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    public void onComposeTweet(Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment fdf = ComposeTweetDialogFragment.newInstance();
        fdf.setArguments(bundle);
        fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        fdf.show(fm, "FRAGMENT_MODAL_COMPOSE");
    }


    public  Tweet constructOfflineTweetUser() {
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
}
