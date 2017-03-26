package com.codepath.apps.twitterapp.fragments;

/**
 * Created by Saranu on 3/23/17.
 */

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.Tweet;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ComposeDialogFragment extends DialogFragment {

    @BindView(R.id.tbTwitter) public Toolbar tbTwitter;
    @BindView(R.id.ivTweetImage)public ImageView ivTweetImage;
    @BindView(R.id.tvUserName) public TextView tvUserName;
    @BindView(R.id.ivVerified)public ImageView ivVerfied;
    @BindView(R.id.tvHandle) public TextView tvHandle;
    @BindView(R.id.tvBody) public TextView tvBody;
    @BindView(R.id.ivMedia)public ImageView ivMedia;
    @BindView(R.id.tvTimeStamp)public TextView tvTimeStamp;
    @BindView(R.id.seperatorview_1)public View seperatorview_1;
    @BindView(R.id.seperatorview_2)public View seperatorview_2;
    @BindView(R.id.seperatorview_3)public View seperatorview_3;
    @BindView(R.id.seperatorview_4)public View seperatorview_4;

    @BindView(R.id.tvRetweetedNo) public TextView tvRetweetedNo;
    @BindView(R.id.tvRetweeted) public TextView tvRetweeted;
    @BindView(R.id.tvLikesNo) public TextView tvLikesNo;
    @BindView(R.id.tvLikes) public TextView tvLikes;

    @BindView(R.id.ivReply)public ImageView ivReply;
    @BindView(R.id.ivRetweet)public ImageView ivRetweet;
    @BindView(R.id.ivFavorite)public ImageView ivFavorite;

    @BindView(R.id.etReplyTweet) public EditText etReplyTweet;
    @BindView(R.id.tvCharsLeft) public TextView tvCharsLeft;
    @BindView(R.id.rlReply) public RelativeLayout rlReply;
    @BindView(R.id.btTweetReply) public Button btTweetReply;

    Tweet tweet;


    public ComposeDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface ComposeTweetDialogListener {
           void onFinishComposeTweetDialog(String tweetText, Tweet tweet);
    }

    public static ComposeDialogFragment newInstance() {
        ComposeDialogFragment frag = new ComposeDialogFragment();
        Bundle args = new Bundle();
        return frag;
    }


    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        getActivity().invalidateOptionsMenu();
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   //     getDialog().setTitle("Filter Settings");
        View fView = inflater.inflate(R.layout.fragment_compose_tweet, container);
        Bundle bundle = this.getArguments();
        ButterKnife.bind(this,fView);
        populateViewsfromObject(bundle);

        // Set an OnMenuItemClickListener to handle menu item clicks
        tbTwitter.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.action_close){
                            getDialog().dismiss();
                        }
                        // Handle the menu item
                        return true;
                    }
                });

        // Inflate a menu to be displayed in the toolbar
        tbTwitter.inflateMenu(R.menu.menu_twitter_fragment);
      //  setHasOptionsMenu(true);
  //     ((AppCompatActivity)getActivity()).setSupportActionBar(tbTwitter);
        return fView;
    }

    /**
     * the menu layout has the 'add/new' menu item
     */
  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();    //remove all items
        menuInflater.inflate(R.menu.menu_twitter_fragment, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();    //remove all items
        getActivity().getMenuInflater().inflate(R.menu.menu_twitter_fragment, menu);
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        btTweetReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateObjectfromViews();
            }
        });

    }




    private void populateViewsfromObject(Bundle bundle) {

        if (bundle != null ) {
            tweet = bundle.getParcelable("TWEET_OBJ");
            if (tweet.getUser().getProfileImageUrl() !=null) {
                String url = tweet.getUser().getProfileImageUrl();
                Glide.with(getContext()).load(url).placeholder(R.drawable.ic_launcher).
                        error(R.drawable.ic_launcher).into(ivTweetImage);
            }
            tvUserName.setText(tweet.getUser().getName());
            tvBody.setText(tweet.getText());

            tvHandle.setText(tweet.getUser().getScreenName());
            tvTimeStamp.setText(tweet.getCreatedAt());//TODO: Convert to time
            ivMedia.setImageResource(0);
            if(tweet.getEntities() != null && tweet.getEntities().getMedia()
                    !=null ){
                String mUrl = tweet.getEntities().getMedia().get(0).getMediaUrl();
                Glide.with(getContext()).load(mUrl).placeholder(R.drawable.ic_launcher).
                        error(R.drawable.ic_launcher).into(ivMedia);
            }
            etReplyTweet.setText("Reply to " + tweet.getUser().getName());
            rlReply.setVisibility(View.GONE);

            etReplyTweet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int maxLength = 142;
                    int charsRemaining = maxLength - s.length();
                    tvCharsLeft.setText(Integer.toString(charsRemaining));
                    if(charsRemaining <0 ){
                        tvCharsLeft.setEnabled(false);
                        tvCharsLeft.setTextColor(Color.RED);
                    }

                }
            });

            etReplyTweet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                      @Override
                                                      public void onFocusChange(View v, boolean hasFocus) {
                                                          if(hasFocus){
                                                              rlReply.setVisibility(View.VISIBLE);
                                                              etReplyTweet.setTextColor(Color.BLACK);
                                                              etReplyTweet.setTextSize(14);
                                                              etReplyTweet.setText(tweet.getUser().getScreenName());
                                                              etReplyTweet.setSelection(etReplyTweet.getText().length());

                                                          }
                                                      }
                                                  }
            );




        }

    }


    private void populateObjectfromViews() {
        ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
        listener.onFinishComposeTweetDialog(etReplyTweet.getText().toString(), tweet);
        dismiss();

        }
    }


