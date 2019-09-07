package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.User;

public class UserInfoActivity extends AppCompatActivity implements User.LogoutResponder {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initToolBar();

        TextView logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener((View v) -> {
            MainActivity.user.logout(this);
        });

        TextView username = (TextView) findViewById(R.id.username_textview);
        username.setText(MainActivity.user.getInfo().getNickname());
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.userinfo_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void LogoutResponder(boolean state) {
        if (state)
        {
            finish();
        } else
        {
            Toast.makeText(this, "登出失败？！", Toast.LENGTH_LONG).show();
        }
    }
}
