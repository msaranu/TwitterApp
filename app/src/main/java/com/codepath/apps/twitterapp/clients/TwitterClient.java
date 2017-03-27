package com.codepath.apps.twitterapp.clients;

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
	public void getHomeTimeline(AsyncHttpResponseHandler handler, long id, boolean scroll){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
        if(scroll){ // Scroll Page
            params.put("max_id", id);
        }else{ //pull to refresh or first page
            params.put("since_id", 1);
        }
		params.put("count", 15);
		client.get(apiUrl, params, handler);

	}


    //Select and set the favorite heart icon.
    public void setFavoriteIcon(AsyncHttpResponseHandler handler, long id){
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
            params.put("id", id);

        client.post(apiUrl, params, handler);

    }

    // POST https://api.twitter.com/1.1/statuses/update.json?
    // status=Maybe%20he%27ll%20finally%20find%20his%20keys.%20%23peterfalk

    public void postStatusUpdate(AsyncHttpResponseHandler handler, String statusText, long inReplyStatusID){
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", statusText);
        if(inReplyStatusID <0) {
            params.put("in_reply_to_status_id", inReplyStatusID);
        }
        client.post(apiUrl, params, handler);

    }

}
