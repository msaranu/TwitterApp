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
import com.codepath.apps.twitterEnhanced.models.Tweet;
import com.codepath.apps.twitterEnhanced.models.User;
import com.codepath.apps.twitterEnhanced.models.Users;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Saranu on 4/2/17.
 */

public class UserListFollowersFragment extends TimelineFragment {
    private TwitterClient client;
    private Users users;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return super.onCreateView(inflater, parent, savedInstanceState);
    }

    public static UserListFollowersFragment newInstance(String screenName) {
        UserListFollowersFragment userListFollowersFragment = new UserListFollowersFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userListFollowersFragment.setArguments(args);
        return userListFollowersFragment;
    }


    @Override
    protected void populateTimeline(int page) {
        long id = -1;
        String screenName = getArguments().getString("screen_name");
        boolean scroll = false; //set it later
        if (page != 0) {
            id = cursorID;
            scroll = true;
        }


        client.getFollowers(new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                users = gson.fromJson(response.toString(), Users.class);

                if(users != null){
                    for(User u : users.getUsers()){
                        Tweet t = new Tweet();
                        t.setUser(u);
                        tweets.add(t);
                    }
                    cursorID = users.getNextCursor();
                    adapter.notifyDataSetChanged();
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests") || errorResponse.toString().contains("Rate limit exceeded")) {
                    Toast.makeText(getContext(), "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(), "TOO MANY REQUESTS ??",
                        Toast.LENGTH_LONG).show();
            }

        }, screenName, id);


    }

}

