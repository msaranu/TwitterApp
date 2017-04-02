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
import com.codepath.apps.twitterEnhanced.models.DirectMessage;
import com.codepath.apps.twitterEnhanced.models.Tweet;
import com.codepath.apps.twitterEnhanced.models.Users;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Saranu on 4/2/17.
 */

public class DirectMessageFragment extends TimelineFragment {

        private TwitterClient client;
        private Users users;
        String screenName;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            client = TwitterApplication.getRestClient();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            return super.onCreateView(inflater, parent, savedInstanceState);
        }

        public static DirectMessageFragment newInstance(String messageText) {
            DirectMessageFragment directMessageFragment = new DirectMessageFragment();
            Bundle args = new Bundle();
            args.putString("message_text",messageText);
            directMessageFragment.setArguments(args);
            return directMessageFragment;
        }


        @Override
        protected void populateTimeline(int page) {
            long id = 0;
            boolean scroll = false; //set it later
            if (page != 0) {
                id = tweets.get(tweets.size() - 1).getId() - 1;
                scroll = true;
            }
            if(getArguments().getString("message_text") !=null){
                    writeDMtoAPI(getArguments().getString("message_text"));
            }


            client.getDirectMessages(new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("DEBUG", response.toString());

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
                    Gson gson = gsonBuilder.create();
                    List<DirectMessage> dmList = gson.fromJson(response.toString(), new TypeToken<List<DirectMessage>>() {
                    }.getType());

                    if(dmList != null) {
                        for (DirectMessage d : dmList) {
                            Tweet t = new Tweet();
                            t.setDirectMessage(d);
                            tweets.add(t);
                        }
                    }
                    adapter.notifyDataSetChanged();


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

            },id, scroll);



        }

    private void writeDMtoAPI(String msgText) {
        String arr[] = msgText.split(" ", 2);
        String screenName = arr[0];
        String msg = arr[1];


        client.sendDirectMessage(new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                if (errorResponse.toString().contains("Too Many Requests") || errorResponse.toString().contains("Rate limit exceeded")) {
                    Toast.makeText(getContext(), "TOO MANY REQUESTS THIS SESSION",
                            Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(), "TOO MANY REQUESTS",
                        Toast.LENGTH_LONG).show();
            }

        },screenName, msg);

    }

}

