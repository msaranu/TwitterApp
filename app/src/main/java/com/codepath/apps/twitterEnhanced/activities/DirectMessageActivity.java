package com.codepath.apps.twitterEnhanced.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.twitterEnhanced.R;
import com.codepath.apps.twitterEnhanced.fragments.DirectMessageFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectMessageActivity extends AppCompatActivity {

    @BindView(R.id.etDMSend) public EditText etDMSend;
    @BindView(R.id.btnDMSend) public Button btnDMSend;
    String messageTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message);
        ButterKnife.bind(this);
        constructFragment(null);

    }

    public void onDMSend(View v){
        if(etDMSend != null && etDMSend.getText() !=null){
            messageTest = etDMSend.getText().toString();
        }
      constructFragment(messageTest);
    }

    public void constructFragment(String messageTest){
        DirectMessageFragment directMessageFragment = DirectMessageFragment.newInstance(messageTest);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentDM, directMessageFragment);
        ft.commit();
    }
}