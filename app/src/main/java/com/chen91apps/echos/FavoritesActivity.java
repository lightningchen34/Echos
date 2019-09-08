package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.ArticlePack;
import com.chen91apps.echos.utils.articles.Favourite;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.utils.listitem.PlainListItemInfo;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.chen91apps.echos.views.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
                    System.out.println("to login");
                listView.refreshFinish();
            }
            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                onFailed();
                listView.refreshFinish();
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
                    System.out.println("to login");
                listView.refreshFinish();
            }
            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                onFailed();
                listView.refreshFinish();
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
                    System.out.println("to login");
                listView.refreshFinish();
            }
            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                onFailed();
                listView.refreshFinish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) return;
                if (i > data.size()) return;
                Favourite.DataBean favourite = (Favourite.DataBean) data.get(i - 1).getContent();
                ArticlePack ap = new Gson().fromJson(favourite.getContent(), new TypeToken<ArticlePack>(){}.getType());

                Intent intent = new Intent();
                intent.setComponent(new ComponentName(view.getContext(), ViewActivity.class));

                if (ap.news != null)
                {
                    intent.putExtra("type", ListInfoPair.TYPE_NEWS);
                    intent.putExtra("content", new Gson().toJson(ap.news));
                } else if (ap.post != null)
                {
                    intent.putExtra("type", ListInfoPair.TYPE_COMMUNITY);
                    intent.putExtra("content", new Gson().toJson(ap.post));
                } else
                {
                    intent.putExtra("type", ListInfoPair.TYPE_RSS);
                    intent.putExtra("content", new Gson().toJson(ap.rss));
                }
                startActivity(intent);
            }
        });
    }

    private void getFavoriteInfo(List<Favourite.DataBean> stream)
    {
        for(int i=0;i<stream.size();i++) {
            data.add(new PlainListItemInfo(""+stream.get(i).getNote(), "收藏时间：" + stream.get(i).getCreate_time(), stream.get(i)));
        }
        System.out.println("init is over");
        if(stream.size()>0)
            lastFavourite_id = stream.get(stream.size() - 1).getFollow_id();
        else
            lastFavourite_id = -1;
        adpter.notifyDataSetChanged();
    }

    public void onFailed()
    {
        Toast.makeText(this, "加载失败，请检查网络连接是否正常。", Toast.LENGTH_LONG).show();
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
