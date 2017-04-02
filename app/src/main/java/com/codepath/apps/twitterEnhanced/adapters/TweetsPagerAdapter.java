package com.codepath.apps.twitterEnhanced.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitterEnhanced.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterEnhanced.fragments.MentionsTimelineFragment;

/**
 * Created by Saranu on 3/30/17.
 */


public class TweetsPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT=2;
    private String tabTitles[] = {"Home", "Mentions"};
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position ==0){
            return new HomeTimelineFragment();
        } else if(position ==1){
            return new MentionsTimelineFragment();
        } else return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
