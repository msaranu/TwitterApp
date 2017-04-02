package com.codepath.apps.twitterEnhanced.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.fragments.SearchTimelineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserExploreActivity extends AppCompatActivity {
    String searchString;
    @BindView(R.id.etSearch)
    TextView etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_explore);
        ButterKnife.bind(this);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchTimelineFragment searchTimelineFragment = SearchTimelineFragment.newInstance(s.toString());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentSearch, searchTimelineFragment);
                ft.commit();

            }
        });



    }
}