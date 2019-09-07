package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.Favourite;
import com.chen91apps.echos.utils.articles.Post;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        initToolBar();

        TextView textView = (TextView) findViewById(R.id.submit_view);
        EditText title = (EditText) findViewById(R.id.newpost_title);
        EditText content = (EditText) findViewById(R.id.newpost_content);

        textView.setOnClickListener((View v) -> {
            Call<Post> call = MainActivity.echosService.addPost(title.getText().toString(), content.getText().toString());
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    Post post = response.body();
                    if (post.getData().get(0) != null)
                    {
                        jump(post.getData().get(0));
                    } else {
                        onFailed();
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    onFailed();
                }
            });
        });
    }

    public void jump(Post.DataBean post)
    {

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, ViewActivity.class));

        intent.putExtra("type", ListInfoPair.TYPE_COMMUNITY);
        intent.putExtra("content", new Gson().toJson(post));
        startActivity(intent);
        finish();
    }

    public void onFailed()
    {
        Toast.makeText(this, "发帖失败。", Toast.LENGTH_LONG).show();
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.newpost_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
