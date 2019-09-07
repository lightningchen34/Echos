package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.User;

public class LoginActivity extends AppCompatActivity {

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
            User.user.login(edituser.getText().toString(), editpass.getText().toString(), this);
        });
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
}
