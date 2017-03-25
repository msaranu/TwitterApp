package com.codepath.apps.twitterapp.activities;

import android.os.Bundle;
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
import com.codepath.apps.twitterapp.fragments.ComposeDialogFragment;
import com.codepath.apps.twitterapp.models.Tweet;
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


public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeTweetDialogListener{
    private TwitterClient client;

    public TimelineActivity(){}

    ArrayList<Tweet> tweets;
    TweetsArrayAdapter adapter;
    @BindView(R.id.rvTweets) RecyclerView rvTweets;
    public static final int FIRST_PAGE=0;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    TweetOfflineService tweetofflineservice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        Toolbar tbTwitter = (Toolbar) findViewById(R.id.tbTwitter);
        setSupportActionBar(tbTwitter);
        client = TwitterApplication.getRestClient();
        bindDataToAdapter(this);
        setRecyleViewLayout();
        setSwipeRefreshLayout();

    }

    private void setSwipeRefreshLayout() {

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
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
        long id=0;
        boolean scroll = false; //set it later
        if (page != 0) {
            id = tweets.get(tweets.size() - 1).getId() - 1;
            scroll=true;
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
                    if(errorResponse.toString().contains("Too Many Requests")||errorResponse.toString().contains("Rate limit exceeded")) {
                        Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS THIS SESSION",
                                Toast.LENGTH_LONG).show();
                    }else  Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS ??",
                            Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                    if(errorResponse.toString().contains("Too Many Requests")) {
                        Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS THIS SESSION",
                                Toast.LENGTH_LONG).show();
                    }else  Toast.makeText(TimelineActivity.this, "TOO MANY REQUESTS ??",
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
                onComposeTweet(bundle);
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

        boolean offline = true;
        if (!offline && NetworkUtil.isInternetAvailable(getApplicationContext()) && NetworkUtil.isOnline()) {
            populateTimeline(page);
        } else {
            tweets.addAll(TweetOfflineService.retrieveTweetsOffline());
            adapter.notifyDataSetChanged();
            Toast.makeText(TimelineActivity.this, "Retreiving Tweets Offline from: ",
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
        if (id == 10) {
            Bundle bundle = new Bundle();
            onComposeTweet(bundle);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onComposeTweet(Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment fdf =  ComposeDialogFragment.newInstance();
         fdf.setArguments(bundle);
        fdf.setStyle( DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme );
        fdf.show(fm, "FRAGMENT_MODAL_COMPOSE");
    }

    @Override
    public void onFinishComposeTweetDialog(Tweet tweet) {
      //  fragFilterSettings = fs;
       // getArticles(FIRST_PAGE);
    }


}
