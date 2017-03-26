package com.codepath.apps.twitterapp.services;

import com.codepath.apps.twitterapp.models.Draft;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by Saranu on 3/23/17.
 */

public class TweetOfflineService {

    public static void saveTweetsOffline(List<Tweet> tweetList){
        for(Tweet t : tweetList) {
            t.user.save();
            List<User> userList1= SQLite.select().
                    from(User.class).queryList();
            List<Tweet> tweetList1= SQLite.select().
                    from(Tweet.class).queryList();
            t.save();
            List<Tweet> tweetList2= SQLite.select().
                    from(Tweet.class).queryList();
            int x=10;
        }
    }

    public static List<Tweet> retrieveTweetsOffline(){
        List<Tweet> tweetList= SQLite.select().
                from(Tweet.class).queryList();
        return tweetList;
    }

    public static void deleteTweetsoffline(){
        SQLite.delete().from(Tweet.class).async().execute();

        List<Tweet> tweetList3= SQLite.select().
                from(Tweet.class).queryList();

        List<User> userList3= SQLite.select().
                from(User.class).queryList();
    }

    public static List<Draft> retrieveTweetDraft(){
        List<Draft> tweetDraft=   SQLite.select().
                from(Draft.class).queryList();
        return tweetDraft;
    }

    public static void deleteTweetDraft(){
         SQLite.delete().
                from(Draft.class).async().execute();
    }
}
