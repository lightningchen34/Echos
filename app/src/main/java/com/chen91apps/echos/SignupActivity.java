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
import android.widget.TextView;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.User;

public class SignupActivity extends AppCompatActivity implements User.SignupResponder
{

    private Intent myIntent;
    private String action;

    private EditText username;
    private EditText nickname;
    private EditText password;
    private EditText password2;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initToolBar();

        myIntent = getIntent();
        action = myIntent.getStringExtra("action");
        System.out.println(action);

        username = (EditText) findViewById(R.id.edit_username);
        nickname = (EditText) findViewById(R.id.edit_nickname);
        password = (EditText) findViewById(R.id.edit_password);
        password2 = (EditText) findViewById(R.id.edit_repassword);

        button = (Button) findViewById(R.id.button_signup);

        button.setOnClickListener((View v) -> {
            if (password.getText().toString().equals(password2.getText().toString()))
            {
                if (username.getText().toString().length() < 4)
                {
                    Toast.makeText(this, "请输入长度在 4-16 之间的用户名", Toast.LENGTH_LONG).show();
                } else if (password.getText().toString().length() < 6)
                {
                    Toast.makeText(this, "密码长度至少为 6", Toast.LENGTH_LONG).show();
                } else if (nickname.getText().toString().length() == 0)
                {
                    Toast.makeText(this, "请输入昵称", Toast.LENGTH_LONG).show();
                } else
                {
                    MainActivity.user.signup(username.getText().toString(), nickname.getText().toString(), password.getText().toString(), this);
                }
            } else
            {
                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void SignupResponder(boolean state, String log) {
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
