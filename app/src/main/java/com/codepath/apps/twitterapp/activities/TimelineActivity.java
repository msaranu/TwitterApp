package com.codepath.apps.twitterapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.applications.TwitterApplication;
import com.codepath.apps.twitterapp.clients.TwitterClient;
import com.codepath.apps.twitterapp.decorators.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterapp.decorators.ItemClickSupport;
import com.codepath.apps.twitterapp.fragments.ComposeTweetDialogFragment;
import com.codepath.apps.twitterapp.fragments.TweetDetailDialogFragment;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.codepath.apps.twitterapp.network.NetworkUtil;
import com.codepath.apps.twitterapp.services.TweetOfflineService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.twitterapp.properties.properties.OFFLINE_PROFILE_URL;
import static com.codepath.apps.twitterapp.properties.properties.OFFLINE_SCREEN_NAME;
import static com.codepath.apps.twitterapp.properties.properties.OFFLINE__NAME;


public class TimelineActivity extends AppCompatActivity implements TweetDetailDialogFragment.ComposeTweetDialogListener {
    private TwitterClient client;

    public TimelineActivity() {
    }

    ArrayList<Tweet> tweets;
    TweetsArrayAdapter adapter;
    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;
    public static final int FIRST_PAGE = 0;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    TweetOfflineService tweetofflineservice;
    @BindView(R.id.fabComposeTweet)
    FloatingActionButton fabComposeTweet;
    Tweet composeTweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        Toolbar tbTwitter = (Toolbar) findViewById(R.id.tbTwitter);
        setSupportActionBar(tbTwitter);
        client = TwitterApplication.getRestClient();
        bindDataToAdapter(this);
        bindChromeIntenttoAdapter();
        setFloatingAction();
        setRecyleViewLayout();
        setSwipeRefreshLayout();


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
                String intentSring = "Title: " + titleOfPage + "\r\n" + "URL: " + urlOfPage ;
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

    private void setFloatingAction() {

        fabComposeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("TWEET_OBJ", new Tweet());
                onComposeTweet(bundle);

            }
        });
    }

    private void setSwipeRefreshLayout() {

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                  getTweets(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void populateTimeline(int page) {
        long id = 0;
        boolean scroll = false; //set it later
        if (page != 0) {
            id = tweets.get(tweets.size() - 1).getId() - 1;
            scroll = true;
        }
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
                // register type adapters here, specify field naming policy, etc.
                Gson gson = gsonBuilder.create();              //  Tweet[] tarray = gson.fromJson(response.toString(),Tweet[].class);
                List<Tweet> tweetList = gson.fromJson(response.toString(), new TypeToken<List<Tweet>>() {
                }.getType());
                if (tweetList == null || tweetList.isEmpty() || tweetList.size() == 0) {
                    Toast.makeText(TimelineActivity.this, "NO TWEETS",
                            Toast.LENGTH_LONG).show();
                } else {
                    tweetofflineservice.deleteTweetsoffline();
                    tweetofflineservice.saveTweetsOffline(tweetList);
                    tweets.addAll(tweetList);
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);

                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests") || errorResponse.toString().contains("Rate limit exceeded")) {
                    Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS ??",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests")) {
                    Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS ??",
                        Toast.LENGTH_LONG).show();

            }

        }, id, scroll);


    }

    private void setRecyleViewLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getTweets(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);
        getTweets(FIRST_PAGE);

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("TWEET_OBJ", tweets.get(position));
                onReplyTweet(bundle);
            }
        });

    }

    private void bindDataToAdapter(TimelineActivity timelineActivity) {
        // fragFilterSettings = new FilterSettings();
        // Create adapter passing in the sample user data
        tweets = new ArrayList<>();
        adapter = new TweetsArrayAdapter(this, tweets);
        // Attach the adapter to the recyclerview to populate items
        rvTweets.setAdapter(adapter);


    }

    public void getTweets(int page) {
        {
            if (page == FIRST_PAGE) {
                tweets.clear();
                scrollListener.resetState();
            }
            callTwitterAPI(page);

        }
    }

    private void callTwitterAPI(int page) {

        //to test offline DB capture without bringing down internet connection everytime
        boolean offline = true;

        if (!offline && NetworkUtil.isInternetAvailable(getApplicationContext()) && NetworkUtil.isOnline()) {
            populateTimeline(page);
        } else {
            tweets.addAll(TweetOfflineService.retrieveTweetsOffline());

            Handler handler = new Handler();

            final Runnable r = new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();

                }
            };

            handler.post(r);
            Toast.makeText(TimelineActivity.this, "Retreiving Tweets Offline",
                    Toast.LENGTH_LONG).show();
            //     swipeContainer.setRefreshing(false);

            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
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


    public void onReplyTweet(Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        TweetDetailDialogFragment fdf = TweetDetailDialogFragment.newInstance();
        fdf.setArguments(bundle);
        fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        fdf.show(fm, "FRAGMENT_MODAL_COMPOSE");
    }


    @Override
    public void onFinishComposeTweetDialog(String tweetText, Tweet tweet) {
        composeTweet = constructOfflineTweetUser();
        composeTweet.setText(tweetText);


        client.postStatusUpdate(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                if (composeTweet.getId() <= 0) {

                    tweets.add(0, composeTweet);
                    adapter.notifyDataSetChanged();
                    rvTweets.scrollToPosition(0);
                    swipeContainer.setRefreshing(false);
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                if (composeTweet.getId() <= 0) {
                    tweets.add(0, composeTweet);
                    adapter.notifyDataSetChanged();
                    rvTweets.scrollToPosition(0);
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests") || errorResponse.toString().contains("Rate limit exceeded")) {
                    Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS 2 ??",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests")) {
                    Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS 3??",
                        Toast.LENGTH_LONG).show();

            }

        }, tweetText, tweet.getId());

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
