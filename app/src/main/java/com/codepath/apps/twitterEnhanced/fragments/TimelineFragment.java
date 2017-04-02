package com.codepath.apps.twitterEnhanced.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.activities.TimelineActivity;
import com.codepath.apps.twitterEnhanced.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterEnhanced.applications.TwitterApplication;
import com.codepath.apps.twitterEnhanced.clients.TwitterClient;
import com.codepath.apps.twitterEnhanced.decorators.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterEnhanced.decorators.ItemClickSupport;
import com.codepath.apps.twitterEnhanced.models.Tweet;
import com.codepath.apps.twitterEnhanced.models.User;
import com.codepath.apps.twitterEnhanced.network.NetworkUtil;
import com.codepath.apps.twitterEnhanced.services.TweetOfflineService;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.twitterEnhanced.properties.properties.OFFLINE_PROFILE_URL;
import static com.codepath.apps.twitterEnhanced.properties.properties.OFFLINE_SCREEN_NAME;
import static com.codepath.apps.twitterEnhanced.properties.properties.OFFLINE__NAME;

/**
 * Created by Saranu on 3/29/17.
 */

public abstract class TimelineFragment extends android.support.v4.app.Fragment
        implements TweetDetailDialogFragment.ComposeTweetDialogListener,
        ComposeTweetDialogFragment.ComposeTweetModalDialogListener {

    ArrayList<Tweet> tweets;
    TweetsArrayAdapter adapter;
    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;
    public static final int FIRST_PAGE = 0;
    private EndlessRecyclerViewScrollListener scrollListener;
    @BindView(R.id.swipeContainer) public SwipeRefreshLayout swipeContainer;
    TweetOfflineService tweetofflineservice;
 //   @BindView(R.id.fabComposeTweet)
  //  FloatingActionButton fabComposeTweet;
    Tweet composeTweet;
    long cursorID = -1;
    TwitterClient client;
    @BindView(R.id.fabComposeTweet)
    FloatingActionButton fabComposeTweet;



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();


    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View v = inflater.inflate(R.layout.fragment_timeline, parent, false);
        ButterKnife.bind(this,v);
       setFloatingAction();

       //start Bind date to adapter
        tweets = new ArrayList<>();
        adapter = new TweetsArrayAdapter(getActivity(), tweets);
        rvTweets.setAdapter(adapter);
        //end bind date to adapter

        setRecyleViewLayout();
    //   swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
       setSwipeRefreshLayout(v);
        return v;
    }


    private void setSwipeRefreshLayout(View v) {

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

    private void callTwitterAPI(int page) {

        boolean offline = false;
        if (!offline && NetworkUtil.isInternetAvailable(getContext()) && NetworkUtil.isOnline()) {
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
            Toast.makeText(getContext(), "Retreiving Tweets Offline from: ",
                    Toast.LENGTH_LONG).show();
        }

        swipeContainer.setRefreshing(false);

    }

    protected abstract void populateTimeline(int page);


    public void getTweets(int page) {
        {
            if (page == FIRST_PAGE) {
                tweets.clear();
                scrollListener.resetState();
            }
            callTwitterAPI(page);


        }
    }
    private void setRecyleViewLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(cursorID !=0) {
                    getTweets(page);
                }
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
        adapter = new TweetsArrayAdapter(getActivity(), tweets);
        // Attach the adapter to the recyclerview to populate items
        rvTweets.setAdapter(adapter);


    }


    public void onReplyTweet(Bundle bundle) {

        FragmentManager fm = getFragmentManager();
        TweetDetailDialogFragment fdf = TweetDetailDialogFragment.newInstance();
        fdf.setArguments(bundle);
        fdf.setTargetFragment(TimelineFragment.this, 300);
        fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        fdf.show(fm, "FRAGMENT_MODAL_COMPOSE");
    }


    @Override
    public void onFinishComposeTweetDialog(String tweetText, Tweet tweet) {
        composeTweet( tweetText, tweet);
    }


    @Override
    public void onFinishComposeModalTweetDialog(String tweetText, Tweet tweet) {
        composeTweet( tweetText,  tweet);
    }

    public void composeTweet(String tweetText, Tweet tweet){
        composeTweet = constructOfflineTweetUser();
        composeTweet.setText(tweetText);
        client = TwitterApplication.getRestClient();


        client.postStatusUpdate(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                if (composeTweet.getId() <= 0) {
                    composeTweet.setId(-1l);
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
                    Toast.makeText(getContext(), "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(), "TOO MANY REQUESTS 2 ??",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests")) {
                    Toast.makeText(getContext(), "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(), "TOO MANY REQUESTS 3??",
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

    public void onComposeTweet(Bundle bundle) {
        FragmentManager fm = getFragmentManager();
        ComposeTweetDialogFragment fdf = ComposeTweetDialogFragment.newInstance();
        fdf.setArguments(bundle);
        fdf.setTargetFragment(TimelineFragment.this, 300);
        fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        fdf.show(fm, "FRAGMENT_MODAL_COMPOSE");

    }


}

