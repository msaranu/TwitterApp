package com.codepath.apps.twitterEnhanced.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitterEnhanced.fragments.UserTimelineFragment;
import com.codepath.apps.twitterEnhanced.fragments.UserTimelineMediaFragment;
import com.codepath.apps.twitterEnhanced.fragments.UserTimelineRepliesFragment;
import com.codepath.apps.twitterEnhanced.fragments.UserTimlineLikesFragment;

/**
 * Created by Saranu on 4/1/17.
 */

public class UserProfilePagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT=4;
        private String tabTitles[] = {"Tweets", "Tweets & Replies", "Media","Likes"};
        private Context context;
        String screenName;

        public UserProfilePagerAdapter(FragmentManager fm, Context context, String screenName){
            super(fm);
            this.context = context;
            this.screenName = screenName;
        }

        @Override
        public Fragment getItem(int position) {
            if(position ==0){
                return new UserTimelineFragment().newInstance(screenName);
            } else if(position ==1){
                return new UserTimelineRepliesFragment().newInstance(screenName);
            } else if(position ==2){
                return new UserTimelineMediaFragment().newInstance(screenName);
            } else if(position ==3){
                return new UserTimlineLikesFragment().newInstance(screenName);
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
