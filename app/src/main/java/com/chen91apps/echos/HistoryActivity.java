package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.News;
import com.chen91apps.echos.utils.articles.Post;
import com.chen91apps.echos.utils.history.HistoryManager;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.utils.listitem.PlainListItemInfo;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements MyListView.MyListViewPullListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mContext = HistoryActivity.this;
        initToolBar();
        initListInfo();
    }

    MyListView listView;
    LinkedList<ListItemInfo> data;
    Context mContext;
    ListItemAdapter adapter;
    private int lastHistory_id;
    List<HistoryManager.HistoryBean> bufBeans;

    void initListInfo()
    {
        listView = (MyListView) findViewById(R.id.list_myhistory);
        listView.setPullListener(this);
        data = new LinkedList<>();
        adapter = new ListItemAdapter(data,mContext);
        listView.setAdapter(adapter);
        bufBeans = HistoryManager.getHistoryList(0);
        getHistoryInfo(bufBeans);
    }

    void getHistoryInfo(List<HistoryManager.HistoryBean> bufBeans)
    {
        for(int i=0;i<bufBeans.size();i++)
        {
            if(bufBeans.get(i).getType() == ListInfoPair.TYPE_NEWS)
            {
                News.DataBean bufNews = bufBeans.get(i).getContent().news;
                data.add(new PlainListItemInfo(bufNews.getTitle(),bufNews.getPublishTime(),bufNews));
            }
            else if(bufBeans.get(i).getType() == ListInfoPair.TYPE_COMMUNITY)
            {
                Post.DataBean bufPost = bufBeans.get(i).getContent().post;
                data.add(new PlainListItemInfo(bufPost.getTitle(),bufPost.getAuthor()+" 发布于 "+bufPost.getCreate_time(),bufPost));
            }
        }
        if(bufBeans.size() > 0)
            lastHistory_id = bufBeans.get(bufBeans.size()-1).getId();
        else
            lastHistory_id = 0;
        adapter.notifyDataSetChanged();
    }

    public void toRefreshListView()
    {
        bufBeans = HistoryManager.getHistoryList(0);
        data.clear();
        getHistoryInfo(bufBeans);
        listView.refreshFinish();
    }

    public void toUpdateListView()
    {
        if(lastHistory_id == 1){
            listView.refreshFinish();
            return;
        }
        bufBeans = HistoryManager.getHistoryList(lastHistory_id-1);
        getHistoryInfo(bufBeans);
        listView.refreshFinish();
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
