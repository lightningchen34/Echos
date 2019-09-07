package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.Post;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.utils.listitem.PlainListItemInfo;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity implements MyListView.MyListViewPullListener{

    MyListView listView;
    LinkedList<ListItemInfo> data;
    Context mContext;
    ListItemAdapter adapter;
    private int lastFavourite_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initToolBar();
        initListInfo();
    }

    public void initListInfo()
    {
        listView = (MyListView)findViewById(R.id.list_myposts);
        listView.setPullListener(this);
        mContext = PostActivity.this;
        data = new LinkedList<>();
        adapter = new ListItemAdapter(data,mContext);
        listView.setAdapter(adapter);
        Call<Post> call = MainActivity.echosService.getMyPost(0);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.body().getState() == 1)
                    getPostInfo(response.body().getData());
                else
                    System.out.println("没有登陆");
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                System.out.println("访问失败");
            }
        });
    }

    public void toRefreshListView()
    {
        Call<Post> call = MainActivity.echosService.getMyPost(0);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.body().getState() == 1) {
                    data.clear();
                    getPostInfo(response.body().getData());
                }
                else
                    System.out.println("没有登陆");
                listView.refreshFinish();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                System.out.println("访问失败");
            }
        });
    }

    public void toUpdateListView()
    {
        Call<Post> call = MainActivity.echosService.getMyPost(lastFavourite_id);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.body().getState() == 1)
                    getPostInfo(response.body().getData());
                else
                    System.out.println("没有登陆");
                listView.refreshFinish();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                System.out.println("访问失败");
            }
        });
    }

    public void getPostInfo(List<Post.DataBean> stream)
    {
        for(int i=0;i<stream.size();i++)
            data.add(new PlainListItemInfo(stream.get(i).getTitle(),stream.get(i).getAuthor()+" 发布于 "+stream.get(i).getCreate_time(),stream.get(i)));
        adapter.notifyDataSetChanged();
        if(stream.size()>0)
            lastFavourite_id = stream.get(stream.size()-1).getPost_id();
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
