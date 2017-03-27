package com.codepath.apps.twitterapp.activities;

/**
 * Created by Saranu on 3/26/17.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.codepath.apps.twitterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogoPageActivity extends Activity {
    protected int splashTime = 4000;
    @BindView(R.id.ivTwitterLogo) public ImageView ivTwitterLogo;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ButterKnife.bind(this);
        ivTwitterLogo.setImageResource(R.drawable.logo_twitter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
                Intent i = new Intent(LogoPageActivity.this, TimelineActivity.class);
                startActivity(i);
            }
        }, splashTime);
    }
}