package com.chen91apps.echos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceFragment;

import com.bumptech.glide.Glide;
import com.chen91apps.echos.utils.Configure;

import org.w3c.dom.Text;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;

public class SettingsActivity extends AppCompatActivity {


    final String aboutUs = "这是我们的介绍\n" +
            "开发团队：陈旭 陈浩展\n"+
            "最新版本：1.0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        TextView about = (TextView)findViewById(R.id.settings_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("关于我们");
                TextView tv = new TextView(SettingsActivity.this);
                tv.setText(aboutUs);
                builder.setMessage(aboutUs);
                //builder.setView(tv,50,0,50,0);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
            }
        });

        TextView clearBuffer = (TextView)findViewById(R.id.setting_clearbuffer);
        clearBuffer.setOnClickListener((View v) -> {

            Glide.get(MainActivity.main).clearMemory();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    MainActivity.acache.clear();
                    Glide.get(MainActivity.main).clearDiskCache();
                }
            };
            thread.start();
        });
    }
}