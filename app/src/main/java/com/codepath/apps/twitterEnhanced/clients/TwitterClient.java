package com.codepath.apps.twitterEnhanced.clients;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "RFngGVnJ4uIMriL4B2QVlaMQK";       // Change this
    public static final String REST_CONSUMER_SECRET = "AUdLAvEJC03M0HGGh96hCASxImpmBFt52QA5cuzktjFPjl0Bag"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cptweets"; // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
 * 	  i.e getApiUrl("statuses/home_timeline.json");
 * 2. Define the parameters to pass to the request (query or body)
 *    i.e RequestParams params = new RequestParams("foo", "bar");
 * 3. Define the request method and make a call to the client
 *    i.e client.get(apiUrl, params, handler);
 *    i.e client.post(apiUrl, params, handler);
 */
    public void getHomeTimeline(AsyncHttpResponseHandler handler, long id, boolean scroll) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        if (scroll) { // Scroll Page
            params.put("max_id", id);
        } else { //pull to refresh or first page
            params.put("since_id", 1);
        }
        params.put("count", 15);
        client.get(apiUrl, params, handler);

    }


    public void getMentionsTimeline(AsyncHttpResponseHandler handler, long id, boolean scroll) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        if (scroll) { // Scroll Page
            params.put("max_id", id);
        } else { //pull to refresh or first page
            params.put("since_id", 1);
        }
        params.put("count", 15);
        client.get(apiUrl, params, handler);

    }


    public void getFavorites(AsyncHttpResponseHandler handler, long id, boolean scroll) {
        String apiUrl = getApiUrl("favorites/list.json");
        RequestParams params = new RequestParams();
        if (scroll) { // Scroll Page
            params.put("max_id", id);
        } else { //pull to refresh or first page
            params.put("since_id", 1);
        }
        params.put("count", 15);
        client.get(apiUrl, params, handler);

    }


    //Select and set the favorite heart icon.
    public void setFavoriteIcon(AsyncHttpResponseHandler handler, long id, boolean favorite) {
        String apiUrl;
        if (favorite) {
            apiUrl = getApiUrl("favorites/destroy.json");
        } else {
            apiUrl = getApiUrl("favorites/create.json");

        }
        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(apiUrl, params, handler);

    }

    // POST https://api.twitter.com/1.1/statuses/update.json?
    // status=Maybe%20he%27ll%20finally%20find%20his%20keys.%20%23peterfalk

    public void postStatusUpdate(AsyncHttpResponseHandler handler, String statusText, long inReplyStatusID) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", statusText);
        if (inReplyStatusID < 0) {
            params.put("in_reply_to_status_id", inReplyStatusID);
        }
        client.post(apiUrl, params, handler);

    }

    public void getRetweet(AsyncHttpResponseHandler handler, String id) {
        String apiUrl = getApiUrl("statuses/retweet/" + id + ".json");
        RequestParams params = new RequestParams();
        client.get(apiUrl, params, handler);
    }


    public void getShowUser(AsyncHttpResponseHandler handler, String screenName) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }


    public void getUserTimeline(AsyncHttpResponseHandler handler, String screenName, long id, boolean scroll) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        if (scroll) { // Scroll Page
            params.put("max_id", id);
        } else { //pull to refresh or first page
            params.put("since_id", 1);
        }
        params.put("count", 15);
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }


    public void getFavorites(AsyncHttpResponseHandler handler, String screenName, long id, boolean scroll) {
        String apiUrl = getApiUrl("favorites/list.json");
        RequestParams params = new RequestParams();
        if (scroll) { // Scroll Page
            params.put("max_id", id);
        } else { //pull to refresh or first page
            params.put("since_id", 1);
        }
        params.put("count", 15);
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }


    public void getFollowers(AsyncHttpResponseHandler handler, String screenName, long id) {
        String apiUrl = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
        params.put("count", 15);
        params.put("screen_name", screenName);
        params.put("skip_Status", true);
        params.put("cursor", id);
        params.put("include_user_entities",false);
        client.get(apiUrl, params, handler);
    }


    public void getFollowing(AsyncHttpResponseHandler handler, String screenName, long id) {
        String apiUrl = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
        params.put("count", 15);
        params.put("screen_name", screenName);
        params.put("skip_Status", true);
        params.put("cursor", id);
        params.put("include_user_entities",false);
        client.get(apiUrl, params, handler);
    }

    public void getSearchTweets(AsyncHttpResponseHandler handler, long id, boolean scroll, String searchQuery) {
        String apiUrl = getApiUrl("search/tweets.json");
        RequestParams params = new RequestParams();
        if (scroll) { // Scroll Page
            params.put("max_id", id);
        } else { //pull to refresh or first page
            params.put("since_id", 1);
        }
        params.put("count", 15);
        params.put("q", searchQuery);
        client.get(apiUrl, params, handler);
    }

    public void setFollowingStatus(AsyncHttpResponseHandler handler, String screenName, boolean friends) {
        String apiUrl;
        if (friends) {
            apiUrl = getApiUrl("friendships/destroy.json");

        } else {
            apiUrl = getApiUrl("friendships/create.json");

        }

        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

    public void sendDirectMessage(AsyncHttpResponseHandler handler, String screenName, String message) {
        String apiUrl = getApiUrl("direct_messages/new.json");
        RequestParams params = new RequestParams();
        params.put("text", message);
        params.put("count", 15);
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

    public void getDirectMessages(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("direct_messages.json");
        RequestParams params = new RequestParams();
        params.put("count", 30);
        client.get(apiUrl, params, handler);
    }

    public void getUserTimelinePhotos(AsyncHttpResponseHandler handler, String screenName, long id, boolean scroll) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        if (scroll) { // Scroll Page
            params.put("max_id", id);
        } else { //pull to refresh or first page
            params.put("since_id", 1);
        }
        params.put("count", 15);
        params.put("screen_name", screenName);
        params.put("exclude_replies", "true");
        params.put("include_rts", "false");
        client.get(apiUrl, params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, null, handler);
    }
}