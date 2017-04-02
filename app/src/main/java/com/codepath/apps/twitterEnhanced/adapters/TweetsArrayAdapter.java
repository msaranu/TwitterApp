package com.codepath.apps.twitterEnhanced.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.activities.UserProfileActivity;
import com.codepath.apps.twitterEnhanced.applications.TwitterApplication;
import com.codepath.apps.twitterEnhanced.clients.TwitterClient;
import com.codepath.apps.twitterEnhanced.models.Sender;
import com.codepath.apps.twitterEnhanced.models.Tweet;
import com.codepath.apps.twitterEnhanced.utils.DateUtil;
import com.codepath.apps.twitterEnhanced.utils.PatternEditableBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Saranu on 3/21/17.
 */

public class TweetsArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    RecyclerView mRecyclerView;
    private List<Tweet> mTweets;
    // Store the context for easy access
    private Context mContext;
    TwitterClient client;
    Tweet tweet;

    private final int IMAGE = 0, NO_IMAGE = 1,  DIRECT_MESSAGE=2;

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Pass in the contact array into the constructor
    public TweetsArrayAdapter(Context context, List<Tweet> articles) {
        mTweets = articles;
        mContext = context;
        client = TwitterApplication.getRestClient();
        setHasStableIds(true);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (mTweets.get(position).getId() == 0 || mTweets.get(position).getUser() ==null) {
            return NO_IMAGE;
        } else
            return IMAGE;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View tweetView;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case IMAGE:

                tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
                viewHolder = new ViewHolder(tweetView);
                break;
            case NO_IMAGE:
                tweetView = inflater.inflate(R.layout.item_user, parent, false);
                viewHolder = new ViewHolderNoImage(tweetView);
                break;

            default:
                return null;

        }

        return viewHolder;

    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
         tweet = mTweets.get(position);

        switch (viewHolder.getItemViewType()) {
            case IMAGE:
                TweetsArrayAdapter.ViewHolder vh = (TweetsArrayAdapter.ViewHolder) viewHolder;
                configureViewHolder(vh, tweet);
                break;
            case NO_IMAGE:
                TweetsArrayAdapter.ViewHolderNoImage vhImage = (TweetsArrayAdapter.ViewHolderNoImage) viewHolder;
                configureViewHolderNoImage(vhImage, tweet);
                break;
            default:
                break;
        }
    }


    private void configureViewHolder(final ViewHolder vh, final Tweet tweet) {
        final ImageView ivImage = vh.ivTweetImage;
        ivImage.setImageResource(0);
        if (tweet.getUser() != null && tweet.getUser().getProfileImageUrl() != null) {
            String url = tweet.getUser().getProfileImageUrl();
            Glide.with(mContext).load(url).placeholder(R.drawable.placeholder).
                    error(R.drawable.ic_launcher).into(ivImage);
        }

        final String screenName = tweet.getUser().getScreenName();
        ivImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserProfileActivity.class);
                i.putExtra("screen_name", screenName);
                v.getContext().startActivity(i);
            }
        });

        vh.tvUserName.setText(tweet.getUser().getName());
        vh.tvBody.setText(tweet.getText());
        vh.tvHandle.setText(tweet.getUser().getScreenName());
        vh.tvTime.setText(DateUtil.getRelativeTimeAgo(tweet.getCreatedAt()));//TODO: Convert to time


        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), R.color.twitterBlue,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getContext(), "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(vh.tvBody);

        ImageView ivMedia = vh.ivMedia;
        ivMedia.setImageResource(0);
        if (tweet.getEntities() != null && tweet.getEntities().getMedia()
                != null) {
            String mUrl = tweet.getEntities().getMedia().get(0).getMediaUrl();
            Glide.with(mContext).load(mUrl).placeholder(R.drawable.ic_launcher).
                    error(R.drawable.ic_launcher).into(ivMedia);

        }

        if (tweet.getRetweetCount() != 0 && tweet.getFavoriteCount() != 0) {

            vh.tvRetweeted.setText(Long.toString(tweet.getRetweetCount()));
            vh.tvFavorited.setText(Long.toString(tweet.getFavoriteCount()));
        } else {
            vh.tvRetweeted.setText("0");
            vh.tvFavorited.setText("0");
        }

        if(tweet.retweeted){
            vh.ivRetweet.setImageResource(R.drawable.ic_vector_retweet_green);
        }

        if(tweet.favorited){
            vh.ivFavorite.setImageResource(R.drawable.ic_vector_heart_red);
        }

        vh.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.setFavoriteIcon(new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if(tweet.favorited) {
                            vh.ivFavorite.setImageResource(R.drawable.ic_vector_heart);
                        }else{
                            vh.ivFavorite.setImageResource(R.drawable.ic_vector_heart_red);
                        }
                        tweet.favorited = !tweet.favorited;
                        try {
                            String favoriteCount= response.getString("favorite_count");
                            vh.tvFavorited.setText(favoriteCount);
                            tweet.setFavoriteCount(Long.parseLong(favoriteCount));
                        } catch (JSONException e) {
                            e.printStackTrace();
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

                },tweet.getId(), tweet.favorited);

            }
        });


        vh.ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.setRetweet(new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if(tweet.retweeted) {
                            vh.ivRetweet.setImageResource(R.drawable.ic_retweet_vector);
                        }else{
                            vh.ivRetweet.setImageResource(R.drawable.ic_vector_retweet_green);
                        }
                        tweet.retweeted = !tweet.retweeted;
                        try {
                            String reTweetCount= response.getString("retweet_count");
                            vh.tvRetweeted.setText(reTweetCount);
                            tweet.setRetweetCount(Long.parseLong(reTweetCount));
                        } catch (JSONException e) {
                            e.printStackTrace();
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

                    public void onFailure(int statusCode, Header[] header, String s1, Throwable t1){
                        Log.d("DEBUG", s1.toString());
                    }
                },tweet.getId(), tweet.retweeted);

            }
        });

    }

    private void configureViewHolderNoImage(ViewHolderNoImage vh, Tweet tweet) {

        if(tweet.getUser() != null ) {
            final ImageView ivImage = vh.ivTweetImage;
            ivImage.setImageResource(0);
            if (tweet.getUser() != null && tweet.getUser().getProfileImageUrl() != null) {
                String url = tweet.getUser().getProfileImageUrl();
                Glide.with(mContext).load(url).placeholder(R.drawable.placeholder).
                        error(R.drawable.ic_launcher).into(ivImage);
            }

            final String screenName = tweet.getUser().getScreenName();
            ivImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), UserProfileActivity.class);
                    i.putExtra("screen_name", screenName);
                    v.getContext().startActivity(i);
                }
            });

            vh.tvUserName.setText(tweet.getUser().getName());
            vh.tvBody.setText(tweet.getText());
            vh.tvHandle.setText(tweet.getUser().getScreenName());

            new PatternEditableBuilder().
                    addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                            new PatternEditableBuilder.SpannableClickedListener() {
                                @Override
                                public void onSpanClicked(String text) {
                                    Toast.makeText(getContext(), "Clicked username: " + text,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).into(vh.tvBody);
        }else{
            final ImageView ivImage = vh.ivTweetImage;
            Sender s = tweet.getDirectMessage().getSender();
            ivImage.setImageResource(0);
            if (s.getProfileImageUrl() != null ) {
                String url = s.getProfileImageUrl();
                Glide.with(mContext).load(url).placeholder(R.drawable.placeholder).
                        error(R.drawable.ic_launcher).into(ivImage);
            }

            final String screenName = s.getScreenName();
            ivImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), UserProfileActivity.class);
                    i.putExtra("screen_name", screenName);
                    v.getContext().startActivity(i);
                }
            });

            vh.tvUserName.setText(s.getName());
            String textMessage = tweet.getDirectMessage().getText() + " @ "
                    + DateUtil.getRelativeTimeAgo(tweet.getDirectMessage().getCreatedAt());
            vh.tvBody.setText(textMessage);
            vh.tvHandle.setText('@' + tweet.getDirectMessage().getSenderScreenName());
            if(tweet.getDirectMessage().getSenderScreenName().toUpperCase().equals("MALLESWARI_S")){
                vh.tvBody.setTextColor(Color.BLUE);
            }else{
                vh.tvBody.setTextColor(Color.RED);

            }

            new PatternEditableBuilder().
                    addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                            new PatternEditableBuilder.SpannableClickedListener() {
                                @Override
                                public void onSpanClicked(String text) {
                                    Toast.makeText(getContext(), "Clicked username: " + text,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).into(vh.tvBody);
        }

    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivTweetImage)
        public ImageView ivTweetImage;
        @BindView(R.id.tvUserName)
        public TextView tvUserName;
        @BindView(R.id.ivVerified)
        public ImageView ivVerified;
        @BindView(R.id.tvHandle)
        public TextView tvHandle;
        @BindView(R.id.tvDot)
        public TextView tvDot;
        @BindView(R.id.tvTime)
        public TextView tvTime;
        @BindView(R.id.tvBody)
        public TextView tvBody;
        @BindView(R.id.ivMedia)
        public ImageView ivMedia;
        @BindView(R.id.ivReply)
        public ImageView ivReply;
        @BindView(R.id.ivRetweet)
        public ImageView ivRetweet;
        @BindView(R.id.tvRetweeted)
        public TextView tvRetweeted;
        @BindView(R.id.ivFavorite)
        public ImageView ivFavorite;
        @BindView(R.id.tvFavorited)
        public TextView tvFavorited;


        //@BindView(R.id.ivMedia2) public FensterVideoView textureView;
        // @BindView(R.id.ivMedia3) public SimpleMediaFensterPlayerController fullSreenMediaPlayerController;


        public ImageView getIvMedia() {
            return ivMedia;
        }

        public void setIvMedia(ImageView ivMedia) {
            this.ivMedia = ivMedia;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getIvVerified() {
            return ivVerified;
        }

        public void setIvVerified(ImageView ivVerified) {
            this.ivVerified = ivVerified;
        }

        public TextView getTvHandle() {
            return tvHandle;
        }

        public void setTvHandle(TextView tvHandle) {
            this.tvHandle = tvHandle;
        }

        public TextView getTvDot() {
            return tvDot;
        }

        public void setTvDot(TextView tvDot) {
            this.tvDot = tvDot;
        }

        public TextView getTvTime() {
            return tvTime;
        }

        public void setTvTime(TextView tvTime) {
            this.tvTime = tvTime;
        }

        public ImageView getIvTweetImage() {
            return ivTweetImage;
        }

        public void setIvTweetImage(ImageView ivTweetImage) {
            this.ivTweetImage = ivTweetImage;
        }

        public TextView getTvUserName() {
            return tvUserName;
        }

        public void setTvUserName(TextView tvUserName) {
            this.tvUserName = tvUserName;
        }

        public TextView getTvBody() {
            return tvBody;
        }

        public void setTvBody(TextView tvBody) {
            this.tvBody = tvBody;
        }

        public ImageView getIvReply() {
            return ivReply;
        }

        public void setIvReply(ImageView ivReply) {
            this.ivReply = ivReply;
        }

        public ImageView getIvRetweet() {
            return ivRetweet;
        }

        public void setIvRetweet(ImageView ivRetweet) {
            this.ivRetweet = ivRetweet;
        }

        public TextView getTvRetweeted() {
            return tvRetweeted;
        }

        public void setTvRetweeted(TextView tvRetweeted) {
            this.tvRetweeted = tvRetweeted;
        }

        public ImageView getIvFavorite() {
            return ivFavorite;
        }

        public void setIvFavorite(ImageView ivFavorite) {
            this.ivFavorite = ivFavorite;
        }

        public TextView getTvFavorited() {
            return tvFavorited;
        }

        public void setTvFavorited(TextView tvFavorited) {
            this.tvFavorited = tvFavorited;
        }


       /* public SimpleMediaFensterPlayerController getMediaFensterPlayerController() {
            return fullSreenMediaPlayerController;
        }

        public void setMediaFensterPlayerController(SimpleMediaFensterPlayerController mediaFensterPlayerController) {
            this.fullSreenMediaPlayerController = mediaFensterPlayerController;
        }*/
    }

    public static class ViewHolderNoImage extends RecyclerView.ViewHolder {
        @BindView(R.id.ivTweetImage)
        public ImageView ivTweetImage;
        @BindView(R.id.tvUserName)
        public TextView tvUserName;
        @BindView(R.id.ivVerified)
        public ImageView ivVerified;
        @BindView(R.id.tvHandle)
        public TextView tvHandle;
        @BindView(R.id.tvBody)
        public TextView tvBody;


        //@BindView(R.id.ivMedia2) public FensterVideoView textureView;
        // @BindView(R.id.ivMedia3) public SimpleMediaFensterPlayerController fullSreenMediaPlayerController;


        public ViewHolderNoImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getIvVerified() {
            return ivVerified;
        }

        public void setIvVerified(ImageView ivVerified) {
            this.ivVerified = ivVerified;
        }

        public TextView getTvHandle() {
            return tvHandle;
        }

        public void setTvHandle(TextView tvHandle) {
            this.tvHandle = tvHandle;
        }

        public ImageView getIvTweetImage() {
            return ivTweetImage;
        }

        public void setIvTweetImage(ImageView ivTweetImage) {
            this.ivTweetImage = ivTweetImage;
        }

        public TextView getTvUserName() {
            return tvUserName;
        }

        public void setTvUserName(TextView tvUserName) {
            this.tvUserName = tvUserName;
        }

        public TextView getTvBody() {
            return tvBody;
        }

        public void setTvBody(TextView tvBody) {
            this.tvBody = tvBody;
        }


    }
}