package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initToolBar();

        TextView textView = (TextView) findViewById(R.id.submit_view);
        EditText content = (EditText) findViewById(R.id.feedback_content);

        textView.setOnClickListener((View v) -> {
            Call<String> call = MainActivity.echosService.addFeedback(content.getText().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String str = response.body();
                    if (str != null && str.equals("1"))
                    {
                        finish();
                    } else {
                        onFailed();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    onFailed();
                }
            });
        });
    }

    public void onFailed()
    {
        Toast.makeText(this, "发送失败。", Toast.LENGTH_LONG).show();
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
