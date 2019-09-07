package com.chen91apps.echos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.chen91apps.echos.fragments.ImageFragment;
import com.chen91apps.echos.fragments.VideoFragment;
import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.ArticlePack;
import com.chen91apps.echos.utils.articles.News;
import com.chen91apps.echos.utils.articles.Post;
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

import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.jzvd.JZVideoPlayer;

public class ViewActivity extends AppCompatActivity implements ImageFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener {

    private ArrayList<Fragment> listFrames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initToolBar();

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
        int type = intent.getIntExtra("type", -1);
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
            if (news.getOrganizations().size() > 0)
            {
                viewSrc.setText(news.getOrganizations().get(0).getMention());
            }

            listFrames = new ArrayList<>();
            if (news.getVideo() != null && news.getVideo().length() > 0)
            {
                listFrames.add(VideoFragment.newInstance(news.getVideo(), ""));
            }
            String image = news.getImage();
            image = image.substring(1, image.length() - 1);
            if (image.length() > 0)
            {
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

            ArticlePack ap = new ArticlePack();
            ap.news = news;
            HistoryManager.addHistory(type, ap);
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

            ArticlePack ap = new ArticlePack();
            ap.post = post;
            HistoryManager.addHistory(type, ap);
        }
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
