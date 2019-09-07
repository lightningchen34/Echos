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
import com.chen91apps.echos.utils.articles.Favourite;
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

public class FavoritesActivity extends AppCompatActivity implements MyListView.MyListViewPullListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initToolBar();

        initListInfo();
    }

    MyListView listView;
    LinkedList<ListItemInfo> data;
    Context mContext;
    ListItemAdapter adpter;
    private int lastFavourite_id;

    public void toRefreshListView()
    {
        Call<Favourite> call = MainActivity.echosService.getFavorite(0);
        call.enqueue(new Callback<Favourite>() {
            @Override
            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                if(response.body().getState() == 1)
                {
                    data.clear();
                    getFavoriteInfo(response.body().getData());
                }
                else
                    System.out.println("ç¨æ·æ²¡æç»é");
                listView.refreshFinish();
            }
            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                System.out.println("è«åå¶å¦çå¤±è´¥äº");
            }
        });
    }

    public void toUpdateListView()
    {
        Call<Favourite> call = MainActivity.echosService.getFavorite(lastFavourite_id);
        call.enqueue(new Callback<Favourite>() {
            @Override
            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                if(response.body().getState() == 1)
                {
                    getFavoriteInfo(response.body().getData());
                }
                else
                    System.out.println("ç¨æ·æ²¡æç»é");
                listView.refreshFinish();
            }
            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                System.out.println("è«åå¶å¦çå¤±è´¥äº");
            }
        });
    }

    public void initListInfo()
    {
        listView = (MyListView) findViewById(R.id.list_myfavorites);
        listView.setPullListener(this);
        mContext = FavoritesActivity.this;
        data = new LinkedList<>();
        adpter = new ListItemAdapter(data,mContext);
        listView.setAdapter(adpter);
        Call<Favourite> call = MainActivity.echosService.getFavorite(0);
        call.enqueue(new Callback<Favourite>() {
            @Override
            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                if(response.body().getState() == 1)
                {
                    getFavoriteInfo(response.body().getData());
                }
                else
                    System.out.println("ç¨æ·æ²¡æç»é");
                listView.refreshFinish();
            }
            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                System.out.println("è«åå¶å¦çå¤±è´¥äº");
            }
        });
    }

    private void getFavoriteInfo(List<Favourite.DataBean> stream)
    {
        for(int i=0;i<stream.size()&&i<20;i++) {
            System.out.println(stream.get(i).getNote()+" this is the Note");
            data.add(new PlainListItemInfo(""+stream.get(i).getNote(), "" + stream.get(i).getCreate_time(), stream.get(i)));
        }
        System.out.println("init is over");
        if(stream.size()>0)
            lastFavourite_id = stream.get(0).getFollow_id()-20;
        else
            lastFavourite_id = -1;
        adpter.notifyDataSetChanged();
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
