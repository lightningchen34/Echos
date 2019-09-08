package com.chen91apps.echos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.chen91apps.echos.fragments.ImageFragment;
import com.chen91apps.echos.fragments.VideoFragment;
import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.ScoreManager;
import com.chen91apps.echos.utils.articles.ArticlePack;
import com.chen91apps.echos.utils.articles.Favourite;
import com.chen91apps.echos.utils.articles.News;
import com.chen91apps.echos.utils.articles.Post;
import com.chen91apps.echos.utils.articles.RSSData;
import com.chen91apps.echos.utils.history.HistoryManager;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewActivity extends AppCompatActivity implements ImageFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener {

    private ArrayList<Fragment> listFrames;

    private String key;
    private String saveContent;

    private String title;
    private String shareContent;
    private int type;

    private ArticlePack ap;

    private boolean followed;

    private MenuItem followItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initToolBar();

        ap = new ArticlePack();
        followed = false;
        initContent();
    }

    private void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initContent()
    {
        TextView viewTitle = (TextView) findViewById(R.id.view_title);
        TextView viewContent = (TextView) findViewById(R.id.view_content);
        TextView viewSrc = (TextView) findViewById(R.id.view_src);
        TextView viewCategory = (TextView) findViewById(R.id.view_category);
        TextView viewAuthor = (TextView) findViewById(R.id.view_author);
        TextView viewTime = (TextView) findViewById(R.id.view_time);
        TextView viewUrl = (TextView) findViewById(R.id.view_url);
        TextView viewTip = (TextView) findViewById(R.id.view_tip);
        ViewPager viewPager = (ViewPager) findViewById(R.id.image_viewpager);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if (type == ListInfoPair.TYPE_NEWS)
        {
            String str = intent.getStringExtra("content");
            News.DataBean news = new Gson().fromJson(str, new TypeToken<News.DataBean>(){}.getType());
            viewTitle.setText(news.getTitle());
            viewAuthor.setText(news.getPublisher());
            viewContent.setText(news.getContent());
            viewCategory.setText(news.getCategory());
            viewTime.setText(news.getPublishTime());
            viewUrl.setText(news.getUrl());

            viewUrl.setOnClickListener((View v) -> {
                Uri uri = Uri.parse(news.getUrl());
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            });

            if (news.getOrganizations().size() > 0)
            {
                viewSrc.setText(news.getOrganizations().get(0).getMention());
            }

            for (News.DataBean.KeywordsBean kb : news.getKeywords())
            {
                ScoreManager.addScore(kb.getWord(), (float) kb.getScore());
            }


            listFrames = new ArrayList<>();
            if (news.getVideo() != null && news.getVideo().length() > 0)
            {
                listFrames.add(VideoFragment.newInstance(news.getVideo(), ""));
            }
            String image = news.getImage();
            if (image.length() > 5)
            {
                image = image.substring(1, image.length() - 1);
                String imgurl[] = image.split(", ");
                for (String url : imgurl)
                {
                    listFrames.add(ImageFragment.newInstance(url, ""));
                }
            }

            if (listFrames.size() > 0) {
                viewPager.setOffscreenPageLimit(listFrames.size());
                viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                    @NonNull
                    @Override
                    public Fragment getItem(int position) {
                        return listFrames.get(position);
                    }

                    @Override
                    public int getCount() {
                        return listFrames.size();
                    }
                });
            } else
            {
                viewPager.setVisibility(View.GONE);
            }
            if (listFrames.size() < 2)
            {
                viewTip.setVisibility(View.GONE);
            }

            ap.news = news;
            HistoryManager.addHistory(type, ap);

            key = type + ":" + news.getUrl();
            saveContent = new Gson().toJson(ap);
            title = news.getTitle();
            shareContent = news.getUrl();
        } else if (type == ListInfoPair.TYPE_COMMUNITY)
        {
            String str = intent.getStringExtra("content");
            Post.DataBean post = new Gson().fromJson(str, new TypeToken<Post.DataBean>(){}.getType());
            viewTitle.setText(post.getTitle());
            viewAuthor.setText(post.getAuthor());
            viewPager.setVisibility(View.GONE);
            viewTip.setVisibility(View.GONE);
            viewContent.setText(post.getContent());
            viewSrc.setText("Echos 社区");
            viewCategory.setText("社区");
            viewTime.setText(post.getCreate_time());
            viewUrl.setText("None");

            ap.post = post;
            HistoryManager.addHistory(type, ap);

            key = type + ":" + post.getPost_id();
            saveContent = new Gson().toJson(ap);
            title = post.getTitle();
            shareContent = title + " 这篇文章在 Echos 上引起热议。";
        } else if (type == ListInfoPair.TYPE_RSS)
        {
            String str = intent.getStringExtra("content");
            RSSData.ItemBean rss = new Gson().fromJson(str, new TypeToken<RSSData.ItemBean>(){}.getType());
            viewTitle.setText(rss.getTitle().getValue());
            viewAuthor.setText(rss.getAuthor().getValue());
            viewPager.setVisibility(View.GONE);
            viewTip.setVisibility(View.GONE);
            viewContent.setText(rss.getDescription().getValue());
            viewSrc.setText("");
            viewCategory.setText(rss.getCategory().getValue());
            viewTime.setText(rss.getPubDate().getValue());
            viewUrl.setText(rss.getLink().getValue());

            ap.rss = rss;
            HistoryManager.addHistory(type, ap);

            key = type + ":" + rss.getLink().getValue();
            saveContent = new Gson().toJson(ap);
            title = rss.getTitle().getValue();
            shareContent = rss.getLink().getValue();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu, menu);
        followItem = menu.getItem(0);

        Call<Favourite> call = MainActivity.echosService.checkFavrite(key);
        call.enqueue(new Callback<Favourite>() {
            @Override
            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                Favourite f = response.body();
                if (f.getData() != null && f.getData().size() > 0)
                {
                    followed = true;
                    followItem.setTitle("取消收藏");
                } else
                {
                    followed = false;
                    followItem.setTitle("收藏");
                }
            }

            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                followed = false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_follow)
        {
            if (!followed)
            {
                Call<Favourite> call = MainActivity.echosService.addFavorite(title, key, saveContent);
                // System.out.println(saveContent.length());
                call.enqueue(new Callback<Favourite>() {
                    @Override
                    public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                        // System.out.println(response.message());
                        Favourite f = response.body();
                        if (f == null) return;
                        // System.out.println("add"+f.getState());
                        if (f.getData() != null && f.getData().size() > 0)
                        {
                            followed = true;
                            followItem.setTitle("取消收藏");
                        } else
                        {
                            followed = false;
                            followItem.setTitle("收藏");
                        }
                    }

                    @Override
                    public void onFailure(Call<Favourite> call, Throwable t) {
                        followed = false;
                    }
                });
            } else
            {
                Call<Favourite> call = MainActivity.echosService.delFavorite(key);
                // System.out.println(saveContent.length());
                call.enqueue(new Callback<Favourite>() {
                    @Override
                    public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                        // System.out.println(response.message());
                        Favourite f = response.body();
                        if (f == null) return;
                        if (f.getState() == 1)
                        {
                            followed = false;
                            followItem.setTitle("收藏");
                        } else
                        {
                            followed = true;
                            followItem.setTitle("取消收藏");
                        }
                    }

                    @Override
                    public void onFailure(Call<Favourite> call, Throwable t) {
                        followed = false;
                    }
                });
            }
        } else if (item.getItemId() == R.id.menu_share)
        {
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);
            share_intent.setType("text/plain");
            share_intent.putExtra(Intent.EXTRA_SUBJECT, "title");//添加分享内容标题
            share_intent.putExtra(Intent.EXTRA_TEXT, shareContent);//添加分享内容
            share_intent = Intent.createChooser(share_intent, "分享");
            startActivity(share_intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress())
        {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onImageFragmentInteraction(Uri uri) {

    }

    @Override
    public void onVideoFragmentInteraction(Uri uri) {

    }
}
