package com.chen91apps.echos;

import android.content.res.Configuration;
import android.content.res.Resources;
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

import android.view.MenuItem;

import com.chen91apps.echos.fragments.ListFragment;
import com.chen91apps.echos.fragments.PageFragment;
import com.chen91apps.echos.utils.ThemeColors;
import com.chen91apps.echos.utils.pairs.TabIconPair;
import com.chen91apps.echos.utils.tabviews.TabViewHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PageFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener {

    private TextView searchTextView;
    private ArrayList<PageFragment> pages;
    private ArrayList<TabIconPair> tabInfo;

    private PageFragment currentFragment = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        // config.locale = Locale.CHINA;
        resources.updateConfiguration(config, dm);

        super.onCreate(savedInstanceState);
        // this.setTheme(R.style.Mytheme_Night);
        setContentView(R.layout.activity_main);

        tabInfo = new ArrayList<>();
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_news), getString(R.string.mainactivaty_tag_news), R.drawable.ic_home_selected, R.drawable.ic_home));
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_community), getString(R.string.mainactivaty_tag_community), R.drawable.ic_community_selected, R.drawable.ic_community));
        tabInfo.add(new TabIconPair(getString(R.string.mainactivaty_text_rss), getString(R.string.mainactivaty_tag_rss), R.drawable.ic_rss_selected, R.drawable.ic_rss));

        initPages();
        initTabs();

        initNavigationView();
        initSearchText();
    }

    private void initNavigationView()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
