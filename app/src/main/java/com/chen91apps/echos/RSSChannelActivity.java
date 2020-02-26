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
import android.util.Pair;
import android.view.WindowManager;

import com.chen91apps.echos.channel.ChannelAdapter;
import com.chen91apps.echos.channel.ChannelBean;
import com.chen91apps.echos.channel.ChannelInfo;
import com.chen91apps.echos.channel.GridSpacingItemDecoration;
import com.chen91apps.echos.channel.ItemDragCallback;
import com.chen91apps.echos.channel.RSSChannelAdapter;
import com.chen91apps.echos.channel.RSSChannelBean;
import com.chen91apps.echos.channel.RSSChannelInfo;
import com.chen91apps.echos.channel.RSSItemDragCallback;
import com.chen91apps.echos.utils.Configure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RSSChannelActivity extends AppCompatActivity implements RSSChannelAdapter.onItemRangeChangeListener {

    private RecyclerView mRecyclerView;
    private List<RSSChannelBean> mList;
    private RSSChannelAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RSSChannelInfo.load();

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
        RSSChannelBean title = new RSSChannelBean();
        title.setLayoutId(R.layout.adapter_title);
        title.setSpanSize(4);
        mList.add(title);
        for (Pair<String, String> bean : RSSChannelInfo.get()) {
            mList.add(new RSSChannelBean(bean.first, bean.second, 1, R.layout.adapter_channel));
        }
        RSSChannelBean buttonBean = new RSSChannelBean();
        buttonBean.setLayoutId(R.layout.adapter_button);
        buttonBean.setSpanSize(4);
        mList.add(buttonBean);
        List<ChannelBean> recommendList = new ArrayList<>();
        mAdapter = new RSSChannelAdapter(this, mList);
        mAdapter.setFixSize(1);
        mAdapter.setSelectedSize(RSSChannelInfo.size());
        mAdapter.setOnItemRangeChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);
        WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int spacing = (m.getDefaultDisplay().getWidth() - dip2px(this, 70) * 4) / 5;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,spacing,true));
        RSSItemDragCallback callback=new RSSItemDragCallback(mAdapter,2, this);
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
        LinkedList<Pair<String, String>> list = new LinkedList<>();
        for (int i = 1; i <= mAdapter.getSelectedSize(); ++i)
        {
            list.add(new Pair<>(mList.get(i).getName(), mList.get(i).getUrl()));
        }
        RSSChannelInfo.set(list);
        RSSChannelInfo.save();
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
