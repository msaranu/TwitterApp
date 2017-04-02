package com.codepath.apps.twitterEnhanced.fragments;

/**
 * Created by Saranu on 3/23/17.
 */

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.models.Tweet;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyAlertDialogFragment extends DialogFragment {

    @BindView(R.id.btnDelete) public Button btnDelete;
    @BindView(R.id.btnSaveDraft)public Button btnSaveDraft;
    @BindView(R.id.btnCancel) public Button btnCancel;

    Tweet tweet;


    public MyAlertDialogFragment() {

    }

    public interface MyAlertDialogFragmentListener {
        void onFinishAlertDialog(boolean dismiss);
    }

    public static MyAlertDialogFragment newInstance() {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        return frag;
    }


    @Override
        public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        // Call super onResume after sizing
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fView = inflater.inflate(R.layout.fragment_draft_overlay, container);
        Bundle bundle = this.getArguments();
        ButterKnife.bind(this,fView);
        return fView;
    }

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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callParentFragment(true);
            }
        });

        btnSaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callParentFragment(false);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void callParentFragment(boolean dismiss) {
        MyAlertDialogFragmentListener listener = (MyAlertDialogFragmentListener) getTargetFragment();
        listener.onFinishAlertDialog(dismiss);
        dismiss();

    }
}


