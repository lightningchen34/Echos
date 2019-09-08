package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.Post_Comments;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAreaActivity extends AppCompatActivity implements MyListView.MyListViewPullListener {

    private int postid;

    private int quote;
    private TextView quoteview;

    private EditText myComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_area);
        initToolBar();

        Intent intent = getIntent();
        postid = intent.getIntExtra("post", 0);

        MyListView listView = (MyListView)findViewById(R.id.comment_area_listView);
        listView.setPullListener(this);
        data = new LinkedList<>();
        adapter = new ListItemAdapter(data,CommentAreaActivity.this);
        listView.setAdapter(adapter);

        quoteview = (TextView) findViewById(R.id.comment_area_quote);
        setQuote(0);
        quoteview.setOnClickListener((View v) -> {
            setQuote(0);
        });

        myComment = (EditText)findViewById(R.id.comment_area_mycomment);
        Button submit = (Button)findViewById(R.id.comment_area_submit);

        submit.setOnClickListener((View v) -> {
            String data = myComment.getText().toString();
            if (data.length() < 5)
            {
                Toast.makeText(this, "评论至少 5 个字符。", Toast.LENGTH_LONG).show();
            } else
            {
                Call<Post_Comments> call = MainActivity.echosService.addComment(postid, data, quote);
                call.enqueue(new Callback<Post_Comments>() {
                    @Override
                    public void onResponse(Call<Post_Comments> call, Response<Post_Comments> response) {
                        Post_Comments pc = response.body();
                        if (pc.getState() == 1)
                        {
                            ToastShow("评论成功");
                            myComment.setText("");
                            myComment.clearFocus();
                            setQuote(0);
                        } else
                        {
                            ToastShow("评论失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<Post_Comments> call, Throwable t) {
                        ToastShow("Failed");
                    }
                });
            }
        });

    }

    public void setQuote(int q)
    {
        quote = q;
        if (quote == 0)
        {
            quoteview.setVisibility(View.GONE);
        } else
        {
            quoteview.setVisibility(View.VISIBLE);
        }
    }

    public void getCommentsInfo(LinkedList<Post_Comments.DataBean> stream)
    {

    }

    public void toRefreshListView()
    {

    }

    public void toUpdateListView()
    {

    }

    LinkedList<ListItemInfo> data;
    ListItemAdapter adapter;
    private int lastComment_id;

    public void ToastShow(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.comment_area_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
