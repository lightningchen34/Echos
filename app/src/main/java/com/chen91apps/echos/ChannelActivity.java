package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.chen91apps.echos.channel.ChannelAdapter;
import com.chen91apps.echos.channel.ChannelBean;
import com.chen91apps.echos.channel.ChannelInfo;
import com.chen91apps.echos.channel.GridSpacingItemDecoration;
import com.chen91apps.echos.channel.ItemDragCallback;
import com.chen91apps.echos.utils.Configure;

import java.util.ArrayList;
import java.util.List;

public class ChannelActivity extends AppCompatActivity implements ChannelAdapter.onItemRangeChangeListener {

    private RecyclerView mRecyclerView;
    private List<ChannelBean> mList;
    private ChannelAdapter mAdapter;
    // private String select[] = {"要闻", "体育", "新时代", "汽车", "时尚", "国际", "电影", "财经", "游戏", "科技", "房产", "政务", "图片", "独家"};
    // private String recommend[] = {"娱乐", "军事", "文化", "视频", "股票", "动漫", "理财", "电竞", "数码", "星座", "教育", "美容", "旅游"};

    private List<String> select = new ArrayList<>();
    private List<String> recommend = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Integer> ids = ChannelInfo.getIndexes();

        boolean[] visited = new boolean[ChannelInfo.size()];
        select.add(ChannelInfo.getTitle(0));
        for (int i = 0; i < ids.size(); ++i)
        {
            int id = ids.get(i);
            if (id != 0)
            {
                select.add(ChannelInfo.getTitle(id));
                visited[id] = true;
            }
        }
        for (int i = 1; i < ChannelInfo.size(); ++i)
        {
            if (!visited[i])
            {
                recommend.add(ChannelInfo.getTitle(i));
            }
        }

        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        initToolBar();
        mRecyclerView = findViewById(R.id.channel_view);
        mList = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mList.get(position).getSpanSize();
            }
        });
        mRecyclerView.setLayoutManager(manager);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setMoveDuration(300);     //设置动画时间
        animator.setRemoveDuration(0);
        mRecyclerView.setItemAnimator(animator);
        ChannelBean title = new ChannelBean();
        title.setLayoutId(R.layout.adapter_title);
        title.setSpanSize(4);
        mList.add(title);
        for (String bean : select) {
            mList.add(new ChannelBean(bean, 1, R.layout.adapter_channel));
        }
        ChannelBean tabBean = new ChannelBean();
        tabBean.setLayoutId(R.layout.adapter_tab);
        tabBean.setSpanSize(4);
        mList.add(tabBean);
        List<ChannelBean> recommendList = new ArrayList<>();
        for (String bean : recommend) {
            recommendList.add(new ChannelBean(bean, 1, R.layout.adapter_channel));
        }
        mList.addAll(recommendList);
        mAdapter = new ChannelAdapter(this, mList, recommendList);
        mAdapter.setFixSize(1);
        mAdapter.setSelectedSize(select.size());
        mAdapter.setRecommend(true);
        mAdapter.setOnItemRangeChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);
        WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int spacing = (m.getDefaultDisplay().getWidth() - dip2px(this, 70) * 4) / 5;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,spacing,true));
        ItemDragCallback callback=new ItemDragCallback(mAdapter,2);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void refreshItemDecoration() {
        mRecyclerView.invalidateItemDecorations();
    }

    @Override
    protected void onPause() {
        super.onPause();
        List<Integer> list = ChannelInfo.getIntList(mList, mAdapter.getSelectedSize());
        ChannelInfo.putIndexes(list);
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.channel_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
