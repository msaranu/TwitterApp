package com.codepath.apps.twitterapp.fragments;

/**
 * Created by Saranu on 3/23/17.
 */

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.Draft;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.services.TweetOfflineService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ComposeTweetDialogFragment extends DialogFragment implements MyAlertDialogFragment.MyAlertDialogFragmentListener {

    @BindView(R.id.tbTwitter) public Toolbar tbTwitter;

    @BindView(R.id.etReplyTweet) public EditText etReplyTweet;
    @BindView(R.id.tvCharsLeft) public TextView tvCharsLeft;
    @BindView(R.id.btTweetReply) public Button btTweetReply;
    @BindView(R.id.rlReply) public RelativeLayout rlReply;

    Tweet tweet;


    public ComposeTweetDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }


    public static ComposeTweetDialogFragment newInstance() {
        ComposeTweetDialogFragment frag = new ComposeTweetDialogFragment();
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
        View fView = inflater.inflate(R.layout.fragment_compose_new_tweet, container);
        Bundle bundle = this.getArguments();
        ButterKnife.bind(this,fView);
        populateViewsfromObject(bundle);

        // Set an OnMenuItemClickListener to handle menu item clicks
        tbTwitter.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.action_close){
                           // getDialog().dismiss();
                            if(!etReplyTweet.getText().toString().isEmpty() ) {
                                FragmentManager fm = getFragmentManager();
                                MyAlertDialogFragment fdf = MyAlertDialogFragment.newInstance();
                                fdf.setTargetFragment(ComposeTweetDialogFragment.this, 300);
                                fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                                fdf.show(fm, "FRAGMENT_MODAL_ALERT");
                            }else{
                                dismiss();
                            }


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

            if(tweet.getText()==null) {
                List draftListfromDB = TweetOfflineService.retrieveTweetDraft();
                if (draftListfromDB != null && draftListfromDB.size() != 0) {
                    String draftString = ((Draft) (draftListfromDB.get(0))).getDraftText().toString();
                    etReplyTweet.setText(draftString);
                    etReplyTweet.setSelection(draftString.length());
                }
            } else {
                etReplyTweet.setText(tweet.getText());
            }

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
                    if (charsRemaining < 0) {
                        tvCharsLeft.setEnabled(false);
                        tvCharsLeft.setTextColor(Color.RED);
                    }

                }
            });


        }
    }


    private void populateObjectfromViews() {
        TweetDetailDialogFragment.ComposeTweetDialogListener listener = (TweetDetailDialogFragment.ComposeTweetDialogListener) getActivity();
        listener.onFinishComposeTweetDialog(etReplyTweet.getText().toString(), tweet);
        dismiss();

    }

    @Override
    public void onFinishAlertDialog( boolean dismiss) {

        if(dismiss){
            TweetOfflineService.deleteTweetDraft();
            dismiss();
        }else{
            saveDrafttoDB();
             dismiss();


        }
    }

    private void saveDrafttoDB() {
        String draftString = etReplyTweet.getText().toString();
        TweetOfflineService.deleteTweetDraft();
        Draft draft = new Draft();
        draft.setDraftText(draftString);
        draft.save();

    }
}


