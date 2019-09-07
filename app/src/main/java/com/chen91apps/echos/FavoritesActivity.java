package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.utils.listitem.PlainListItemInfo;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;

public class FavoritesActivity extends AppCompatActivity implements MyListView.MyListViewPullListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initToolBar();

        initTestInfo();
    }

    MyListView testview;
    LinkedList<ListItemInfo> testinfo;
    Context mContext;
    ListItemAdapter adpter;

    public void toRefreshListView()
    {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getRefreshData();
                adpter.notifyDataSetChanged();
                testview.refreshFinish();
            }
        },5000);
    }

    public void toUpdateListView()
    {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUpdateData();
                adpter.notifyDataSetChanged();
                testview.refreshFinish();
            }
        },5000);
    }


    private void getRefreshData()
    {
        System.out.println("now in getrefreshdata");
        testinfo.addFirst(new PlainListItemInfo("new refresh titile is here!","new title look at the subtitle"));
    }

    private void getUpdateData()
    {
        System.out.println("now in getUpdateData");
        for(int i=0;i<3;i++)
            testinfo.add(new PlainListItemInfo("footer news here","this is the subtitle"));
        testview.setSelection(testview.getCount()-1);
    }

    public void initTestInfo()
    {
        testview = (MyListView) findViewById(R.id.list_myfavorites);
        testview.setPullListener(this);

        mContext = FavoritesActivity.this;
        testinfo = new LinkedList<>();
        for(int i=0;i<10;i++)
            testinfo.add(new PlainListItemInfo(" titile is here!"," look at the subtitle"));
        adpter = new ListItemAdapter(testinfo,this);
        testview.setAdapter(adpter);

    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
