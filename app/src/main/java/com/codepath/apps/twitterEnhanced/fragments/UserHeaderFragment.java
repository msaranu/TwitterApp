
package com.codepath.apps.twitterEnhanced.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.applications.TwitterApplication;
import com.codepath.apps.twitterEnhanced.clients.TwitterClient;
import com.codepath.apps.twitterEnhanced.databinding.FragmentUserHeaderBinding;
import com.codepath.apps.twitterEnhanced.models.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Saranu on 3/31/17.
 */

public class UserHeaderFragment extends android.support.v4.app.Fragment {
    TwitterClient client;
    User user;
    String screenName;
    FragmentUserHeaderBinding fragmentUserHeaderBinding;
    @BindView(R.id.ivTweetImage) public ImageView ivTweetImage;
    //  @BindView(R.id.ivProfileBackground) public ImageView ivProfileBackground;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        screenName=  getArguments().getString("screen_name");

        populateUserHeader();
    }

    public static UserHeaderFragment newInstance(String screenName) {
        UserHeaderFragment userHeaderFragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userHeaderFragment.setArguments(args);
        return userHeaderFragment;
    }

    //  @BindingAdapter({"imageUrl"})
    private static void setImageUrl(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).placeholder(R.drawable.ic_launcher).
                error(R.drawable.ic_launcher).into(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentUserHeaderBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_user_header, parent, false);
        View view = fragmentUserHeaderBinding.getRoot();
        ButterKnife.bind(this,view);
        return fragmentUserHeaderBinding.getRoot();
    }


    protected void populateUserHeader() {
        if (screenName != null) {
            client.getShowUser(new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                    user = gson.fromJson(response.toString(), User.class);
                    fragmentUserHeaderBinding.setUser(user);
                    setImageUrl(ivTweetImage, user.getProfileImageUrl());
                    //   setImageUrl(ivProfileBackground,user.getProfileBackgroundImageUrl());//backgrondImage
                    if (user == null) {
                        Toast.makeText(getContext(), "NO TWEET MENTIONS",
                                Toast.LENGTH_LONG).show();
                    } else {


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

            }, screenName);


        } else {
            client.getUserInfo(new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                    user = gson.fromJson(response.toString(), User.class);
                    fragmentUserHeaderBinding.setUser(user);
                    setImageUrl(ivTweetImage, user.getProfileImageUrl());
                    //   setImageUrl(ivProfileBackground,user.getProfileBackgroundImageUrl());//backgrondImage
                    if (user == null) {
                        Toast.makeText(getContext(), "NO TWEET MENTIONS",
                                Toast.LENGTH_LONG).show();
                    } else {


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

            });

        }

    }
}