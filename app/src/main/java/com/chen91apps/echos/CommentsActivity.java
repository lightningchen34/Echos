package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.PostComments;
import com.chen91apps.echos.utils.listitem.CommentListItemInfo;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity implements MyListView.MyListViewPullListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initToolBar();

        listView = (MyListView)findViewById(R.id.list_mycomments);
        listView.setPullListener(this);
        data = new LinkedList<>();
        adapter = new ListItemAdapter(data,this);
        listView.setAdapter(adapter);

        Call<PostComments> call = MainActivity.echosService.getMyComments(0);
        call.enqueue(new Callback<PostComments>() {
            @Override
            public void onResponse(Call<PostComments> call, Response<PostComments> response) {
                if(response.body().getData().size()>0)
                    getCommentsInfo(response.body().getData());
                else
                    onNull();
            }

            @Override
            public void onFailure(Call<PostComments> call, Throwable t) {
                t.printStackTrace();
                onFailed();
            }
        });
    }

    public void onNull()
    {
        Toast.makeText(this,"这里没有评论", Toast.LENGTH_LONG).show();
    }

    void getCommentsInfo(List<PostComments.DataBean> stream)
    {
        for(int i=0;i<stream.size();i++)
            data.add(new CommentListItemInfo(stream.get(i)));
            //data.add(new PlainListItemInfo(stream.get(i).getContent(),stream.get(i).getAuthor()+" 发布于 "+stream.get(i).getCreate_time(),stream.get(i)));
        adapter.notifyDataSetChanged();

        if(stream.size()>0)
            lastCommments_id = stream.get(stream.size() - 1).getComment_id();
        else
            lastCommments_id = -1;

        if(lastCommments_id == 0)
            lastCommments_id = -1;
    }

    MyListView listView;
    LinkedList<ListItemInfo> data;
    ListItemAdapter adapter;
    private int lastCommments_id;

    public void toRefreshListView()
    {
        Call<PostComments> call = MainActivity.echosService.getMyComments(0);
        call.enqueue(new Callback<PostComments>() {
            @Override
            public void onResponse(Call<PostComments> call, Response<PostComments> response) {
                if(response.body().getData().size()>0) {
                    data.clear();
                    getCommentsInfo(response.body().getData());
                }
                else
                    onNull();
                listView.refreshFinish();
            }

            @Override
            public void onFailure(Call<PostComments> call, Throwable t) {
                onFailed();
                listView.refreshFinish();
            }
        });
    }

    public void toUpdateListView()
    {
        Call<PostComments> call = MainActivity.echosService.getMyComments(lastCommments_id);
        call.enqueue(new Callback<PostComments>() {
            @Override
            public void onResponse(Call<PostComments> call, Response<PostComments> response) {
                if(response.body().getData().size()>0)
                    getCommentsInfo(response.body().getData());
                listView.refreshFinish();
            }

            @Override
            public void onFailure(Call<PostComments> call, Throwable t) {
                listView.refreshFinish();
                onFailed();
            }
        });
        listView.refreshFinish();
    }

    public void onFailed()
    {
        Toast.makeText(this, "加载失败，请检查网络连接是否正常。", Toast.LENGTH_LONG).show();
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.comments_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
