package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.User;

import java.lang.reflect.Field;

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
        username.setOnClickListener((View v)->{
            final EditText inputServer = new EditText(this);
            inputServer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputServer.selectAll();
                }
            });
            inputServer.setText("请输入昵称");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("更换昵称");
            builder.setNegativeButton("Cancel",null);
            builder.setView(inputServer,50,0,50,-20);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String input;
                    input = inputServer.getText().toString();
                    System.out.println("gettext = "+input);
                }
            });
            AlertDialog dialog = builder.create();
//            try {
////                Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
////                mAlert.setAccessible(true);
////                Object mAlertController = mAlert.get(dialog);
////                Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
////                mMessage.setAccessible(true);
////                TextView mMessageView = (TextView) mMessage.get(mAlertController);
////                mMessageView.setTextColor(Color.blue());
//            }
//            catch (Exception e) {}
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
        });
        username.setOnClickListener(null);
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
    public void LogoutResponder(boolean state)
    {
        if (state)
        {
            finish();
        } else
        {
            Toast.makeText(this, "登出失败？！", Toast.LENGTH_LONG).show();
        }
    }
}
