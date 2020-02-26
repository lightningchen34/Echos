package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.User;

public class LoginActivity extends AppCompatActivity implements User.LoginResponder {

    private Intent myIntent;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();

        EditText edituser = (EditText) findViewById(R.id.edit_username);
        EditText editpass = (EditText) findViewById(R.id.edit_password);

        Button buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener((View v) -> {
            MainActivity.user.login(edituser.getText().toString(), editpass.getText().toString(), this);
        });

        Button buttonSignup = (Button) findViewById(R.id.button_signup);
        buttonSignup.setOnClickListener((View v) -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(this, SignupActivity.class));
            intent.putExtra("action", action);
            startActivity(intent);
            finish();
        });

        myIntent = getIntent();
        action = myIntent.getStringExtra("action");
        System.out.println(action);
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void LoginResponder(boolean state, String log) {
        if (state)
        {
            System.out.println(action);
            Intent intent = new Intent();
            if (action.equals("userinfo"))
                intent.setComponent(new ComponentName(this, UserInfoActivity.class));
            else if (action.equals("post"))
                intent.setComponent(new ComponentName(this, PostActivity.class));
            else if (action.equals("comments"))
                intent.setComponent(new ComponentName(this, CommentsActivity.class));
            else if (action.equals("favorites"))
                intent.setComponent(new ComponentName(this, FavoritesActivity.class));
            else if (action.equals("history"))
                intent.setComponent(new ComponentName(this, HistoryActivity.class));
            else if (action.equals("feedback"))
                intent.setComponent(new ComponentName(this, FeedbackActivity.class));
            else if (action.equals("newpost"))
                intent.setComponent(new ComponentName(this, NewPostActivity.class));
            else
            {
                finish();
                return;
            }
            startActivity(intent);
            finish();
        } else
        {
            Toast.makeText(this, log, Toast.LENGTH_LONG).show();
        }
    }
}
