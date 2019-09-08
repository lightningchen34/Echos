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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.Post_Comments;
import com.chen91apps.echos.utils.listitem.CommentListItemInfo;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.utils.listitem.PlainListItemInfo;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAreaActivity extends AppCompatActivity implements MyListView.MyListViewPullListener {

    private int postid;

    private int quote;
    private TextView quoteview;

    private EditText myComment;
    MyListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_area);
        initToolBar();

        Intent intent = getIntent();
        postid = intent.getIntExtra("post", 0);

        listView = (MyListView)findViewById(R.id.comment_area_listView);
        listView.setPullListener(this);
        data = new LinkedList<>();
        adapter = new ListItemAdapter(data,CommentAreaActivity.this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0 || i > data.size()) return;
                setQuote(((CommentListItemInfo) data.get(i - 1)).getId());
            }
        });

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

        Call<Post_Comments> call = MainActivity.echosService.getComments(postid,0);
        call.enqueue(new Callback<Post_Comments>() {
            @Override
            public void onResponse(Call<Post_Comments> call, Response<Post_Comments> response) {
                if(response.body().getData().size()>0)
                    getCommentsInfo(response.body().getData());
                else
                    ToastShow("当前没有评论");
            }

            @Override
            public void onFailure(Call<Post_Comments> call, Throwable t) {

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

    public void getCommentsInfo(List<Post_Comments.DataBean> stream)
    {
        for(int i=0;i<stream.size();i++)
            data.add(new CommentListItemInfo(stream.get(i)));
            // data.add(new PlainListItemInfo(stream.get(i).getContent(),stream.get(i).getAuthor()+" 发布于 "+stream.get(i).getCreate_time(),stream.get(i)));

        if(stream.size()>0)
            lastComment_id = stream.get(stream.size() - 1).getComment_id();
        else
            lastComment_id = -1;

        if(lastComment_id == 0)
            lastComment_id = -1;

        adapter.notifyDataSetChanged();
    }

    public void toRefreshListView()
    {
        Call<Post_Comments> call = MainActivity.echosService.getComments(postid,0);
        call.enqueue(new Callback<Post_Comments>() {
            @Override
            public void onResponse(Call<Post_Comments> call, Response<Post_Comments> response) {
                if(response.body().getData().size()>0) {
                    data.clear();
                    getCommentsInfo(response.body().getData());
                    listView.refreshFinish();
                }
                else
                    ToastShow("当前没有评论");
            }

            @Override
            public void onFailure(Call<Post_Comments> call, Throwable t) {
                listView.refreshFinish();
            }
        });
    }

    public void toUpdateListView()
    {
        Call<Post_Comments> call = MainActivity.echosService.getComments(postid,lastComment_id);
        call.enqueue(new Callback<Post_Comments>() {
            @Override
            public void onResponse(Call<Post_Comments> call, Response<Post_Comments> response) {
                if(response.body().getData().size()>0) {
                    getCommentsInfo(response.body().getData());
                    listView.refreshFinish();
                }
                else
                    ToastShow("当前没有评论");
            }

            @Override
            public void onFailure(Call<Post_Comments> call, Throwable t) {
                listView.refreshFinish();
            }
        });
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
