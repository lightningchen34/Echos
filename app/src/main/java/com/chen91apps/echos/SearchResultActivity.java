package com.chen91apps.echos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chen91apps.echos.fragments.ListFragment;
import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.pairs.ListInfoPair;

import org.w3c.dom.Text;

public class SearchResultActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener {

    private String value;

    private FragmentPagerAdapter adapter;
    private ListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        if (value == null) {
            Intent intent = getIntent();
            value = intent.getStringExtra("search_value");
        }

        initToolBar();
        initPage();
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_result_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        TextView tv = (TextView) findViewById(R.id.search_result_text);
        tv.setText(value + " 的搜索结果");
    }

    public void initPage(){
        fragment = ListFragment.newInstance("", value, ListInfoPair.TYPE_NEWS);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) return fragment;
                return null;
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return PagerAdapter.POSITION_NONE;
            }
        };
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_search_result);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(Uri uri) {

    }
}
