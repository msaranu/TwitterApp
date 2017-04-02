package com.codepath.apps.twitterEnhanced.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitterEnhanced.applications.TwitterApplication;
import com.codepath.apps.twitterEnhanced.clients.TwitterClient;
import com.codepath.apps.twitterEnhanced.models.Statuses;
import com.codepath.apps.twitterEnhanced.models.Tweet;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Saranu on 3/30/17.
 */

public class SearchTimelineFragment extends TimelineFragment {
    private TwitterClient client;
    String searchQuery;
    Statuses statuses;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        searchQuery=  getArguments().getString("search_query");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return super.onCreateView(inflater, parent, savedInstanceState);
    }



    public static SearchTimelineFragment newInstance(String searchString) {
        SearchTimelineFragment searchTimelineFragment = new SearchTimelineFragment();
        Bundle args = new Bundle();
        args.putString("search_query", searchString);
        searchTimelineFragment.setArguments(args);
        return searchTimelineFragment;
    }

    @Override
    protected void populateTimeline(int page) {
        long id = 0;
        boolean scroll = false; //set it later
        if (page != 0) {
            id = tweets.get(tweets.size() - 1).getId() - 1;
            scroll = true;
        }
        client.getSearchTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                  Log.d("DEBUG", response.toString());
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                statuses = gson.fromJson(response.toString(), Statuses.class);
                List<Tweet> tweetList = statuses.getStatuses();
                if (tweetList == null || tweetList.isEmpty() || tweetList.size() == 0) {
                    Toast.makeText(getContext(), "NO TWEET MENTIONS",
                            Toast.LENGTH_LONG).show();
                } else {
                    tweetofflineservice.deleteTweetsoffline();
                    tweetofflineservice.saveTweetsOffline(tweetList);
                    tweets.addAll(tweetList);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests") || errorResponse.toString().contains("Rate limit exceeded")) {
                    Toast.makeText(getContext(), "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(), "TOO MANY REQUESTS ??",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests")) {
                    Toast.makeText(getContext(), "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(), "TOO MANY REQUESTS ??",
                        Toast.LENGTH_LONG).show();

            }

        }, id, scroll, searchQuery );


    }

}
