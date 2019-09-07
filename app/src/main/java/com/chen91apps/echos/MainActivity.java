package com.chen91apps.echos;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.chen91apps.echos.utils.ImageLoader;
import com.chen91apps.echos.utils.User;
import com.chen91apps.echos.utils.articles.News;
import com.chen91apps.echos.utils.pairs.TabIconPair;
import com.chen91apps.echos.utils.retrofit.EchosService;
import com.chen91apps.echos.utils.retrofit.RetrofitService;
import com.chen91apps.echos.utils.tabviews.TabViewHelper;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements PageFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener, User.OnLoginStateChanged {

    private TextView searchTextView;
    private ArrayList<PageFragment> pages;
    private ArrayList<TabIconPair> tabInfo;

    private int[] drawer_list_indexes;

    public static ACache acache;
    public static User user;
    public static RetrofitService newsService;
    public static EchosService echosService;

    private boolean show = true;

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
        ClearableCookieJar cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookie)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        ImageLoader.imageLoader.setContext(this);

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://echos.lightning34.cn/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        echosService = retrofit.create(EchosService.class);

        Retrofit retrofitNews = new Retrofit.Builder()
                .baseUrl("https://api2.newsminer.net/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        newsService = retrofitNews.create(RetrofitService.class);

        // RequestNews();

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        // config.locale = Locale.CHINA;
        resources.updateConfiguration(config, dm);

        acache = ACache.get(this);
        Configure.day_or_night = (Boolean) acache.getAsObject("day_or_night");
        if (Configure.day_or_night == null)
        {
            Configure.day_or_night = true;
            acache.put("day_or_night", Configure.day_or_night);
        }

        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarFullTransparent();

        tabInfo = new ArrayList<>();
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_news), getString(R.string.mainactivaty_tag_news), R.drawable.ic_home_selected, R.drawable.ic_home));
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_community), getString(R.string.mainactivaty_tag_community), R.drawable.ic_community_selected, R.drawable.ic_community));
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_rss), getString(R.string.mainactivaty_tag_rss), R.drawable.ic_rss_selected, R.drawable.ic_rss));

        if (show) {
            initPages();
            initTabs();
        }

        initDrawer();
        initSearchText();

        user = new User(echosService, this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("save");
        outState.putInt("tabPosition", tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("restore");
        int tabPosition = savedInstanceState.getInt("tabPosition");
        tabLayout.getTabAt(tabPosition).select();
    }

    @Override
    public void recreate() {
        if (show) {
            System.out.println("recreate");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (Fragment fragment : pages) {
                if (fragment.isAdded()) {
                    fragmentTransaction.remove(fragment);
                }
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
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
                R.string.menu_post,
                R.string.menu_comments,
                R.string.menu_favorites,
                R.string.menu_history,
                R.string.menu_feedback
        };

        String[] data = new String[drawer_list_indexes.length];
        for (int i = 0; i < drawer_list_indexes.length; ++i)
            data[i] = getString(drawer_list_indexes[i]);

        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, data));

        drawerListView.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            if (i == 0)
            {
                if (user.checkLogin())
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, UserInfoActivity.class));
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                } else
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, LoginActivity.class));
                    intent.putExtra("action", "userinfo");
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
            } else if (i == 1)
            {
                if (user.checkLogin())
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, PostActivity.class));
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                } else
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, LoginActivity.class));
                    intent.putExtra("action", "post");
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
            } else if (i == 2)
            {
                if (user.checkLogin())
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, CommentsActivity.class));
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                } else
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, LoginActivity.class));
                    intent.putExtra("action", "comments");
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
            } else if (i == 3)
            {
                if (user.checkLogin())
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, FavoritesActivity.class));
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                } else
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, LoginActivity.class));
                    intent.putExtra("action", "favorites");
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
            } else if (i == 4)
            {
                if (user.checkLogin())
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, HistoryActivity.class));
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                } else
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, LoginActivity.class));
                    intent.putExtra("action", "history");
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
            } else if (i == 5)
            {
                if (user.checkLogin())
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, FeedbackActivity.class));
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                } else
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(this, LoginActivity.class));
                    intent.putExtra("action", "feedback");
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
            } else
            {
                //
            }
        });

        final TextView dnMode = (TextView) findViewById(R.id.drawer_dnmode);
        final TextView settings = (TextView) findViewById(R.id.drawer_settings);

        dnMode.setOnClickListener((View view) -> {
            Configure.day_or_night = !Configure.day_or_night;
            acache.put("day_or_night", Configure.day_or_night);
            recreate();
        });
        settings.setOnClickListener((View view) -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(this, SettingsActivity.class));
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        });
        dnMode.setText(Configure.day_or_night ? R.string.menu_night_mode : R.string.menu_day_mode);
        settings.setText(R.string.menu_settings);

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void initSearchText()
    {
        searchTextView = (TextView) findViewById(R.id.search_textview);
        searchTextView.setOnClickListener((View view) -> {
            if (view.getId() == searchTextView.getId())
            {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(this, SearchActivity.class));
                startActivity(intent);
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

        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = findViewById(R.id.main_tablayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private TabLayout tabLayout;

    private void initTabs()
    {
        tabLayout = findViewById(R.id.main_tablayout);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("return");
    }

    @Override
    public void onPageFragmentInteraction(Uri uri) {
        // TODO
    }

    @Override
    public void onListFragmentInteraction(Uri uri) {
        // TODO
    }

    @Override
    public void OnLoginStateChanged(boolean state) {
        final TextView username = (TextView) findViewById(R.id.username_textview);
        if (state)
        {
            username.setText(user.getInfo().getNickname());
        } else
        {
            username.setText("未登录");
        }
    }
}
