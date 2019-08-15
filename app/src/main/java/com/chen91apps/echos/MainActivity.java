package com.chen91apps.echos;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.chen91apps.echos.fragments.ListFragment;
import com.chen91apps.echos.fragments.PageFragment;
import com.chen91apps.echos.utils.ACache;
import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.pairs.TabIconPair;
import com.chen91apps.echos.utils.tabviews.TabViewHelper;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements PageFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener {

    private TextView searchTextView;
    private ArrayList<PageFragment> pages;
    private ArrayList<TabIconPair> tabInfo;

    private int[] drawer_list_indexes;

    public static ACache acache;

    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        // config.locale = Locale.CHINA;
        resources.updateConfiguration(config, dm);

        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);

        acache = ACache.get(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarFullTransparent();

        tabInfo = new ArrayList<>();
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_news), getString(R.string.mainactivaty_tag_news), R.drawable.ic_home_selected, R.drawable.ic_home));
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_community), getString(R.string.mainactivaty_tag_community), R.drawable.ic_community_selected, R.drawable.ic_community));
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_rss), getString(R.string.mainactivaty_tag_rss), R.drawable.ic_rss_selected, R.drawable.ic_rss));

        initPages();
        initTabs();

        initDrawer();
        initSearchText();
    }

    @Override
    public void recreate() {
        System.out.println("recreate");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : pages)
        {
            if (fragment.isAdded())
            {
                fragmentTransaction.remove(fragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        super.recreate();
    }

    private void initDrawer()
    {
        ImageView background = (ImageView) findViewById(R.id.drawer_background);
        background.setImageResource(Configure.day_or_night ? R.mipmap.back : R.mipmap.back2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        ListView drawerListView = (ListView) findViewById(R.id.drawer_listview);

        drawer_list_indexes = new int[] {
                R.string.menu_profile,
                R.string.menu_vip,
                R.string.menu_theme,
                R.string.menu_post,
                R.string.menu_comments,
                R.string.menu_history,
                R.string.menu_favorites,
                R.string.menu_feedback
        };

        String[] data = new String[drawer_list_indexes.length];
        for (int i = 0; i < drawer_list_indexes.length; ++i)
            data[i] = getString(drawer_list_indexes[i]);

        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, data));

        drawerListView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                {
                    // TODO
                    Toast.makeText(view.getContext(), "我的信息", Toast.LENGTH_LONG).show();
                } else if (i == 1)
                {
                    // TODO
                    Toast.makeText(view.getContext(), "我的会员", Toast.LENGTH_LONG).show();
                } else if (i == 2)
                {
                    // TODO
                    Toast.makeText(view.getContext(), "个性换肤", Toast.LENGTH_LONG).show();
                } else if (i == 3)
                {
                    // TODO
                    Toast.makeText(view.getContext(), "我的发帖", Toast.LENGTH_LONG).show();
                } else if (i == 4)
                {
                    // TODO
                    Toast.makeText(view.getContext(), "我的回帖", Toast.LENGTH_LONG).show();
                } else if (i == 5)
                {
                    // TODO
                    Toast.makeText(view.getContext(), "我的阅历", Toast.LENGTH_LONG).show();
                } else if (i == 6)
                {
                    // TODO
                    Toast.makeText(view.getContext(), "我的收藏", Toast.LENGTH_LONG).show();
                } else if (i == 7)
                {
                    // TODO
                    Toast.makeText(view.getContext(), "意见反馈", Toast.LENGTH_LONG).show();
                } else
                {
                    // TODO
                }
            }
        });
        
        final TextView dnMode = (TextView) findViewById(R.id.drawer_dnmode);
        final TextView settings = (TextView) findViewById(R.id.drawer_settings);

        dnMode.setOnClickListener((View view) -> {
            Configure.day_or_night = !Configure.day_or_night;
                recreate();
            });
        dnMode.setText(Configure.day_or_night ? R.string.menu_night_mode : R.string.menu_day_mode);
        settings.setText(R.string.menu_settings);

    }

    private void initSearchText()
    {
        searchTextView = (TextView) findViewById(R.id.search_textview);
        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == searchTextView.getId())
                {
                    Toast.makeText(view.getContext(), "Go to SearchActivity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initPages()
    {
        System.out.println("Init Fragment");

        pages = new ArrayList<PageFragment>();
        for (TabIconPair info : tabInfo)
        {
            pages.add(PageFragment.newInstance(info.getTag(), ""));
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return pages.get(position);
            }

            @Override
            public int getCount() {
                return pages.size();
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabInfo.get(position).getText();
            }
        });

        TabLayout tabLayout = findViewById(R.id.main_tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initTabs()
    {
        TabLayout tabLayout = findViewById(R.id.main_tablayout);
        for (int i = 0; i < tabLayout.getTabCount(); ++i)
        {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            TabViewHelper.setIconTabActive(tab, tabInfo.get(i), tab.isSelected());
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                TabViewHelper.setIconTabActive(tab, tabInfo.get(index), true);
                pages.get(index).reselectPage();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                TabViewHelper.setIconTabActive(tab, tabInfo.get(index), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageFragmentInteraction(Uri uri) {
        // TODO
    }

    @Override
    public void onListFragmentInteraction(Uri uri) {
        // TODO
    }
}
