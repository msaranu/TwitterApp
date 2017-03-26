package com.codepath.apps.twitterapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saranu on 3/21/17.
 */

public class TweetsArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    RecyclerView mRecyclerView;
    private List<Tweet> mTweets;
    // Store the context for easy access
    private Context mContext;

    private final int IMAGE = 0, NO_IMAGE = 1;

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Pass in the contact array into the constructor
    public TweetsArrayAdapter(Context context, List<Tweet> articles) {
        mTweets = articles;
        mContext = context;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (mTweets.get(position).getUser().getProfileImageUrl() == null){
            return NO_IMAGE;
        } else
            return IMAGE;
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
                tweetView = inflater.inflate(R.layout.item_tweet_image, parent, false);
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
        Tweet tweet = mTweets.get(position);

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

    private void configureViewHolder(final ViewHolder vh, Tweet tweet) {
        ImageView ivImage = vh.ivTweetImage;
        ivImage.setImageResource(0);
        if (tweet.getUser().getProfileImageUrl() !=null) {
            String url = tweet.getUser().getProfileImageUrl();
            Glide.with(mContext).load(url).placeholder(R.drawable.ic_launcher).
                    error(R.drawable.ic_launcher).into(ivImage);
        }
          vh.tvUserName.setText(tweet.getUser().getName());
          vh.tvBody.setText(tweet.getText());
        vh.tvHandle.setText(tweet.getUser().getScreenName());
        vh.tvTime.setText("1hr");//TODO: Convert to time
        ImageView ivMedia = vh.ivMedia;
        ivMedia.setImageResource(0);
        if(tweet.getEntities() != null && tweet.getEntities().getMedia()
                !=null ){
            String mUrl = tweet.getEntities().getMedia().get(0).getMediaUrl();
            Glide.with(mContext).load(mUrl).placeholder(R.drawable.ic_launcher).
                    error(R.drawable.ic_launcher).into(ivMedia);
        }

        if(tweet.getEntities() != null && tweet.getEntities().getMedia()
                !=null ) {
            if(tweet.getEntities().getMedia().get(0).getExpandedUrl().contains("video")) {
                //  String mUrl = tweet.getEntities().getMedia().get(0).getExpandedUrl();
                //   vh.fullSreenMediaPlayerController.setVisibilityListener(this);
                //   vh.textureView.setMediaController(vh.fullSreenMediaPlayerController);
                //  vh.textureView.setOnPlayStateListener(vh.fullSreenMediaPlayerController);
            //    vh.textureView.setVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
             //   vh.textureView.start();
            }
        }


    }

    private void configureViewHolderNoImage(ViewHolderNoImage vhImage, Tweet tweet) {
        vhImage.tvUserName.setText(tweet.getUser().getScreenName());
        vhImage.tvBody.setText(tweet.getText());
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivTweetImage) public ImageView ivTweetImage;
        @BindView(R.id.tvUserName) public TextView tvUserName;
        @BindView(R.id.ivVerified) public ImageView ivVerified;
        @BindView(R.id.tvHandle) public TextView tvHandle;
        @BindView(R.id.tvDot) public TextView tvDot;
        @BindView(R.id.tvTime) public TextView tvTime;
        @BindView(R.id.tvBody) public TextView tvBody;
        @BindView(R.id.ivMedia) public ImageView ivMedia;
        @BindView(R.id.ivReply) public ImageView ivReply;
        @BindView(R.id.ivRetweet) public ImageView ivRetweet;
        @BindView(R.id.tvRetweeted) public TextView tvRetweeted;
        @BindView(R.id.ivFavorite) public ImageView ivFavorite;
        @BindView(R.id.tvFavorited) public TextView tvFavorited;
    //    @BindView(R.id.ivMedia) public FensterVideoView textureView;
      //  @BindView(R.id.ivMedia) public SimpleMediaFensterPlayerController fullSreenMediaPlayerController;


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

        @BindView(R.id.tvBody)
        public TextView tvBody;
        @BindView(R.id.tvUserName)
        public TextView tvUserName;

        public ViewHolderNoImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public TextView getTvBody() {
            return tvBody;
        }

        public void setTvBody(TextView tvBody) {
            this.tvBody = tvBody;
        }

        public TextView getTvUserName() {
            return tvUserName;
        }

        public void setTvUserName(TextView tvUserName) {
            this.tvUserName = tvUserName;
        }
    }
}